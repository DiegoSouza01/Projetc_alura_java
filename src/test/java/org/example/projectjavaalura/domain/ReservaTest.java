package org.example.projectjavaalura.domain;

import org.example.projectjavaalura.model.Reserva;
import org.example.projectjavaalura.model.Sala;
import org.example.projectjavaalura.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private Sala sala;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1L);
        sala.setNome("Sala de Reunião");
        sala.setCapacidade(10);
        sala.setAtiva(true);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
    }

    @Test
    @DisplayName("Deve retornar TRUE quando os horários se sobrepõem exatamente")
    void deveRetornarTrueQuandoHouverConflito() {
        LocalDateTime inicio1 = LocalDateTime.of(2026, 3, 10, 14, 0);
        LocalDateTime fim1 = LocalDateTime.of(2026, 3, 10, 16, 0);
        Reserva reservaExistente = new Reserva(sala, usuario, inicio1, fim1);

        LocalDateTime inicio2 = LocalDateTime.of(2026, 3, 10, 15, 0); // Começa no meio da outra
        LocalDateTime fim2 = LocalDateTime.of(2026, 3, 10, 17, 0);
        Reserva novaReserva = new Reserva(sala, usuario, inicio2, fim2);

        assertTrue(novaReserva.isConflitante(reservaExistente));
    }

    @Test
    @DisplayName("Deve retornar FALSE quando uma reserva termina exatemente quando a outra começa (intervalo semiaberto)")
    void deveRetornarFalseQuandoHorariosForemConcecutivos() {
        LocalDateTime inicio1 = LocalDateTime.of(2026, 3, 10, 14, 0);
        LocalDateTime fim1 = LocalDateTime.of(2026, 3, 10, 15, 0);
        Reserva reservaExistente = new Reserva(sala, usuario, inicio1, fim1);

        LocalDateTime inicio2 = LocalDateTime.of(2026, 3, 10, 15, 0); // Começa bem no fim da anterior
        LocalDateTime fim2 = LocalDateTime.of(2026, 3, 10, 16, 0);
        Reserva novaReserva = new Reserva(sala, usuario, inicio2, fim2);

        assertFalse(novaReserva.isConflitante(reservaExistente));
    }

    @Test
    @DisplayName("Deve retornar FALSE se a reserva existente estiver cancelada, mesmo no mesmo horário")
    void deveRetornarFalseSeReservaCancelada() {
        LocalDateTime inicio = LocalDateTime.of(2026, 3, 10, 14, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 3, 10, 16, 0);
        Reserva reservaExistente = new Reserva(sala, usuario, inicio, fim);
        reservaExistente.cancelar(); // Cancelamos a existente

        Reserva novaReserva = new Reserva(sala, usuario, inicio, fim);

        assertFalse(novaReserva.isConflitante(reservaExistente));
    }
}