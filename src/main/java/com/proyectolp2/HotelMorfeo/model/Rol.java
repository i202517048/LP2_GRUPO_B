package com.proyectolp2.HotelMorfeo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_rol")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rol {
	
	@Id
	@Column(name = "id_rol")
	private Integer idRol;
	
	@Column(name = "descripcion")
	private String descripcion;
	
}