package br.unifil.edu.controller;

import br.unifil.edu.model.User;
import br.unifil.edu.services.UserService;
import br.unifil.edu.views.usuarios.UsuariosView;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class UserController {

    private final UserService userService;
    @Setter
    private UsuariosView view;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void carregarUsuarios() {
        try {
            List<User> usuarios = userService.findAll();
            view.atualizarGrid(usuarios);
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    public void salvarUsuario(User user) {
        try {
            userService.save(user);
            view.fecharFormulario();
            view.mostrarNotificacao("Usuário salvo com sucesso!");
            carregarUsuarios();
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    public void deletarUsuario(User user) {
        try {
            userService.delete(user.getId());
            view.fecharFormulario();
            view.mostrarNotificacao("Usuário deletado com sucesso!");
            carregarUsuarios();
        } catch (Exception e) {
            view.mostrarNotificacao("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    public void onAdicionarNovoClicked() {
        view.abrirFormulario(new User(), true);
    }

    public void onEditarClicked(User user) {
        view.abrirFormulario(user, false);
    }
}