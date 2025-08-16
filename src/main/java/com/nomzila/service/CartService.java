package com.nomzila.service;

import com.nomzila.model.Cart;
import com.nomzila.model.CartItem;
import com.nomzila.request.AddCartItemRequest;

public interface CartService {

	
	public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception;
	public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;
	public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;
	public Double calculateCartTotals(Cart cart) throws Exception;
	
	public Cart findCartById(Long id)throws Exception;
	public Cart findCartByUsserId(Long userId) throws Exception;
	public Cart clearCart(Long userId) throws Exception;
	
}
