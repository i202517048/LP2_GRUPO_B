package com.proyectolp2.HotelMorfeo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyectolp2.HotelMorfeo.dto.ResultadoResponse;
import com.proyectolp2.HotelMorfeo.model.Huesped;
import com.proyectolp2.HotelMorfeo.repository.HuespedesRepository;
import com.proyectolp2.HotelMorfeo.repository.ReservaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HuespedesService {

	private final HuespedesRepository huespedesRepository;

	// Consultar reservas del huésped
	private final ReservaRepository reservaRepository;

	// Método listar
	public List<Huesped> getAll() {
		return huespedesRepository.findAllByOrderByIdHuespedDesc();
	}

	// obtener un huésped
	public Huesped getOne(Integer id) {
		if (id == null) {
			return null;
		}

		return huespedesRepository.findById(id).orElse(null);
	}

	// Registrar un huésped
	@Transactional
	public ResultadoResponse create(Huesped huesped) {
		String error = validar(huesped);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		String documento = huesped.getDocumento().trim();

		if (huespedesRepository.existsByDocumentoIgnoreCase(documento)) {
			return new ResultadoResponse(false, "Ya existe un huésped con ese documento.");
		}

		huesped.setIdHuesped(null);
		huesped.setNombres(huesped.getNombres().trim());
		huesped.setApellidos(huesped.getApellidos().trim());
		huesped.setDocumento(documento);
		huesped.setTelefono(limpiarOpcional(huesped.getTelefono()));
		huesped.setEmail(limpiarOpcional(huesped.getEmail()));

		// El estado inicial se establece en el servidor
		huesped.setActivoHuespedes(1);

		var registrado = huespedesRepository.save(huesped);

		var mensaje = String.format("Huésped con Id %s registrado.", registrado.getIdHuesped());

		return new ResultadoResponse(true, mensaje);
	}

	// Actualizar conservando la activación
	@Transactional
	public ResultadoResponse update(Huesped huesped) {
		if (huesped == null || huesped.getIdHuesped() == null) {
			return new ResultadoResponse(false, "Debe indicar el huésped.");
		}

		var existente = huespedesRepository.findById(huesped.getIdHuesped()).orElse(null);

		if (existente == null) {
			return new ResultadoResponse(false, "El huésped no existe.");
		}

		String error = validar(huesped);

		if (error != null) {
			return new ResultadoResponse(false, error);
		}

		String documento = huesped.getDocumento().trim();

		boolean documentoRepetido = huespedesRepository.existsByDocumentoIgnoreCaseAndIdHuespedNot(documento,
				existente.getIdHuesped());

		if (documentoRepetido) {
			return new ResultadoResponse(false, "Ya existe otro huésped con ese documento.");
		}

		existente.setNombres(huesped.getNombres().trim());
		existente.setApellidos(huesped.getApellidos().trim());
		existente.setDocumento(documento);
		existente.setTelefono(limpiarOpcional(huesped.getTelefono()));
		existente.setEmail(limpiarOpcional(huesped.getEmail()));

		// No se modifica activoHuespedes desde el formulario de edición

		var registrado = huespedesRepository.save(existente);

		var mensaje = String.format("Huésped con Id %s actualizado.", registrado.getIdHuesped());

		return new ResultadoResponse(true, mensaje);
	}

	// Activar o desactivar sin eliminar
	@Transactional
	public ResultadoResponse cambiarEstado(Integer id) {
		if (id == null) {
			return new ResultadoResponse(false, "Debe indicar el huésped.");
		}

		var huesped = huespedesRepository.findById(id).orElse(null);

		if (huesped == null) {
			return new ResultadoResponse(false, "El huésped no existe.");
		}

		boolean estaActivo = Integer.valueOf(1).equals(huesped.getActivoHuespedes());

		if (estaActivo) {
			long compromisos = reservaRepository.contarReservasPendientesOActivasPorHuesped(id);

			if (compromisos > 0) {
				return new ResultadoResponse(false,
						"No se puede desactivar al huésped porque tiene " + "reservas pendientes o activas.");
			}
		}

		huesped.setActivoHuespedes(estaActivo ? 0 : 1);

		huespedesRepository.save(huesped);

		String accion = estaActivo ? "desactivado" : "activado";

		var mensaje = String.format("Huésped con Id %s %s.", huesped.getIdHuesped(), accion);

		return new ResultadoResponse(true, mensaje);
	}

	// Validaciones de registro y edición
	private String validar(Huesped huesped) {
		if (huesped == null) {
			return "Ingrese los datos del huésped.";
		}

		if (huesped.getNombres() == null || huesped.getNombres().isBlank()) {
			return "Ingrese los nombres del huésped.";
		}

		if (huesped.getNombres().trim().length() > 100) {
			return "Los nombres deben tener como máximo 100 caracteres.";
		}

		if (huesped.getApellidos() == null || huesped.getApellidos().isBlank()) {
			return "Ingrese los apellidos del huésped.";
		}

		if (huesped.getApellidos().trim().length() > 100) {
			return "Los apellidos deben tener como máximo 100 caracteres.";
		}

		if (huesped.getDocumento() == null || huesped.getDocumento().isBlank()) {
			return "Ingrese el documento del huésped.";
		}

		if (huesped.getDocumento().trim().length() > 20) {
			return "El documento debe tener como máximo 20 caracteres.";
		}

		String telefono = limpiarOpcional(huesped.getTelefono());

		if (telefono != null) {
			if (telefono.length() > 20) {
				return "El teléfono debe tener como máximo 20 caracteres.";
			}

			if (!telefono.matches("\\+?[0-9() -]+") || !telefono.matches(".*[0-9].*")) {
				return "Ingrese un teléfono válido. Puede utilizar números, "
						+ "espacios, paréntesis, guiones y el signo + al inicio.";
			}
		}

		String email = limpiarOpcional(huesped.getEmail());

		if (email != null) {
			if (email.length() > 100) {
				return "El correo debe tener como máximo 100 caracteres.";
			}

			// Comprobación básica de formato
			if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
				return "Ingrese un correo electrónico válido.";
			}
		}

		return null;
	}

	// Normalizar campos opcionales
	private String limpiarOpcional(String valor) {
		if (valor == null || valor.isBlank()) {
			return null;
		}

		return valor.trim();
	}
}