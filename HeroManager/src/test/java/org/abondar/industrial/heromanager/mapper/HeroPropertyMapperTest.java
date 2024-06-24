package org.abondar.industrial.heromanager.mapper;

import org.abondar.industrial.heromanager.model.db.Hero;
import org.abondar.industrial.heromanager.model.db.HeroProperty;
import org.abondar.industrial.heromanager.model.db.PropertyType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HeroPropertyMapperTest {

    @Test
    public void unwrapPropertiesTest() {
        var mapper = new HeroPropertyMapper();
        var hero = new Hero();

        var map = Map.of(
                PropertyType.ASSOCIATION, List.of("ta","ta1","ta2"),
                PropertyType.POWER,List.of("tp","tp1","tp2"),
                PropertyType.WEAPON,List.of("tw","tw1","tw2")
        );

        var properties = mapper.mapProperties(map,hero);

        assertFalse(properties.isEmpty());
        assertEquals(9,properties.size());
        assertEquals(hero,properties.get(0).getHero());

    }


    @Test
    public void wrapPropertiesTest() {
        var mapper = new HeroPropertyMapper();

        var prop1 = new HeroProperty();
        prop1.setPropertyValue("test");
        prop1.setPropertyType(PropertyType.POWER);

        var prop2 = new HeroProperty();
        prop2.setPropertyType(PropertyType.ASSOCIATION);
        prop2.setPropertyValue("test1");

        var res = mapper.unmapProperties(List.of(
                prop1,prop2
        ));

        assertFalse(res.isEmpty());
        assertEquals(2,res.size());
        assertEquals("test",res.get(PropertyType.POWER).get(0));
        assertEquals("test1",res.get(PropertyType.ASSOCIATION).get(0));
    }
}
