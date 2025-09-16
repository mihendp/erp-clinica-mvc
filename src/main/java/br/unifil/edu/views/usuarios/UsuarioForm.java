package br.unifil.edu.views.usuarios;

import br.unifil.edu.model.Role;
import br.unifil.edu.model.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class UsuarioForm extends Dialog {

    private final Binder<User> binder = new BeanValidationBinder<>(User.class);
    private User user;

    private final TextField name = new TextField("Nome");
    private final TextField username = new TextField("Nome de usuário");
    private final PasswordField password = new PasswordField("Senha");
    private final MultiSelectComboBox<Role> roles = new MultiSelectComboBox<>("Perfis de Acesso");

    private final Button saveButton = new Button("Salvar");
    private final Button deleteButton = new Button("Deletar");
    private final Button closeButton = new Button("Cancelar");

    public UsuarioForm() {
        setHeaderTitle("Dados do Usuário");
        configurarCampos();
        configurarBinder();
        add(createFormLayout(), createButtonLayout());
    }

    private void configurarCampos() {
        roles.setItems(Role.values());
    }

    private void configurarBinder() {
        binder.forField(password).bind(
            user -> "", // Nunca exibe a senha atual
            (user, pwd) -> {
                // Atualiza a senha apenas se um novo valor for digitado
                if (pwd != null && !pwd.isBlank()) {
                    user.setPassword(pwd);
                }
            }
        );
        binder.bindInstanceFields(this);
    }

    private Component createFormLayout() {
        return new FormLayout(name, username, password, roles);
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, user)));
        closeButton.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        return new HorizontalLayout(saveButton, deleteButton, closeButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.user);
            fireEvent(new SaveEvent(this, this.user));
        } catch (ValidationException e) {
            // A validação falhou, os erros são mostrados nos campos
        }
    }

    public void setUser(User user) {
        this.user = user;
        binder.readBean(user);
        // A senha é obrigatória apenas para novos usuários
        password.setRequired(user == null || user.getId() == null);
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    // Eventos (padrão igual ao do PacienteForm)
    public static abstract class UsuarioFormEvent extends ComponentEvent<UsuarioForm> {
        private final User user;
        protected UsuarioFormEvent(UsuarioForm source, User user) {
            super(source, false);
            this.user = user;
        }
        public User getUser() { return user; }
    }

    public static class SaveEvent extends UsuarioFormEvent {
        SaveEvent(UsuarioForm source, User user) { super(source, user); }
    }

    public static class DeleteEvent extends UsuarioFormEvent {
        DeleteEvent(UsuarioForm source, User user) { super(source, user); }
    }

    public static class CloseEvent extends UsuarioFormEvent {
        CloseEvent(UsuarioForm source) { super(source, null); }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) { return addListener(SaveEvent.class, listener); }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { return addListener(DeleteEvent.class, listener); }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) { return addListener(CloseEvent.class, listener); }
}