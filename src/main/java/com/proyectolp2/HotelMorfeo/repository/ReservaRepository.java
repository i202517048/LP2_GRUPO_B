package com.proyectolp2.HotelMorfeo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectolp2.HotelMorfeo.model.Reserva;


public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	
	List<Reserva> findAllByOrderByIdReservaDesc();
	@Query("""
			select r from Reserva r
			where
				(:numero is null or :numero = '' or r.habitacion.numero = :numero)
				and
				(:nombres is null or :nombres = '' or r.huesped.nombres = :nombres)
		""")
	List<Reserva> findByFilters(
			@Param("numero") String numero,
			@Param("nombres") String nombres
	);

	}
