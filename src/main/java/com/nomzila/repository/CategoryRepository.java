package com.nomzila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nomzila.model.Category;
import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long>{

	
	public List<Category> findByRestaurantId(Long id);
}
