package br.group3.modules.medico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas operações de negócio relacionadas à entidade {@link Medico}.
 * <p>Esta classe gerencia as operações de CRUD (Create, Read, Update, Delete)
 * para médicos, interagindo com o {@link IMedicoRepository} para acessar o banco de dados.</p>
 *
 * @author Grupo 3
 * @see Medico
 * @see IMedicoRepository
 * @see MedicoDTO
 */
@Service
public class MedicoService {

    private final IMedicoRepository  medicoRepository;

    /**
     * Construtor para injeção de dependência do {@link IMedicoRepository}.
     *
     * @param medicoRepository A instância de {@link IMedicoRepository} a ser injetada.
     */
    @Autowired
    public MedicoService(IMedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    /**
     * Salva um novo médico no banco de dados a partir de um DTO.
     *
     * @param medicoDTO O DTO {@link MedicoDTO} contendo os dados do médico.
     * @return O médico salvo como entidade {@link Medico}, incluindo o ID gerado.
     */
    public Medico salvarMedico(MedicoDTO medicoDTO) {
        Medico medico = new Medico();
        medico.setNomeCompleto(medicoDTO.getNomeCompleto());
        medico.setCrm(medicoDTO.getCrm());
        medico.setTelefone(medicoDTO.getTelefone());
        medico.setEmail(medicoDTO.getEmail());
        medico.setEspecialidade(medicoDTO.getEspecialidade());

        return medicoRepository.save(medico);
    }

    /**
     * Busca um médico pelo seu ID.
     *
     * @param id O ID do médico a ser buscado.
     * @return Um {@link Optional} contendo o médico como entidade {@link Medico} se encontrado, ou vazio caso contrário.
     */
    public Optional<Medico> buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id);
    }

    /**
     * Retorna uma lista com todos os médicos cadastrados.
     *
     * @return Uma {@link List} de entidades {@link Medico}.
     */
    public List<Medico> listarTodosMedicos() {
        return medicoRepository.findAll();
    }

    /**
     * Atualiza as informações de um médico existente a partir de um DTO.
     *
     * @param id O ID do médico a ser atualizado.
     * @param medicoDTO O objeto {@link MedicoDTO} com as informações atualizadas.
     * @return O médico atualizado como entidade {@link Medico}, ou {@code null} se o médico com o ID fornecido não for encontrado.
     */
    public Medico atualizarMedico(Long id, MedicoDTO medicoDTO) {
        Optional<Medico> medicoExistente = medicoRepository.findById(id);

        if (medicoExistente.isPresent()) {
            Medico medico = medicoExistente.get();
            medico.setNomeCompleto(medicoDTO.getNomeCompleto());
            medico.setCrm(medicoDTO.getCrm());
            medico.setTelefone(medicoDTO.getTelefone());
            medico.setEmail(medicoDTO.getEmail());
            medico.setEspecialidade(medicoDTO.getEspecialidade());

            return medicoRepository.save(medico);
        }
        return null;
    }

    /**
     * Deleta um médico pelo seu ID.
     *
     * @param id O ID do médico a ser deletado.
     */
    public void deletarMedico(Long id) {
        medicoRepository.deleteById(id);
    }
}