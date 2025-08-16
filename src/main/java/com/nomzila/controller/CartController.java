package com.nomzila.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomzila.model.Cart;
import com.nomzila.model.CartItem;
import com.nomzila.model.User;
import com.nomzila.request.AddCartItemRequest;
import com.nomzila.request.UpdateCartItemRequest;
import com.nomzila.service.CartService;
import com.nomzila.service.UserService;

@RestController
@RequestMapping("/api")
public class CartController {

   @Autowired private CartService cartService;
   @Autowired private UserService userService;
   
   @PutMapping("/cart/add")
   public ResponseEntity<?> addItemToCart(@RequestBody AddCartItemRequest req,
		   @RequestHeader("Authorization") String jwt) throws Exception{
	   
	   CartItem cartItem = cartService.addItemToCart(req, jwt);
	   return new ResponseEntity<CartItem>(cartItem,HttpStatus.OK);
   }
   
   @PutMapping("/cart-item/update")
   public ResponseEntity<?> updateCartItemQuantity(@RequestBody UpdateCartItemRequest req,
		   @RequestHeader("Authorization") String jwt) throws Exception{
	   
	   CartItem cartItem = cartService.updateCartItemQuantity(req.getCartItemId(), req.getQuantity());
	   return new ResponseEntity<CartItem>(cartItem,HttpStatus.OK);
   }
   
   @DeleteMapping("/cart-item/{id}/remove")
   public ResponseEntity<?> removeCartItem(@PathVariable Long id,
		   @RequestHeader("Authorization") String jwt) throws Exception{
	   
	  Cart cart = cartService.removeItemFromCart(id, jwt);
	   return new ResponseEntity<Cart>(cart,HttpStatus.OK);
   }
   
   
   @PutMapping("/cart/clear")
   public ResponseEntity<?> clearCart(
		   @RequestHeader("Authorization") String jwt) throws Exception{
	   
	   User user = userService.findUserByJwtToken(jwt);
	   
	  Cart cart = cartService.clearCart(user.getId());
	   return new ResponseEntity<Cart>(cart,HttpStatus.OK);
   }
   
   @GetMapping("/cart")
   public ResponseEntity<?> findUserCart(
		   @RequestHeader("Authorization") String jwt) throws Exception{
	   
	   User user = userService.findUserByJwtToken(jwt);
	   
	  Cart cart = cartService.findCartByUsserId(user.getId());
	   return new ResponseEntity<Cart>(cart,HttpStatus.OK);
   }
   
}
