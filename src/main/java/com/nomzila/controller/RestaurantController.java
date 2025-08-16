package com.nomzila.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomzila.dto.RestaurantDto;
import com.nomzila.model.Restaurant;
import com.nomzila.model.User;
import com.nomzila.service.RestaurantService;
import com.nomzila.service.UserService;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private UserService userService;

	
	@GetMapping("/search")
	public ResponseEntity<?> searchRestaurant(@RequestParam String keyword,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		List<Restaurant> restaurant = restaurantService.searchRestaurants(keyword);
		
		return new ResponseEntity<List<Restaurant>>(restaurant,HttpStatus.OK);
	}
	
	
	@GetMapping
	public ResponseEntity<?> getAllRestaurant(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		List<Restaurant> restaurant = restaurantService.getAllRestaurants();
		
		return new ResponseEntity<List<Restaurant>>(restaurant,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findRestaurantById(@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		Restaurant restaurant = restaurantService.findRestaurantById(id);
		
		return new ResponseEntity<Restaurant>(restaurant,HttpStatus.OK);
	}
	
	@PutMapping("/{id}/add-favorites")
	public ResponseEntity<?> addToFevorites(@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);

		RestaurantDto dto = restaurantService.addToFevorites(id, user);
		
		return new ResponseEntity<RestaurantDto>(dto,HttpStatus.OK);
	}

}
