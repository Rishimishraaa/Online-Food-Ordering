package com.nomzila.service;

import java.util.List;

import com.nomzila.dto.RestaurantDto;
import com.nomzila.model.Restaurant;
import com.nomzila.model.User;
import com.nomzila.request.CreateRestaurantRequest;

public interface RestaurantService {
	public Restaurant createRestaurant(CreateRestaurantRequest req, User user);
	public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception;
	public void deleteRestaurant(Long restaurantId) throws Exception;
	public List<Restaurant> getAllRestaurants();
	public List<Restaurant> searchRestaurants(String keyword);
	public Restaurant findRestaurantById(Long restaurantId)throws Exception;
	public Restaurant getRestaurantByUserId(Long userId) throws Exception;
	public RestaurantDto addToFevorites(Long restaurantId, User user) throws Exception;
	public Restaurant updateRestaurantStatus(Long id) throws Exception;
}
