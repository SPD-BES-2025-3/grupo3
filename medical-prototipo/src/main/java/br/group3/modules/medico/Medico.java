package br.group3.modules.medico;

import br.group3.modules.especialidade.Especialidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa um médico no sistema.
 * <p>Esta entidade mapeia a tabela "medico" no banco de dados e contém
 * informações de identificação do médico, detalhes de contato e suas
 * especialidades médicas.</p>
 *
 * @author Grupo 3
 *
 * */
@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    /**
     * O ID único do médico.
     * <p>É gerado automaticamente pelo banco de dados.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Long idMedico;

    /**
     * O nome completo do médico.
     * <p>Este campo não pode ser nulo e tem um comprimento máximo de 255 caracteres.</p>
     */
    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    /**
     * O número do Conselho Regional de Medicina (CRM) do médico.
     * <p>Este campo é obrigatório, deve ser único e tem um comprimento máximo de 20 caracteres.</p>
     */
    @Column(name = "crm", unique = true, length = 20, nullable = false)
    private String crm;

    /**
     * O número de telefone de contato do médico.
     * <p>Tem um comprimento máximo de 20 caracteres.</p>
     */
    @Column(name = "telefone", length = 20)
    private String telefone;

    /**
     * O endereço de e-mail do médico.
     * <p>Este campo deve ser único e tem um comprimento máximo de 255 caracteres.</p>
     */
    @Column(name = "email", unique = true, length = 255)
    private String email;

    /**
     * Um conjunto de especialidades que este médico possui.
     * <p>Esta é uma relação muitos-para-muitos, mapeada através da tabela de junção "medico_especialidade".</p>
     * <p>Inicializado como um {@link HashSet} vazio para evitar {@code NullPointerException}.</p>
     */
    @ManyToMany
    @JoinTable(
            name = "medico_especialidade",
            joinColumns = @JoinColumn(name = "id_medico"),
            inverseJoinColumns = @JoinColumn(name = "id_especialidade")
    )
    private Set<Especialidade> especialidades = new HashSet<>();

}