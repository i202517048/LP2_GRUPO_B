package com.proyectolp2.HotelMorfeo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyectolp2.HotelMorfeo.model.Usuario;
import com.proyectolp2.HotelMorfeo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	@Transactional(readOnly = true)
	public Usuario autenticar(String cuenta, String clave) {

		if (cuenta == null || cuenta.isBlank() || clave == null || clave.isBlank()) {
			return null;
		}

		var usuarios = usuarioRepository.findByCuenta(cuenta.trim());

		// Rechazar cuentas inexistentes o duplicadas
		if (usuarios.size() != 1) {
			return null;
		}

		var usuario = usuarios.get(0);

		// Solo usuarios activos
		if (!Boolean.TRUE.equals(usuario.getActivo())) {
			return null;
		}

		// Comprobación temporal de contraseña en texto plano
		if (!clave.equals(usuario.getClave())) {
			return null;
		}

		if (usuario.getRol() == null) {
			return null;
		}

		Integer idRol = usuario.getRol().getIdRol();

		// Solo recepcionista o administrador
		if (!Integer.valueOf(2).equals(idRol) && !Integer.valueOf(3).equals(idRol)) {
			return null;
		}

		return usuario;
	}
}