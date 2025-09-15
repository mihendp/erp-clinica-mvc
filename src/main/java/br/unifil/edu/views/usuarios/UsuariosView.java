package br.unifil.edu.views.usuarios;

import br.unifil.edu.controller.UserController;
import br.unifil.edu.data.Role;
import br.unifil.edu.model.User;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Usuários")
@Route("usuarios")
@Menu(order = 5, icon = LineAwesomeIconUrl.USER_SHIELD_SOLID)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class UsuariosView extends Composite<VerticalLayout> {

    private final UserController controller;
    private final UsuarioForm form;
    private final Grid<User> grid = new Grid<>(User.class);
    private final Button adicionarUsuarioButton = new Button("Adicionar usuário");

    @Autowired
    public UsuariosView(UserController controller) {
        this.controller = controller;
        this.controller.setView(this);
        this.form = new UsuarioForm();

        configurarLayout();
        configurarGrid();
        configurarListeners();

        this.controller.carregarUsuarios();
    }

    private void configurarLayout() {
        VerticalLayout headerLayout = new VerticalLayout();
        H2 h2 = new H2("Usuários");
        Paragraph textSmall = new Paragraph("Gerencie acessos e permissões dos usuários de sua clínica");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        headerLayout.add(h2, textSmall);
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);

        HorizontalLayout toolbar = new HorizontalLayout(headerLayout, adicionarUsuarioButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(FlexComponent.Alignment.END);

        adicionarUsuarioButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(toolbar, grid);
        getContent().setSizeFull();
    }

    private void configurarGrid() {
        grid.setSizeFull();
        grid.setColumns("id", "name", "username");
        grid.addColumn(user -> user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.joining(", ")))
            .setHeader("Perfis de Acesso")
            .setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addComponentColumn(user -> {
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> controller.onEditarClicked(user));
            return editButton;
        }).setHeader("Editar");
    }

    private void configurarListeners() {
        adicionarUsuarioButton.addClickListener(e -> controller.onAdicionarNovoClicked());
        form.addSaveListener(event -> controller.salvarUsuario(event.getUser()));
        form.addDeleteListener(event -> controller.deletarUsuario(event.getUser()));
        form.addCloseListener(event -> fecharFormulario());
    }

    public void atualizarGrid(List<User> usuarios) {
        grid.setItems(usuarios);
    }

    public void abrirFormulario(User user, boolean criandoNovo) {
        form.getDeleteButton().setVisible(!criandoNovo);
        form.setUser(user);
        form.open();
    }

    public void fecharFormulario() {
        form.setUser(null);
        form.close();
    }

    public void mostrarNotificacao(String mensagem) {
        Notification.show(mensagem, 3000, Notification.Position.BOTTOM_CENTER);
    }
}