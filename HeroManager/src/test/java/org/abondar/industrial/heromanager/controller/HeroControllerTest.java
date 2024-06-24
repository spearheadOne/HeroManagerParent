package org.abondar.industrial.heromanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.industrial.heromanager.exception.HeroExceptionHandler;
import org.abondar.industrial.heromanager.exception.HeroNotFoundException;
import org.abondar.industrial.heromanager.exception.PropertyNotFoundException;
import org.abondar.industrial.heromanager.model.request.HeroCreateRequest;
import org.abondar.industrial.heromanager.model.request.HeroUpdateRequest;
import org.abondar.industrial.heromanager.model.response.HeroCreateResponse;
import org.abondar.industrial.heromanager.model.response.HeroResponse;
import org.abondar.industrial.heromanager.service.HeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({MockitoExtension.class})
public class HeroControllerTest {

    @Mock
    private HeroService heroService;

    @InjectMocks
    private HeroController heroController;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private final HeroResponse heroResponse = new HeroResponse(1L,
            "Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
            "Test", List.of("ass1"));

    @BeforeEach
    public void setup() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(heroController)
                        .setControllerAdvice(new HeroExceptionHandler())
                        .build();

        mapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @Test
    public void createHeroTest() throws Exception {
        var request = new HeroCreateRequest("Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("ass1"));

        var json = mapper.writeValueAsString(request);
        when(heroService.createHero(request)).thenReturn(new HeroCreateResponse(1L));

        mockMvc.perform(post(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.heroId", is(1)));

    }


    @Test
    public void createHeroBadRequestTest() throws Exception {
        var request = new HeroCreateRequest(null, "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("ass1"));

        var json = mapper.writeValueAsString(request);

        mockMvc.perform(post(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void createHeroListBadRequestTest() throws Exception {
        var request = new HeroCreateRequest("TEST", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of());

        var json = mapper.writeValueAsString(request);

        mockMvc.perform(post(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void createHeroBigListBadRequestTest() throws Exception {
        var request = new HeroCreateRequest("TEST", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "f"));

        var json = mapper.writeValueAsString(request);

        mockMvc.perform(post(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void updateHeroTest() throws Exception {
        var request = new HeroUpdateRequest("Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("ass1"));

        var json = mapper.writeValueAsString(request);
        when(heroService.updateHero(1L, request)).thenReturn(heroResponse);

        mockMvc.perform(put(EndpointUtil.API_V1_ROOT+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Hero Name")))
                .andExpect(jsonPath("$.powers.length()").value(1))
                .andExpect(jsonPath("$.powers[0]").value("pwd"));
    }

    @Test
    public void updateHeroBadRequestTest() throws Exception {
        var request = new HeroUpdateRequest("Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "f"));

        var json = mapper.writeValueAsString(request);

        mockMvc.perform(put(EndpointUtil.API_V1_ROOT+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateHeroIdBadRequestTest() throws Exception {
        var request = new HeroUpdateRequest("Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("a", "b", "c", "d", "e"));

        var json = mapper.writeValueAsString(request);

        mockMvc.perform(put(EndpointUtil.API_V1_ROOT+"/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void updateHeroNotFoundTest() throws Exception {
        var request = new HeroUpdateRequest("Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("ass1"));

        var json = mapper.writeValueAsString(request);
        when(heroService.updateHero(1L, request)).thenThrow(HeroNotFoundException.class);

        mockMvc.perform(put(EndpointUtil.API_V1_ROOT+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getHeroByAliasTest() throws Exception {
        when(heroService.getHeroByAlias(anyString())).thenReturn(heroResponse);

        mockMvc.perform(get(EndpointUtil.API_V1_ROOT+"/alias/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.alias", is("Hero Alias")));
    }

    @Test
    public void getHeroByAliasNotFoundTest() throws Exception {
        when(heroService.getHeroByAlias(anyString())).thenThrow(HeroNotFoundException.class);

        mockMvc.perform(get(EndpointUtil.API_V1_ROOT+"/alias/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void getHeroByNameTest() throws Exception {
        when(heroService.getHeroByName(anyString())).thenReturn(heroResponse);

        mockMvc.perform(get(EndpointUtil.API_V1_ROOT+"/name/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Hero Name")));
    }

    @Test
    public void getHeroByNameNotFoundTest() throws Exception {
        when(heroService.getHeroByName(anyString())).thenThrow(HeroNotFoundException.class);

        mockMvc.perform(get(EndpointUtil.API_V1_ROOT+"/name/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllHeroesTest() throws Exception {
        when(heroService.getAllHeroes(0,1)).thenReturn(List.of(heroResponse));

        mockMvc.perform(get(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("offset","0")
                        .queryParam("limit","1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void getAllHeroesLimitBadRequestTest() throws Exception {
        mockMvc.perform(get(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("offset","0")
                        .queryParam("limit","0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllHeroesOffsetBadRequestTest() throws Exception {
        mockMvc.perform(get(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("offset","-1")
                        .queryParam("limit","1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllHeroesNotFoundTest() throws Exception {
        when(heroService.getAllHeroes(0,1)).thenReturn(List.of());

        mockMvc.perform(get(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("offset","0")
                        .queryParam("limit","1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteHeroTest() throws Exception {
        doNothing().when(heroService).deleteHero(anyLong());

        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(heroService, times(1)).deleteHero(anyLong());
    }

    @Test
    public void deleteHeroBadRequestTest() throws Exception {

        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteHeroNotFoundTest() throws Exception {
        doThrow(HeroNotFoundException.class).when(heroService).deleteHero(anyLong());

        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void deleteHeroPropertyTest() throws Exception {
        doNothing().when(heroService).deleteHeroProperty(anyLong(),anyString());

        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/prop/1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(heroService, times(1)).deleteHeroProperty(anyLong(),anyString());
    }

    @Test
    public void deleteHeroPropertyHeroNotFoundTest() throws Exception {
        doThrow(HeroNotFoundException.class).when(heroService).deleteHeroProperty(anyLong(),anyString());

        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/prop/1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteHeroPropertyValueNotFoundTest() throws Exception {
        doThrow(PropertyNotFoundException.class).when(heroService).deleteHeroProperty(anyLong(),anyString());

        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/prop/1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteHeroPropertyIdBadRequestTest() throws Exception {
        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/prop/-1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteHeroPropertyNoPropTest() throws Exception {
        mockMvc.perform(delete(EndpointUtil.API_V1_ROOT+"/prop/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
