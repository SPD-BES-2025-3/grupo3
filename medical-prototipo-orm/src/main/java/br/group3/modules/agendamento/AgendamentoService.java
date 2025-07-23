package br.group3.modules.agendamento;

import br.group3.modules.medico.IMedicoRepository;
import br.group3.modules.medico.Medico;
import br.group3.modules.medico.IMedicoRepository;
import br.group3.modules.paciente.Paciente;
import br.group3.modules.paciente.IPacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas operações de negócio relacionadas à entidade {@link Agendamento}.
 * <p>Esta classe gerencia as operações de CRUD para agendamentos, interagindo com o
 * {@link IAgendamentoRepository}, {@link IPacienteRepository} e {@link IMedicoRepository}
 * para acessar o banco de dados e validar as associações.</p>
 *
 * @author Grupo 3
 * @see Agendamento
 * @see IAgendamentoRepository
 * @see AgendamentoDTO
 */
@Service
public class AgendamentoService {

    private final IAgendamentoRepository agendamentoRepository;
    private final IPacienteRepository pacienteRepository; // Injetar PacienteRepository
    private final IMedicoRepository medicoRepository;     // Injetar MedicoRepository

    /**
     * Construtor para injeção de dependência dos repositórios.
     *
     * @param agendamentoRepository A instância de {@link IAgendamentoRepository}.
     * @param pacienteRepository A instância de {@link IPacienteRepository}.
     * @param medicoRepository A instância de {@link IMedicoRepository}.
     */
    @Autowired
    public AgendamentoService(IAgendamentoRepository agendamentoRepository,
                              IPacienteRepository pacienteRepository,
                              IMedicoRepository medicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    /**
     * Salva um novo agendamento no banco de dados a partir de um DTO.
     * Valida a existência do paciente e do médico antes de salvar.
     *
     * @param agendamentoDTO O DTO {@link AgendamentoDTO} contendo os dados do agendamento.
     * @return O agendamento salvo como entidade {@link Agendamento}, incluindo o ID gerado,
     * ou {@code null} se o paciente ou médico não forem encontrados.
     */
    public Agendamento salvarAgendamento(AgendamentoDTO agendamentoDTO) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(agendamentoDTO.getIdPaciente());
        Optional<Medico> medicoOptional = medicoRepository.findById(agendamentoDTO.getIdMedico());

        if (pacienteOptional.isEmpty() || medicoOptional.isEmpty()) {
            return null;
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(agendamentoDTO.getDataHora());
        agendamento.setMotivo(agendamentoDTO.getMotivo());
        agendamento.setStatus(agendamentoDTO.getStatus());
        agendamento.setPaciente(pacienteOptional.get());
        agendamento.setMedico(medicoOptional.get());

        return agendamentoRepository.save(agendamento);
    }

    /**
     * Busca um agendamento pelo seu ID.
     *
     * @param id O ID do agendamento a ser buscado.
     * @return Um {@link Optional} contendo o agendamento como entidade {@link Agendamento} se encontrado, ou vazio caso contrário.
     */
    public Optional<Agendamento> buscarAgendamentoPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    /**
     * Retorna uma lista com todos os agendamentos cadastrados.
     *
     * @return Uma {@link List} de entidades {@link Agendamento}.
     */
    public List<Agendamento> listarTodosAgendamentos() {
        return agendamentoRepository.findAll();
    }

    /**
     * Atualiza as informações de um agendamento existente a partir de um DTO.
     * Valida a existência do paciente e do médico se seus IDs forem fornecidos no DTO.
     *
     * @param id O ID do agendamento a ser atualizado.
     * @param agendamentoDTO O objeto {@link AgendamentoDTO} com as informações atualizadas.
     * @return O agendamento atualizado como entidade {@link Agendamento}, ou {@code null} se o agendamento,
     * paciente ou médico com os IDs fornecidos não forem encontrados.
     */
    public Agendamento atualizarAgendamento(Long id, AgendamentoDTO agendamentoDTO) {
        Optional<Agendamento> agendamentoExistente = agendamentoRepository.findById(id);

        if (agendamentoExistente.isPresent()) {
            Agendamento agendamento = agendamentoExistente.get();
            agendamento.setDataHora(agendamentoDTO.getDataHora());
            agendamento.setMotivo(agendamentoDTO.getMotivo());
            agendamento.setStatus(agendamentoDTO.getStatus());

             if (!agendamento.getPaciente().getIdPaciente().equals(agendamentoDTO.getIdPaciente())) {
                Optional<Paciente> novoPacienteOptional = pacienteRepository.findById(agendamentoDTO.getIdPaciente());
                if (novoPacienteOptional.isEmpty()) {
                    return null;
                }
                agendamento.setPaciente(novoPacienteOptional.get());
            }

             if (!agendamento.getMedico().getIdMedico().equals(agendamentoDTO.getIdMedico())) {
                Optional<Medico> novoMedicoOptional = medicoRepository.findById(agendamentoDTO.getIdMedico());
                if (novoMedicoOptional.isEmpty()) {
                    return null;
                }
                agendamento.setMedico(novoMedicoOptional.get());
            }

            return agendamentoRepository.save(agendamento);
        }
        return null;
    }

    /**
     * Deleta um agendamento pelo seu ID.
     *
     * @param id O ID do agendamento a ser deletado.
     */
    public void deletarAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }
}