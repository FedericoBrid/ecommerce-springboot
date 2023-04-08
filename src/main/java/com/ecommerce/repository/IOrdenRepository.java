package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Orden;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {

}
