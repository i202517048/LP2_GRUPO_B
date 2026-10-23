package com.proyectolp2.HotelMorfeo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_boletas")
@lombok.Getter
@lombok.Setter
@NoArgsConstructor
@AllArgsConstructor
public class Boleta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_boleta")
	private Integer idBoleta;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_reserva", nullable = false, unique = true)
	private Reserva reserva;

	@Column(name = "fecha_emision", nullable = false)
	private LocalDateTime fechaEmision;

	@Column(name = "total", nullable = false, precision = 10, scale = 2)
	private BigDecimal total;

	@Column(name = "cliente_nombre", nullable = false, length = 201)
	private String clienteNombre;

	@Column(name = "cliente_documento", nullable = false, length = 20)
	private String clienteDocumento;

	// Cabecera y detalle como en el ejemplo del profesor.
	@OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL)
	private List<DetalleBoleta> detalles = new ArrayList<>();
}
