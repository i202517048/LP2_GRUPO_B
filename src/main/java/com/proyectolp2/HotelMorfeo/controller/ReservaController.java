package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("reserva")
public class ReservaController {
	private final ReservaService reservaService;
	private final HuespedesService huespedesService;
	private final HabitacionesService habitacionesService;
	
	@GetMapping("listado")
	public String listado(@ModelAttribute ReservaFilter filter,Model model) {
		model.addAttribute("lstReservas", reservaService.search(filter));
		model.addAttribute("filter",filter);
		model.addAttribute("lstHuespedes", huespedesService.getAll());
		model.addAttribute("lstHabitaciones", habitacionesService.getAll());
		
		return "reserva/listado";
	}
	
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("lstHuespedes", huespedesService.getAll());
		model.addAttribute("lstHabitaciones", habitacionesService.getAll());
		model.addAttribute("reservas", new Reserva());
		
		return "reserva/nuevo";
	}
	
	@PostMapping("registrar")
	public String registrar(@ModelAttribute Reserva reservas, RedirectAttributes flash) {
		var response = reservaService.create(reservas); 
		
		var alert = Alert.sweetToast(response.mensaje(), "success", 5000);
		flash.addFlashAttribute("alert", alert);
		return "redirect:/reserva/listado";
	}
	
	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id ,Model model) {
		
		model.addAttribute("lstHuespedes", huespedesService.getAll());
		model.addAttribute("lstHabitaciones", habitacionesService.getAll());
				
		model.addAttribute("reserva", reservaService.getOne(id));
		
		Reserva reserva = reservaService.getOne(id);
		
		System.out.println(reserva.getFechaCheckin());
	    System.out.println(reserva.getFechaCheckoutPrevista());
	    
		return "reserva/edicion";
	}
	
	
	// Controller que consume el service update
		@PostMapping("grabar")
		public String grabar(@ModelAttribute Reserva reservas, RedirectAttributes flash) {
			var response = reservaService.update(reservas);

			var alert = Alert.sweetToast(response.mensaje(), "success", 5000);
			flash.addFlashAttribute("alert", alert);
			return "redirect:/reserva/listado";
		}

	
}
