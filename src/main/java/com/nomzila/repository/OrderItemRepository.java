package com.nomzila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nomzila.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
