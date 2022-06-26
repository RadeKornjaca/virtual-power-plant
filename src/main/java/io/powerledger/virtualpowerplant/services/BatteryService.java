package io.powerledger.virtualpowerplant.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.powerledger.virtualpowerplant.entites.Battery;
import io.powerledger.virtualpowerplant.repositories.BatteryRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BatteryService {

    private BatteryRepository batteryRepository;
    
    public void saveAll(List<Battery> batteries) {
        batteryRepository.saveAll(batteries);
    }
    
    public List<Battery> batteriesInPostcodeRange(int fromPostcode, int toPostcode) {
        final List<Battery> batteries = batteryRepository.findByPostcodeBetweenOrderByNameAsc(fromPostcode, toPostcode);

        return batteries;
    }

    public double totalCapacity(List<Battery> batteries) {
        return batteries.stream()
                        .map(battery -> battery.getWattCapacity())
                        .reduce(0.0, Double::sum);
    }

    public double averageCapacity(List<Battery> batteries) {
        return batteries.stream()
                .map(battery -> battery.getWattCapacity())
                .collect(Collectors.summarizingDouble(Double::doubleValue))
                .getAverage();
    }
}