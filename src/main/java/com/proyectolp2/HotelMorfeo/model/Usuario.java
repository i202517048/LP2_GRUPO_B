package com.proyectolp2.HotelMorfeo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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

    @Column(name = "cuenta", nullable = false, length = 45)
    private String cuenta;

    @Column(name = "clave", nullable = false, length = 45)
    private String clave;

    @Column(name = "fecha_nac")
    private LocalDate fechaNac;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;
}
