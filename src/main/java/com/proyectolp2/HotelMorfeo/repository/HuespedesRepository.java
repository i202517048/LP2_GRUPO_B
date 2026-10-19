package com.proyectolp2.HotelMorfeo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectolp2.HotelMorfeo.model.Huesped;

public interface HuespedesRepository extends JpaRepository<Huesped, Integer> {

	List<Huesped> findAllByOrderByIdHuespedDesc();

	List<Huesped> findAllByOrderByApellidosAscNombresAscIdHuespedAsc();

	// Comprobar documento al registrar
	boolean existsByDocumentoIgnoreCase(String documento);

	// Comprobar documento al editar
	boolean existsByDocumentoIgnoreCaseAndIdHuespedNot(String documento, Integer idHuesped);
}