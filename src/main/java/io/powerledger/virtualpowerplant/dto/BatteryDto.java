package io.powerledger.virtualpowerplant.dto;

import lombok.Data;

@Data
public class BatteryDto {
	private long id;
	private String name;
	private int postcode;
	private double wattCapacity;
}
