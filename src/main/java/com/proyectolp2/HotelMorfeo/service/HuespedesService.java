package com.proyectolp2.HotelMorfeo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyectolp2.HotelMorfeo.model.Huesped;
import com.proyectolp2.HotelMorfeo.repository.HuespedesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HuespedesService {
	private final HuespedesRepository huespedesRepository;
	
	@GetMapping("nuevo")
	public List<Huesped> getAll() {
		return huespedesRepository.findAll();
	}

}
