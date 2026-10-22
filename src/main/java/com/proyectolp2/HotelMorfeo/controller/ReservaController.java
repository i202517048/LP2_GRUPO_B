package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

// Detectar errores de conversion del formulario
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyectolp2.HotelMorfeo.dto.ReservaFilter;
import com.proyectolp2.HotelMorfeo.model.Reserva;
import com.proyectolp2.HotelMorfeo.service.HabitacionesService;
import com.proyectolp2.HotelMorfeo.service.HuespedesService;
import com.proyectolp2.HotelMorfeo.service.ReservaService;
import com.proyectolp2.HotelMorfeo.util.Alert;

import com.proyectolp2.HotelMorfeo.dto.UsuarioSesion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("reserva")
public class ReservaController {

	private final ReservaService reservaService;
	private final HuespedesService huespedesService;
	private final HabitacionesService habitacionesService;

	@GetMapping("listado")
	public String listado(@ModelAttribute ReservaFilter filter, Model model, HttpServletRequest request,
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
		// -------------------------------

		model.addAttribute("lstReservas", reservaService.search(filter));
		model.addAttribute("filter", filter);
		model.addAttribute("lstHuespedes", huespedesService.getAll());
		model.addAttribute("lstHabitaciones", habitacionesService.getAll());

		return "reserva/listado";
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
		// -------------------------------

		model.addAttribute("lstHuespedes", huespedesService.getAll());
		model.addAttribute("lstHabitaciones", habitacionesService.getAll());
		model.addAttribute("reservas", new Reserva());

		return "reserva/nuevo";
	}

	// Conservar formulario y mostrar errores reales
	// ------------------
	@PostMapping("registrar")
	public String registrar(@ModelAttribute("reservas") Reserva reservas, BindingResult bindingResult, Model model,
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
		// -------------------------------

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			model.addAttribute("lstHuespedes", huespedesService.getAll());
			model.addAttribute("lstHabitaciones", habitacionesService.getAll());

			return "reserva/nuevo";
		}

		var response = reservaService.create(reservas);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			model.addAttribute("lstHuespedes", huespedesService.getAll());
			model.addAttribute("lstHabitaciones", habitacionesService.getAll());

			return "reserva/nuevo";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/reserva/listado";
	}
	// ------------------

	// Consultar una vez y permitir edicion solo de P
	// ------------------
	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash, HttpServletRequest request,
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
		// -------------------------------

		var reserva = reservaService.getOne(id);

		if (reserva == null) {
			flash.addFlashAttribute("alert", Alert.sweetToast("La reserva no existe.", "error", 5000));

			return "redirect:/reserva/listado";
		}

		if (!"P".equals(reserva.getEstado())) {
			flash.addFlashAttribute("alert",
					Alert.sweetToast("Solo se pueden editar reservas pendientes de ingreso.", "error", 5000));

			return "redirect:/reserva/listado";
		}

		model.addAttribute("lstHuespedes", huespedesService.getAll());
		model.addAttribute("lstHabitaciones", habitacionesService.getAll());
		model.addAttribute("reserva", reserva);

		return "reserva/edicion";
	}
	// ------------------

	// Controller que consume el service update
	// Comprobar registro y devolver errores a edicion
	// ------------------
	@PostMapping("grabar")
	public String grabar(@ModelAttribute("reserva") Reserva reservas, BindingResult bindingResult, Model model,
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
		// -------------------------------

		if (reservas.getIdReserva() == null) {
			flash.addFlashAttribute("alert", Alert.sweetToast("Debe indicar una reserva válida.", "error", 5000));

			return "redirect:/reserva/listado";
		}

		var existente = reservaService.getOne(reservas.getIdReserva());

		if (existente == null) {
			flash.addFlashAttribute("alert", Alert.sweetToast("La reserva no existe.", "error", 5000));

			return "redirect:/reserva/listado";
		}

		if (!"P".equals(existente.getEstado())) {
			flash.addFlashAttribute("alert",
					Alert.sweetToast("Solo se pueden editar reservas pendientes de ingreso.", "error", 5000));

			return "redirect:/reserva/listado";
		}

		// Recuperar datos controlados por el servidor
		// No reemplazar huesped, habitacion ni fechas escritos por el usuario
		reservas.setEstado(existente.getEstado());
		reservas.setFechaCheckoutReal(existente.getFechaCheckoutReal());
		reservas.setPrecioNocheAcordado(existente.getPrecioNocheAcordado());

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			model.addAttribute("lstHuespedes", huespedesService.getAll());
			model.addAttribute("lstHabitaciones", habitacionesService.getAll());

			return "reserva/edicion";
		}

		var response = reservaService.update(reservas);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			model.addAttribute("lstHuespedes", huespedesService.getAll());
			model.addAttribute("lstHabitaciones", habitacionesService.getAll());

			return "reserva/edicion";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/reserva/listado";
	}
	// ------------------
}