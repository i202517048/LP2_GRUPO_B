package com.proyectolp2.HotelMorfeo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyectolp2.HotelMorfeo.dto.ResultadoResponse;
import com.proyectolp2.HotelMorfeo.model.ServicioAdicional;
import com.proyectolp2.HotelMorfeo.repository.ServicioAdicionalRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioAdicionalService {

	private final ServicioAdicionalRepository servicioAdicionalRepository;

	public List<ServicioAdicional> getAll() {
		return servicioAdicionalRepository.findAllByOrderByIdServicioDesc();
	}

	@Transactional
	public ResultadoResponse create(ServicioAdicional servicio) {

		if (servicio.getNombre() == null || servicio.getNombre().isBlank()) {
			return new ResultadoResponse(false, "Ingrese el nombre del servicio.");
		}

		String nombre = servicio.getNombre().trim();

		if (nombre.length() > 100) {
			return new ResultadoResponse(false, "El nombre debe tener como máximo 100 caracteres.");
		}

		BigDecimal precio = servicio.getPrecio();

		if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
			return new ResultadoResponse(false, "El precio debe ser mayor a cero.");
		}

		if (precio.stripTrailingZeros().scale() > 2) {
			return new ResultadoResponse(false, "El precio debe tener como máximo dos decimales.");
		}

		if (precio.compareTo(new BigDecimal("99999999.99")) > 0) {
			return new ResultadoResponse(false, "El precio supera el máximo permitido.");
		}

		servicio.setIdServicio(null);
		servicio.setNombre(nombre);
		servicio.setActivoServicios(1);

		servicioAdicionalRepository.save(servicio);

		return new ResultadoResponse(true, "Servicio registrado correctamente.");
	}
}