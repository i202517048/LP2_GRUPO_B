package com.proyectolp2.HotelMorfeo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectolp2.HotelMorfeo.model.ServicioAdicional;

public interface ServicioAdicionalRepository extends JpaRepository<ServicioAdicional, Integer> {

	// Consumos vigentes que aun no tienen detalle de boleta
	@Query("""
			select count(c)
			from ReservaServicio c
			where c.servicioAdicional.idServicio = :idServicio
			    and c.anulado = false
			    and not exists (
			        select d.idDetalle
			        from DetalleBoleta d
			        where d.reservaServicio = c
			    )
			""")
	long contarConsumosPendientesDeFacturacion(@Param("idServicio") Integer idServicio);
}