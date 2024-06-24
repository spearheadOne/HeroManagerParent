package org.abondar.industrial.heromanager.mapper;

import org.abondar.industrial.heromanager.model.db.Hero;
import org.abondar.industrial.heromanager.model.db.HeroProperty;
import org.abondar.industrial.heromanager.model.db.PropertyType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HeroPropertyMapper {

    public List<HeroProperty> mapProperties(Map<PropertyType,List<String>> properties, Hero hero) {
        return properties.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(prop -> {
                            if (prop!=null && !prop.isBlank()){
                                HeroProperty heroProp = new HeroProperty();
                                heroProp.setPropertyType(entry.getKey());
                                heroProp.setHero(hero);
                                heroProp.setPropertyValue(prop);
                                return heroProp;
                            }
                         return null;
                        }))
                .collect(Collectors.toList());
    }

    public Map<PropertyType,List<String>> unmapProperties(List<HeroProperty> properties) {
        Map<PropertyType, List<String>> groupedProperties = new HashMap<>();

        properties.forEach(prop -> groupedProperties.computeIfAbsent(prop.getPropertyType(), k -> new ArrayList<>())
                .add(prop.getPropertyValue()));

        return groupedProperties;
    }
}
