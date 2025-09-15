package br.unifil.edu.controller;

import br.unifil.edu.model.Medico;
import br.unifil.edu.repository.MedicoRepository;
import br.unifil.edu.views.medicos.MedicosView;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class MedicoController {
    private final MedicoRepository medicoRepository;
    @Setter
    private MedicosView view;

    @Autowired
    public MedicoController(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public void carregarMedicos() {
        try {
            List<Medico> medicos = medicoRepository.findAll();
            view.atualizarGrid(medicos);
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao carregar médicos: " + e.getMessage());
        }
    }

    public void salvarMedico(Medico medico) {
        try {
            medicoRepository.save(medico);
            view.fecharFormulario();
            view.mostrarNotificacao("Médico salvo com sucesso!");
            carregarMedicos(); // Recarrega a lista para mostrar o novo/atualizado médico
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao salvar médico: " + e.getMessage());
        }
    }

    public void deletarMedico(Medico medico) {
        try {
            medicoRepository.delete(medico);
            view.fecharFormulario();
            view.mostrarNotificacao("Médico deletado com sucesso!");
            carregarMedicos(); // Recarrega a lista
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao deletar médico: " + e.getMessage());
        }
    }

    public void onAdicionarNovoClicked() {
        view.abrirFormulario(new Medico(), true); // Abre com um médico em branco
    }

    public void onEditarClicked(Medico medico) {
        view.abrirFormulario(medico, false); // Abre com o médico selecionado
    }
}