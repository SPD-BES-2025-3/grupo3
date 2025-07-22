package br.group3.modules.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Representa um paciente no sistema.
 * <p>Esta entidade mapeia a tabela "paciente" no banco de dados e contém
 * informações de identificação do paciente, como nome completo, data de nascimento,
 * CPF, endereço e telefone.</p>
 *
 * @author Grupo 3
 */
@Entity
@Table(name = "paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    /**
     * O ID único do paciente.
     * <p>É gerado automaticamente pelo banco de dados.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long idPaciente;

    /**
     * O nome completo do paciente.
     * <p>Este campo não pode ser nulo e tem um comprimento máximo de 255 caracteres.</p>
     */
    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    /**
     * A data de nascimento do paciente.
     */
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    /**
     * O Cadastro de Pessoa Física (CPF) do paciente.
     * <p>Este campo é obrigatório, deve ser único e tem um comprimento exato de 11 caracteres.</p>
     */
    @Column(name = "cpf", unique = true, length = 11, nullable = false)
    private String cpf;

    /**
     * O endereço completo do paciente.
     * <p>Tem um comprimento máximo de 500 caracteres.</p>
     */
    @Column(name = "endereco", length = 500)
    private String endereco;

    /**
     * O número de telefone de contato do paciente.
     * <p>Tem um comprimento máximo de 20 caracteres.</p>
     */
    @Column(name = "telefone", length = 20)
    private String telefone;

}