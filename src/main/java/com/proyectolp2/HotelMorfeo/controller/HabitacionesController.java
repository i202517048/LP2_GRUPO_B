package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyectolp2.HotelMorfeo.service.HabitacionesService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("habitaciones")
public class HabitacionesController {

	private final HabitacionesService habitacionesService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstHabitaciones", habitacionesService.getAll());

		return "habitaciones/listado";
	}
}