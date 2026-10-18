package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_habitaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habitacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_habitacion")
	private Integer idHabitacion;

	@Column(name = "numero", nullable = false, unique = true, length = 10)
	private String numero;

	@Column(name = "tipo", nullable = false, length = 1)
	private String tipo;

	@Column(name = "precio_noche", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioNoche;

	@Column(name = "estado", nullable = false, length = 1)
	private String estado = "D";

	@Column(name = "activo_habitaciones", nullable = false)
	private Boolean activoHabitaciones = true;

	public String getActivoDescripcion() {
		return Boolean.TRUE.equals(activoHabitaciones) ? "Activo" : "Inactivo";
	}

	public String getTipoDescripcion() {

		if (tipo == null) {
			return "Desconocido";
		}

		return switch (tipo) {
		case "S" -> "Simple";
		case "D" -> "Doble";
		default -> "Desconocido";
		};
	}

	public String getEstadoDescripcion() {

		if (estado == null) {
			return "Desconocido";
		}

		return switch (estado) {
		case "D" -> "Disponible";
		case "O" -> "Ocupada";
		case "M" -> "Mantenimiento";
		default -> "Desconocido";
		};
	}
}