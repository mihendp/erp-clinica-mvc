package br.unifil.edu.controller;

import br.unifil.edu.model.Paciente;
import br.unifil.edu.repository.PacienteRepository;
import br.unifil.edu.views.pacientes.PacientesView;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class PacienteController {
    private final PacienteRepository pacienteRepository;
    @Setter
    private PacientesView view;

    @Autowired
    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public void carregarPacientes() {
        try {
            List<Paciente> pacientes = pacienteRepository.findAll();
            view.atualizarGrid(pacientes);
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao carregar pacientes: " + e.getMessage());
        }
    }

    public void salvarPaciente(Paciente paciente) {
        try {
            pacienteRepository.save(paciente);
            view.fecharFormulario();
            view.mostrarNotificacao("Paciente salvo com sucesso!");
            carregarPacientes(); // Recarrega a lista para mostrar o novo/atualizado paciente
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao salvar paciente: " + e.getMessage());
        }
    }

    public void deletarPaciente(Paciente paciente) {
        try {
            pacienteRepository.delete(paciente);
            view.fecharFormulario();
            view.mostrarNotificacao("Paciente deletado com sucesso!");
            carregarPacientes(); // Recarrega a lista
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao deletar paciente: " + e.getMessage());
        }
    }

    public void onAdicionarNovoClicked() {
        view.abrirFormulario(new Paciente(), true); // Abre com um paciente em branco
    }

    public void onEditarClicked(Paciente paciente) {
        view.abrirFormulario(paciente, false); // Abre com o paciente selecionado
    }
}
