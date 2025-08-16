package com.nomzila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nomzila.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
