package br.unifil.edu.repository;

import br.unifil.edu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.unifil.edu.model.Medico;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{

    // Método para encontrar o perfil de médico associado a um usuário
    Optional<Medico> findByUser(User user);
} 