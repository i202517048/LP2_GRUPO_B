package com.proyectolp2.HotelMorfeo.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "nombres", nullable = false, length = 45)
	private String nombres;

	@Column(name = "apellidos", nullable = false, length = 45)
	private String apellidos;

	@Column(name = "cuenta", nullable = false, unique = true, length = 45)
	private String cuenta;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Column(name = "clave", nullable = false, length = 255)
	private String clave;

	@Column(name = "fecha_nac")
	private LocalDate fechaNac;

	@Column(name = "activo", nullable = false)
	private Boolean activo = true;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_rol", nullable = false)
	private Rol rol;

	public String getFullName() {
		return String.format("%s %s", nombres, apellidos);
	}

	public String getActivoDescripcion() {
		return Boolean.TRUE.equals(activo) ? "Activo" : "Inactivo";
	}
}