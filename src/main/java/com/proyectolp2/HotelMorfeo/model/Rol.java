package com.proyectolp2.HotelMorfeo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;
}
