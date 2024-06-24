package org.abondar.industrial.heromanager.mapper;

import org.abondar.industrial.heromanager.model.db.Hero;
import org.abondar.industrial.heromanager.model.db.PropertyType;
import org.abondar.industrial.heromanager.model.response.HeroResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HeroResponseMapper {

    public HeroResponse mapHero(Hero hero, Map<PropertyType, List<String>> properties) {
        return new HeroResponse(
                hero.getId(),
                hero.getAlias(),
                hero.getName(),
                properties.getOrDefault(PropertyType.POWER,List.of()),
                properties.getOrDefault(PropertyType.WEAPON,List.of()),
                hero.getOrigin(),
                properties.getOrDefault(PropertyType.ASSOCIATION,List.of())
                
        );
        
    }
    
}
