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

import com.proyectolp2.HotelMorfeo.model.Huesped;
import com.proyectolp2.HotelMorfeo.service.HuespedesService;
import com.proyectolp2.HotelMorfeo.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("huespedes")
public class HuespedesController {

	private final HuespedesService huespedesService;

	// Listado de huéspedes
	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstHuespedes", huespedesService.getAll());

		return "huespedes/listado";
	}

	// Formulario de registro
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("huesped", new Huesped());

		return "huespedes/nuevo";
	}

	// Registrar huésped
	@PostMapping("registrar")
	public String registrar(@ModelAttribute("huesped") Huesped huesped, BindingResult bindingResult, Model model,
			RedirectAttributes flash) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			return "huespedes/nuevo";
		}

		var response = huespedesService.create(huesped);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			return "huespedes/nuevo";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/huespedes/listado";
	}

	// Formulario de edición
	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash) {

		var huesped = huespedesService.getOne(id);

		if (huesped == null) {
			var alert = Alert.sweetToast("El huésped no existe.", "error", 5000);

			flash.addFlashAttribute("alert", alert);

			return "redirect:/huespedes/listado";
		}

		model.addAttribute("huesped", huesped);

		return "huespedes/edicion";
	}

	// Actualizar huésped
	@PostMapping("grabar")
	public String grabar(@ModelAttribute("huesped") Huesped huesped, BindingResult bindingResult, Model model,
			RedirectAttributes flash) {

		if (huesped.getIdHuesped() == null) {
			var alert = Alert.sweetToast("Debe indicar el huésped.", "error", 5000);

			flash.addFlashAttribute("alert", alert);

			return "redirect:/huespedes/listado";
		}

		var existente = huespedesService.getOne(huesped.getIdHuesped());

		if (existente == null) {
			var alert = Alert.sweetToast("El huésped no existe.", "error", 5000);

			flash.addFlashAttribute("alert", alert);

			return "redirect:/huespedes/listado";
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			return "huespedes/edicion";
		}

		var response = huespedesService.update(huesped);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			return "huespedes/edicion";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/huespedes/listado";
	}

	// Activar o desactivar huésped
	@PostMapping("estado")
	public String cambiarEstado(@RequestParam Integer id, RedirectAttributes flash) {

		var response = huespedesService.cambiarEstado(id);

		var alert = Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000);

		flash.addFlashAttribute("alert", alert);

		return "redirect:/huespedes/listado";
	}
}