package br.group3.modules.paciente;

 import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gerenciar operações relacionadas a pacientes.
 * <p>Esta classe expõe endpoints para realizar operações de CRUD (Create, Read, Update, Delete)
 * em registros de pacientes, interagindo com o {@link PacienteService}.</p>
 * <p>Os dados de entrada são formatados usando {@link PacienteDTO} e
 * as respostas de sucesso retornam a entidade {@link Paciente} completa.</p>
 *
 * @author Grupo 3
 * @see PacienteService
 * @see PacienteDTO
 * @see Paciente
 */
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    /**
     * Construtor para injeção de dependência do {@link PacienteService}.
     *
     * @param pacienteService A instância de {@link PacienteService} a ser injetada.
     */
    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    /**
     * Cria um novo paciente no sistema.
     * <p>Recebe um {@link PacienteDTO} no corpo da requisição, valida-o e salva o paciente.</p>
     *
     * @param pacienteDTO O DTO contendo os dados do paciente a ser criado.
     * É anotado com {@link Valid} para ativar as validações definidas no DTO.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Paciente} do paciente criado
     * e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Paciente> criarPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        Paciente novoPaciente = pacienteService.salvarPaciente(pacienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
    }

    /**
     * Retorna todos os pacientes cadastrados no sistema.
     *
     * @return Uma {@link ResponseEntity} contendo uma {@link List} de entidades {@link Paciente}
     * e o status HTTP 200 (OK). A lista pode estar vazia se não houver pacientes.
     */
    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodosPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodosPacientes();
        return ResponseEntity.ok(pacientes);
    }

    /**
     * Busca um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser buscado, passado como variável de caminho.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Paciente} do paciente encontrado
     * e o status HTTP 200 (OK), ou status 404 (Not Found) se o paciente não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.buscarPacientePorId(id);
        return paciente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza as informações de um paciente existente.
     * <p>Recebe o ID do paciente na URL e um {@link PacienteDTO} no corpo da requisição
     * com os dados a serem atualizados.</p>
     *
     * @param id O ID do paciente a ser atualizado, passado como variável de caminho.
     * @param pacienteDTO O DTO contendo os novos dados do paciente.
     * É anotado com {@link Valid} para ativar as validações definidas no DTO.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Paciente} do paciente atualizado
     * e o status HTTP 200 (OK), ou status 404 (Not Found) se o paciente não for encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizarPaciente(@PathVariable Long id,
                                                      @Valid @RequestBody PacienteDTO pacienteDTO) {
        Paciente pacienteAtualizado = pacienteService.atualizarPaciente(id, pacienteDTO);
        if (pacienteAtualizado != null) {
            return ResponseEntity.ok(pacienteAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deleta um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser deletado, passado como variável de caminho.
     * @return Uma {@link ResponseEntity} com status HTTP 204 (No Content) se a deleção for bem-sucedida,
     * indicando que a operação foi completada mas não há conteúdo para retornar.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        pacienteService.deletarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}