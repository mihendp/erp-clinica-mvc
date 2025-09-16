package br.unifil.edu.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.unifil.edu.model.Role;
import br.unifil.edu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);
    List<User> findByRolesIn(Collection<Role> roles);
}
