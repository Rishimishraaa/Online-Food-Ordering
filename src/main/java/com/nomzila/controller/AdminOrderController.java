package com.nomzila.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomzila.model.Order;
import com.nomzila.model.User;
import com.nomzila.service.OrderService;
import com.nomzila.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

	@Autowired private OrderService orderService;
	@Autowired private UserService userService;
	
	
	  
	  @GetMapping("/order/restaurant/{id}")
	   public ResponseEntity<?> getOrderHistory(
			   @PathVariable Long id,
			   @RequestParam(required = false) String order_status,
			   @RequestHeader("Authorization") String jwt) throws Exception{
		  
		  User user = userService.findUserByJwtToken(jwt);
		   
		  List<Order> usersOrder = orderService.getRestaurantsOrder(id, order_status);
		   return new ResponseEntity<List<Order>>(usersOrder,HttpStatus.OK);
	   }
	  
	  @PutMapping("/order/{orderId}/{orderStatus}")
	   public ResponseEntity<?> updateOrderStatus(
			   @PathVariable Long id,
			   @PathVariable String orderStatus,
			   @RequestHeader("Authorization") String jwt) throws Exception{
		  
		  User user = userService.findUserByJwtToken(jwt);
		   
		 Order updateOrder = orderService.updateOrder(id, orderStatus);
		   return new ResponseEntity<Order>(updateOrder,HttpStatus.OK);
	   }
}
