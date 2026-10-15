package com.proyectolp2.HotelMorfeo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyectolp2.HotelMorfeo.model.Habitacion;
import com.proyectolp2.HotelMorfeo.repository.HabitacionesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitacionesService {
	private final HabitacionesRepository habitacionesRepository;
	
	@GetMapping("nuevo")
	public List<Habitacion> getAll() {
		return habitacionesRepository.findAll();
	}

}
