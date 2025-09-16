package br.unifil.edu.services;

import br.unifil.edu.model.Paciente;
import br.unifil.edu.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    public void save(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    public void delete(Paciente paciente) {
        pacienteRepository.delete(paciente);
    }
}
