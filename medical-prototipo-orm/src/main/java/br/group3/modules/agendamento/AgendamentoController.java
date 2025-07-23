package br.group3.modules.agendamento;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gerenciar operações relacionadas a agendamentos.
 * <p>Esta classe expõe endpoints para realizar operações de CRUD (Create, Read, Update, Delete)
 * em registros de agendamentos, interagindo com o {@link AgendamentoService}.</p>
 * <p>Os dados de entrada são formatados usando {@link AgendamentoDTO} e
 * as respostas de sucesso retornam a entidade {@link Agendamento} completa.</p>
 *
 * @author Grupo 3
 * @see AgendamentoService
 * @see AgendamentoDTO
 * @see Agendamento
 */
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    /**
     * Construtor para injeção de dependência do {@link AgendamentoService}.
     *
     * @param agendamentoService A instância de {@link AgendamentoService} a ser injetada.
     */
    @Autowired
    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    /**
     * Cria um novo agendamento no sistema.
     * <p>Recebe um {@link AgendamentoDTO} no corpo da requisição, valida-o e salva o agendamento.</p>
     *
     * @param agendamentoDTO O DTO contendo os dados do agendamento a ser criado.
     * É anotado com {@link Valid} para ativar as validações definidas no DTO.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Agendamento} do agendamento criado
     * e o status HTTP 201 (Created), ou status 400 (Bad Request) se o paciente ou médico não forem encontrados.
     */
    @PostMapping
    public ResponseEntity<Agendamento> criarAgendamento(@Valid @RequestBody AgendamentoDTO agendamentoDTO) {
        Agendamento novoAgendamento = agendamentoService.salvarAgendamento(agendamentoDTO);
        if (novoAgendamento != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Retorna todos os agendamentos cadastrados no sistema.
     *
     * @return Uma {@link ResponseEntity} contendo uma {@link List} de entidades {@link Agendamento}
     * e o status HTTP 200 (OK). A lista pode estar vazia se não houver agendamentos.
     */
    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodosAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.listarTodosAgendamentos();
        return ResponseEntity.ok(agendamentos);
    }

    /**
     * Busca um agendamento pelo seu ID.
     *
     * @param id O ID do agendamento a ser buscado, passado como variável de caminho.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Agendamento} do agendamento encontrado
     * e o status HTTP 200 (OK), ou status 404 (Not Found) se o agendamento não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarAgendamentoPorId(@PathVariable Long id) {
        Optional<Agendamento> agendamento = agendamentoService.buscarAgendamentoPorId(id);
        return agendamento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza as informações de um agendamento existente.
     * <p>Recebe o ID do agendamento na URL e um {@link AgendamentoDTO} no corpo da requisição
     * com os dados a serem atualizados.</p>
     *
     * @param id O ID do agendamento a ser atualizado, passado como variável de caminho.
     * @param agendamentoDTO O DTO contendo os novos dados do agendamento.
     * É anotado com {@link Valid} para ativar as validações definidas no DTO.
     * @return Uma {@link ResponseEntity} contendo a entidade {@link Agendamento} do agendamento atualizado
     * e o status HTTP 200 (OK), ou status 404 (Not Found) se o agendamento, paciente ou médico não for encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizarAgendamento(@PathVariable Long id,
                                                            @Valid @RequestBody AgendamentoDTO agendamentoDTO) {
        Agendamento agendamentoAtualizado = agendamentoService.atualizarAgendamento(id, agendamentoDTO);
        if (agendamentoAtualizado != null) {
            return ResponseEntity.ok(agendamentoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deleta um agendamento pelo seu ID.
     *
     * @param id O ID do agendamento a ser deletado, passado como variável de caminho.
     * @return Uma {@link ResponseEntity} com status HTTP 204 (No Content) se a deleção for bem-sucedida,
     * indicando que a operação foi completada mas não há conteúdo para retornar.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable Long id) {
        agendamentoService.deletarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}