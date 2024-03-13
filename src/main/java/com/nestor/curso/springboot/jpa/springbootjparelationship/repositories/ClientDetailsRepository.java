package com.nestor.curso.springboot.jpa.springbootjparelationship.repositories;

import org.springframework.data.repository.CrudRepository;

import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.ClientDetails;

public interface ClientDetailsRepository extends CrudRepository <ClientDetails, Long> {

}
