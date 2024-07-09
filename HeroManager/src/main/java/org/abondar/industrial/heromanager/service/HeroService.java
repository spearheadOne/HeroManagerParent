package org.abondar.industrial.heromanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abondar.industrial.heromanager.exception.HeroNotFoundException;
import org.abondar.industrial.heromanager.mapper.HeroResponseMapper;
import org.abondar.industrial.heromanager.mapper.HeroPropertyMapper;
import org.abondar.industrial.heromanager.model.db.Hero;
import org.abondar.industrial.heromanager.model.db.PropertyType;
import org.abondar.industrial.heromanager.model.request.HeroCreateRequest;
import org.abondar.industrial.heromanager.model.request.HeroUpdateRequest;
import org.abondar.industrial.heromanager.model.response.HeroCreateResponse;
import org.abondar.industrial.heromanager.model.response.HeroResponse;
import org.abondar.industrial.heromanager.repo.HeroPropertyRepository;
import org.abondar.industrial.heromanager.repo.HeroRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HeroService  {

    private final HeroRepository heroRepository;

    private final HeroPropertyRepository propertyRepository;

    private final HeroPropertyMapper propertyMapper;

    private final HeroResponseMapper heroResponseMapper;
    private final HeroPropertyRepository heroPropertyRepository;

    public HeroCreateResponse createHero(HeroCreateRequest request) {
        var hero = new Hero();
        hero.setName(request.name());
        hero.setAlias(request.alias());
        hero.setOrigin(request.origin());

        heroRepository.save(hero);
        log.info("Created hero: {}", hero.getId());

        var properties = propertyMapper.mapProperties((Map.of(
                PropertyType.ASSOCIATION, request.associations(),
                PropertyType.POWER, request.powers(),
                PropertyType.WEAPON, request.weapons())), hero);

        propertyRepository.saveAll(properties);

        properties.forEach(p-> log.info("Created property: {} :{}",p.getPropertyType(), p.getPropertyValue()));

        return new HeroCreateResponse(hero.getId());
    }

    public HeroResponse updateHero(Long heroId, HeroUpdateRequest request) {
        var hero = checkHero(heroId);

        Optional.ofNullable(request.name()).ifPresent(hero::setName);
        Optional.ofNullable(request.alias()).ifPresent(hero::setAlias);
        Optional.ofNullable(request.origin()).ifPresent(hero::setOrigin);

        heroRepository.updateHero(heroId,request.alias(),request.name(), request.origin());
        log.info("Updated hero: {}", hero.getId());

        var existingProperties = heroPropertyRepository.findByHeroId(heroId);
        var existingPropertiesMap = propertyMapper.unmapProperties(existingProperties);

        Map<PropertyType, List<String>> propertiesMap = new HashMap<>();

        if (request.associations()!=null && !request.associations().isEmpty()) {
            propertiesMap.put(PropertyType.ASSOCIATION, request.associations());
        }
        if (request.powers()!=null && !request.powers().isEmpty()) {
            propertiesMap.put(PropertyType.POWER, request.powers());
        }

        if (request.weapons()!=null && !request.weapons().isEmpty()) {
            propertiesMap.put(PropertyType.WEAPON, request.weapons());
        }

        for (Map.Entry<PropertyType, List<String>> entry : propertiesMap.entrySet()) {
            PropertyType propertyType = entry.getKey();
            List<String> newValues = entry.getValue();
            existingPropertiesMap.merge(propertyType, newValues, (oldValues, newValuesList) -> {
                oldValues.addAll(newValuesList);
                return oldValues;
            });
        }

        var updateProperties = propertyMapper.mapProperties(existingPropertiesMap, hero);
        propertyRepository.saveAll(updateProperties);
        updateProperties.forEach(p-> log.info("Extended hero properties with: {}: {}",p.getPropertyType(), p.getPropertyValue()));

        return heroResponseMapper.mapHero(hero, existingPropertiesMap);
    }

    public HeroResponse getHeroByAlias(String alias) {
        var found = heroRepository.findByAlias(alias)
                .orElseThrow(HeroNotFoundException::new);

        log.info("Found hero by alias: {}", alias);

        var properties = propertyRepository.findByHeroId(found.getId());
        var propertiesMap = propertyMapper.unmapProperties(properties);

        properties.forEach(p-> log.info("Found properties for hero: {}: {}",p.getPropertyType(), p.getPropertyValue()));

        return heroResponseMapper.mapHero(found, propertiesMap);
    }

    public HeroResponse getHeroByName(String name) {
        var found = heroRepository.findByName(name)
                .orElseThrow(HeroNotFoundException::new);

        log.info("Found hero by name: {}", name);

        var properties = propertyRepository.findByHeroId(found.getId());
        var propertiesMap = propertyMapper.unmapProperties(properties);

        properties.forEach(p-> log.info("Found properties for hero: {}: {}",p.getPropertyType(), p.getPropertyValue()));

        return heroResponseMapper.mapHero(found, propertiesMap);
    }

    public List<HeroResponse> getAllHeroes(int offset, int limit) {

        var pageRequest = PageRequest.of(offset, limit);
        var heroPages = heroRepository.findAll(pageRequest);

        var heroes = heroPages.getContent();
        if (heroes.isEmpty()) {
            throw new HeroNotFoundException();
        }

        heroes.forEach(hero -> log.info("Found hero: {}", hero.getId()));

        return mapHeroes(heroes);
    }

    public void deleteHero(long heroId) {
        checkHero(heroId);

        heroRepository.deleteById(heroId);

        log.info("Deleted hero: {}", heroId);
    }

    public void deleteHeroProperty(long heroId, String propertyValue) {
        checkHero(heroId);

        propertyRepository.findByHeroIdAndPropertyValue(heroId,propertyValue)
                .orElseThrow(HeroNotFoundException::new);

        propertyRepository.deleteByHeroIdAndPropertyValue(heroId, propertyValue);

        log.info("Deleted property {} for hero: {}", propertyValue,heroId);
    }

    public List<HeroResponse> getHeroByProperty(String propertyValue, String propertyType, int offset, int limit){
        var pageRequest = PageRequest.of(offset, limit);

        var heroIds = propertyRepository
                .findHeroIdsByPropertyValueAndPropertyType(propertyValue,PropertyType.valueOf(propertyType), pageRequest);
        if (heroIds.isEmpty()){
            throw new HeroNotFoundException();
        }

        var heroes = heroRepository.findAllById(heroIds);

        return mapHeroes(heroes);
    };


    private Hero checkHero(Long id) {
        return heroRepository.findById(id)
                .orElseThrow(HeroNotFoundException::new);
    }

    private List<HeroResponse> mapHeroes(List<Hero> heroes){
        var heroIds = heroes.stream()
                .map(Hero::getId)
                .toList();

        var properties = propertyRepository.findByHeroIds(heroIds);
        var propertiesMap = propertyMapper.unmapProperties(properties);

        return heroes.stream().map(
                        hero -> heroResponseMapper.mapHero(hero, propertiesMap))
                .collect(Collectors.toList());
    }
}
