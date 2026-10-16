package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyectolp2.HotelMorfeo.model.ServicioAdicional;
import com.proyectolp2.HotelMorfeo.service.ServicioAdicionalService;
import com.proyectolp2.HotelMorfeo.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("servicios")
public class ServicioAdicionalController {

	private final ServicioAdicionalService servicioAdicionalService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstServicios", servicioAdicionalService.getAll());

		return "servicios/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("servicio", new ServicioAdicional());

		return "servicios/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute("servicio") ServicioAdicional servicio, BindingResult bindingResult,
			Model model, RedirectAttributes flash) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			return "servicios/nuevo";
		}

		var response = servicioAdicionalService.create(servicio);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			return "servicios/nuevo";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);
		flash.addFlashAttribute("alert", alert);

		return "redirect:/servicios/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash) {

		var servicio = servicioAdicionalService.getOne(id);

		if (servicio == null) {
			var alert = Alert.sweetToast("El servicio no existe.", "error", 5000);

			flash.addFlashAttribute("alert", alert);

			return "redirect:/servicios/listado";
		}

		model.addAttribute("servicio", servicio);

		return "servicios/edicion";
	}

	// Controller que consume el service update
	@PostMapping("grabar")
	public String grabar(@ModelAttribute("servicio") ServicioAdicional servicio, BindingResult bindingResult,
			Model model, RedirectAttributes flash) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "Revise los datos ingresados.");
			return "servicios/edicion";
		}

		var response = servicioAdicionalService.update(servicio);

		if (!response.success()) {
			model.addAttribute("error", response.mensaje());
			return "servicios/edicion";
		}

		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);
		flash.addFlashAttribute("alert", alert);

		return "redirect:/servicios/listado";
	}
}