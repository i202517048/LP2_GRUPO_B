package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_serviciosadicionales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioAdicional {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servicio")
	private Integer idServicio;

	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;

	@Column(name = "precio", nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;

	@Column(name = "activo_servicios", nullable = false)
	private Integer activoServicios = 1;

	public String getActivoDescripcion() {
		return Integer.valueOf(1).equals(activoServicios) ? "Activo" : "Inactivo";
	}
}