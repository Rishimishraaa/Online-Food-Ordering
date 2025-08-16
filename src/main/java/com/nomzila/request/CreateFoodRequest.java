package com.nomzila.request;

import java.util.List;

import com.nomzila.model.Category;
import com.nomzila.model.IngredientsItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodRequest {

	private String name;
	private String description;
	private Double price;
	private Category category;
	private List<String> images;
	private Long restaurantId;
	private boolean vegetarian;
	private boolean seasional;
	private List<IngredientsItem> ingredients;
}
