package com.proyectolp2.HotelMorfeo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservas")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reserva")
	private Integer idReserva;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_huesped")
	private Huespedes huespedes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_habitacion")
	private Habitaciones habitaciones;
		
	@Column(name = "fecha_checkin")
	private LocalDate fechaCheckin;
	
	@Column(name = "fecha_checkout_prevista")
	private LocalDate fechaCheckoutPrevista;

	
	@Column(name = "fecha_checkout_real")
	private LocalDateTime fechaCheckoutReal;
	
	@Column(name = "estado")
	private String estado;

}