package com.carsforrepublic.service;

import com.carsforrepublic.configuration.PriceConfig;
import com.carsforrepublic.helper.CSVHelper;
import com.carsforrepublic.model.Car;
import com.carsforrepublic.repository.CarsRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 */
@Service
public class CarsForRepublicService {

	@Autowired
	private CarsRepository repository;

	@Autowired
	private PriceConfig priceConfig;



	
	public void save(MultipartFile file) {
		try {
			List<Car> cars = CSVHelper.csvToCars(file.getInputStream());
			repository.saveAll(cars);
		} catch (IOException e) {
			throw new RuntimeException("Fail to store csv data: " + e.getMessage());
		}
	}
	
	public ByteArrayInputStream load() {

		List<Car> cars = repository.findAll(Sort.by("carNumber"));
		ByteArrayInputStream in = CSVHelper.carsToCSV(cars);
		return in;
	}
	
	public List<Car> getAllCars() {
		List<Car> list= repository.findAll(Sort.by("carNumber"));
		return list;
	}
}
