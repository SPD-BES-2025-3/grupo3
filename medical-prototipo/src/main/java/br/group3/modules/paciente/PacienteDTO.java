package br.group3.modules.paciente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para representar os dados de entrada de um {@link br.group3.modules.paciente.Paciente}.
 * Utilizado para receber informações em requisições de criação ou atualização de pacientes.
 * Inclui validações para garantir a integridade dos dados recebidos.
 *
 * @author Grupo 3
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    /**
     * O nome completo do paciente.
     * Deve ser preenchido e ter entre 3 e 255 caracteres.
     */
    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 255, message = "O nome completo deve ter entre 3 e 255 caracteres.")
    private String nomeCompleto;

    /**
     * A data de nascimento do paciente.
     * Deve ser uma data no passado.
     */
    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    private LocalDate dataNascimento;

    /**
     * O Cadastro de Pessoa Física (CPF) do paciente.
     * Deve ser preenchido, ter exatamente 11 dígitos e seguir o formato numérico.
     */
    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    /**
     * O endereço completo do paciente.
     * Pode ser nulo, mas se preenchido, deve ter no máximo 500 caracteres.
     */
    @Size(max = 500, message = "O endereço não pode exceder 500 caracteres.")
    private String endereco;

    /**
     * O número de telefone de contato do paciente.
     * Pode ser nulo, mas se preenchido, deve ter no máximo 20 caracteres.
     */
    @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
    private String telefone;
}