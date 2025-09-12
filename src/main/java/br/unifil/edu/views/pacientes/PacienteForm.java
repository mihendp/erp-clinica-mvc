package br.unifil.edu.views.pacientes;

import br.unifil.edu.model.Genero;
import br.unifil.edu.model.Paciente;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class PacienteForm extends Dialog {

    private final Binder<Paciente> binder = new BeanValidationBinder<>(Paciente.class);
    private Paciente paciente;

    // Campos do formulário
    private final TextField nome = new TextField("Nome");
    private final TextField cpf = new TextField("CPF");
    private final TextField telefone = new TextField("Telefone");
    private final EmailField email = new EmailField("E-mail");
    private final DatePicker dataNascimento = new DatePicker("Data de Nascimento");
    private final ComboBox<Genero> genero = new ComboBox<>("Gênero");

    private final TextField endereco = new TextField("Endereço");
    private final TextField cidade = new TextField("Cidade");
    private final TextField estado = new TextField("Estado");
    private final TextField cep = new TextField("CEP");

    private final TextField nomeContatoEmergencia = new TextField("Nome Contato de Emergência");
    private final TextField telefoneContatoEmergencia = new TextField("Telefone Contato de Emergência");


    // Botões
    private final Button saveButton = new Button("Salvar");
    private final Button deleteButton = new Button("Deletar");
    private final Button closeButton = new Button("Cancelar");


    public PacienteForm() {
        setHeaderTitle("Dados do Paciente");
        configurarCampos();
        configurarBinder();
        add(createFormLayout(), createButtonLayout());
    }

    private void configurarCampos() {
        genero.setItems(Genero.values());
        genero.setItemLabelGenerator(Genero::getDescricao);
    }

    private void configurarBinder() {
        binder.bindInstanceFields(this);
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                nome, cpf, telefone, email, dataNascimento, genero,
                endereco, cidade, estado, cep,
                nomeContatoEmergencia, telefoneContatoEmergencia
        );
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        return formLayout;
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, paciente)));
        closeButton.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, deleteButton, closeButton);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        binder.setBean(paciente);
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    // Eventos
    public static abstract class PacienteFormEvent extends ComponentEvent<PacienteForm> {
        private final Paciente paciente;

        protected PacienteFormEvent(PacienteForm source, Paciente paciente) {
            super(source, false);
            this.paciente = paciente;
        }

        public Paciente getPaciente() {
            return paciente;
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

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}