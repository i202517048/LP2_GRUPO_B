package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyectolp2.HotelMorfeo.dto.UsuarioSesion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "redirect:/login";
	}

	// Pantalla de inicio
	@GetMapping("/home/dashboard")
	public String dashboard(HttpServletRequest request, HttpServletResponse httpResponse) {
		// Comprobar acceso antes de operar
		httpResponse.setHeader("Cache-Control", "no-store");

		var sesion = request.getSession(false);

		if (sesion == null || !(sesion.getAttribute("usuarioSesion") instanceof UsuarioSesion usuario)) {
			return "redirect:/login?sesion";
		}

		if (usuario.idUsuario() == null || usuario.idUsuario() <= 0
				|| (!Integer.valueOf(2).equals(usuario.idRol()) && !Integer.valueOf(3).equals(usuario.idRol()))) {
			sesion.invalidate();
			return "redirect:/login?sesion";
		}
		// -------------------------------

		return "home/dashboard";
	}

}
