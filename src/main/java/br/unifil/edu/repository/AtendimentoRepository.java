package br.unifil.edu.repository;

import br.unifil.edu.model.Agendamento;
import br.unifil.edu.model.Atendimento;
import br.unifil.edu.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    // Encontra um atendimento a partir do seu agendamento correspondente
    Optional<Atendimento> findByAgendamento(Agendamento agendamento);

}