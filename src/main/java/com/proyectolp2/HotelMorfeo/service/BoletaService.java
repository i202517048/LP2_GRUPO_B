package com.proyectolp2.HotelMorfeo.service;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyectolp2.HotelMorfeo.model.*;
import com.proyectolp2.HotelMorfeo.repository.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletaService {
	private final BoletaRepository boletaRepository;
	private final ReservaRepository reservaRepository;
	private final ReservaServicioRepository reservaServicioRepository;
	private final HabitacionesRepository habitacionesRepository;
	private static final ZoneId ZONA = ZoneId.of("America/Lima");
	private static final BigDecimal MAXIMO = new BigDecimal("99999999.99");

	public List<Boleta> getAll() {
		return boletaRepository.findAllByOrderByIdBoletaDesc();
	}

	public List<Reserva> getReservasActivas() {
		return reservaRepository.findActivasSinBoleta();
	}

	@Transactional(readOnly = true)
	public Boleta getOne(Integer id) {
		if (id == null || id <= 0)
			return null;
		var boleta = boletaRepository.findById(id).orElse(null);
		if (boleta != null)
			boleta.getDetalles().size();
		return boleta;
	}

	@Transactional(readOnly = true)
	public Boleta preparar(Integer idReserva) {
		validarId(idReserva);
		var reserva = reservaRepository.findById(idReserva)
				.orElseThrow(() -> new IllegalArgumentException("La reserva no existe."));
		return construir(reserva, LocalDateTime.now(ZONA));
	}

	// Guardar cabecera y detalles mediante cascada, como en el ejemplo del
	// profesor.
	@Transactional
	public Integer create(Integer idReserva) {
		validarId(idReserva);
		var reserva = reservaRepository.buscarParaEmitir(idReserva)
				.orElseThrow(() -> new IllegalArgumentException("La reserva no existe."));
		var habitacion = habitacionesRepository.buscarParaSalida(reserva.getHabitacion().getIdHabitacion())
				.orElseThrow(() -> new IllegalArgumentException("La habitación no existe."));
		if (!"O".equals(habitacion.getEstado()))
			throw new IllegalArgumentException(
					"La habitación de la estancia debe estar ocupada. Revise su estado antes de emitir.");
		if (reservaRepository.countByHabitacionIdHabitacionAndEstadoAndIdReservaNot(habitacion.getIdHabitacion(), "A",
				idReserva) > 0)
			throw new IllegalArgumentException(
					"Hay otra estancia activa en esta habitación. Corrija esa inconsistencia antes de emitir.");
		var salida = LocalDateTime.now(ZONA);
		var boleta = construir(reserva, salida);
		var registrada = boletaRepository.saveAndFlush(boleta);
		reserva.setEstado("F");
		reserva.setFechaCheckoutReal(salida);
		habitacion.setEstado("D");
		reservaRepository.save(reserva);
		habitacionesRepository.save(habitacion);
		return registrada.getIdBoleta();
	}

	private Boleta construir(Reserva reserva, LocalDateTime salida) {
		if (!"A".equals(reserva.getEstado()))
			throw new IllegalArgumentException("Solo se emiten boletas de reservas activas.");
		if (boletaRepository.existsByReservaIdReserva(reserva.getIdReserva()))
			throw new IllegalArgumentException("La reserva ya tiene una boleta emitida.");
		if (reserva.getFechaCheckoutReal() != null)
			throw new IllegalArgumentException("La reserva ya registra una salida real.");
		if (reserva.getFechaCheckin() == null || reserva.getFechaCheckin().isAfter(salida.toLocalDate()))
			throw new IllegalArgumentException("La fecha de entrada no es válida para emitir la boleta.");
		var huesped = reserva.getHuesped();
		if (huesped == null || huesped.getNombres() == null || huesped.getApellidos() == null
				|| huesped.getDocumento() == null || huesped.getFullName().isBlank()
				|| huesped.getDocumento().isBlank())
			throw new IllegalArgumentException("Complete los datos del huésped antes de emitir.");
		var boleta = new Boleta();
		boleta.setReserva(reserva);
		boleta.setFechaEmision(salida);
		boleta.setClienteNombre(huesped.getFullName().trim());
		boleta.setClienteDocumento(huesped.getDocumento().trim());
		long noches = Math.max(1, ChronoUnit.DAYS.between(reserva.getFechaCheckin(), salida.toLocalDate()));
		if (noches > Integer.MAX_VALUE)
			throw new IllegalArgumentException("La cantidad de noches supera el límite permitido.");
		agregar(boleta, null, "Alojamiento habitación " + reserva.getHabitacion().getNumero(), (int) noches,
				reserva.getPrecioNocheAcordado());
		for (var consumo : reservaServicioRepository
				.findByReservaIdReservaAndAnuladoFalseOrderByIdReservaServicioAsc(reserva.getIdReserva())) {
			if (consumo.getFechaConsumo() == null || consumo.getFechaConsumo().isAfter(salida)
					|| consumo.getFechaConsumo().toLocalDate().isBefore(reserva.getFechaCheckin()))
				throw new IllegalArgumentException("Hay un consumo con fecha inconsistente. Revíselo antes de emitir.");
			if (consumo.getServicioAdicional() == null || consumo.getServicioAdicional().getNombre() == null)
				throw new IllegalArgumentException("Hay un consumo sin servicio válido.");
			agregar(boleta, consumo, consumo.getServicioAdicional().getNombre(), consumo.getCantidad(),
					consumo.getPrecioUnitario());
		}
		var total = boleta.getDetalles().stream().map(DetalleBoleta::getSubtotal).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		validarImporte(total);
		boleta.setTotal(total.setScale(2));
		return boleta;
	}

	private void agregar(Boleta boleta, ReservaServicio consumo, String descripcion, Integer cantidad,
			BigDecimal precio) {
		if (cantidad == null || cantidad <= 0)
			throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
		validarImporte(precio);
		var subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
		validarImporte(subtotal);
		if (descripcion == null || descripcion.isBlank() || descripcion.length() > 150)
			throw new IllegalArgumentException("La descripción del detalle no es válida.");
		var detalle = new DetalleBoleta();
		detalle.setBoleta(boleta);
		detalle.setReservaServicio(consumo);
		detalle.setDescripcion(descripcion);
		detalle.setCantidad(cantidad);
		detalle.setPrecioUnitario(precio.setScale(2));
		detalle.setSubtotal(subtotal.setScale(2));
		boleta.getDetalles().add(detalle);
	}

	private void validarImporte(BigDecimal importe) {
		if (importe == null || importe.signum() <= 0 || importe.compareTo(MAXIMO) > 0
				|| importe.stripTrailingZeros().scale() > 2)
			throw new IllegalArgumentException("Hay un importe inválido o que supera el máximo permitido.");
	}

	private void validarId(Integer id) {
		if (id == null || id <= 0)
			throw new IllegalArgumentException("Seleccione una reserva válida.");
	}
}
