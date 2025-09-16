package br.unifil.edu.services;

import br.unifil.edu.model.Role;
import br.unifil.edu.model.User;
import br.unifil.edu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> get(Long id) {
        return usuarioRepository.findById(id);
    }

    public User save(User user) {
        // Verifica se uma nova senha foi fornecida para fazer o encode
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setHashedPassword(passwordEncoder.encode(user.getPassword()));
        }
        return usuarioRepository.save(user);
    }

    public List<User> findAllDoctors() {
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.DOCTOR);
        return usuarioRepository.findByRolesIn(roles);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<User> findAll() {
        return usuarioRepository.findAll();
    }

    public int count() {
        return (int) usuarioRepository.count();
    }

}
