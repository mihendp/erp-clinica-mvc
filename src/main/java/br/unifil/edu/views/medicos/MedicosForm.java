package br.unifil.edu.views.medicos;

import br.unifil.edu.model.Especialidade;
import br.unifil.edu.model.Medico;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class MedicosForm extends Dialog {

    private final Binder<Medico> binder = new BeanValidationBinder<>(Medico.class);
    private Medico medico;

    // Campos do formulário
    private final TextField nome = new TextField("Nome");
    private final TextField crm = new TextField("CRM");
    private final ComboBox<Especialidade> especialidade = new ComboBox<>("Especialidade");
    private final TextField telefone = new TextField("Telefone");
    private final EmailField email = new EmailField("E-mail");

    // Botões
    private final Button saveButton = new Button("Salvar");
    private final Button deleteButton = new Button("Deletar");
    private final Button closeButton = new Button("Cancelar");

    public MedicosForm() {
        setHeaderTitle("Dados do Médico");
        configurarCampos();
        configurarBinder();
        add(createFormLayout(), createButtonLayout());
    }

    private void configurarCampos() {
        especialidade.setItems(Especialidade.values());
        especialidade.setItemLabelGenerator(Especialidade::getDescricao);
    }

    private void configurarBinder() {
        binder.bindInstanceFields(this);
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nome, crm, especialidade, telefone, email);
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
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, medico)));
        closeButton.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, deleteButton, closeButton);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
        binder.setBean(medico);
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    // Eventos
    public static abstract class MedicosFormEvent extends ComponentEvent<MedicosForm> {
        private final Medico medico;

        protected MedicosFormEvent(MedicosForm source, Medico medico) {
            super(source, false);
            this.medico = medico;
        }

        public Medico getMedico() {
            return medico;
        }
    }

    public static class SaveEvent extends MedicosFormEvent {
        SaveEvent(MedicosForm source, Medico medico) {
            super(source, medico);
        }
    }

    public static class DeleteEvent extends MedicosFormEvent {
        DeleteEvent(MedicosForm source, Medico medico) {
            super(source, medico);
        }
    }

    public static class CloseEvent extends MedicosFormEvent {
        CloseEvent(MedicosForm source) {
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