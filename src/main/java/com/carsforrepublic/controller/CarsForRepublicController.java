package com.carsforrepublic.controller;

import com.carsforrepublic.helper.CSVHelper;
import com.carsforrepublic.message.ResponseMessage;
import com.carsforrepublic.model.Car;
import com.carsforrepublic.service.CarsForRepublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class which will expose endpoints to
 * upload and download the car-lot and calculate final price.
 */
@CrossOrigin("*")
@Controller
@RequestMapping("/api/carsforrepublic")
public class CarsForRepublicController {

	@Autowired
	CarsForRepublicService carsService;

	/**
	 * This API is responsible to upload a csv
	 * and convert it to car lot list and save it
	 * @param file
	 * @return
	 */
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (CSVHelper.hasCSVFormat(file)) {
			try {
				carsService.save(file);


				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	/**
	 * This API is responsible to get all the car list as JSON
	 * @return
	 */
	@GetMapping("/cars")
	public ResponseEntity<List<Car>> getAllCars() {
		try {
			List<Car> cars = carsService.getAllCars();

			if (cars.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(cars, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * This API is responsible to get all the car list as csv file
	 * @return
	 */
	@GetMapping("/download")
	public ResponseEntity<Resource> getFile() {
		String filename = "cars.csv";
		InputStreamResource file = new InputStreamResource(carsService.load());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/csv"))
				.body(file);
	}
}
