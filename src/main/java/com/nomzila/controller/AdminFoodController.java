package com.nomzila.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomzila.model.Food;
import com.nomzila.model.Restaurant;
import com.nomzila.model.User;
import com.nomzila.request.CreateFoodRequest;
import com.nomzila.response.MessageResponse;
import com.nomzila.service.FoodService;
import com.nomzila.service.RestaurantService;
import com.nomzila.service.UserService;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {
	
	@Autowired FoodService foodService;
	@Autowired UserService userService;
	@Autowired RestaurantService restaurantService;
	
	
	@PostMapping
	public ResponseEntity<?> createFood(@RequestBody CreateFoodRequest req, 
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user =userService.findUserByJwtToken(jwt);
		
		Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
		
		Food food = foodService.createFood(req, req.getCategory(), restaurant);
		
		return new ResponseEntity<Food>(food,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteFood(@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user =userService.findUserByJwtToken(jwt);
		foodService.deleteFood(id);
		MessageResponse res = new MessageResponse();
		res.setMessage("food deleted successfully");
		return new ResponseEntity<MessageResponse>(res,HttpStatus.OK);
	}
	
	
	
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateFoodAvailability(@PathVariable  Long id,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user =userService.findUserByJwtToken(jwt);
	
		
		Food food = foodService.updateAvailabilityStatus(id);
		
		return new ResponseEntity<Food>(food,HttpStatus.OK);
	}

}
