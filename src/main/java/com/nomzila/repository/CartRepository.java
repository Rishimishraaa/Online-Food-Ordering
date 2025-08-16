package com.nomzila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nomzila.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
	public Cart findByCustomerId(Long userId);
}
