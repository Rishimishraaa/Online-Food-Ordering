package com.nomzila.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomzila.model.Food;
import com.nomzila.model.User;
import com.nomzila.service.FoodService;
import com.nomzila.service.RestaurantService;
import com.nomzila.service.UserService;

@RestController
@RequestMapping("/api/food")
public class FoodController {
	

	@Autowired FoodService foodService;
	@Autowired UserService userService;
	@Autowired RestaurantService restaurantService;
	
	
	@GetMapping("/search")
	public ResponseEntity<?> searchFood(@RequestParam String keyword, 
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user =userService.findUserByJwtToken(jwt);
		
		List<Food> foods = foodService.searchFood(keyword);
		
		return new ResponseEntity<List<Food>>(foods,HttpStatus.OK);
	}
	
	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<?> getRestaurantFood(@PathVariable Long restaurantId,
			@RequestParam boolean vagetarian,
			@RequestParam boolean seasonal,
			@RequestParam boolean nonveg,
			@RequestParam(required = false) String foodCategory,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user =userService.findUserByJwtToken(jwt);
		
		List<Food> foods = foodService.getRestaurantFood(restaurantId, vagetarian, nonveg, seasonal, foodCategory);
		
		return new ResponseEntity<List<Food>>(foods,HttpStatus.OK);
	}

}
