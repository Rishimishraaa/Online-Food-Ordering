package com.nomzila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nomzila.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
