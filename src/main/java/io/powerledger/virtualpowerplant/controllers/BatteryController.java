package io.powerledger.virtualpowerplant.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.powerledger.virtualpowerplant.dto.BatteriesInRangeDTO;
import io.powerledger.virtualpowerplant.dto.BatteryDto;
import io.powerledger.virtualpowerplant.entites.Battery;
import io.powerledger.virtualpowerplant.services.BatteryService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/batteries")
@AllArgsConstructor
public class BatteryController {

    private final BatteryService batteryService;
    private final ModelMapper modelMapper;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void addBatteriesEndpoint(@RequestBody List<BatteryDto> batteryListDto) {
        final List<Battery> batteryList = batteryListDto.stream()
                .map(batteryDto -> modelMapper.map(batteryDto, Battery.class))
                .collect(Collectors.toList());

        batteryService.saveAll(batteryList);
    }
    
    @GetMapping
    public @ResponseBody BatteriesInRangeDTO getBatteriesInRangeWithStatisticsEndpoint(
            @RequestParam int fromPostcode,
            @RequestParam int toPostcode) {
        final List<Battery> batteries = batteryService.batteriesInPostcodeRange(fromPostcode, toPostcode);

        final double totalCapacity = batteryService.totalCapacity(batteries);
        final double avgCapacity = batteryService.averageCapacity(batteries);

        var batteriesInRange = new BatteriesInRangeDTO(batteries, totalCapacity, avgCapacity);

        return batteriesInRange;
    }

}
