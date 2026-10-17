package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyectolp2.HotelMorfeo.dto.UsuarioSesion;
import com.proyectolp2.HotelMorfeo.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final UsuarioService usuarioService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String ingresar(@RequestParam String cuenta, @RequestParam String clave, HttpServletRequest request,
			Model model) {

		var usuario = usuarioService.autenticar(cuenta, clave);

		if (usuario == null) {
			model.addAttribute("error", "No se pudo iniciar sesión. Revise sus credenciales y acceso.");

			model.addAttribute("cuenta", cuenta);

			return "login";
		}

		// Eliminar la sesión anterior y crear una nueva al autenticarse
		var sesionAnterior = request.getSession(false);

		if (sesionAnterior != null) {
			sesionAnterior.invalidate();
		}

		var usuarioSesion = new UsuarioSesion(usuario.getIdUsuario(), usuario.getNombres(), usuario.getApellidos(),
				usuario.getRol().getIdRol());

		var sesion = request.getSession(true);
		sesion.setAttribute("usuarioSesion", usuarioSesion);
		sesion.setMaxInactiveInterval(30 * 60);

		return "redirect:/reserva/listado";
	}

	@PostMapping("/logout")
	public String salir(HttpServletRequest request) {
		var sesion = request.getSession(false);

		if (sesion != null) {
			sesion.invalidate();
		}

		return "redirect:/login?logout";
	}
}