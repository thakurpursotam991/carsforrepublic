package com.carsforrepublic.repository;

import com.carsforrepublic.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarsRepository extends JpaRepository<Car, Long>{

}
