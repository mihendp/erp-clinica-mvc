package br.unifil.edu.services;

import br.unifil.edu.model.Medico;
import br.unifil.edu.model.User;
import br.unifil.edu.model.Agendamento;
import br.unifil.edu.model.Paciente;
import br.unifil.edu.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final MedicoService medicoService; // Para buscar os médicos
    private final PacienteService pacienteService; // Para buscar os pacientes

    public List<Agendamento> findAll() {
        return agendamentoRepository.findAll();
    }

    public Agendamento save(Agendamento agendamento) {
        // Valida se o médico selecionado já possui outro atendimento naquele horário
        Optional<Agendamento> conflito = agendamentoRepository.findByMedicoAndDataHora(agendamento.getMedico(), agendamento.getDataHora());

        // Se existe um conflito, verifica se não é o próprio agendamento que está sendo editado
        if (conflito.isPresent() && !conflito.get().getId().equals(agendamento.getId())) {
            throw new IllegalStateException("O médico selecionado já possui um agendamento neste horário.");
        }

        return agendamentoRepository.save(agendamento);
    }

    public void delete(Long id) {
        agendamentoRepository.deleteById(id);
    }

    // Métodos para popular os ComboBoxes no formulário
    public List<Medico> findAllDoctors() {
        return medicoService.findAll();
    }

    public List<Paciente> findAllPacientes() {
        return pacienteService.findAll();
    }
}