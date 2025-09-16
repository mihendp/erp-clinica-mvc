package br.unifil.edu.model;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Medico {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String telefone;

    @Email(message = "E-mail inválido")
    private String email;

    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    private String crm;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
    
}
