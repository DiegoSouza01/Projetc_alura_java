package org.example.projectjavaalura.controller;

import org.example.projectjavaalura.model.Sala;
import org.example.projectjavaalura.service.SalaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public List<Sala> listar() {
        return salaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscar(@PathVariable Long id) {
        Sala sala = salaService.buscarPorId(id);
        return ResponseEntity.ok(sala);
    }

    @PostMapping
    public ResponseEntity<Sala> criar(@RequestBody Sala sala) {
        Sala novaSala = salaService.salvar(sala);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> atualizar(@PathVariable Long id, @RequestBody Sala sala) {
        Sala salaAtualizada = salaService.atualizar(id, sala);
        return ResponseEntity.ok(salaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        salaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}