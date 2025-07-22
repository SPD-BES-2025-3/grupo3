package br.group3.modules.paciente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas operações de negócio relacionadas à entidade {@link Paciente}.
 * <p>Esta classe gerencia as operações de CRUD (Create, Read, Update, Delete)
 * para pacientes, interagindo com o {@link IPacienteRepository} para acessar o banco de dados.</p>
 *
 * @author Grupo 3
 * @see Paciente
 * @see IPacienteRepository
 * @see PacienteDTO
 */
@Service
public class PacienteService {

    private final IPacienteRepository IPacienteRepository;

    /**
     * Construtor para injeção de dependência do {@link IPacienteRepository}.
     *
     * @param IPacienteRepository A instância de {@link IPacienteRepository} a ser injetada.
     */
    @Autowired
    public PacienteService(IPacienteRepository IPacienteRepository) {
        this.IPacienteRepository = IPacienteRepository;
    }

    /**
     * Salva um novo paciente no banco de dados a partir de um DTO.
     *
     * @param pacienteDTO O DTO {@link PacienteDTO} contendo os dados do paciente.
     * @return O paciente salvo como entidade {@link Paciente}, incluindo o ID gerado.
     */
    public Paciente salvarPaciente(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto(pacienteDTO.getNomeCompleto());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setEndereco(pacienteDTO.getEndereco());
        paciente.setTelefone(pacienteDTO.getTelefone());

        return IPacienteRepository.save(paciente);
    }

    /**
     * Busca um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser buscado.
     * @return Um {@link Optional} contendo o paciente como entidade {@link Paciente} se encontrado, ou vazio caso contrário.
     */
    public Optional<Paciente> buscarPacientePorId(Long id) {
        return IPacienteRepository.findById(id);
    }

    /**
     * Retorna uma lista com todos os pacientes cadastrados.
     *
     * @return Uma {@link List} de entidades {@link Paciente}.
     */
    public List<Paciente> listarTodosPacientes() {
        return IPacienteRepository.findAll();
    }

    /**
     * Atualiza as informações de um paciente existente a partir de um DTO.
     *
     * @param id O ID do paciente a ser atualizado.
     * @param pacienteDTO O objeto {@link PacienteDTO} com as informações atualizadas.
     * @return O paciente atualizado como entidade {@link Paciente}, ou {@code null} se o paciente com o ID fornecido não for encontrado.
     */
    public Paciente atualizarPaciente(Long id, PacienteDTO pacienteDTO) {
        Optional<Paciente> pacienteExistente = IPacienteRepository.findById(id);

        if (pacienteExistente.isPresent()) {
            Paciente paciente = pacienteExistente.get();
            paciente.setNomeCompleto(pacienteDTO.getNomeCompleto());
            paciente.setDataNascimento(pacienteDTO.getDataNascimento());
            paciente.setEndereco(pacienteDTO.getEndereco());
            paciente.setTelefone(pacienteDTO.getTelefone());

            return IPacienteRepository.save(paciente);
        }
        return null;
    }

    /**
     * Deleta um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser deletado.
     */
    public void deletarPaciente(Long id) {
        IPacienteRepository.deleteById(id);
    }
}