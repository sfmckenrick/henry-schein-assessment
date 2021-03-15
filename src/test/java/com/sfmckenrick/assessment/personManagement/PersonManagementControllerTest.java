package com.sfmckenrick.assessment.personManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfmckenrick.assessment.authentication.Authority;
import com.sfmckenrick.assessment.authentication.Role;
import com.sfmckenrick.assessment.personManagement.exception.PersonNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.Date;


@SpringBootTest
@AutoConfigureMockMvc
class PersonManagementControllerTest {

    // Endpoints
    private final String GET_MAPPING = "/v1/get-person/{personId}";
    private final String DELETE_MAPPING = "/v1/delete-person/{personId}";
    private final String POST_MAPPING = "/v1/post-person/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonManagementService service;

    @BeforeEach
    public void clean() {
        service.clear();
    }

    @Test
    @WithMockUser(username = "public", password = "password", roles = Role.USER, authorities = Authority.READ)
    void testGetPersonByIdExists() throws Exception {
        Person expected = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(expected);

        Person result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders
                .get(GET_MAPPING, expected.getId()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString(),
        Person.class);

        Assertions.assertEquals(expected, result);
    }

    @Test
    @WithMockUser(username = "public", password = "password", roles = Role.USER, authorities = Authority.READ)
    void testGetPersonByIdNotExists() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders
                .get(GET_MAPPING, 1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(result.contains("Unable"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = Authority.WRITE)
    void testDeletePersonByIdExists() throws Exception {
        Person expected = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(expected);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(DELETE_MAPPING, expected.getId()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThrows(PersonNotFoundException.class, () -> service.getPersonById(expected.getId()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = Authority.WRITE)
    void testDeletePersonByIdNotExists() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders
                .delete(DELETE_MAPPING, 1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(result.contains("Unable"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = Authority.WRITE)
    void testInsertPerson() throws Exception {
        Person person = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        String json = new ObjectMapper().writeValueAsString(person);

        Person result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders
                        .post(POST_MAPPING)
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
    @WithMockUser(username = "admin", authorities = Authority.WRITE)
    void testInsertPersonMalformed() throws Exception {
        Person person = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        String json = new ObjectMapper().writeValueAsString(person).replace("first", "frist");

        String result = mockMvc.perform(MockMvcRequestBuilders
                        .post(POST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                        .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue(result.contains("Data integrity"));
    }
}