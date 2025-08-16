package com.nomzila.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nomzila.model.Category;
import com.nomzila.model.Restaurant;
import com.nomzila.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	
	@Autowired RestaurantService restaurantService;
	@Autowired CategoryRepository categoryRepository;
	
	@Override
	public Category createCategory(String name, Long userId) throws Exception {
		Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
		
		return categoryRepository.save(Category.builder().name(name)
						.restaurant(restaurant)
						.build());
	}

	@Override
	public List<Category> findCategoryByRestaurantId(Long id) throws Exception {
		Restaurant restaurant = restaurantService.getRestaurantByUserId(id);
		return categoryRepository.findByRestaurantId(restaurant.getId());
	}

	@Override
	public Category findCategoryById(Long id) throws Exception {
		Optional<Category> category = categoryRepository.findById(id);
		if(category.isEmpty()) {
			throw new Exception("category not found");
		}
		return category.get();
	}

}
