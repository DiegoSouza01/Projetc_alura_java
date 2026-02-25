package org.example.projectjavaalura.controller;

import org.example.projectjavaalura.model.Reserva;
import org.example.projectjavaalura.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> criar(@RequestParam Long salaId,
                                         @RequestParam Long usuarioId,
                                         @RequestParam LocalDateTime inicio,
                                         @RequestParam LocalDateTime fim) {
        Reserva reserva = reservaService.criarReserva(salaId, usuarioId, inicio, fim);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(reservaCancelada);
    }
}
