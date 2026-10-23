package com.proyectolp2.HotelMorfeo.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectolp2.HotelMorfeo.model.Boleta;
public interface BoletaRepository extends JpaRepository<Boleta,Integer> {
 List<Boleta> findAllByOrderByIdBoletaDesc();
 boolean existsByReservaIdReserva(Integer idReserva);
}
