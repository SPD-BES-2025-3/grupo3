package br.group3.modules.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um médico no sistema.
 *
 * <p>Esta entidade mapeia a tabela "medico" no banco de dados e contém
 * informações de identificação do médico, detalhes de contato e sua
 * especialidade médica principal.</p>
 *
 * @author Grupo 3
 */
@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    /**
     * O ID único do médico.
     * <p>É gerado automaticamente pelo banco de dados para garantir exclusividade.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Long idMedico;

    /**
     * O nome completo do médico.
     * <p>Campo obrigatório com um limite de 255 caracteres para garantir consistência.</p>
     */
    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    /**
     * O número do Conselho Regional de Medicina (CRM) do médico.
     * <p>Campo obrigatório e único, com um limite de 20 caracteres, para identificação profissional.</p>
     */
    @Column(name = "crm", unique = true, length = 20, nullable = false)
    private String crm;

    /**
     * O número de telefone de contato do médico.
     * <p>Permite até 20 caracteres, adequado para diversos formatos de telefone.</p>
     */
    @Column(name = "telefone", length = 20)
    private String telefone;

    /**
     * O endereço de e-mail do médico.
     * <p>Campo único com um limite de 255 caracteres para contato digital.</p>
     */
    @Column(name = "email", unique = true, length = 255)
    private String email;

    /**
     * A especialidade médica do profissional.
     * <p>Armazenada como uma string para simplicidade, permitindo até 100 caracteres.
     * Ex: "Cardiologia", "Pediatria", etc.</p>
     */
    @Column(name = "especialidade", length = 100)
    private String especialidade;
}