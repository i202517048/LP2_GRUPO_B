package com.proyectolp2.HotelMorfeo.dto;

import java.io.Serializable;

public record UsuarioSesion(Integer idUsuario, String nombres, String apellidos, Integer idRol)
		implements Serializable {
}