package com.nomzila.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nomzila.dto.RestaurantDto;
import com.nomzila.model.Address;
import com.nomzila.model.Restaurant;
import com.nomzila.model.User;
import com.nomzila.repository.AddressRepository;
import com.nomzila.repository.RestaurantRepository;
import com.nomzila.repository.UserRepository;
import com.nomzila.request.CreateRestaurantRequest;

@Service
public class RestaurantServiceImpl implements RestaurantService{

	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserService userService;
	
	@Override
	public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
		Address address = addressRepository.save(req.getAddress());
		
		Restaurant restaurant = Restaurant.builder().address(address)
							.contactInformation(req.getContactInformation())
							.cuisineType(req.getCuisineType())
							.description(req.getDescription())
							.images(req.getImages())
							.name(req.getName())
							.openingHours(req.getOpeningHours())
							.registrationDate(LocalDateTime.now())
							.owner(user)
							.build();
		
		return restaurantRepository.save(restaurant);
	}

	@Override
	public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
		
		Restaurant restaurant = findRestaurantById(restaurantId);
		
		if(restaurant.getCuisineType()!=null) {
			restaurant.setCuisineType(updatedRestaurant.getCuisineType());
		}
		
		if(restaurant.getDescription()!=null) {
			restaurant.setDescription(updatedRestaurant.getDescription());
		}
		
		if(restaurant.getName()!=null) {
			restaurant.setName(updatedRestaurant.getName());
		}
		
		return restaurantRepository.save(restaurant);
	}

	@Override
	public void deleteRestaurant(Long restaurantId) throws Exception {
		Restaurant restaurant = findRestaurantById(restaurantId);
		restaurantRepository.delete(restaurant);
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	@Override
	public List<Restaurant> searchRestaurants(String keyword) {
		return restaurantRepository.findBySearchQuery(keyword);
	}

	@Override
	public Restaurant findRestaurantById(Long restaurantId) throws Exception {
		Optional<Restaurant> opt = restaurantRepository.findById(restaurantId);
		if(opt.isEmpty()) {
			throw new Exception("restaurant not found with id "+restaurantId);
		}
		return opt.get();
	}

	@Override
	public Restaurant getRestaurantByUserId(Long userId) throws Exception {
		Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
		if(restaurant==null) {
			throw new Exception("restaurant not found with owner id "+userId);
		}
		return restaurant;
	}

	@Override
	public RestaurantDto addToFevorites(Long restaurantId, User user) throws Exception {
		
		Restaurant restaurant = findRestaurantById(restaurantId);
		RestaurantDto dto = RestaurantDto.builder().description(restaurant.getDescription())
							   .images(restaurant.getImages())
							   .title(restaurant.getName())
							   .id(restaurantId)
							   .build();
		
	
		boolean isFevorited = false;
		List<RestaurantDto> fevorites = user.getFavorites();
		for(RestaurantDto fevorite : fevorites) {
			if(fevorite.getId().equals(restaurantId)) {
				isFevorited=true;
				break;
			}
		}
		
		if(isFevorited) {
			fevorites.removeIf(fevorite -> fevorite.getId().equals(restaurantId));
		}else {
			fevorites.add(dto);
		}
		
		userRepository.save(user);
		return dto;
	}

	@Override
	public Restaurant updateRestaurantStatus(Long id) throws Exception {
		Restaurant restaurant = findRestaurantById(id);
		restaurant.setOpen(!restaurant.isOpen());
		return restaurantRepository.save(restaurant);
	}

}
