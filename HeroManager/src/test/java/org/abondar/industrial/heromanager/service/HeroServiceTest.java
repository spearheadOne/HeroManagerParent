package org.abondar.industrial.heromanager.service;

import org.abondar.industrial.heromanager.exception.HeroNotFoundException;
import org.abondar.industrial.heromanager.mapper.HeroPropertyMapper;
import org.abondar.industrial.heromanager.mapper.HeroResponseMapper;
import org.abondar.industrial.heromanager.model.db.Hero;
import org.abondar.industrial.heromanager.model.db.HeroProperty;
import org.abondar.industrial.heromanager.model.db.PropertyType;
import org.abondar.industrial.heromanager.model.request.HeroCreateRequest;
import org.abondar.industrial.heromanager.model.request.HeroUpdateRequest;
import org.abondar.industrial.heromanager.repo.HeroPropertyRepository;
import org.abondar.industrial.heromanager.repo.HeroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeroServiceTest {

    @Mock
    private HeroRepository heroRepo;

    @Mock
    private HeroPropertyRepository heroPropertyRepo;

    @InjectMocks
    private HeroService heroService;

    @Spy
    private HeroPropertyMapper propertyMapper = new HeroPropertyMapper();

    @Spy
    private HeroResponseMapper heroResponseMapper = new HeroResponseMapper();

    @Test
    public void createHeroTest() {
        var request = new HeroCreateRequest(
                "test",
                "test",
                List.of("pw1","pw2"),
                List.of(),
                "test",
                List.of("as1")
        );

        var hpList = List.of(new HeroProperty());

        when(heroRepo.save(any(Hero.class))).thenReturn(new Hero());
        when(heroPropertyRepo.saveAll(anyList())).thenReturn(hpList);

        var resp = heroService.createHero(request);

        assertNotNull(resp);
        assertEquals(0,resp.heroId());
    }

    @Test
    public void updateHeroTest() {
        var property = new HeroProperty();
        property.setPropertyType(PropertyType.ASSOCIATION);
        property.setPropertyValue("test");

        when(heroRepo.findById(anyLong())).thenReturn(Optional.of(new Hero()));
        when(heroPropertyRepo.findByHeroId(anyLong())).thenReturn(List.of(property));

        var request = new HeroUpdateRequest(
                "test",
                null,
                List.of(),
                List.of(),
                null,
                List.of("as1")
        );

        var response = heroService.updateHero(1L, request);

        verify(heroRepo, times(1)).updateHero(1L,request.alias(),response.name(),request.origin());
        verify(heroPropertyRepo, times(1)).saveAll(anyList());

        assertNotNull(response);
        assertEquals(request.alias(),response.alias());
        assertFalse(response.associations().isEmpty());
        assertEquals(2,response.associations().size());
        assertEquals(request.associations().get(0),response.associations().get(1));
    }

    @Test
    public void getHeroByAliasTest(){
        var hero = new Hero();
        hero.setId(1);

        var alias = "test";

        var hp1 = new HeroProperty();
        hp1.setPropertyType(PropertyType.ASSOCIATION);
        hp1.setPropertyValue("as1");

        var hp2 = new HeroProperty();
        hp2.setPropertyType(PropertyType.POWER);
        hp2.setPropertyValue("pw1");

        var props = List.of(hp1,hp2);

        when(heroRepo.findByAlias(alias)).thenReturn(Optional.of(hero));
        when(heroPropertyRepo.findByHeroId(1L)).thenReturn(props);

        var resp = heroService.getHeroByAlias(alias);

        assertNotNull(resp);
        assertEquals(hero.getId(),resp.id());
        assertFalse(resp.powers().isEmpty());
        assertEquals(1,resp.powers().size());
        assertEquals(hp2.getPropertyValue(),resp.powers().get(0));
        assertFalse(resp.associations().isEmpty());
        assertEquals(1,resp.associations().size());
        assertEquals(hp1.getPropertyValue(),resp.associations().get(0));

    }

    @Test
    public void getHeroByAliasNotFoundTest(){
        when(heroRepo.findByAlias(anyString())).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () -> heroService.getHeroByAlias("test"));
    }


    @Test
    public void getHeroByNamesTest(){
        var hero = new Hero();
        hero.setId(1);

        var name = "test";

        var hp1 = new HeroProperty();
        hp1.setPropertyType(PropertyType.ASSOCIATION);
        hp1.setPropertyValue("as1");

        var props = List.of(hp1);

        when(heroRepo.findByName(name)).thenReturn(Optional.of(hero));
        when(heroPropertyRepo.findByHeroId(1L)).thenReturn(props);

        var resp = heroService.getHeroByName(name);

        assertNotNull(resp);
        assertEquals(hero.getId(),resp.id());
        assertEquals(hp1.getPropertyValue(),resp.associations().get(0));

    }

    @Test
    public void getHeroByNameNotFoundTest(){
        when(heroRepo.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () -> heroService.getHeroByName("test"));
    }

    @Test
    public void getAllHeroesTest(){
        var hero = new Hero();
        hero.setId(1);

        var hp1 = new HeroProperty();
        hp1.setPropertyType(PropertyType.ASSOCIATION);
        hp1.setPropertyValue("as1");

        when(heroRepo.findAll(PageRequest.of(0,2)))
                .thenReturn(new PageImpl<>(List.of(hero)));
        when(heroPropertyRepo.findByHeroIds(List.of(1L))).thenReturn(List.of(hp1));

        var resp = heroService.getAllHeroes(0,2);

        assertNotNull(resp);
        assertFalse(resp.isEmpty());
        assertEquals(1,resp.size());
        assertEquals(1,resp.get(0).id());
        assertEquals(1,resp.get(0).associations().size());
        assertEquals(hp1.getPropertyValue(),resp.get(0).associations().get(0));

    }

    @Test
    public void getAllHeroesNotFoundTest(){
        var hero = new Hero();
        hero.setId(1);

        when(heroRepo.findAll(PageRequest.of(0,1)))
                .thenReturn(new PageImpl<>(List.of()));


        assertThrows(HeroNotFoundException.class, () ->  heroService.getAllHeroes(0,1));

    }

    @Test
    public void getHeroByPropertyTest(){
        var offset = 0;
        var limit = 1;
        var propType = PropertyType.ASSOCIATION;
        var propValue = "as1";
        var heroId = 1L;
        var hero = new Hero();
        hero.setId(heroId);

        when(heroPropertyRepo
                .findHeroIdsByPropertyValueAndPropertyType(propValue,propType,PageRequest.of(offset, limit)))
                .thenReturn(List.of(heroId));

        when(heroRepo.findAllById(List.of(heroId))).thenReturn(List.of(hero));

        var resp = heroService.getHeroByProperty(propValue,propType.name(),offset,limit);

        assertNotNull(resp);
        assertFalse(resp.isEmpty());
        assertEquals(1,resp.size());
        assertEquals(1,resp.get(0).id());
    }

    @Test
    public void getHeroByPropertyWrongTest(){
        assertThrows(IllegalArgumentException.class, () ->
                heroService.getHeroByProperty("test","test",0,1));
    }

    @Test
    public void deleteHeroTest(){
        when(heroRepo.findById(anyLong())).thenReturn(Optional.of(new Hero()));

        heroService.deleteHero(1L);

        verify(heroRepo, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteHeroNotFoundTest(){
        when(heroRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () ->  heroService.deleteHero(1L));
    }

    @Test
    public void deleteHeroPropertyTest(){
        when(heroRepo.findById(anyLong())).thenReturn(Optional.of(new Hero()));
        when(heroPropertyRepo.findByHeroIdAndPropertyValue(anyLong(),anyString()))
                .thenReturn(Optional.of(new HeroProperty()));

        heroService.deleteHeroProperty(1L,"test");

        verify(heroPropertyRepo, times(1)).deleteByHeroIdAndPropertyValue(anyLong(),anyString());
    }

    @Test
    public void deleteHeroPropertyHeroNotFoundTest(){
        when(heroRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () ->  heroService.deleteHeroProperty(1L, "test"));
    }

    @Test
    public void deleteHeroPropertyValueNotFoundTest(){
        when(heroRepo.findById(anyLong())).thenReturn(Optional.of(new Hero()));
        when(heroPropertyRepo.findByHeroIdAndPropertyValue(anyLong(),anyString())).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () ->  heroService.deleteHeroProperty(1L, "test"));
    }
}
