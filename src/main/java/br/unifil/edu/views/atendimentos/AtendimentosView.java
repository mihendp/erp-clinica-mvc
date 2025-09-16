
package br.unifil.edu.views.atendimentos;

import br.unifil.edu.controller.AtendimentoController;
import br.unifil.edu.model.Agendamento;
import br.unifil.edu.model.Atendimento;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Atendimentos")
@Route("atendimentos")
@Menu(order = 4, icon = LineAwesomeIconUrl.NOTES_MEDICAL_SOLID)
@RolesAllowed({"DOCTOR", "ADMIN"})
@Uses(Icon.class)
public class AtendimentosView extends Composite<VerticalLayout> {

    private final AtendimentoController controller;
    private AtendimentoForm form;
    private final TextField filtroCpf = new TextField("CPF do Paciente");
    private final Grid<Agendamento> grid = new Grid<>(Agendamento.class);

    @Autowired
    public AtendimentosView(AtendimentoController controller) {
        this.controller = controller;
        this.controller.setView(this);
        this.form = new AtendimentoForm();

        configurarLayout();
        configurarGrid();
        configurarListeners();

        this.controller.carregarAgendamentos(""); // Carga inicial
    }

    private void configurarLayout() {
        H2 h2 = new H2("Atendimentos");
        Paragraph textSmall = new Paragraph("Gerencie as consultas de seus atendimentos");
        VerticalLayout header = new VerticalLayout(h2, textSmall);
        header.setPadding(false);
        header.setSpacing(false);

        Button pesquisarButton = new Button("Pesquisar");
        pesquisarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout toolbar = new HorizontalLayout(filtroCpf, pesquisarButton);
        toolbar.setAlignItems(FlexComponent.Alignment.END);

        getContent().add(header, toolbar, grid);
        getContent().setSizeFull();
    }

    private void configurarGrid() {
        grid.setSizeFull();
        grid.setColumns(); // Limpa colunas

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        grid.addColumn(ag -> ag.getPaciente().getNome()).setHeader("Paciente").setSortable(true);
        grid.addColumn(ag -> ag.getDataHora().format(formatter)).setHeader("Data Agendada").setSortable(true);
        grid.addColumn(ag -> ag.getStatus().getDescricao()).setHeader("Status").setSortable(true);

        grid.addComponentColumn(agendamento -> {
            Button atenderButton = new Button("Realizar Atendimento", new Icon(VaadinIcon.STETHOSCOPE));
            atenderButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
            atenderButton.addClickListener(e -> controller.onAtenderClicked(agendamento));
            // Desabilita o botão se a consulta já foi realizada
            atenderButton.setEnabled(agendamento.getStatus() != br.unifil.edu.model.StatusAgendamento.REALIZADO);
            return atenderButton;
        }).setHeader("Ações");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configurarListeners() {
        filtroCpf.addKeyPressListener(Key.ENTER, e -> pesquisar());
        getContent().getChildren()
                .filter(c -> c instanceof HorizontalLayout)
                .findFirst()
                .ifPresent(layout -> layout.getChildren()
                        .filter(c -> c instanceof Button)
                        .findFirst()
                        .ifPresent(button -> ((Button) button).addClickListener(e -> pesquisar())));

        form.addSaveListener(event -> controller.salvarAtendimento(event.getAtendimento()));
        form.addCloseListener(event -> fecharFormulario());
    }

    private void pesquisar() {
        controller.carregarAgendamentos(filtroCpf.getValue());
    }

    public void atualizarGrid(List<Agendamento> agendamentos) {
        grid.setItems(agendamentos);
    }

    public void abrirFormulario(Atendimento atendimento) {
        form.setAtendimento(atendimento);
        form.open();
    }

    public void fecharFormulario() {
        form.close();
    }

    public void limparFiltroEAtualizar() {
        filtroCpf.clear();
        pesquisar();
    }

    public void mostrarNotificacao(String mensagem) {
        Notification.show(mensagem, 3000, Notification.Position.BOTTOM_CENTER);
    }
}