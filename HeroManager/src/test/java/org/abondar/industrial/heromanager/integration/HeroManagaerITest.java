package org.abondar.industrial.heromanager.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.industrial.heromanager.controller.EndpointUtil;
import org.abondar.industrial.heromanager.model.request.HeroCreateRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Tag("integration")
public class HeroManagaerITest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.0")
            .withExposedPorts(3306)
            .withDatabaseName("herodb")
            .withUsername("hero")
            .withPassword("hero123");
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        mapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @BeforeAll
    public static void init() {
        mySQLContainer.start();
    }


    @Test
    public void createHeroTest() throws Exception {
        var request = new HeroCreateRequest("Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("ass1"));

        var json = mapper.writeValueAsString(request);

        mockMvc.perform(post(EndpointUtil.API_V1_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));

    }

    @Test
    public void findHeroTest() throws Exception {
        var request = new HeroCreateRequest("Hero Alias", "Hero Name", List.of("pwd"), List.of("we"),
                "Test", List.of("ass1"));

        var json = mapper.writeValueAsString(request);

        mockMvc.perform(post(EndpointUtil.API_V1_ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get(EndpointUtil.API_V1_ROOT + "/alias/Hero Alias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Hero Name")));

    }


}
