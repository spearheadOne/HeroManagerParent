package org.abondar.industrial.heromanager.repo;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class HeroRepositoryTest extends BaseRepositoryTest{

    @Test
    public void saveHeroTest() {
        var hero = createHero();

        assertTrue(hero.getId() > 0);
    }

    @Test
    public void findHeroByAliasTest() {
        var hero = createHero();
        var found = heroRepository.findByAlias(hero.getAlias());

        assertTrue(found.isPresent());
        assertEquals(found.get().getId(),hero.getId());
    }

    @Test
    public void findHeroByNameTest() {
        var hero = createHero();
        var found = heroRepository.findByName(hero.getName());

        assertTrue(found.isPresent());
        assertEquals(found.get().getId(),hero.getId());
    }

    @Test
    public void findHeroByNameCacheTest() {
        cacheManager.getCache("heroesCache").clear();
        var hero = createHero();

        var found = heroRepository.findByName(hero.getName());

        assertFalse(found.isEmpty());
        assertNotNull(cacheManager.getCache("heroPropertiesCache"));
    }

    @Test
    public void findAllTest() {
        createHero();
        createHero();

        var pageReq = PageRequest.of(0,10);
        var found = heroRepository.findAll(pageReq);

        assertEquals(2,found.getContent().size());
    }

    @Test
    public void findAllWithOffsetTest() {
        createHero();
        var hero = createHero();

        var pageReq = PageRequest.of(1,1);
        var found = heroRepository.findAll(pageReq);

        assertEquals(1,found.getContent().size());
        assertEquals(hero.getId(), found.getContent().get(0).getId());
    }

    @Test
    public void updateHeroTest(){
        var hero = createHero();
        hero.setAlias("alias2");

        heroRepository.updateHero(hero.getId(),hero.getAlias(),null,null);
        var found = heroRepository.findById(hero.getId());

        assertTrue(found.isPresent());
        assertEquals(hero.getAlias(), found.get().getAlias());
    }

    @Test
    public void deleteHeroTest(){
        var hero = createHero();
        heroRepository.deleteById(hero.getId());

        var foundHero = heroRepository.findById(hero.getId());
        assertTrue(foundHero.isEmpty());
    }

}
