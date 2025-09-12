package br.unifil.edu.views.pacientes;

import br.unifil.edu.controller.PacienteController;
import br.unifil.edu.model.Paciente;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Pacientes")
@Route("pacientes")
@Menu(order = 3, icon = LineAwesomeIconUrl.USERS_SOLID)
@PermitAll
public class PacientesView extends Composite<VerticalLayout> {
    private final PacienteController controller;
    private final PacienteForm form;
    private final Grid<Paciente> grid = new Grid<>(Paciente.class);
    private final Button adicionarPacienteButton = new Button("Adicionar paciente");

    @Autowired
    public PacientesView(PacienteController controller) {
        this.controller = controller;
        this.controller.setView(this);

        this.form = new PacienteForm();

        configurarLayout();
        configurarGrid();
        configurarListeners();

        this.controller.carregarPacientes();
    }

    private void configurarLayout() {
        adicionarPacienteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(adicionarPacienteButton, grid);
        getContent().setSizeFull();
    }

    private void configurarGrid() {
        grid.setSizeFull();
        // Define colunas mais relevantes e formata a data
        grid.setColumns("id", "nome", "cpf", "telefone", "email");
        grid.addColumn(paciente -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return paciente.getDataNascimento() != null ? paciente.getDataNascimento().format(formatter) : "";
        }).setHeader("Data de Nascimento").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));

        grid.addComponentColumn(paciente -> {
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> controller.onEditarClicked(paciente));
            return editButton;
        }).setHeader("Editar");
    }

    private void configurarListeners() {
        adicionarPacienteButton.addClickListener(e -> controller.onAdicionarNovoClicked());
        form.addSaveListener(event -> controller.salvarPaciente(event.getPaciente()));
        form.addDeleteListener(event -> controller.deletarPaciente(event.getPaciente()));
        form.addCloseListener(event -> fecharFormulario());
    }

    public void atualizarGrid(List<Paciente> pacientes) {
        grid.setItems(pacientes);
    }

    public void abrirFormulario(Paciente paciente, boolean criandoNovo) {
        form.getDeleteButton().setVisible(!criandoNovo);
        form.setPaciente(paciente);
        form.open();
    }

    public void fecharFormulario() {
        form.setPaciente(null);
        form.close();
    }

    public void mostrarNotificacao(String mensagem) {
        Notification.show(mensagem, 3000, Notification.Position.BOTTOM_CENTER);
    }
}