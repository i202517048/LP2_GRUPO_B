package com.proyectolp2.HotelMorfeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
		
	@GetMapping("/")
	public String home() {
			return "redirect:/login";
	}
	
	// Pantalla de inicio
	@GetMapping("/home/dashboard")
	public String dashboard() {
	    return "home/dashboard";
	}

}
