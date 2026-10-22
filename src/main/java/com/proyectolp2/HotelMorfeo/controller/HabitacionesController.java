package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyectolp2.HotelMorfeo.model.Habitacion;
import com.proyectolp2.HotelMorfeo.service.HabitacionesService;
import com.proyectolp2.HotelMorfeo.util.Alert;

import com.proyectolp2.HotelMorfeo.dto.UsuarioSesion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("habitaciones")
public class HabitacionesController {

	private final HabitacionesService habitacionesService;

	@GetMapping("listado")
	public String listado(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
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

		model.addAttribute("lstHabitaciones", habitacionesService.getAllByIdDesc());

		return "habitaciones/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
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

		if (!Integer.valueOf(3).equals(usuario.idRol())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		// -------------------------------

		Habitacion habitacion = new Habitacion();

		habitacion.setEstado("D");
		habitacion.setActivoHabitaciones(true);

		model.addAttribute("habitacion", habitacion);

		return "habitaciones/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute("habitacion") Habitacion habitacion, BindingResult bindingResult,
			Model model, RedirectAttributes flash, HttpServletRequest request, HttpServletResponse httpResponse) {
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

		if (!Integer.valueOf(3).equals(usuario.idRol())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		// -------------------------------

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			return "habitaciones/nuevo";
		}

		var response = habitacionesService.create(habitacion);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			return "habitaciones/nuevo";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/habitaciones/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash, HttpServletRequest request,
			HttpServletResponse httpResponse) {
		// comprobar acceso antes de operar
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

		if (!Integer.valueOf(3).equals(usuario.idRol())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		// -------------------------------

		var habitacion = habitacionesService.getOne(id);

		if (habitacion == null) {
			var alert = Alert.sweetToast("La habitación no existe.", "error", 5000);

			flash.addFlashAttribute("alert", alert);
			return "redirect:/habitaciones/listado";
		}

		model.addAttribute("habitacion", habitacion);

		return "habitaciones/edicion";
	}

	@PostMapping("grabar")
	public String grabar(@ModelAttribute("habitacion") Habitacion habitacion, BindingResult bindingResult, Model model,
			RedirectAttributes flash, HttpServletRequest request, HttpServletResponse httpResponse) {
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

		if (!Integer.valueOf(3).equals(usuario.idRol())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		// -------------------------------

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			return "habitaciones/edicion";
		}

		var response = habitacionesService.update(habitacion);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			return "habitaciones/edicion";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/habitaciones/listado";
	}

	@PostMapping("estado")
	public String cambiarEstado(@RequestParam Integer id, RedirectAttributes flash, HttpServletRequest request,
			HttpServletResponse httpResponse) {
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

		if (!Integer.valueOf(3).equals(usuario.idRol())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		// -------------------------------

		var response = habitacionesService.cambiarEstado(id);

		var alert = Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/habitaciones/listado";
	}

}