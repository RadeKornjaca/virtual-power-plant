package io.powerledger.virtualpowerplant.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.powerledger.virtualpowerplant.entites.Battery;
import io.powerledger.virtualpowerplant.repositories.BatteryRepository;

@DataJpaTest
class BatteryRepositoryTest {
    
    @Autowired
    private BatteryRepository batteryRepository;

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
            .id(3l)
            .name("TestBattery3")
            .postcode(23000)
            .wattCapacity(500.0)
            .build();
    Battery battery4 = Battery.builder()
            .id(4l)
            .name("TestBattery4")
            .postcode(11000)
            .wattCapacity(1000.0)
            .build();
    
    @Test
    void givenBatteriesList_saveAll_success() throws Exception {
        List<Battery> batteries = Arrays.asList(battery1, battery2, battery3, battery4);
        List<Battery> savedBatteries = batteryRepository.saveAll(batteries);
        
        assertEquals(4, savedBatteries.size());
    }
    
    @Test
    void givenFromPostcodeToPostcode_findByPostcodeBetweenOrderByNameAsc_success() throws Exception {
        List<Battery> batteries = Arrays.asList(battery1, battery2, battery3, battery4);
        batteryRepository.saveAll(batteries);
        
        int fromPostcode = 11000;
        int toPostcode = 22000;
        
        List<Battery> filteredBatteries = batteryRepository.findByPostcodeBetweenOrderByNameAsc(fromPostcode, toPostcode);
        
        assertEquals(3, filteredBatteries.size());
        assertEquals(1, filteredBatteries.get(0).getId());
        assertEquals(2, filteredBatteries.get(1).getId());
        assertEquals(4, filteredBatteries.get(2).getId());
    }
}
