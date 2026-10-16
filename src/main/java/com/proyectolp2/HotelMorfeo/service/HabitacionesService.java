package com.proyectolp2.HotelMorfeo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyectolp2.HotelMorfeo.model.Habitacion;
import com.proyectolp2.HotelMorfeo.repository.HabitacionesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitacionesService {

	private final HabitacionesRepository habitacionesRepository;

	public List<Habitacion> getAll() {
		return habitacionesRepository.findAll();
	}
}