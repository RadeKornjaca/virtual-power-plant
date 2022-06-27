package io.powerledger.virtualpowerplant.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatteriesInRangeDTO {
    List<BatteryDto> batteries;
    double totalCapacity;
    double avgCapacity;
}
