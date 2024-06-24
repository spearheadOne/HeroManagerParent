package org.abondar.industrial.heromanager.repo;

import jakarta.persistence.EntityManager;
import org.abondar.industrial.heromanager.model.db.Hero;
import org.abondar.industrial.heromanager.model.db.HeroProperty;
import org.abondar.industrial.heromanager.model.db.PropertyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CascadeRepoTest {

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private HeroPropertyRepository heroPropertyRepository;

    @Test
    public void cascadeDeleteTest(){
        var hero = new Hero();
        hero.setName("test");
        hero.setAlias("test");
        hero.setOrigin("test");

        hero = heroRepository.save(hero);

        var prop = new HeroProperty();
        prop.setHero(hero);
        prop.setPropertyType(PropertyType.ASSOCIATION);
        prop.setPropertyValue("war-machine");
        heroPropertyRepository.save(prop);

        heroRepository.deleteById(hero.getId());


        var foundHero = heroRepository.findById(hero.getId());
        assertTrue(foundHero.isEmpty());

        var found = heroPropertyRepository.findById(prop.getId());
        assertTrue(found.isEmpty());
    }

}
