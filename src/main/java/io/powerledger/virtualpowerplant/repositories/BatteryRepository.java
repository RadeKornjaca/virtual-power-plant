package io.powerledger.virtualpowerplant.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.powerledger.virtualpowerplant.entites.Battery;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {

    List<Battery> findByPostcodeBetweenOrderByNameAsc(int fromPostcode, int toPostcode);
}
