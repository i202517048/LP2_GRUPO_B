package com.proyectolp2.HotelMorfeo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectolp2.HotelMorfeo.model.ServicioAdicional;

public interface ServicioAdicionalRepository extends JpaRepository<ServicioAdicional, Integer> {

	List<ServicioAdicional> findAllByOrderByIdServicioDesc();
}