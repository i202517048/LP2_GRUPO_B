package com.proyectolp2.HotelMorfeo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectolp2.HotelMorfeo.model.Reservas;

public interface ReservaRepository extends JpaRepository<Reservas, Integer>{
	
}
