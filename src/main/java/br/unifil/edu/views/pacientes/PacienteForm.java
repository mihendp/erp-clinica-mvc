package br.unifil.edu.views.pacientes;

import br.unifil.edu.model.Paciente;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

public class PacienteForm extends Dialog {

    TextField nome = new TextField("Nome");
    TextField cpf = new TextField("CPF");
    TextField telefone = new TextField("Telefone");

    Button saveButton = new Button("Salvar");
    @Getter
    Button deleteButton = new Button("Deletar");
    Button cancelButton = new Button("Cancelar");

    Binder<Paciente> binder = new BeanValidationBinder<>(Paciente.class);

    public PacienteForm() {
        setHeaderTitle("Dados do Paciente");
        binder.bindInstanceFields(this);

        add(createFormLayout());
        add(createButtonLayout());
    }

    public void setPaciente(Paciente paciente) {
        binder.setBean(paciente);
    }

    private Component createFormLayout() {
        return new FormLayout(nome, cpf, telefone);
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Sistema de Eventos para comunicar com a View principal
    @Getter
    public static abstract class PacienteFormEvent extends ComponentEvent<PacienteForm> {
        private final Paciente paciente;
        protected PacienteFormEvent(PacienteForm source, Paciente paciente) {
            super(source, false);
            this.paciente = paciente;
        }
    }

    public static class SaveEvent extends PacienteFormEvent {
        SaveEvent(PacienteForm source, Paciente paciente) {
            super(source, paciente);
        }
    }

    public static class DeleteEvent extends PacienteFormEvent {
        DeleteEvent(PacienteForm source, Paciente paciente) {
            super(source, paciente);
        }
    }

    public static class CloseEvent extends PacienteFormEvent {
        CloseEvent(PacienteForm source) {
            super(source, null);
        }
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }
    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }
    public void addCloseListener(ComponentEventListener<CloseEvent> listener) {
        addListener(CloseEvent.class, listener);
    }
}