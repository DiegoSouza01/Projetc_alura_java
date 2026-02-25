package org.example.projectjavaalura.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(nullable = false)
    private boolean ativa = true;

    public void setCapacidade(Integer capacidade) {
        if (capacidade == null || capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade da sala deve ser um valor positivo.");
        }
        this.capacidade = capacidade;
    }
}
