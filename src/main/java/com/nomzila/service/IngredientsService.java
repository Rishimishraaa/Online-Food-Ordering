package com.nomzila.service;

import java.util.List;

import com.nomzila.model.IngredientCategory;
import com.nomzila.model.IngredientsItem;

public interface IngredientsService {

	public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;
	
	public IngredientCategory findIngredientCategoryById(Long id) throws Exception;
	
	public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception;
	
	public IngredientsItem createIngredientsItem(Long restaurantId, String ingredientName, Long ingredientCategoryId) throws Exception;
	
	public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId);
	
	public IngredientsItem updateStock(Long id) throws Exception;
}
