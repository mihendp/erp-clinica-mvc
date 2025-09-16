package br.unifil.edu.repository;

import br.unifil.edu.model.Medico;
import br.unifil.edu.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    /**
     * Procura por um agendamento existente para um médico específico em uma data e hora exatas.
     * @param medico O médico a ser verificado.
     * @param dataHora A data e hora a ser verificada.
     * @return Um Optional contendo o agendamento conflitante, se houver.
     */
    Optional<Agendamento> findByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);

    @Query("SELECT a FROM Agendamento a WHERE a.medico = :medico AND a.paciente.cpf LIKE %:cpf%")
    List<Agendamento> findByMedicoAndPacienteCpfContaining(@Param("medico") Medico medico, @Param("cpf") String cpf);
}