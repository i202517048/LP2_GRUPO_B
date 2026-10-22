package com.proyectolp2.HotelMorfeo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectolp2.HotelMorfeo.model.Huesped;

public interface HuespedesRepository extends JpaRepository<Huesped, Integer> {

	List<Huesped> findAllByOrderByIdHuespedDesc();

	List<Huesped> findAllByOrderByApellidosAscNombresAscIdHuespedAsc();

	// Comprobar documento al registrar
	boolean existsByDocumentoIgnoreCase(String documento);

	// Comprobar documento al editar
	boolean existsByDocumentoIgnoreCaseAndIdHuespedNot(String documento, Integer idHuesped);

	// Nombre completo, documento y activación
	@Query("""
			select h from Huesped h
			where
				(:nombre is null or :nombre = ''
					or lower(concat(concat(h.nombres, ' '), h.apellidos))
						like lower(concat(concat('%', :nombre), '%')))
				and
				(:documento is null or :documento = ''
					or lower(h.documento)
						like lower(concat(concat('%', :documento), '%')))
				and
				(:activo is null or h.activoHuespedes = :activo)
			order by h.idHuesped desc
			""")
	List<Huesped> findByFilters(@Param("nombre") String nombre, @Param("documento") String documento,
			@Param("activo") Integer activo);
}
