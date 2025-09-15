package br.unifil.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.unifil.edu.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{

    
} 