package br.unifil.edu.controller;

import br.unifil.edu.model.Agendamento;
import br.unifil.edu.services.AgendamentoService;
import br.unifil.edu.views.agendamentos.AgendamentosView;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    @Setter
    private AgendamentosView view;

    @Autowired
    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    public void carregarAgendamentos() {
        try {
            List<Agendamento> agendamentos = agendamentoService.findAll();
            view.atualizarGrid(agendamentos);
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao carregar agendamentos: " + e.getMessage());
        }
    }

    public void salvarAgendamento(Agendamento agendamento) {
        try {
            agendamentoService.save(agendamento);
            view.fecharFormulario();
            view.mostrarNotificacao("Agendamento salvo com sucesso!");
            carregarAgendamentos();
        } catch (IllegalStateException ise) {
            // Captura a exceção de validação específica do serviço
            view.mostrarNotificacao(ise.getMessage());
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao salvar agendamento: " + e.getMessage());
        }
    }

    public void deletarAgendamento(Agendamento agendamento) {
        try {
            agendamentoService.delete(agendamento.getId());
            view.fecharFormulario();
            view.mostrarNotificacao("Agendamento deletado com sucesso!");
            carregarAgendamentos();
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao deletar agendamento: " + e.getMessage());
        }
    }

    public void onAdicionarNovoClicked() {
        view.abrirFormulario(new Agendamento(), true);
    }

    public void onEditarClicked(Agendamento agendamento) {
        view.abrirFormulario(agendamento, false);
    }
}