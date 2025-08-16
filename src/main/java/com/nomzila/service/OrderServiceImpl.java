package com.nomzila.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nomzila.model.Address;
import com.nomzila.model.Cart;
import com.nomzila.model.CartItem;
import com.nomzila.model.Order;
import com.nomzila.model.OrderItem;
import com.nomzila.model.Restaurant;
import com.nomzila.model.User;
import com.nomzila.repository.AddressRepository;
import com.nomzila.repository.OrderItemRepository;
import com.nomzila.repository.OrderRepository;
import com.nomzila.repository.RestaurantRepository;
import com.nomzila.repository.UserRepository;
import com.nomzila.request.OrderRequest;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired private OrderRepository orderRepository;
	@Autowired private OrderItemRepository orderItemRepository;
	@Autowired private AddressRepository addressRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private RestaurantService restaurantService;
	@Autowired private CartService cartService;
	
	
	@Override
	public Order createOrder(OrderRequest order, User user) throws Exception {
		Address shippingAddress = order.getDeliveryAddress();
		
		Address saveAddress = addressRepository.save(shippingAddress);
		
		if(!user.getAddresses().contains(saveAddress)) {
			user.getAddresses().add(saveAddress);
			userRepository.save(user);
		}
		
		Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());
		
		Order createdOrder = Order.builder().customer(user)
					   .createdAt(new Date())
					   .orderStatus("PENDING")
					   .deliveryAddress(saveAddress)
					   .restaurant(restaurant)
					   .build();
		
		Cart cart = cartService.findCartByUsserId(user.getId());
		List<OrderItem> orderItems = new ArrayList<>();
		
		for(CartItem cartItem : cart.getItem()) {
			OrderItem orderItem = OrderItem.builder().food(cartItem.getFood())
							   .ingredients(cartItem.getIngredients())
							   .quantity(cartItem.getQuantity())
							   .totalPrice(cartItem.getTotalPrice())
							   .build();
			
			
			OrderItem savedOrderItem = orderItemRepository.save(orderItem);
			orderItems.add(savedOrderItem);
			
		}
		
		Double totalPrice = cartService.calculateCartTotals(cart);
		
		createdOrder.setItems(orderItems);
		createdOrder.setTotalAmount(totalPrice);
		
		Order savedOrder = orderRepository.save(createdOrder);
		restaurant.getOrders().add(savedOrder);
		
		return savedOrder;
	}

	@Override
	public Order updateOrder(Long orderId, String orderStatus) throws Exception {
		Order order= findOrderById(orderId);
		if(orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED") 
				|| orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING")) {
			
			order.setOrderStatus(orderStatus);
			return orderRepository.save(order);
		}
		throw new  Exception("please select a valid order status");
	}

	@Override
	public void cancelOrder(Long orderId) throws Exception {
		Order order = findOrderById(orderId);
		orderRepository.deleteById(orderId);
		
	}

	@Override
	public List<Order> getUsersOrder(Long userId) throws Exception {
		return orderRepository.findByCustomerId(userId);
	}

	@Override
	public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
		List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
		
		if(orderStatus !=null) {
			orders =orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
		}
		return orders;
	}

	@Override
	public Order findOrderById(Long orderId) throws Exception {
		Optional<Order> order = orderRepository.findById(orderId);
		if(order.isEmpty()) {
			throw new Exception("order not found");
		}
		return order.get();
	}

}
