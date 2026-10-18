package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_reservaservicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaServicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reserva_servicio")
	private Integer idReservaServicio;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_reserva", nullable = false)
	private Reserva reserva;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_servicio", nullable = false)
	private ServicioAdicional servicioAdicional;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad = 1;

	@Column(name = "fecha_consumo", nullable = false)
	private LocalDateTime fechaConsumo;

	@Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioUnitario;

	@Column(name = "anulado", nullable = false)
	private Boolean anulado = false;

	@Column(name = "fecha_anulacion")
	private LocalDateTime fechaAnulacion;

	@Column(name = "motivo_anulacion", length = 250)
	private String motivoAnulacion;

	public String getAnuladoDescripcion() {
		return Boolean.TRUE.equals(anulado) ? "Anulado" : "Vigente";
	}
}