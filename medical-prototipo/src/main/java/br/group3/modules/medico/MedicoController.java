package br.group3.modules.medico;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gerenciar operações relacionadas a médicos.
 * <p>Esta classe expõe endpoints para realizar operações de CRUD (Create, Read, Update, Delete)
 * em registros de médicos, interagindo com o {@link MedicoService}.</p>
 * <p>Os dados de entrada são formatados usando {@link MedicoDTO} e
 * as respostas de sucesso retornam a entidade {@link Medico} completa.</p>
 *
 * @author Grupo 3
 * @see MedicoService
 * @see MedicoDTO
 * @see Medico
 */
@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    /**
     * Construtor para injeção de dependência do {@link MedicoService}.
     *
     * @param medicoService A instância de {@link MedicoService} a ser injetada.
     */
    @Autowired
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    /**
     * Cria um novo médico no sistema.
     * <p>Recebe um {@link MedicoDTO} no corpo da requisição, valida-o e salva o médico.</p>
     *
     * @param medicoDTO O DTO contendo os dados do médico a ser criado.
     * É anotado com {@link Valid} para ativar as validações definidas no DTO.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Medico} do médico criado
     * e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Medico> criarMedico(@Valid @RequestBody MedicoDTO medicoDTO) {
        Medico novoMedico = medicoService.salvarMedico(medicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMedico);
    }

    /**
     * Retorna todos os médicos cadastrados no sistema.
     *
     * @return Uma {@link ResponseEntity} contendo uma {@link List} de entidades {@link Medico}
     * e o status HTTP 200 (OK). A lista pode estar vazia se não houver médicos.
     */
    @GetMapping
    public ResponseEntity<List<Medico>> listarTodosMedicos() {
        List<Medico> medicos = medicoService.listarTodosMedicos();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Busca um médico pelo seu ID.
     *
     * @param id O ID do médico a ser buscado, passado como variável de caminho.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Medico} do médico encontrado
     * e o status HTTP 200 (OK), ou status 404 (Not Found) se o médico não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarMedicoPorId(@PathVariable Long id) {
        Optional<Medico> medico = medicoService.buscarMedicoPorId(id);
        return medico.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza as informações de um médico existente.
     * <p>Recebe o ID do médico na URL e um {@link MedicoDTO} no corpo da requisição
     * com os dados a serem atualizados.</p>
     *
     * @param id O ID do médico a ser atualizado, passado como variável de caminho.
     * @param medicoDTO O DTO contendo os novos dados do médico.
     * É anotado com {@link Valid} para ativar as validações definidas no DTO.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Medico} do médico atualizado
     * e o status HTTP 200 (OK), ou status 404 (Not Found) se o médico não for encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Medico> atualizarMedico(@PathVariable Long id,
                                                  @Valid @RequestBody MedicoDTO medicoDTO) {
        Medico medicoAtualizado = medicoService.atualizarMedico(id, medicoDTO);
        if (medicoAtualizado != null) {
            return ResponseEntity.ok(medicoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deleta um médico pelo seu ID.
     *
     * @param id O ID do médico a ser deletado, passado como variável de caminho.
     * @return Uma {@link ResponseEntity} com status HTTP 204 (No Content) se a deleção for bem-sucedida,
     * indicando que a operação foi completada mas não há conteúdo para retornar.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedico(@PathVariable Long id) {
        medicoService.deletarMedico(id);
        return ResponseEntity.noContent().build();
    }
}