package br.group3.modules.agendamento;

import br.group3.modules.medico.Medico;
import br.group3.modules.paciente.Paciente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa um agendamento de consulta ou procedimento no sistema.
 * <p>Esta entidade mapeia a tabela "agendamento" no banco de dados e contém
 * informações sobre a data e hora do agendamento, o motivo, o status
 * e os participantes (paciente e médico).</p>
 *
 * @author Grupo 3
 */
@Entity
@Table(name = "agendamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    /**
     * O ID único do agendamento.
     * <p>É gerado automaticamente pelo banco de dados.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long idAgendamento;

    /**
     * A data e hora exatas do agendamento.
     */
    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    /**
     * O motivo ou finalidade do agendamento.
     * <p>Limita-se a 255 caracteres.</p>
     */
    @Column(name = "motivo", length = 255)
    private String motivo;

    /**
     * O status atual do agendamento.
     * <p>Os possíveis status são definidos na enumeração {@link StatusAgendamento}.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento", length = 20, nullable = false)
    private StatusAgendamento status;

    /**
     * O paciente associado a este agendamento.
     * <p>Este campo é uma chave estrangeira para a entidade {@link Paciente} e não pode ser nulo.</p>
     */
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    /**
     * O médico associado a este agendamento.
     * <p>Este campo é uma chave estrangeira para a entidade {@link Medico} e não pode ser nulo.</p>
     */
    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    /**
     * Enumeração que define os possíveis status para um agendamento.
     * <p>Cada status possui uma descrição amigável.</p>
     */
    public enum StatusAgendamento {
        /**
         * O agendamento foi confirmado e está programado.
         */
        AGENDADO("Agendado"),
        /**
         * O agendamento está aguardando confirmação ou ação.
         */
        PENDENTE("Pendente"),
        /**
         * O agendamento foi cancelado.
         */
        CANCELADO("Cancelado");

        private final String descricao;

        /**
         * Construtor para a enumeração StatusAgendamento.
         *
         * @param descricao A descrição amigável do status.
         */
        StatusAgendamento(String descricao) {
            this.descricao = descricao;
        }

        /**
         * Retorna a descrição amigável do status do agendamento.
         *
         * @return A descrição do status.
         */
        public String getDescricao() {
            return descricao;
        }
    }
}