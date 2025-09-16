package br.unifil.edu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "agendamento_id", unique = true)
    private Agendamento agendamento;

    @NotNull
    private LocalDate dataAtendimento;

    @Lob // Large Object, para textos longos
    @Column(length = 2000)
    private String anamnese; // Histórico e queixas do paciente

    @Lob
    @Column(length = 2000)
    private String diagnostico; // Diagnóstico do médico

    @Lob
    @Column(length = 2000)
    private String prescricao; // Receitas e tratamentos

    @Lob
    @Column(length = 1000)
    private String observacoes; // Outras notas
}