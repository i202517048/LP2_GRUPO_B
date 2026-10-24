package com.proyectolp2.HotelMorfeo.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class ReservaFechaFilter {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCheckIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCheckOut;

    // Constructor vacío (Obligatorio para que Spring pueda instanciarlo)
    public ReservaFechaFilter() {}

    // Getters y Setters
    public LocalDate getFechaCheckIn() {
        return fechaCheckIn;
    }

    public void setFechaCheckIn(LocalDate fechaCheckIn) {
        this.fechaCheckIn = fechaCheckIn;
    }

    public LocalDate getFechaCheckOut() {
        return fechaCheckOut;
    }

    public void setFechaCheckOut(LocalDate fechaCheckOut) {
        this.fechaCheckOut = fechaCheckOut;
    }
}