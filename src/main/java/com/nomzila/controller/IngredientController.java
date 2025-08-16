package com.nomzila.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomzila.model.IngredientCategory;
import com.nomzila.model.IngredientsItem;
import com.nomzila.request.IngredientCategoryRequest;
import com.nomzila.request.IngredientItemRequest;
import com.nomzila.service.IngredientsService;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {

	@Autowired private IngredientsService ingredientsService;
	
	
	@PostMapping("/category")
	public ResponseEntity<?> createIngredientCategory(
			@RequestBody IngredientCategoryRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		IngredientCategory item = ingredientsService.createIngredientCategory(req.getName(), req.getRestaurantId());
		return new ResponseEntity<IngredientCategory>(item,HttpStatus.CREATED);
	}
	
	@PostMapping
	public ResponseEntity<?> createIngredientItem(
			@RequestBody IngredientItemRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		IngredientsItem ingredientsItem = ingredientsService.createIngredientsItem(req.getRestaurantId(), req.getName(), req.getCategoryId());
		return new ResponseEntity<IngredientsItem>(ingredientsItem,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}/stoke")
	public ResponseEntity<?> updateIngredientStoke(
			@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		IngredientsItem ingredientsItem = ingredientsService.updateStock(id);
		return new ResponseEntity<IngredientsItem>(ingredientsItem,HttpStatus.OK);
	}
	
	
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<?> getRestaurantIngredient(
			@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		List<IngredientsItem> list = ingredientsService.findRestaurantsIngredients(id);
		return new ResponseEntity<List<IngredientsItem>>(list,HttpStatus.OK);
	}
	
	
	@GetMapping("/restaurant/{id}/category")
	public ResponseEntity<?> getRestaurantIngredientCategory(
			@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		List<IngredientCategory> catList = ingredientsService.findIngredientCategoryByRestaurantId(id);
		return new ResponseEntity<List<IngredientCategory>>(catList,HttpStatus.OK);
	}

}
