package io.powerledger.virtualpowerplant.dto;

import java.util.List;

import io.powerledger.virtualpowerplant.entites.Battery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatteriesInRangeDTO {

    List<Battery> batteries;
    double totalCapacity;
    double avgCapacity;
}
