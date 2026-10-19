package com.proyectolp2.HotelMorfeo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectolp2.HotelMorfeo.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

	List<Reserva> findAllByOrderByIdReservaDesc();

	@Query("""
			select r from Reserva r
			where
				(:numero is null or :numero = '' or r.habitacion.numero = :numero)
				and
				(:nombres is null or :nombres = '' or r.huesped.nombres = :nombres)
			""")
	List<Reserva> findByFilters(@Param("numero") String numero, @Param("nombres") String nombres);

	// Comprobar superposición de reservas P/A
	@Query("""
			select count(r)
			from Reserva r
			where r.habitacion.idHabitacion = :idHabitacion
				and r.estado in ('P', 'A')
				and r.fechaCheckin < :fechaSalida
				and r.fechaCheckoutPrevista > :fechaEntrada
				and (
					:idReservaExcluir is null
					or r.idReserva <> :idReservaExcluir
				)
			""")
	long contarReservasSuperpuestas(@Param("idHabitacion") Integer idHabitacion,
			@Param("fechaEntrada") LocalDate fechaEntrada, @Param("fechaSalida") LocalDate fechaSalida,
			@Param("idReservaExcluir") Integer idReservaExcluir);

	// Comprobar compromisos de una habitación
	@Query("""
			select count(r)
			from Reserva r
			where r.habitacion.idHabitacion = :idHabitacion
				and r.estado in ('P', 'A')
			""")
	long contarReservasPendientesOActivas(@Param("idHabitacion") Integer idHabitacion);

	// INICIO ADAPTACION HREP04: comprobar compromisos de un huésped
	@Query("""
			select count(r)
			from Reserva r
			where r.huesped.idHuesped = :idHuesped
				and r.estado in ('P', 'A')
			""")
	long contarReservasPendientesOActivasPorHuesped(@Param("idHuesped") Integer idHuesped);
	// FIN ADAPTACION HREP04
}