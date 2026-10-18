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

	// Método listar
	public List<ServicioAdicional> getAll() {
		return servicioAdicionalRepository.findAll();
	}

	// Método crear
	@Transactional
	public ResultadoResponse create(ServicioAdicional servicio) {

		String error = validar(servicio);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		servicio.setIdServicio(null);
		servicio.setNombre(servicio.getNombre().trim());
		servicio.setActivoServicios(1);

		var registrado = servicioAdicionalRepository.save(servicio);

		var mensaje = String.format("Servicio con Id %s registrado.", registrado.getIdServicio());

		return new ResultadoResponse(true, mensaje);
	}

	// Método obtener
	public ServicioAdicional getOne(Integer id) {
		return servicioAdicionalRepository.findById(id).orElse(null);
	}

	// Método actualizar
	@Transactional
	public ResultadoResponse update(ServicioAdicional servicio) {

		// Comprobar objeto e identificador
		if (servicio == null || servicio.getIdServicio() == null) {
			return new ResultadoResponse(false, "Debe indicar el servicio.");
		}

		var existente = servicioAdicionalRepository.findById(servicio.getIdServicio()).orElse(null);

		if (existente == null) {
			return new ResultadoResponse(false, "El servicio no existe.");
		}

		String error = validar(servicio);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		// Proteger el nombre de consumos sin facturar
		// ----------------
		String nombre = servicio.getNombre().trim();

		boolean cambiaNombre = !nombre.equals(existente.getNombre());

		if (cambiaNombre
				&& servicioAdicionalRepository.contarConsumosPendientesDeFacturacion(existente.getIdServicio()) > 0) {

			return new ResultadoResponse(false,
					"No se puede cambiar el nombre: el servicio tiene " + "consumos vigentes sin facturar.");
		}

		existente.setNombre(nombre);
		// ----------------

		existente.setPrecio(servicio.getPrecio());

		var registrado = servicioAdicionalRepository.save(existente);

		var mensaje = String.format("Servicio con Id %s actualizado.", registrado.getIdServicio());

		return new ResultadoResponse(true, mensaje);
	}

	// Validaciones para crear y actualizar
	private String validar(ServicioAdicional servicio) {

		// INICIO ADAPTACION SAS03: evitar acceso a un objeto nulo
		if (servicio == null) {
			return "Debe indicar los datos del servicio.";
		}
		// FIN ADAPTACION SAS03

		if (servicio.getNombre() == null || servicio.getNombre().isBlank()) {
			return "Ingrese el nombre del servicio.";
		}

		if (servicio.getNombre().trim().length() > 100) {
			return "El nombre debe tener como máximo 100 caracteres.";
		}

		BigDecimal precio = servicio.getPrecio();

		if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
			return "El precio debe ser mayor a cero.";
		}

		if (precio.stripTrailingZeros().scale() > 2) {
			return "El precio debe tener como máximo dos decimales.";
		}

		if (precio.compareTo(new BigDecimal("99999999.99")) > 0) {
			return "El precio supera el máximo permitido.";
		}

		return null;
	}
}