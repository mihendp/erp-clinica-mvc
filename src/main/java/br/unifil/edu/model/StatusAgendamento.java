package br.unifil.edu.model;

public enum StatusAgendamento {
    AGENDADO("Agendado"),
    REALIZADO("Realizado"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}