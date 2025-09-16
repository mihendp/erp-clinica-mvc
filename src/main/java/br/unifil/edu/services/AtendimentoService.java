package br.unifil.edu.services;

import br.unifil.edu.model.*;
import br.unifil.edu.repository.AgendamentoRepository;
import br.unifil.edu.repository.AtendimentoRepository;
import br.unifil.edu.repository.MedicoRepository;
import br.unifil.edu.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final MedicoRepository medicoRepository;
    private final AuthenticatedUser authenticatedUser;


    /**
     * Busca os agendamentos do médico logado, opcionalmente filtrando pelo CPF do paciente.
     */
    public List<Agendamento> findAgendamentosDoMedicoLogado(String filtroCpf) {
        Optional<Medico> medicoOpt = authenticatedUser.get()
                .flatMap(medicoRepository::findByUser);

        if (medicoOpt.isEmpty()) {
            return Collections.emptyList(); // Retorna lista vazia se não encontrar o perfil de médico
        }

        return agendamentoRepository.findByMedicoAndPacienteCpfContaining(medicoOpt.get(), filtroCpf == null ? "" : filtroCpf);
    }

    /**
     * Busca um atendimento existente para um agendamento ou cria um novo se não existir.
     */
    public Atendimento findOrCreateAtendimentoParaAgendamento(Agendamento agendamento) {
        return atendimentoRepository.findByAgendamento(agendamento)
                .orElseGet(() -> {
                    Atendimento novoAtendimento = new Atendimento();
                    novoAtendimento.setAgendamento(agendamento);
                    novoAtendimento.setDataAtendimento(LocalDate.now());
                    return novoAtendimento;
                });
    }

    /**
     * Salva o atendimento e atualiza o status do agendamento para "Realizado".
     */
    @Transactional
    public Atendimento save(Atendimento atendimento) {
        Atendimento savedAtendimento = atendimentoRepository.save(atendimento);

        // Atualiza o status do agendamento original
        Agendamento agendamento = savedAtendimento.getAgendamento();
        agendamento.setStatus(StatusAgendamento.REALIZADO);
        agendamentoRepository.save(agendamento);

        return savedAtendimento;
    }
}