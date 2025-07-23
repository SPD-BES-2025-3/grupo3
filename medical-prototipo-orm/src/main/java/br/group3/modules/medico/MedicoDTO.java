package br.group3.modules.medico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para representar os dados de entrada de um {@link br.group3.modules.medico.Medico}.
 * Utilizado para receber informações em requisições de criação ou atualização de médicos.
 * Inclui validações para garantir a integridade dos dados recebidos.
 *
 * @author Grupo 3
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDTO {

    /**
     * O nome completo do médico.
     * Deve ser preenchido e ter entre 3 e 255 caracteres.
     */
    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 255, message = "O nome completo deve ter entre 3 e 255 caracteres.")
    private String nomeCompleto;

    /**
     * O número do Conselho Regional de Medicina (CRM) do médico.
     * Deve ser preenchido, ter no máximo 20 caracteres e seguir o formato alfanumérico.
     */
    @NotBlank(message = "O CRM é obrigatório.")
    @Size(max = 20, message = "O CRM não pode exceder 20 caracteres.")
    private String crm;

    /**
     * O número de telefone de contato do médico.
     * Pode ser nulo, mas se preenchido, deve ter no máximo 20 caracteres.
     */
    @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
    private String telefone;

    /**
     * O endereço de e-mail do médico.
     * Pode ser nulo, mas se preenchido, deve ser um formato de e-mail válido e ter no máximo 255 caracteres.
     */
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "O e-mail deve ser válido.")
    @Size(max = 255, message = "O e-mail não pode exceder 255 caracteres.")
    private String email;

    /**
     * A especialidade médica do profissional.
     * Pode ser nula, mas se preenchida, deve ter no máximo 100 caracteres.
     */
    @Size(max = 100, message = "A especialidade não pode exceder 100 caracteres.")
    private String especialidade;
}