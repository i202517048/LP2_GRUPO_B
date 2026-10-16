package com.proyectolp2.HotelMorfeo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectolp2.HotelMorfeo.model.Habitacion;



public interface HabitacionesRepository extends JpaRepository<Habitacion, Integer> {
	
	boolean existsByNumeroIgnoreCase(String numero);
	
	boolean existsByNumeroIgnoreCaseAndIdHabitacionNot(
	        String numero, Integer idHabitacion
	    );
	
}
