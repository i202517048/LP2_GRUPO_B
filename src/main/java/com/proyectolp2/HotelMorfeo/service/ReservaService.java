package com.proyectolp2.HotelMorfeo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyectolp2.HotelMorfeo.dto.ResultadoResponse;
import com.proyectolp2.HotelMorfeo.model.Reserva;

import com.proyectolp2.HotelMorfeo.repository.ReservaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {
	private final ReservaRepository reservaRepository;
		
	@GetMapping("listado")
	public List<Reserva> getAll() {
		return reservaRepository.findAll();
		
	}
		
	// Método Crear
	@Transactional
	public ResultadoResponse create(Reserva reservas) {
		try {
			var registrado = reservaRepository.save(reservas);
			var mensaje = String.format("Reserva con Id %s registrado.", registrado.getIdReserva());
			
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}
	
	// Método obtener
	public Reserva getOne(Integer id) {
		
		return reservaRepository.findById(id).orElse(null);
	}
	
	
	// Método actualizar
		@Transactional
		public ResultadoResponse update(Reserva reservas) {
			try {
				var registrado = reservaRepository.save(reservas);

				var mensaje = String.format("Inventario Nro. %s actualizado.", registrado.getIdReserva());

				return new ResultadoResponse(true, mensaje);

			} catch (Exception e) {
				e.printStackTrace();
				return new ResultadoResponse(false, "Hubo un error en la trasacción");
			}
		}
	

}
