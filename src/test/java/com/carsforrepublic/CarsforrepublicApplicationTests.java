package com.carsforrepublic;

import com.carsforrepublic.model.Car;
import com.carsforrepublic.repository.CarsRepository;
import com.carsforrepublic.service.CarsForRepublicService;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CarsforrepublicApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private CarsForRepublicService carsForRepublicService;

	@MockBean
	private CarsRepository carsRepository;


	@Test
	public void getAllCarsTest(){
		List<Car> collect = Stream.of(Car.builder().carNumber(2).amphibious(true).basePrice(150.0).
				finalPrice(300.0).colour("METTALIC").noWheelDrive(2).isErrorRecord(false).build()).collect(Collectors.toList());
		when(carsRepository.findAll(Sort.by(Sort.Direction.ASC, "carNumber"))).thenReturn(collect);
		assertEquals(1,carsForRepublicService.getAllCars().size());
	}

	@Test
	public void loadCarTest(){
		List<Car> collect = Stream.of(Car.builder().carNumber(2).amphibious(true).basePrice(150.0).
				finalPrice(300.0).colour("METTALIC").noWheelDrive(2).isErrorRecord(false).build()).collect(Collectors.toList());
		when(carsRepository.findAll(Sort.by(Sort.Direction.ASC, "carNumber"))).thenReturn(collect);
		assert(carsForRepublicService.load() instanceof ByteArrayInputStream);
	}

	@Test
	public void uploadCarTest(){
		MockMultipartFile file
				= new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"13,200,CHROMATIC,2,TRUE".getBytes()
		);
		List<Car> collect = Stream.of(Car.builder().carNumber(2).amphibious(true).basePrice(150.0).
				finalPrice(300.0).colour("METTALIC").noWheelDrive(2).isErrorRecord(false).build()).collect(Collectors.toList());
		when(carsRepository.saveAll(collect)).thenReturn(collect);
		carsForRepublicService.save(file);
		verify(carsRepository,times(1)).saveAll(collect);
	}


}
