package org.example.projectjavaalura.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status = StatusReserva.ATIVA;

    public Reserva(Sala sala, Usuario usuario, LocalDateTime inicio, LocalDateTime fim) {
        if (!sala.isAtiva()) {
            throw new IllegalArgumentException("Não é possível reservar uma sala inativa.");
        }
        if (inicio == null || fim == null || !inicio.isBefore(fim)) {
            throw new IllegalArgumentException("A data/hora de início deve ser anterior à data/hora de fim.");
        }

        this.sala = sala;
        this.usuario = usuario;
        this.dataHoraInicio = inicio;
        this.dataHoraFim = fim;
        this.status = StatusReserva.ATIVA;
    }

    public void cancelar() {
        if (this.status == StatusReserva.CANCELADA) {
            throw new IllegalStateException("Esta reserva já se encontra cancelada.");
        }
        this.status = StatusReserva.CANCELADA;
    }

    public boolean isConflitante(Reserva outraReserva) {
        if (this.status == StatusReserva.CANCELADA || outraReserva.getStatus() == StatusReserva.CANCELADA) {
            return false;
        }

        if (!this.sala.getId().equals(outraReserva.getSala().getId())) {
            return false;
        }

        return this.dataHoraInicio.isBefore(outraReserva.getDataHoraFim()) &&
                this.dataHoraFim.isAfter(outraReserva.getDataHoraInicio());
    }
}
