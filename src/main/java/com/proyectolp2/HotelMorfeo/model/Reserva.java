package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reserva")
	private Integer idReserva;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_huesped", nullable = false)
	private Huesped huesped;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_habitacion", nullable = false)
	private Habitacion habitacion;

	// Entrada en formato ISO
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(name = "fecha_checkin", nullable = false)
	private LocalDate fechaCheckin;

	// Salida prevista en formato ISO
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(name = "fecha_checkout_prevista", nullable = false)
	private LocalDate fechaCheckoutPrevista;

	@Column(name = "fecha_checkout_real")
	private LocalDateTime fechaCheckoutReal;

	@Column(name = "estado", nullable = false, length = 1)
	private String estado = "P";

	@Column(name = "precio_noche_acordado", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioNocheAcordado;

	public String getEstadoDescripcion() {

		if (estado == null) {
			return "Desconocido";
		}

		return switch (estado) {
		case "P" -> "Pendiente de ingreso";
		case "A" -> "Activa";
		case "F" -> "Finalizada";
		case "C" -> "Cancelada";
		default -> "Desconocido";
		};
	}
}