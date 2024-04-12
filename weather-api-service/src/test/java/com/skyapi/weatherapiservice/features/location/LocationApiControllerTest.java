package com.skyapi.weatherapiservice.features.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherapicommon.Location;
import com.skyapi.weatherapiservice.helper.error.LocationNotFoundException;

@WebMvcTest(LocationApiController.class)
class LocationApiControllerTest {

        private static final String END_POINT_PATH = "/v1/locations";

        @Autowired
        MockMvc mockMvc;
        @Autowired
        ObjectMapper mapper;
        @MockBean
        LocationService service;

        @Test
        void testAddShouldReturn400BadRequest() throws Exception {
                Location location = new Location();

                String bodyContent = mapper.writeValueAsString(location);

                mockMvc.perform(
                                MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json")
                                                .content(bodyContent))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testAddShouldReturn200Created() throws Exception {
                Location location = Location.builder()
                                .code("NYC_USA")
                                .cityName("New York City")
                                .regionName("New York")
                                .countryCode("US")
                                .countryName("United State of America")
                                .enabled(true)
                                .build();

                Mockito.when(service.add(location)).thenReturn(location);

                String bodyContent = mapper.writeValueAsString(location);

                mockMvc.perform(
                                MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json")
                                                .content(bodyContent))
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is("NYC_USA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.city_name", is("New York City")))
                                .andExpect(MockMvcResultMatchers.header().string("Location", "/v1/location/NYC_USA"))
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testValidateRequestNullBodyLocationCode() throws Exception {
                Location location = new Location();

                String bodyContent = mapper.writeValueAsString(location);

                mockMvc.perform(
                                MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json")
                                                .content(bodyContent))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                                                is("City name can not be null")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1]",
                                                is("Country code can not be null")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[2]",
                                                is("Location code can not be null")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[3]",
                                                is("Country name can not be null")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[4]",
                                                is("Region name can not be null")))
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testValidateLengthRequestBodyLocationCode() throws Exception {
                Location location = Location.builder()
                                .code("")
                                .cityName("")
                                .regionName("")
                                .countryCode("")
                                .countryName("")
                                .enabled(true)
                                .build();

                String bodyContent = mapper.writeValueAsString(location);

                String resposeBody = mockMvc.perform(
                                MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json")
                                                .content(bodyContent))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                                .andDo(MockMvcResultHandlers.print())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                assertThat(resposeBody).contains("Location code must have 3-12 characters")
                                .contains("City name must have 3-128 characters")
                                .contains("Region name must have 3-128 characters")
                                .contains("Country name must have 3-64 characters")
                                .contains("Country code must have 2 characters");
        }

        @Test
        void testListShouldRetur200Ok() throws Exception {
                Location location1 = Location.builder()
                                .code("NYC_USA")
                                .cityName("New York City")
                                .regionName("New York")
                                .countryCode("US")
                                .countryName("United State of America")
                                .enabled(true)
                                .build();
                Location location2 = Location.builder()
                                .code("LACA_USA")
                                .cityName("Los Angeles")
                                .regionName("California")
                                .countryCode("US")
                                .countryName("United State of America")
                                .enabled(true)
                                .build();

                Mockito.when(service.list()).thenReturn(List.of(location1, location2));

                mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code", is("NYC_USA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city_name", is("New York City")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].code", is("LACA_USA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city_name", is("Los Angeles")))
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testListShouldReturn204NoContent() throws Exception {
                Mockito.when(service.list()).thenReturn(Collections.emptyList());

                mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                                .andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testGetShouldReturn200OK() throws Exception {
                String code = "NYC_USA";
                String requestURI = END_POINT_PATH + "/" + code;
                Location location = Location.builder()
                                .code("NYC_USA")
                                .cityName("New York City")
                                .regionName("New York")
                                .countryCode("US")
                                .countryName("United State of America")
                                .enabled(true)
                                .build();

                Mockito.when(service.get(code)).thenReturn(location);

                mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(code)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.city_name", is("New York City")))
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testGetShouldReturn405MethodNotAlllowed() throws Exception {
                String requestURI = END_POINT_PATH + "/ABDEF";

                mockMvc.perform(MockMvcRequestBuilders.post(requestURI))
                                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testGetShouldReturn404NotFound() throws Exception {
                String code = "/ABDEF";
                String requestURI = END_POINT_PATH + code;

                Mockito.when(service.get(code)).thenReturn(null);

                mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testUpdateShouldReturn400BadRequest() throws Exception {
                Location location = new Location();

                String bodyContent = mapper.writeValueAsString(location);

                mockMvc.perform(
                                MockMvcRequestBuilders.put(END_POINT_PATH).contentType("application/json")
                                                .content(bodyContent))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        void testUpdateShouldReturn404NotFound() throws Exception {
                Location location = Location.builder()
                                .code("NYC_USA")
                                .cityName("New York City")
                                .regionName("New York")
                                .countryCode("US")
                                .countryName("United State of America")
                                .enabled(true)
                                .build();

                Mockito.when(service.update(location)).thenThrow(LocationNotFoundException.class);

                String bodyContent = mapper.writeValueAsString(location);

                mockMvc.perform(
                                MockMvcRequestBuilders.put(END_POINT_PATH).contentType("application/json")
                                                .content(bodyContent))
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testUpdateShouldReturn200OK() throws Exception {
                Location location = Location.builder()
                                .code("NYC_USA")
                                .cityName("New York City")
                                .regionName("New York")
                                .countryCode("US")
                                .countryName("United State of America")
                                .enabled(true)
                                .build();

                String bodyContent = mapper.writeValueAsString(location);

                Mockito.when(service.update(location)).thenReturn(location);

                mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH).contentType("application/json")
                                .content(bodyContent))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is("NYC_USA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.city_name", is("New York City")))
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        void testDeleteShoubleReturn404NotFound() throws Exception {
                String code = "ABCD";
                String deleteEndpoint = END_POINT_PATH + "/" + code;

                Mockito.doThrow(LocationNotFoundException.class).when(service).delete(code);

                mockMvc.perform(MockMvcRequestBuilders.delete(deleteEndpoint).contentType("application/json"))
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testDeleteShouldReturn204NoContent() throws Exception {
                String code = "DELHI_IN";
                String deleteEndpoint = END_POINT_PATH + "/" + code;

                Mockito.doNothing().when(service).delete(code);

                mockMvc.perform(MockMvcRequestBuilders.delete(deleteEndpoint))
                                .andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andDo(MockMvcResultHandlers.print());
        }
}
