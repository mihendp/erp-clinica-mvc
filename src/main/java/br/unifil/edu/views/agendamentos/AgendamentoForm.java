package br.unifil.edu.views.agendamentos;

import br.unifil.edu.model.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class AgendamentoForm extends Dialog {

    private final Binder<Agendamento> binder = new BeanValidationBinder<>(Agendamento.class);

    private final ComboBox<Paciente> paciente = new ComboBox<>("Paciente");
    private final ComboBox<Medico> medico = new ComboBox<>("Médico");
    private final DateTimePicker dataHora = new DateTimePicker("Data e Hora");
    private final ComboBox<StatusAgendamento> status = new ComboBox<>("Status");
    private final TextArea observacoes = new TextArea("Observações");

    private final Button saveButton = new Button("Salvar");
    private final Button deleteButton = new Button("Deletar");
    private final Button closeButton = new Button("Cancelar");

    public AgendamentoForm(List<Paciente> pacientes, List<Medico> medicos) {
        setHeaderTitle("Dados do Agendamento");
        configurarCampos(pacientes, medicos);
        binder.bindInstanceFields(this);
        add(createFormLayout(), createButtonLayout());
    }

    private void configurarCampos(List<Paciente> pacientes, List<Medico> medicos) {
        paciente.setItems(pacientes);
        paciente.setItemLabelGenerator(Paciente::getNome);
        medico.setItems(medicos);
        medico.setItemLabelGenerator(Medico::getNome);
        status.setItems(StatusAgendamento.values());
        status.setItemLabelGenerator(StatusAgendamento::getDescricao);
    }

    private Component createFormLayout() {
        return new FormLayout(paciente, medico, dataHora, status, observacoes);
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, binder.getBean())));
        closeButton.addClickListener(e -> fireEvent(new CloseEvent(this)));

        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        return new HorizontalLayout(saveButton, deleteButton, closeButton);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setAgendamento(Agendamento agendamento) {
        binder.setBean(agendamento);
        // Define um status padrão para novos agendamentos
        if (agendamento != null && agendamento.getId() == null) {
            status.setValue(StatusAgendamento.AGENDADO);
        }
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    // Eventos (padrão)
    public static abstract class AgendamentoFormEvent extends ComponentEvent<AgendamentoForm> {
        private final Agendamento agendamento;
        protected AgendamentoFormEvent(AgendamentoForm source, Agendamento agendamento) {
            super(source, false);
            this.agendamento = agendamento;
        }
        public Agendamento getAgendamento() { return agendamento; }
    }

    public static class SaveEvent extends AgendamentoFormEvent {
        SaveEvent(AgendamentoForm source, Agendamento agendamento) { super(source, agendamento); }
    }

    public static class DeleteEvent extends AgendamentoFormEvent {
        DeleteEvent(AgendamentoForm source, Agendamento agendamento) { super(source, agendamento); }
    }

    public static class CloseEvent extends AgendamentoFormEvent {
        CloseEvent(AgendamentoForm source) { super(source, null); }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) { return addListener(SaveEvent.class, listener); }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { return addListener(DeleteEvent.class, listener); }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) { return addListener(CloseEvent.class, listener); }
}