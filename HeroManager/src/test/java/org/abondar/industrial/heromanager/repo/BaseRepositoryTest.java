package org.abondar.industrial.heromanager.repo;


import org.abondar.industrial.heromanager.model.db.Hero;
import org.abondar.industrial.heromanager.model.db.HeroProperty;
import org.abondar.industrial.heromanager.model.db.PropertyType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BaseRepositoryTest {

    @Autowired
    protected HeroRepository heroRepository;

    @Autowired
    protected HeroPropertyRepository heroPropertyRepository;

    @Autowired
    protected CacheManager cacheManager;

    protected Hero createHero() {
        var hero = new Hero();
        hero.setName("test");
        hero.setAlias("test");
        hero.setOrigin("test");
        return heroRepository.save(hero);
    }

    protected HeroProperty createProperty(Hero hero) {
        var prop = new HeroProperty();
        prop.setHero(hero);
        prop.setPropertyType(PropertyType.ASSOCIATION);
        prop.setPropertyValue("war-machine");

        return heroPropertyRepository.save(prop);
    }
}
