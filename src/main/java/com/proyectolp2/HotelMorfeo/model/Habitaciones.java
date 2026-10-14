package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "habitaciones")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Habitaciones {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_habitacion")
	private Integer idHabitacion;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "precio_noche")
	private BigDecimal precioNoche;
	
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "activo_habitaciones")
	private Byte activoHabitaciones;

}
