package com.nomzila.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nomzila.model.Cart;
import com.nomzila.model.CartItem;
import com.nomzila.model.Food;
import com.nomzila.model.User;
import com.nomzila.repository.CartItemRepository;
import com.nomzila.repository.CartRepository;
import com.nomzila.repository.FoodRepository;
import com.nomzila.repository.UserRepository;
import com.nomzila.request.AddCartItemRequest;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired CartRepository cartRepository;
	@Autowired UserRepository userRepository;
	@Autowired UserService userService;
	@Autowired CartItemRepository cartItemRepository;
	@Autowired FoodRepository foodRepository;
	@Autowired FoodService foodService;
	

	@Override
	public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Food food = foodService.findFoodById(req.getFoodId());
		Cart cart = cartRepository.findByCustomerId(user.getId());
		
		for(CartItem cartItem : cart.getItem()) {
			if(cartItem.getFood().equals(food)) {
				int newQty = cartItem.getQuantity()+req.getQuantity();
				return updateCartItemQuantity(cartItem.getId(),newQty);
			}
		}
		
		CartItem cartItem = CartItem.builder().food(food)
						  .cart(cart)
						  .quantity(req.getQuantity())
						  .ingredients(req.getIngredients())
						  .totalPrice(req.getQuantity()*food.getPrice())
						  .build();
		
		CartItem save = cartItemRepository.save(cartItem);
		cart.getItem().add(save);
		
		return save;
	}

	@Override
	public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
		Optional<CartItem> cartItem =  cartItemRepository.findById(cartItemId);
		if(cartItem.isEmpty()) {
			throw new Exception("cart item not found");
		}
		
		CartItem item = cartItem.get();
		item.setQuantity(quantity);
		
		item.setTotalPrice(item.getFood().getPrice()*quantity);
		return cartItemRepository.save(item);
	}

	@Override
	public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Cart cart = cartRepository.findByCustomerId(user.getId());
		
		Optional<CartItem> cartItem =  cartItemRepository.findById(cartItemId);
		if(cartItem.isEmpty()) {
			throw new Exception("cart item not found");
		}
		
		CartItem item = cartItem.get();
		cart.getItem().remove(item);
		
		return cartRepository.save(cart);
	}

	@Override
	public Double calculateCartTotals(Cart cart) throws Exception {
		Double total = 0d;
		
		for(CartItem cartItem : cart.getItem()) {
			total += cartItem.getFood().getPrice()*cartItem.getQuantity();
		}
		return total;
	}

	@Override
	public Cart findCartById(Long id) throws Exception {
		Optional<Cart> cart = cartRepository.findById(id);
		if(cart.isEmpty()) {
			throw new Exception("cart not found with id "+id);
		}
		return cart.get();
	}

	@Override
	public Cart findCartByUsserId(Long userId) throws Exception {
		 Cart cart = cartRepository.findByCustomerId(userId);
		 cart.setTotal(calculateCartTotals(cart));
		 return cart;
	}

	@Override
	public Cart clearCart(Long userId) throws Exception {
		Cart cart = findCartByUsserId(userId);
		cart.getItem().clear();
		return cartRepository.save(cart);
	}

}
