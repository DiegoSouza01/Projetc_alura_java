package org.example.projectjavaalura.service;

import org.example.projectjavaalura.model.Sala;
import org.example.projectjavaalura.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalaService {

    private final SalaRepository salaRepository;

    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<Sala> listarTodas() {
        return salaRepository.findAll();
    }

    public Sala buscarPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada."));
    }

    @Transactional
    public Sala salvar(Sala sala) {
        return salaRepository.save(sala);
    }

    @Transactional
    public Sala atualizar(Long id, Sala salaAtualizada) {
        Sala salaExistente = buscarPorId(id);

        salaExistente.setNome(salaAtualizada.getNome());
        salaExistente.setCapacidade(salaAtualizada.getCapacidade());

        return salaRepository.save(salaExistente);
    }

    @Transactional
    public void deletar(Long id) {
        Sala sala = buscarPorId(id);
        sala.setAtiva(false);
        salaRepository.save(sala);
    }
}