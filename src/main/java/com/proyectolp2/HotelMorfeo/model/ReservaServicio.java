package com.proyectolp2.HotelMorfeo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservaservicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva_servicio")
    private Integer idReservaServicio;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioAdicional servicioAdicional;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;

    @Column(name = "fecha_consumo", nullable = false)
    private LocalDateTime fechaConsumo;
}
