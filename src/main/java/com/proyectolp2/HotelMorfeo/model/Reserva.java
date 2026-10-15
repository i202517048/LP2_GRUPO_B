package com.proyectolp2.HotelMorfeo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name = "id_huesped", nullable = false)
    private Huesped huesped;

    @ManyToOne
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    @Column(name = "fecha_checkin", nullable = false)
    private LocalDate fechaCheckin;

    @Column(name = "fecha_checkout_prevista", nullable = false)
    private LocalDate fechaCheckoutPrevista;

    @Column(name = "fecha_checkout_real")
    private LocalDateTime fechaCheckoutReal;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado = "A";
}
