package org.example.projectjavaalura.service;

import org.example.projectjavaalura.model.Reserva;
import org.example.projectjavaalura.model.Sala;
import org.example.projectjavaalura.model.Usuario;
import org.example.projectjavaalura.repository.ReservaRepository;
import org.example.projectjavaalura.repository.SalaRepository;
import org.example.projectjavaalura.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(ReservaRepository reservaRepository, SalaRepository salaRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reserva criarReserva(Long salaId, Long usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada."));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        List<Reserva> conflitos = reservaRepository.buscarConflitos(salaId, inicio, fim);
        if (!conflitos.isEmpty()) {
            throw new IllegalStateException("Conflito de horário: a sala já está reservada neste período.");
        }

        Reserva novaReserva = new Reserva(sala, usuario, inicio, fim);
        return reservaRepository.save(novaReserva);
    }

    @Transactional
    public Reserva cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada."));
        reserva.cancelar();
        return reservaRepository.save(reserva);
    }
}