package io.powerledger.virtualpowerplant.controllers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.powerledger.virtualpowerplant.dto.BatteryDto;
import io.powerledger.virtualpowerplant.entites.Battery;
import io.powerledger.virtualpowerplant.services.BatteryService;

@WebMvcTest(BatteryController.class)
class BatteryControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper mapper;
    
    @Autowired
    ModelMapper modelMapper;
    
    @MockBean
    BatteryService batteryService;
    
    Battery battery1 = Battery.builder()
            .id(1l)
            .name("TestBattery1")
            .postcode(21000)
            .wattCapacity(800.0)
            .build();
    Battery battery2 = Battery.builder()
            .id(2l)
            .name("TestBattery2")
            .postcode(22000)
            .wattCapacity(500.0)
            .build();
    Battery battery3 = Battery.builder()
            .id(1l)
            .name("TestBattery3")
            .postcode(23000)
            .wattCapacity(500.0)
            .build();
    Battery battery4 = Battery.builder()
            .id(1l)
            .name("TestBattery4")
            .postcode(11000)
            .wattCapacity(1000.0)
            .build();

    @Test
    void getAllBatteriesBetweenPostcodesSortedByNameAsc_success() throws Exception {
        int fromPostcode = 21000;
        int toPostcode = 23000;;

        List<Battery> batteries = Arrays.asList(battery1, battery2, battery3);
        
        Mockito.when(batteryService.batteriesInPostcodeRange(fromPostcode, toPostcode)).thenReturn(batteries);
        Mockito.when(batteryService.totalCapacity(batteries)).thenReturn(1800.0);
        Mockito.when(batteryService.averageCapacity(batteries)).thenReturn(600.0);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/batteries")
                .param("fromPostcode", Integer.toString(fromPostcode))
                .param("toPostcode", Integer.toString(toPostcode))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteries", hasSize(3)))
                .andExpect(jsonPath("$.batteries[0].name").exists())
                .andExpect(jsonPath("$.batteries[0].postcode").exists())
                .andExpect(jsonPath("$.batteries[0].wattCapacity").exists())
                .andExpect(jsonPath("$.batteries[1].name").exists())
                .andExpect(jsonPath("$.batteries[1].postcode").exists())
                .andExpect(jsonPath("$.batteries[1].wattCapacity").exists())
                .andExpect(jsonPath("$.batteries[2].name").exists())
                .andExpect(jsonPath("$.batteries[2].postcode").exists())
                .andExpect(jsonPath("$.batteries[2].wattCapacity").exists())
                .andExpect(jsonPath("$.totalCapacity").value(1800.0))
                .andExpect(jsonPath("$.avgCapacity").value(600.0))
                .andReturn();
    }
    
    @Test
    void getAllBatteriesBetweenPostcodesSortedByNameAsc_emptyResponse_success() throws Exception {
        int fromPostcode = 21000;
        int toPostcode = 23000;

        List<Battery> batteries = new ArrayList<Battery>();

        Mockito.when(batteryService.batteriesInPostcodeRange(fromPostcode, toPostcode)).thenReturn(batteries);
        Mockito.when(batteryService.totalCapacity(batteries)).thenReturn(0.0);
        Mockito.when(batteryService.averageCapacity(batteries)).thenReturn(0.0);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/batteries")
                .param("fromPostcode", Integer.toString(fromPostcode))
                .param("toPostcode", Integer.toString(toPostcode))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.batteries").isEmpty())
                .andExpect(jsonPath("$.totalCapacity").value(0.0))
                .andExpect(jsonPath("$.avgCapacity").value(0.0))
                .andReturn();
    }
    
    @Test
    void addBatteries_success() throws Exception {
        List<Battery> batteries = Arrays.asList(battery1, battery2, battery3, battery4);
        List<Battery> batteriesWithNullIds = batteries.stream().map(battery -> Battery.builder()
                                                                                      .id(null)
                                                                                      .name(battery.getName())
                                                                                      .postcode(battery.getPostcode())
                                                                                      .wattCapacity(battery.getWattCapacity())
                                                                                      .build())
                                                                .collect(Collectors.toList());
        List<BatteryDto> batteriesDto = batteries.stream()
                                                 .map(battery -> modelMapper.map(battery, BatteryDto.class))
                                                 .collect(Collectors.toList());
        
        Mockito.when(batteryService.saveAll(batteriesWithNullIds)).thenReturn(batteries);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(batteriesDto));
        
        mockMvc.perform(mockRequest)
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$", hasSize(4)))
               .andExpect(jsonPath("$[0].name").exists())
               .andExpect(jsonPath("$[0].postcode").exists())
               .andExpect(jsonPath("$[0].wattCapacity").exists())
               .andExpect(jsonPath("$[1].name").exists())
               .andExpect(jsonPath("$[1].postcode").exists())
               .andExpect(jsonPath("$[1].wattCapacity").exists())
               .andExpect(jsonPath("$[2].name").exists())
               .andExpect(jsonPath("$[2].postcode").exists())
               .andExpect(jsonPath("$[2].wattCapacity").exists())
               .andExpect(jsonPath("$[3].name").exists())
               .andExpect(jsonPath("$[3].postcode").exists())
               .andExpect(jsonPath("$[3].wattCapacity").exists())
               .andReturn();
    }
}
