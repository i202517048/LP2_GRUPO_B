package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;
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
@Table(name = "boletas")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Boletas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_boleta")
	private Integer idBoleta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_reserva")
	private Reservas reservas;
	
	@Column(name = "fecha_emision")
	private LocalDateTime fechaEmision;
	
	@Column(name = "noches")
	private Integer noches;
	
	@Column(name = "subtotal_habitacion")
	private BigDecimal subtotalHabitacion;
	
	@Column(name = "subtotal_servicios")
	private BigDecimal subtotalServicios;
	
	@Column(name = "total")
	private BigDecimal total;
	
}
