package br.unifil.edu.views.agendamentos;

import br.unifil.edu.controller.AgendamentoController;
import br.unifil.edu.model.Agendamento;
import br.unifil.edu.services.AgendamentoService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Agendamentos")
@Route(value = "") // Rota principal da aplicação
@Menu(order = 1, icon = LineAwesomeIconUrl.CALENDAR)
@PermitAll
@Uses(Icon.class)
public class AgendamentosView extends Composite<VerticalLayout> {

    private final AgendamentoController controller;
    private AgendamentoForm form;
    private final AgendamentoService service; // Para buscar pacientes e médicos
    private final Grid<Agendamento> grid = new Grid<>(Agendamento.class);
    private final Button adicionarAgendamentoButton = new Button("Agendar consulta");

    @Autowired
    public AgendamentosView(AgendamentoController controller, AgendamentoService service) {
        this.controller = controller;
        this.service = service;
        this.controller.setView(this);

        configurarLayout();
        configurarGrid();
        configurarListeners();

        this.controller.carregarAgendamentos();
    }

    private void configurarLayout() {
        VerticalLayout headerLayout = new VerticalLayout();
        H2 h2 = new H2("Agendamentos");
        Paragraph textSmall = new Paragraph("Faça a gerência dos seus agendamentos");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        headerLayout.add(h2, textSmall);
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);

        HorizontalLayout toolbar = new HorizontalLayout(headerLayout, adicionarAgendamentoButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(FlexComponent.Alignment.END);

        adicionarAgendamentoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        getContent().add(toolbar, grid);
        getContent().setSizeFull();
    }

    private void configurarGrid() {
        grid.setSizeFull();
        grid.setColumns(); // Limpa as colunas padrão

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        grid.addColumn(agendamento -> agendamento.getPaciente().getNome())
                .setHeader("Paciente").setSortable(true);
        grid.addColumn(agendamento -> agendamento.getMedico().getNome())
                .setHeader("Médico").setSortable(true);
        grid.addColumn(agendamento -> agendamento.getDataHora().format(formatter))
                .setHeader("Data e Hora").setSortable(true);
        grid.addColumn(agendamento -> agendamento.getStatus().getDescricao())
                .setHeader("Status").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addComponentColumn(agendamento -> {
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> controller.onEditarClicked(agendamento));
            return editButton;
        }).setHeader("Editar");
    }

    private void configurarListeners() {
        adicionarAgendamentoButton.addClickListener(e -> controller.onAdicionarNovoClicked());
    }

    public void atualizarGrid(List<Agendamento> agendamentos) {
        grid.setItems(agendamentos);
    }

    public void abrirFormulario(Agendamento agendamento, boolean criandoNovo) {
        // Cria um novo formulário a cada abertura para garantir que os dados mais recentes de médicos e pacientes sejam carregados
        form = new AgendamentoForm(service.findAllPacientes(), service.findAllDoctors());
        form.getDeleteButton().setVisible(!criandoNovo);
        form.setAgendamento(agendamento);

        // Reconfigura os listeners para o novo form
        form.addSaveListener(event -> controller.salvarAgendamento(event.getAgendamento()));
        form.addDeleteListener(event -> controller.deletarAgendamento(event.getAgendamento()));
        form.addCloseListener(event -> fecharFormulario());

        form.open();
    }

    public void fecharFormulario() {
        if (form != null) {
            form.close();
        }
    }

    public void mostrarNotificacao(String mensagem) {
        Notification.show(mensagem, 3000, Notification.Position.BOTTOM_CENTER);
    }
}