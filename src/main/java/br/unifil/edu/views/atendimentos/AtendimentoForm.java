package br.unifil.edu.views.atendimentos;

import br.unifil.edu.model.Atendimento;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class AtendimentoForm extends Dialog {

    private final Binder<Atendimento> binder = new BeanValidationBinder<>(Atendimento.class);

    // Campos de exibição (não editáveis)
    private final TextField pacienteNome = new TextField("Paciente");
    private final TextField dataAgendamento = new TextField("Data Agendada");

    // Campos de preenchimento
    private final TextArea anamnese = new TextArea("Anamnese");
    private final TextArea diagnostico = new TextArea("Diagnóstico");
    private final TextArea prescricao = new TextArea("Prescrição");
    private final TextArea observacoes = new TextArea("Observações");

    private final Button saveButton = new Button("Salvar");
    private final Button closeButton = new Button("Cancelar");

    public AtendimentoForm() {
        setHeaderTitle("Registro de Atendimento");
        setWidth("80vw");
        setMaxWidth("800px");

        binder.bindInstanceFields(this);
        add(createFormLayout(), createButtonLayout());
    }

    private Component createFormLayout() {
        pacienteNome.setReadOnly(true);
        dataAgendamento.setReadOnly(true);
        anamnese.setHeight("150px");
        diagnostico.setHeight("150px");
        prescricao.setHeight("150px");

        FormLayout formLayout = new FormLayout(pacienteNome, dataAgendamento, anamnese, diagnostico, prescricao, observacoes);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        formLayout.setColspan(anamnese, 2);
        formLayout.setColspan(diagnostico, 2);
        formLayout.setColspan(prescricao, 2);
        formLayout.setColspan(observacoes, 2);
        return formLayout;
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> validateAndSave());
        closeButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(saveButton, closeButton);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setAtendimento(Atendimento atendimento) {
        binder.setBean(atendimento);

        if (atendimento != null && atendimento.getAgendamento() != null) {
            pacienteNome.setValue(atendimento.getAgendamento().getPaciente().getNome());
            dataAgendamento.setValue(atendimento.getAgendamento().getDataHora().toString());
        }
    }

    // Eventos (padrão)
    public static abstract class AtendimentoFormEvent extends ComponentEvent<AtendimentoForm> {
        private final Atendimento atendimento;
        protected AtendimentoFormEvent(AtendimentoForm source, Atendimento atendimento) { super(source, false); this.atendimento = atendimento; }
        public Atendimento getAtendimento() { return atendimento; }
    }

    public static class SaveEvent extends AtendimentoFormEvent {
        SaveEvent(AtendimentoForm source, Atendimento atendimento) { super(source, atendimento); }
    }

    public static class CloseEvent extends AtendimentoFormEvent {
        CloseEvent(AtendimentoForm source) { super(source, null); }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) { return addListener(SaveEvent.class, listener); }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) { return addListener(CloseEvent.class, listener); }
}