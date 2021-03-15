package com.sfmckenrick.assessment.personManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfmckenrick.assessment.authentication.JwtRequest;
import com.sfmckenrick.assessment.authentication.JwtResponse;
import com.sfmckenrick.assessment.personManagement.exception.PersonNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Base64Utils;

import java.util.Date;


@SpringBootTest
@AutoConfigureMockMvc
class PersonManagementControllerTest {

    // Endpoints
    private final String GET_MAPPING = "/v1/get-person/{personId}";
    private final String DELETE_MAPPING = "/v1/delete-person/{personId}";
    private final String POST_MAPPING = "/v1/post-person/";
    private final String TOKEN_MAPPING = "/v1/auth/token/";

    // Authentication tokens.
    private final String BASIC_ADMIN = "Basic " + Base64Utils.encodeToString("admin:password".getBytes());
    private final String BASIC_PUBLIC = "Basic " + Base64Utils.encodeToString("public:password".getBytes());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonManagementService service;

    @BeforeEach
    public void clean() {
        service.clear();
    }

    @Test
    public void testGetPersonByIdExistsBasicAdmin() throws Exception {
        Person expected = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(expected);

        Person result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders
                .get(GET_MAPPING, expected.getId()).header(HttpHeaders.AUTHORIZATION, BASIC_ADMIN))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString(),
        Person.class);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testGetPersonByIdNotExistsBasicAdmin() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders
                .get(GET_MAPPING, 1).header(HttpHeaders.AUTHORIZATION, BASIC_ADMIN))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(result.contains("Unable"));
    }

    @Test
    public void testDeletePersonByIdExistsBasicAdmin() throws Exception {
        Person expected = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(expected);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(DELETE_MAPPING, expected.getId()).header(HttpHeaders.AUTHORIZATION, BASIC_ADMIN))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThrows(PersonNotFoundException.class, () -> service.getPersonById(expected.getId()));
    }

    @Test
    public void testDeletePersonByIdNotExistsBasicAdmin() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders
                .delete(DELETE_MAPPING, 1).header(HttpHeaders.AUTHORIZATION, BASIC_ADMIN))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(result.contains("Unable"));
    }

    @Test
    public void testInsertPersonBasicAdmin() throws Exception {
        Person person = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        String json = new ObjectMapper().writeValueAsString(person);

        Person result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders
                        .post(POST_MAPPING).header(HttpHeaders.AUTHORIZATION, BASIC_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                        .andReturn().getResponse().getContentAsString(),
                Person.class);

        Assertions.assertEquals(person.getFirstName(), result.getFirstName());
        Assertions.assertEquals(person.getMiddleName(), result.getMiddleName());
        Assertions.assertEquals(person.getLastName(), result.getLastName());
        Assertions.assertEquals(person.getDateOfBirth(), result.getDateOfBirth());
        Assertions.assertNotNull(result.getId());
        Assertions.assertNull(person.getId());
        Assertions.assertNotNull(service.getPersonById(result.getId()));
    }

    @Test
    public void testInsertPersonMalformedBasicAdmin() throws Exception {
        Person person = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        String json = new ObjectMapper().writeValueAsString(person).replace("first", "frist");

        String result = mockMvc.perform(MockMvcRequestBuilders
                        .post(POST_MAPPING).header(HttpHeaders.AUTHORIZATION, BASIC_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                        .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(result.contains("Data integrity"));
    }

    @Test
    public void testIncorrectRoleBasicPublic() throws Exception {
        Person expected = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(expected);

        String result = mockMvc.perform(MockMvcRequestBuilders
                        .get(GET_MAPPING, expected.getId()).header(HttpHeaders.AUTHORIZATION, BASIC_PUBLIC))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                        .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testGetWithJwt() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("public", "password");
        String json = new ObjectMapper().writeValueAsString(jwtRequest);
        JwtResponse jwt = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.post(TOKEN_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString(),
                JwtResponse.class);

        Person expected = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(expected);

        Person result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders
                        .get(GET_MAPPING, expected.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken()))
                        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                        .andReturn().getResponse().getContentAsString(),
                Person.class);

        Assertions.assertEquals(expected, result);

    }
}