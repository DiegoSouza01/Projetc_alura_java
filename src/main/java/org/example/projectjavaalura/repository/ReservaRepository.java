package org.example.projectjavaalura.repository;

import org.example.projectjavaalura.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Page<Reserva> findAll(Pageable pageable);

    Page<Reserva> findByUsuarioId(Long usuarioId, Pageable pageable);

    @Query("SELECT r FROM Reserva r WHERE r.sala.id = :salaId " +
            "AND r.status = 'ATIVA' " +
            "AND r.dataHoraInicio < :fim " +
            "AND r.dataHoraFim > :inicio")
    List<Reserva> buscarConflitos(@Param("salaId") Long salaId,
                                  @Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim);
}