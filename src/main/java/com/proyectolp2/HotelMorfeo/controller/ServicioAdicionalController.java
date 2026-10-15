package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyectolp2.HotelMorfeo.model.ServicioAdicional;
import com.proyectolp2.HotelMorfeo.service.ServicioAdicionalService;
import com.proyectolp2.HotelMorfeo.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("servicios")
@RequiredArgsConstructor
public class ServicioAdicionalController {

	private final ServicioAdicionalService servicioAdicionalService;

	// LISTADO
	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstServicios", servicioAdicionalService.getAll());

		return "servicios/listado";
	}
	
	// NUEVO
	@GetMapping("nuevo")
	public String nuevo(Model model) {
	    model.addAttribute("servicio", new ServicioAdicional());

	    return "servicios/nuevo";
	}
	
	// REGISTRAR
	@PostMapping("registrar")
	public String registrar(
	        @ModelAttribute("servicio") ServicioAdicional servicio,
	        Model model,
	        RedirectAttributes flash) {

	    var response = servicioAdicionalService.create(servicio);

	    if (!response.success()) {
	        model.addAttribute("error", response.mensaje());
	        return "servicios/nuevo";
	    }

	    flash.addFlashAttribute(
	            "alert",
	            Alert.sweetToast(response.mensaje(), "success", 5000));

	    return "redirect:/servicios/listado";
	}
}