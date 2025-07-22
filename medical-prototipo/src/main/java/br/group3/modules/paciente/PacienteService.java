package br.group3.modules.paciente;

import br.group3.modules.paciente.Paciente;
import br.group3.modules.paciente.PacienteDTO;
import br.group3.modules.paciente.PacienteRepository;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas operações de negócio relacionadas à entidade {@link Paciente}.
 * <p>Esta classe gerencia as operações de CRUD (Create, Read, Update, Delete)
 * para pacientes, interagindo com o {@link PacienteRepository} para acessar o banco de dados.</p>
 *
 * @author Grupo 3
 * @see Paciente
 * @see PacienteRepository
 * @see PacienteDTO
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    /**
     * Construtor para injeção de dependência do {@link PacienteRepository}.
     *
     * @param pacienteRepository A instância de {@link PacienteRepository} a ser injetada.
     */
    @Autowired
    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
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

        return pacienteRepository.save(paciente);
    }

    /**
     * Busca um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser buscado.
     * @return Um {@link Optional} contendo o paciente como entidade {@link Paciente} se encontrado, ou vazio caso contrário.
     */
    public Optional<Paciente> buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id);
    }

    /**
     * Retorna uma lista com todos os pacientes cadastrados.
     *
     * @return Uma {@link List} de entidades {@link Paciente}.
     */
    public List<Paciente> listarTodosPacientes() {
        return pacienteRepository.findAll();
    }

    /**
     * Atualiza as informações de um paciente existente a partir de um DTO.
     *
     * @param id O ID do paciente a ser atualizado.
     * @param pacienteDTO O objeto {@link PacienteDTO} com as informações atualizadas.
     * @return O paciente atualizado como entidade {@link Paciente}, ou {@code null} se o paciente com o ID fornecido não for encontrado.
     */
    public Paciente atualizarPaciente(Long id, PacienteDTO pacienteDTO) {
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(id);

        if (pacienteExistente.isPresent()) {
            Paciente paciente = pacienteExistente.get();
            paciente.setNomeCompleto(pacienteDTO.getNomeCompleto());
            paciente.setDataNascimento(pacienteDTO.getDataNascimento());
            paciente.setEndereco(pacienteDTO.getEndereco());
            paciente.setTelefone(pacienteDTO.getTelefone());

            return pacienteRepository.save(paciente);
        }
        return null;
    }

    /**
     * Deleta um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser deletado.
     */
    public void deletarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
}