package com.carsforrepublic.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
	@Id
	@Column(name = "ID")
	@SequenceGenerator(
			name = "cars_sequence",
			sequenceName = "cars_sequence",
			allocationSize = 1
	)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "cars_sequence"
	)
	private long id;

	@Column(name = "CAR")
	private String carNumber;

	@Column(name = "BASE_PRICE")
	private  String basePrice;

	@Column(name = "COLOUR")
	private String colour;
	
	@Column(name = "NO_WHEEL_DRIVE")
	private String noWheelDrive;
	
	@Column(name = "AMPHIBIOUS")
	private String amphibious;

	@Column(name = "IS_ERROR")
	private boolean isErrorRecord;

	@Column(name = "FINAL_PRICE")
	private Double finalPrice;

	@Column(name = "ERROR")
	private String error;

	@Override
	public String toString() {
		return "Car [id=" + id + ", carNumber=" + carNumber + ", basePrice=" + basePrice + ", colour=" + colour + ", noWheelDrive=" + noWheelDrive + ", amphibious=" + amphibious
				+ "]";
	}
	
}
