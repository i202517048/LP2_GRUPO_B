package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_detalle_boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleBoleta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle")
	private Integer idDetalle;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_boleta", nullable = false)
	private Boleta boleta;

	@ManyToOne
	@JoinColumn(name = "id_reserva_servicio", unique = true)
	private ReservaServicio reservaServicio;

	@Column(name = "descripcion", nullable = false, length = 150)
	private String descripcion;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad = 1;

	@Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioUnitario;

	@Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
	private BigDecimal subtotal;
}