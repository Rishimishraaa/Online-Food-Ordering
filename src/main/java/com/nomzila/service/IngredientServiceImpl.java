package com.nomzila.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nomzila.model.IngredientCategory;
import com.nomzila.model.IngredientsItem;
import com.nomzila.model.Restaurant;
import com.nomzila.repository.IngredientCategoryRepository;
import com.nomzila.repository.IngredientItemRepository;

@Service
public class IngredientServiceImpl implements IngredientsService{
	
	@Autowired private IngredientCategoryRepository ingredientCategoryRepository;
	@Autowired private IngredientItemRepository ingredientItemRepository;
	@Autowired private RestaurantService restaurantService;
	
	
	@Override
	public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
		Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
		
		IngredientCategory category = new IngredientCategory();
		category.setRestaurant(restaurant);
		category.setName(name);
		return ingredientCategoryRepository.save(category);
	}
	
	
	@Override
	public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
		Optional<IngredientCategory> opt = ingredientCategoryRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new Exception("ingredient category not found");
		}
		return opt.get();
	}
	
	
	@Override
	public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception {
		restaurantService.findRestaurantById(restaurantId);
		return ingredientCategoryRepository.findByRestaurantId(restaurantId);
	}
	
	
	@Override
	public IngredientsItem createIngredientsItem(Long restaurantId, String ingredientName, Long ingredientCategoryId)
			throws Exception {
		
		Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
		IngredientCategory category = findIngredientCategoryById(ingredientCategoryId);
		IngredientsItem item = new IngredientsItem();
		item.setName(ingredientName);
		item.setRestaurant(restaurant);
		item.setCategory(category);
		
		IngredientsItem save = ingredientItemRepository.save(item);
		category.getIngredients().add(save);
		return save;
	}
	@Override
	public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
		return ingredientItemRepository.findByRestaurantId(restaurantId);
	}
	@Override
	public IngredientsItem updateStock(Long id) throws Exception {
		Optional<IngredientsItem> opt = ingredientItemRepository.findById(id);
		if(opt.isEmpty()) {
			throw new Exception("ingredient not found");
		}
		IngredientsItem item = opt.get();
		item.setInStoke(!item.isInStoke());
		return ingredientItemRepository.save(item);
	}
	

}
