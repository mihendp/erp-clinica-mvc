package br.unifil.edu.views.medicos;

import br.unifil.edu.controller.MedicoController;
import br.unifil.edu.model.Medico;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Medicos")
@Route("medicos")
@Menu(order = 2, icon = LineAwesomeIconUrl.STETHOSCOPE_SOLID)
// @RolesAllowed("USER")
@PermitAll
public class MedicosView extends Composite<VerticalLayout> {
    private final MedicoController controller;
    private final MedicosForm form;
    private final Grid<Medico> grid = new Grid<>(Medico.class);
    private final Button adicionarMedicoButton = new Button("Adicionar médico");

    @Autowired
    public MedicosView(MedicoController controller) {
        this.controller = controller;
        this.controller.setView(this);

        this.form = new MedicosForm();

        configurarLayout();
        configurarGrid();
        configurarListeners();

        this.controller.carregarMedicos();
    }

    private void configurarLayout() {
        adicionarMedicoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Exibe o botão apenas para ADMIN
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        adicionarMedicoButton.setVisible(isAdmin);

        getContent().add(adicionarMedicoButton, grid);
        getContent().setSizeFull();
    }

    private void configurarGrid() {
        grid.setSizeFull();
        grid.setColumns("id", "nome", "crm", "especialidade", "telefone", "email");
        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));

        grid.addComponentColumn(medico -> {
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> controller.onEditarClicked(medico));
            return editButton;
        }).setHeader("Editar");
    }

    private void configurarListeners() {
        adicionarMedicoButton.addClickListener(e -> controller.onAdicionarNovoClicked());
        form.addSaveListener(event -> controller.salvarMedico(event.getMedico()));
        form.addDeleteListener(event -> controller.deletarMedico(event.getMedico()));
        form.addCloseListener(event -> fecharFormulario());
    }

    public void atualizarGrid(List<Medico> medicos) {
        grid.setItems(medicos);
    }

    public void abrirFormulario(Medico medico, boolean criandoNovo) {
        form.getDeleteButton().setVisible(!criandoNovo);
        form.setMedico(medico);
        form.open();
    }

    public void fecharFormulario() {
        form.setMedico(null);
        form.close();
    }

    public void mostrarNotificacao(String mensagem) {
        Notification.show(mensagem, 3000, Notification.Position.BOTTOM_CENTER);
    }
}