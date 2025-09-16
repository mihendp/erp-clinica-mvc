package br.unifil.edu.services;

import br.unifil.edu.model.Medico;
import br.unifil.edu.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;


    public List<Medico> findAll() {
        return medicoRepository.findAll();
    }

    public void save(Medico medico) {
        medicoRepository.save(medico);
    }

    public void delete(Medico medico) {
        medicoRepository.delete(medico);
    }
}
