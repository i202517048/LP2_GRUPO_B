package com.proyectolp2.HotelMorfeo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyectolp2.HotelMorfeo.model.Habitaciones;
import com.proyectolp2.HotelMorfeo.repository.HabitacionesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitacionesService {
	private final HabitacionesRepository habitacionesRepository;
	
	@GetMapping("nuevo")
	public List<Habitaciones> getAll() {
		return habitacionesRepository.findAll();
	}

}
