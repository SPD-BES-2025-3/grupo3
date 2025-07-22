package br.group3.modules.agendamento;

import br.group3.modules.agendamento.Agendamento.StatusAgendamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para representar os dados de entrada de um {@link br.group3.modules.agendamento.Agendamento}.
 * Utilizado para receber informações em requisições de criação ou atualização de agendamentos.
 * Inclui validações para garantir a integridade dos dados recebidos.
 *
 * @author Grupo 3
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoDTO {

    /**
     * A data e hora exatas do agendamento.
     * Deve ser uma data e hora no presente ou futuro e não pode ser nula.
     */
    @NotNull(message = "A data e hora do agendamento são obrigatórias.")
    @FutureOrPresent(message = "A data e hora do agendamento devem ser no presente ou futuro.")
    private LocalDateTime dataHora;

    /**
     * O motivo ou finalidade do agendamento.
     * Pode ser nulo, mas se preenchido, deve ter no máximo 255 caracteres.
     */
    @Size(max = 255, message = "O motivo do agendamento não pode exceder 255 caracteres.")
    private String motivo;

    /**
     * O status atual do agendamento.
     * Não pode ser nulo.
     */
    @NotNull(message = "O status do agendamento é obrigatório.")
    private StatusAgendamento status;

    /**
     * O ID do paciente associado a este agendamento.
     * Não pode ser nulo, pois um agendamento deve ter um paciente.
     */
    @NotNull(message = "O ID do paciente é obrigatório.")
    private Long idPaciente;

    /**
     * O ID do médico associado a este agendamento.
     * Não pode ser nulo, pois um agendamento deve ter um médico.
     */
    @NotNull(message = "O ID do médico é obrigatório.")
    private Long idMedico;
}