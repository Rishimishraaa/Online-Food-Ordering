package com.nomzila.service;

import java.util.List;

import com.nomzila.model.Category;
import com.nomzila.model.Food;
import com.nomzila.model.Restaurant;
import com.nomzila.request.CreateFoodRequest;

public interface FoodService {
	public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);
	void deleteFood(Long foodId) throws Exception;
	
	public List<Food> getRestaurantFood(Long restaurantId, boolean isVegitarian, 
			boolean isNonveg, boolean isSeasonal, 
			String foodCategory);
	
	public List<Food> searchFood(String keyword);
	public Food findFoodById(Long id) throws Exception;
	public Food updateAvailabilityStatus(Long foodId) throws Exception;
	
	
	
}
