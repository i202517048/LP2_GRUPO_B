package com.proyectolp2.HotelMorfeo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyectolp2.HotelMorfeo.dto.ResultadoResponse;
import com.proyectolp2.HotelMorfeo.model.Habitacion;
import com.proyectolp2.HotelMorfeo.repository.HabitacionesRepository;

// Consultar reservas relacionadas
import com.proyectolp2.HotelMorfeo.repository.ReservaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitacionesService {

	private final HabitacionesRepository habitacionesRepository;

	// Comprobar compromisos de la habitacion
	private final ReservaRepository reservaRepository;

	public List<Habitacion> getAll() {
		return habitacionesRepository.findAllByOrderByNumeroAsc();
	}

	public List<Habitacion> getAllByIdDesc() {
		return habitacionesRepository.findAllByOrderByIdHabitacionDesc();
	}

	@Transactional
	public ResultadoResponse create(Habitacion habitacion) {

		habitacion.setActivoHabitaciones(true);

		// Fijar estado inicial desde el servidor
		habitacion.setEstado("D");

		String error = validar(habitacion);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		if (habitacion.getNumero() == null || habitacion.getNumero().isBlank()) {
			return new ResultadoResponse(false, "Ingrese el número de habitación.");
		}

		String numero = habitacion.getNumero().trim();

		if (habitacionesRepository.existsByNumeroIgnoreCase(numero)) {
			return new ResultadoResponse(false, "Ya existe una habitación con ese número.");
		}

		habitacion.setIdHabitacion(null);
		habitacion.setNumero(numero);

		var registrado = habitacionesRepository.save(habitacion);

		return new ResultadoResponse(true, "Habitación registrada correctamente. ID: " + registrado.getIdHabitacion());
	}

	public Habitacion getOne(Integer id) {
		return habitacionesRepository.findById(id).orElse(null);
	}

	@Transactional
	public ResultadoResponse update(Habitacion habitacion) {

		if (habitacion.getIdHabitacion() == null) {
			return new ResultadoResponse(false, "Debe indicar la habitación a editar.");
		}

		var existente = habitacionesRepository.findById(habitacion.getIdHabitacion()).orElse(null);

		if (existente == null) {
			return new ResultadoResponse(false, "La habitación no existe.");
		}

		habitacion.setActivoHabitaciones(existente.getActivoHabitaciones());

		// Conservar la ocupacion registrada
		habitacion.setEstado(existente.getEstado());

		String error = validar(habitacion);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		String numero = habitacion.getNumero().trim();

		if (habitacionesRepository.existsByNumeroIgnoreCaseAndIdHabitacionNot(numero, existente.getIdHabitacion())) {

			return new ResultadoResponse(false, "Ya existe otra habitación con ese número.");
		}

		// Impedir cambio de tipo con reservas P/A
		if (!habitacion.getTipo().equals(existente.getTipo())
				&& reservaRepository.contarReservasPendientesOActivas(existente.getIdHabitacion()) > 0) {

			return new ResultadoResponse(false,
					"No se puede cambiar el tipo: la habitación tiene " + "reservas pendientes o activas.");
		}

		existente.setNumero(numero);
		existente.setTipo(habitacion.getTipo());
		existente.setPrecioNoche(habitacion.getPrecioNoche());

		// Conserva el estado recuperado de la base
		existente.setEstado(habitacion.getEstado());

		var actualizado = habitacionesRepository.save(existente);

		return new ResultadoResponse(true,
				"Habitación con ID " + actualizado.getIdHabitacion() + " actualizada correctamente.");
	}

	private String validar(Habitacion habitacion) {

		if (habitacion.getNumero() == null || habitacion.getNumero().isBlank()) {
			return "Ingrese el número de habitación.";
		}

		if (habitacion.getNumero().trim().length() > 10) {
			return "El número debe tener máximo 10 caracteres.";
		}

		// Tipos admitidos por el SQL
		if (habitacion.getTipo() == null || !habitacion.getTipo().matches("[SD]")) {
			return "Seleccione un tipo de habitación válido: Simple o Doble.";
		}

		BigDecimal precio = habitacion.getPrecioNoche();

		if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
			return "El precio debe ser mayor a cero.";
		}

		if (precio.stripTrailingZeros().scale() > 2 || precio.compareTo(new BigDecimal("99999999.99")) > 0) {
			return "El precio supera los límites permitidos.";
		}

		// Estados admitidos por el SQL
		if (habitacion.getEstado() == null || !habitacion.getEstado().matches("[DOM]")) {
			return "Seleccione un estado válido.";
		}

		return null;
	}

	@Transactional
	public ResultadoResponse cambiarEstado(Integer id) {

		var habitacion = habitacionesRepository.findById(id).orElse(null);

		if (habitacion == null) {
			return new ResultadoResponse(false, "La habitación no existe.");
		}

		boolean nuevoEstado = !Boolean.TRUE.equals(habitacion.getActivoHabitaciones());

		// Impedir desactivacion con compromisos
		if (!nuevoEstado
				&& ("O".equals(habitacion.getEstado()) || reservaRepository.contarReservasPendientesOActivas(id) > 0)) {

			return new ResultadoResponse(false,
					"No se puede desactivar: la habitación está ocupada " + "o tiene reservas pendientes o activas.");
		}

		habitacion.setActivoHabitaciones(nuevoEstado);

		habitacionesRepository.save(habitacion);

		return new ResultadoResponse(true,
				nuevoEstado ? "Habitación activada correctamente." : "Habitación desactivada correctamente.");
	}
}