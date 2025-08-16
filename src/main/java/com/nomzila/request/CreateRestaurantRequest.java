package com.nomzila.request;
import java.util.List;

import com.nomzila.model.Address;
import com.nomzila.model.ContactInformation;

import lombok.Data;

@Data
public class CreateRestaurantRequest {
	private Long id;
	private String name;
	private String description;
	private String cuisineType;
	private Address address;
	private ContactInformation contactInformation;
	private String openingHours;
	private List<String> images;
}
