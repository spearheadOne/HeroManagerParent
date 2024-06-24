package org.abondar.industrial.heromanager.repo;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HeroPropertyRepositoryTest extends BaseRepositoryTest {


    @Test
    public void savePropertiesTest() {
        var property = createProperty(createHero());

        assertTrue(property.getId() > 0);
    }

    @Test
    public void findByHeroIdTest() {
        var property = createProperty(createHero());
        var found = heroPropertyRepository.findByHeroId(property.getHero().getId());

        assertFalse(found.isEmpty());
        assertEquals(1,found.size());
        assertEquals(property.getPropertyType(),found.get(0).getPropertyType());
        assertEquals(property.getPropertyValue(),found.get(0).getPropertyValue());
    }

    @Test
    public void findByHeroIdAndValueTest() {
        var property = createProperty(createHero());
        var found = heroPropertyRepository.findByHeroIdAndPropertyValue(property.getHero().getId(),property.getPropertyValue());

        assertFalse(found.isEmpty());
        assertEquals(property.getId(),found.get().getId());
    }

    @Test
    public void findByHeroIdCacheTest() {
        cacheManager.getCache("heroPropertiesCache").clear();
        var property = createProperty(createHero());

        var found = heroPropertyRepository.findByHeroId(property.getHero().getId());
        assertFalse(found.isEmpty());

        assertNotNull(cacheManager.getCache("heroPropertiesCache"));
    }

    @Test
    public void findPropertiesTest() {
        var property = createProperty(createHero());
        var res =heroPropertyRepository.findByHeroId(property.getId());

        assertFalse(res.isEmpty());
        assertEquals(property.getId(),res.get(0).getId());
    }


    @Test
    public void findPropertiesByIdsTest() {
        var hero = createHero();
        var property = createProperty(hero);
        var res =heroPropertyRepository.findByHeroIds(List.of(hero.getId()));

        assertFalse(res.isEmpty());
        assertEquals(property.getId(),res.get(0).getId());
    }

    @Test
    public void findHeroIdsByPropertyValueAndPropertyType(){
        var hero = createHero();
        var property = createProperty(hero);

        var pageReq = PageRequest.of(0,1);
        var res = heroPropertyRepository.
                findHeroIdsByPropertyValueAndPropertyType(property.getPropertyValue(),
                        property.getPropertyType(),pageReq);

        assertFalse(res.isEmpty());
        assertEquals(1,res.size());
        assertEquals(hero.getId(),res.get(0));
    }

    @Test
    public void deletePropertiesTest() {
        var hero = createHero();
        var property = createProperty(hero);
        heroPropertyRepository.deleteByHeroIdAndPropertyValue(hero.getId(),property.getPropertyValue());

        var found = heroPropertyRepository.findByHeroId(hero.getId());
        assertTrue(found.isEmpty());
    }
}
