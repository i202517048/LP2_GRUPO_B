package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyectolp2.HotelMorfeo.dto.AutenticacionFilter;
import com.proyectolp2.HotelMorfeo.dto.UsuarioSesion;
import com.proyectolp2.HotelMorfeo.service.UsuarioService;
import com.proyectolp2.HotelMorfeo.util.Alert;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final UsuarioService usuarioService;

	// Formulario y acceso de una sesión existente
	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		var sesion = request.getSession(false);

		if (sesion != null && sesion.getAttribute("usuarioSesion") instanceof UsuarioSesion) {
			return "redirect:/home/dashboard";
		}

		model.addAttribute("filter", new AutenticacionFilter(null, null));
		return "login";
	}

	@PostMapping("/login")
	public String ingresar(@ModelAttribute("filter") AutenticacionFilter filter, HttpServletRequest request,
			Model model, RedirectAttributes flash) {

		var usuario = usuarioService.autenticar(filter.cuenta(), filter.clave());

		if (usuario == null) {
			model.addAttribute("error", "No se pudo iniciar sesión. Revise sus credenciales y acceso.");

			// Conservar la cuenta sin devolver la contraseña a la vista
			model.addAttribute("filter", new AutenticacionFilter(filter.cuenta(), null));

			return "login";
		}

		var sesionAnterior = request.getSession(false);

		if (sesionAnterior != null) {
			sesionAnterior.invalidate();
		}

		var usuarioSesion = new UsuarioSesion(usuario.getIdUsuario(), usuario.getNombres(), usuario.getApellidos(),
				usuario.getRol().getIdRol());

		var sesion = request.getSession(true);
		sesion.setAttribute("usuarioSesion", usuarioSesion);
		sesion.setMaxInactiveInterval(30 * 60);

		// Mensaje constante para la alerta del layout
		flash.addFlashAttribute("alert", Alert.sweetToast("Bienvenido a Hotel Morfeo.", "success", 4000));

		return "redirect:/home/dashboard";
	}

	// Cierre de sesión real mediante POST
	@PostMapping("/logout")
	public String salir(HttpServletRequest request) {
		var sesion = request.getSession(false);

		if (sesion != null) {
			sesion.invalidate();
		}

		return "redirect:/login?logout";
	}
}
