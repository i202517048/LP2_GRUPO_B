package com.proyectolp2.HotelMorfeo.service;

import java.util.List;

// Fechas segun la zona horaria del hotel
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.proyectolp2.HotelMorfeo.dto.ReservaFilter;
import com.proyectolp2.HotelMorfeo.dto.ResultadoResponse;
import com.proyectolp2.HotelMorfeo.model.Reserva;
import com.proyectolp2.HotelMorfeo.repository.ReservaRepository;

import com.proyectolp2.HotelMorfeo.model.Habitacion;
import com.proyectolp2.HotelMorfeo.repository.HabitacionesRepository;
import com.proyectolp2.HotelMorfeo.repository.HuespedesRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {

	private final ReservaRepository reservaRepository;

	// Dependencias para validar relaciones
	private final HabitacionesRepository habitacionesRepository;
	private final HuespedesRepository huespedesRepository;

	public List<Reserva> getAll() {
		return reservaRepository.findAllByOrderByIdReservaDesc();
	}

	public List<Reserva> search(ReservaFilter filter) {
		return reservaRepository.findByFilters(filter.numero(), filter.nombres());
	}

	// Método crear
	// Validar disponibilidad y guardar tarifa acordada
	@Transactional
	public ResultadoResponse create(Reserva reservas) {

		String error = validar(reservas);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		var huesped = huespedesRepository.findById(reservas.getHuesped().getIdHuesped()).orElse(null);

		if (huesped == null || !Integer.valueOf(1).equals(huesped.getActivoHuespedes())) {
			return new ResultadoResponse(false, "Seleccione un huésped existente y activo.");
		}

		var habitacion = habitacionesRepository.findById(reservas.getHabitacion().getIdHabitacion()).orElse(null);

		error = validarHabitacion(habitacion);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		long coincidencias = reservaRepository.contarReservasSuperpuestas(habitacion.getIdHabitacion(),
				reservas.getFechaCheckin(), reservas.getFechaCheckoutPrevista(), null);

		if (coincidencias > 0) {
			return new ResultadoResponse(false, "La habitación no está disponible para las fechas indicadas.");
		}

		// Crear un registro nuevo usando datos comprobados en el servidor.
		var nuevaReserva = new Reserva();

		nuevaReserva.setHuesped(huesped);
		nuevaReserva.setHabitacion(habitacion);
		nuevaReserva.setFechaCheckin(reservas.getFechaCheckin());
		nuevaReserva.setFechaCheckoutPrevista(reservas.getFechaCheckoutPrevista());

		nuevaReserva.setEstado("P");
		nuevaReserva.setFechaCheckoutReal(null);
		nuevaReserva.setPrecioNocheAcordado(habitacion.getPrecioNoche());

		var registrado = reservaRepository.save(nuevaReserva);

		var mensaje = String.format("Reserva con Id %s registrada.", registrado.getIdReserva());

		return new ResultadoResponse(true, mensaje);
	}

	// Método obtener
	public Reserva getOne(Integer id) {
		return reservaRepository.findById(id).orElse(null);
	}

	// Método actualizar
	// Editar solo pendientes y conservar datos historicos
	@Transactional
	public ResultadoResponse update(Reserva reservas) {

		if (reservas == null || reservas.getIdReserva() == null) {
			return new ResultadoResponse(false, "Debe indicar la reserva a editar.");
		}

		var existente = reservaRepository.findById(reservas.getIdReserva()).orElse(null);

		if (existente == null) {
			return new ResultadoResponse(false, "La reserva no existe.");
		}

		if (!"P".equals(existente.getEstado())) {
			return new ResultadoResponse(false, "Solo se pueden editar reservas pendientes de ingreso.");
		}

		String error = validar(reservas);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		var huesped = huespedesRepository.findById(reservas.getHuesped().getIdHuesped()).orElse(null);

		if (huesped == null) {
			return new ResultadoResponse(false, "El huésped no existe.");
		}

		boolean cambiaHuesped = !huesped.getIdHuesped().equals(existente.getHuesped().getIdHuesped());

		// Un huesped inactivo conserva sus reservas anteriores, pero no puede asignarse como un muevo titular
		if (cambiaHuesped && !Integer.valueOf(1).equals(huesped.getActivoHuespedes())) {
			return new ResultadoResponse(false, "El nuevo huésped debe estar activo.");
		}

		var habitacion = habitacionesRepository.findById(reservas.getHabitacion().getIdHabitacion()).orElse(null);

		error = validarHabitacion(habitacion);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		long coincidencias = reservaRepository.contarReservasSuperpuestas(habitacion.getIdHabitacion(),
				reservas.getFechaCheckin(), reservas.getFechaCheckoutPrevista(), existente.getIdReserva());

		if (coincidencias > 0) {
			return new ResultadoResponse(false, "La habitación tiene otra reserva para esas fechas.");
		}

		boolean cambiaHabitacion = !habitacion.getIdHabitacion().equals(existente.getHabitacion().getIdHabitacion());

		if (cambiaHabitacion) {
			existente.setPrecioNocheAcordado(habitacion.getPrecioNoche());
		}

		existente.setHuesped(huesped);
		existente.setHabitacion(habitacion);
		existente.setFechaCheckin(reservas.getFechaCheckin());
		existente.setFechaCheckoutPrevista(reservas.getFechaCheckoutPrevista());

		// Se conservan estado y checkout real del registro existente
		// Si no cambia la habitacion, tambien se conserva la tarifa acordada

		var registrado = reservaRepository.save(existente);

		var mensaje = String.format("Reserva con Id %s actualizada.", registrado.getIdReserva());

		return new ResultadoResponse(true, mensaje);
	}

	// Validaciones compartidas de registro y edicion
	private String validar(Reserva reservas) {

		if (reservas == null) {
			return "Debe indicar los datos de la reserva.";
		}

		if (reservas.getHuesped() == null || reservas.getHuesped().getIdHuesped() == null
				|| reservas.getHuesped().getIdHuesped() <= 0) {
			return "Seleccione un huésped.";
		}

		if (reservas.getHabitacion() == null || reservas.getHabitacion().getIdHabitacion() == null
				|| reservas.getHabitacion().getIdHabitacion() <= 0) {
			return "Seleccione una habitación.";
		}

		if (reservas.getFechaCheckin() == null || reservas.getFechaCheckoutPrevista() == null) {
			return "Ingrese las fechas de entrada y salida prevista.";
		}

		LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));

		if (reservas.getFechaCheckin().isBefore(hoy)) {
			return "La fecha de entrada no puede ser anterior al día actual.";
		}

		if (!reservas.getFechaCheckoutPrevista().isAfter(reservas.getFechaCheckin())) {
			return "La salida prevista debe ser posterior a la entrada.";
		}

		return null;
	}

	private String validarHabitacion(Habitacion habitacion) {

		if (habitacion == null) {
			return "La habitación no existe.";
		}

		if (!Boolean.TRUE.equals(habitacion.getActivoHabitaciones())) {
			return "La habitación está inactiva.";
		}

		if ("M".equals(habitacion.getEstado())) {
			return "La habitación está en mantenimiento.";
		}

		if (!"D".equals(habitacion.getEstado()) && !"O".equals(habitacion.getEstado())) {
			return "La habitación tiene un estado no válido.";
		}

		return null;
	}
}