package org.example.projectjavaalura.service;

import org.example.projectjavaalura.model.Reserva;
import org.example.projectjavaalura.model.Sala;
import org.example.projectjavaalura.model.Usuario;
import org.example.projectjavaalura.repository.ReservaRepository;
import org.example.projectjavaalura.repository.SalaRepository;
import org.example.projectjavaalura.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private SalaRepository salaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Sala sala;
    private Usuario usuario;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1L);
        sala.setAtiva(true);

        usuario = new Usuario();
        usuario.setId(1L);

        inicio = LocalDateTime.of(2026, 3, 10, 10, 0);
        fim = LocalDateTime.of(2026, 3, 10, 12, 0);
    }

    @Test
    @DisplayName("Deve criar reserva com sucesso (Caminho Feliz)")
    void deveCriarReservaComSucesso() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reservaRepository.buscarConflitos(1L, inicio, fim)).thenReturn(Collections.emptyList());

        Reserva reservaSalva = new Reserva(sala, usuario, inicio, fim);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        Reserva resultado = reservaService.criarReserva(1L, 1L, inicio, fim);

        assertNotNull(resultado);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando houver conflito no banco")
    void deveLancarErroQuandoHouverConflito() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Reserva conflito = new Reserva(sala, usuario, inicio, fim);
        when(reservaRepository.buscarConflitos(1L, inicio, fim)).thenReturn(List.of(conflito));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservaService.criarReserva(1L, 1L, inicio, fim);
        });

        assertEquals("Conflito de horário: a sala já está reservada neste período.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se sala não existir")
    void deveLancarErroSeSalaNaoExistir() {
        when(salaRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.criarReserva(99L, 1L, inicio, fim);
        });

        assertEquals("Sala não encontrada.", exception.getMessage());
    }
}