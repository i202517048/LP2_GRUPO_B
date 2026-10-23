package com.proyectolp2.HotelMorfeo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectolp2.HotelMorfeo.model.Habitacion;

public interface HabitacionesRepository extends JpaRepository<Habitacion, Integer> {

	List<Habitacion> findAllByOrderByIdHabitacionDesc();

	List<Habitacion> findAllByOrderByNumeroAsc();

	boolean existsByNumeroIgnoreCase(String numero);

	boolean existsByNumeroIgnoreCaseAndIdHabitacionNot(String numero, Integer idHabitacion);

	// PARA BOLETA
	@org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
	@org.springframework.data.jpa.repository.Query("select h from Habitacion h where h.idHabitacion = :id")
	java.util.Optional<Habitacion> buscarParaSalida(@org.springframework.data.repository.query.Param("id") Integer id);
}
