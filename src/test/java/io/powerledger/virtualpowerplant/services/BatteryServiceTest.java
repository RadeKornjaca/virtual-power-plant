package io.powerledger.virtualpowerplant.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.powerledger.virtualpowerplant.entites.Battery;
import io.powerledger.virtualpowerplant.repositories.BatteryRepository;

class BatteryServiceTest {

    @Mock
    private BatteryRepository batteryRepository;
    private BatteryService batteryService;
    
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        batteryService = new BatteryService(batteryRepository);
    }
    
    @AfterEach
    void teardown() throws Exception {
        autoCloseable.close();
    }
    
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
    void saveAllBatteries() {
        List<Battery> batteries = Arrays.asList(battery1, battery2, battery3, battery4);

        batteryService.saveAll(batteries);

        verify(batteryRepository).saveAll(batteries);
    }
    
    @Test
    void getAllBatteriesInPostcodeRange() {
        int fromPostcode = anyInt();
        int toPostcode = anyInt();

        batteryService.batteriesInPostcodeRange(fromPostcode, toPostcode);

        verify(batteryRepository).findByPostcodeBetweenOrderByNameAsc(fromPostcode, toPostcode);
    }
    
    @Test
    void givenListOfBatteries_whenTotalCapacity_thenReturnTotalCapacity() {
        List<Battery> batteries = Arrays.asList(battery1, battery2, battery3, battery4);

        double totalCapacity = batteryService.totalCapacity(batteries);

        assertEquals(2800, totalCapacity);
    }

    @Test
    void givenEmptyListOfBatteries_whenTotalCapacity_thenReturnZeroTotalCapacity() {
        List<Battery> batteries = new ArrayList<>();

        double totalCapacity = batteryService.totalCapacity(batteries);

        assertEquals(0, totalCapacity);
    }
    
    @Test
    void givenListOfBatteries_whenAvgCapacity_thenReturnAverageCapacity() {
        List<Battery> batteries = Arrays.asList(battery1, battery2, battery3, battery4);

        double avgCapacity = batteryService.averageCapacity(batteries);

        assertEquals(700.0, avgCapacity);
    }

    @Test
    void givenEmptyListOfBatteries_whenAvgCapacity_thenReturnZeroAverageCapacity() {
        List<Battery> batteries = new ArrayList<>();

        double avgCapacity = batteryService.averageCapacity(batteries);

        assertEquals(0.0, avgCapacity);
    }
}
