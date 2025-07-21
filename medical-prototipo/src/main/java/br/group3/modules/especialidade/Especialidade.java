package br.group3.modules.especialidade;

import br.group3.modules.medico.Medico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa uma especialidade médica no sistema.
 * <p>Esta entidade mapeia a tabela "especialidade" no banco de dados e contém
 * informações sobre o nome e a descrição da especialidade, além de estar
 * associada a múltiplos médicos que possuem essa especialidade.</p>
 *
 * @author Grupo 3
 */
@Entity
@Table(name = "especialidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Especialidade {

    /**
     * O ID único da especialidade.
     * <p>É gerado automaticamente pelo banco de dados.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidade")
    private Long idEspecialidade;

    /**
     * O nome da especialidade.
     * <p>Este campo não pode ser nulo, deve ser único e tem um comprimento máximo de 100 caracteres.</p>
     */
    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    /**
     * Uma descrição detalhada da especialidade.
     * <p>Limita-se a 255 caracteres.</p>
     */
    @Column(name = "descricao", length = 255)
    private String descricao;

    /**
     * Um conjunto de médicos que possuem esta especialidade.
     * <p>Esta é uma relação muitos-para-muitos, mapeada pela propriedade "especialidades" na entidade {@link Medico}.</p>
     * <p>Inicializado como um {@link HashSet} vazio para evitar {@code NullPointerException}.</p>
     */
    @ManyToMany(mappedBy = "especialidades")
    private Set<Medico> medicos = new HashSet<>();
}