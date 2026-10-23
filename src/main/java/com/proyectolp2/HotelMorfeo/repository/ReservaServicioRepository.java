package com.proyectolp2.HotelMorfeo.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectolp2.HotelMorfeo.model.ReservaServicio;
public interface ReservaServicioRepository extends JpaRepository<ReservaServicio,Integer> {
 List<ReservaServicio> findByReservaIdReservaAndAnuladoFalseOrderByIdReservaServicioAsc(Integer idReserva);
}
