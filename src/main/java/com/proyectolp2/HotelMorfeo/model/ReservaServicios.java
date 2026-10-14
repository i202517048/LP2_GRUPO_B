package com.proyectolp2.HotelMorfeo.model;

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
@Table(name = "reservaservicios")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservaServicios {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reserva_servicio")
	private Integer idReservaServicio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_reserva")
	private Reservas reservas;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_servicio")
	private ServiciosAdicionales serviciosAdicionales;
		
	@Column(name = "cantidad")
	private Integer cantidad;
	
	@Column(name = "fecha_consumo")
	private LocalDateTime fechaConsumo;

}