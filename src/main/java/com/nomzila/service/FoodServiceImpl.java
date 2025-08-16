package com.nomzila.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nomzila.model.Category;
import com.nomzila.model.Food;
import com.nomzila.model.Restaurant;
import com.nomzila.repository.FoodRepository;
import com.nomzila.request.CreateFoodRequest;

@Service
public class FoodServiceImpl implements FoodService{
	
	@Autowired private FoodRepository foodRepository;


	@Override
	public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {
		
		Food food = Food.builder().foodCategory(category)
					  .restaurant(restaurant)
					  .description(req.getDescription())
					  .images(req.getImages())
					  .name(req.getName())
					  .price(req.getPrice())
					  .ingredients(req.getIngredients())
					  .isSeasonal(req.isSeasional())
					  .isVegetarian(req.isVegetarian())
					  .build();
		
		Food saveFood = foodRepository.save(food);
		restaurant.getFoods().add(saveFood);
		
		return saveFood;
	}

	@Override
	public void deleteFood(Long foodId) throws Exception {
		Food food  = findFoodById(foodId);
		food.setRestaurant(null);
		foodRepository.save(food);
		
	}

	@Override
	public List<Food> getRestaurantFood(Long restaurantId, 
			boolean isVegitarian,
			boolean isNonveg, 
			boolean isSeasonal,
			String foodCategory) {
		
		List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
		
		if(isVegitarian) {
			foods=filterByVegiterian(foods);
		}
		
		if(isNonveg) {
			foods = filterByNonveg(foods);
		}
		
		if(isSeasonal) {
			foods = filterBySeasonal(foods);
		}
		
		if(foodCategory!=null && !foodCategory.equals("")) {
			foods = filterByCategory(foods, foodCategory);
		}
		
		return foods;
	}

	private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
		
		return foods.stream().filter(food -> {
			if(food.getFoodCategory()!=null) {
				return food.getFoodCategory().getName().equals(foodCategory);
			}
			return false;
		}).collect(Collectors.toList());
	}

	private List<Food> filterBySeasonal(List<Food> foods) {
		return foods.stream().filter(food -> food.isSeasonal()==true).collect(Collectors.toList());
	}

	private List<Food> filterByNonveg(List<Food> foods) {
	 return foods.stream().filter(food -> food.isVegetarian()==false).collect(Collectors.toList());
	}

	private List<Food> filterByVegiterian(List<Food> foods) {
		return foods.stream().filter(food -> food.isVegetarian()==true).collect(Collectors.toList());
	}
	
	
	
	

	@Override
	public List<Food> searchFood(String keyword) {
		
		return foodRepository.searchFood(keyword);
	}

	@Override
	public Food findFoodById(Long id) throws Exception {
		Optional<Food> food = foodRepository.findById(id);
		
		if(!food.isEmpty()) {
			throw new Exception("food not exists");
		}
		return food.get();
	}

	@Override
	public Food updateAvailabilityStatus(Long foodId) throws Exception {
			Food food = findFoodById(foodId);
			food.setAvailable(!food.isAvailable());
		return foodRepository.save(food);
	}

}
