package br.unifil.edu.controller;

import br.unifil.edu.model.Agendamento;
import br.unifil.edu.model.Atendimento;
import br.unifil.edu.services.AtendimentoService;
import br.unifil.edu.views.atendimentos.AtendimentosView;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;
    @Setter
    private AtendimentosView view;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    public void carregarAgendamentos(String filtroCpf) {
        try {
            List<Agendamento> agendamentos = atendimentoService.findAgendamentosDoMedicoLogado(filtroCpf);
            view.atualizarGrid(agendamentos);
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao carregar agendamentos: " + e.getMessage());
        }
    }

    public void onAtenderClicked(Agendamento agendamento) {
        try {
            Atendimento atendimento = atendimentoService.findOrCreateAtendimentoParaAgendamento(agendamento);
            view.abrirFormulario(atendimento);
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao iniciar atendimento: " + e.getMessage());
        }
    }

    public void salvarAtendimento(Atendimento atendimento) {
        try {
            atendimentoService.save(atendimento);
            view.fecharFormulario();
            view.mostrarNotificacao("Atendimento salvo com sucesso!");
            view.limparFiltroEAtualizar(); // Recarrega o grid
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao salvar atendimento: " + e.getMessage());
        }
    }
}