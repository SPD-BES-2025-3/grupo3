package br.group3.agendamento;

import br.group3.modules.agendamento.Agendamento;
import br.group3.modules.agendamento.AgendamentoController;
import br.group3.modules.agendamento.AgendamentoDTO;
import br.group3.modules.agendamento.AgendamentoService;
import br.group3.modules.medico.Medico;
import br.group3.modules.paciente.Paciente;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.group3.modules.agendamento.Agendamento.StatusAgendamento;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de testes para {@link AgendamentoController}.
 * <p>
 * Esta classe utiliza {@link WebMvcTest} para focar no teste da camada web (controller),
 * isolando-a das camadas de serviço e repositório. O {@link AgendamentoService} é "mockado"
 * usando {@link MockBean}, permitindo controlar seu comportamento e verificar se
 * o controller interage corretamente com ele e com as requisições/respostas HTTP.
 * </p>
 *
 * @author Grupo 3
 * @see AgendamentoController
 * @see WebMvcTest
 */
@WebMvcTest(AgendamentoController.class)
public class AgendamentoControllerTest {

    /**
     * Objeto utilizado para simular requisições HTTP e verificar respostas.
     * É injetado automaticamente pelo Spring Boot Test.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Instância mock da camada de serviço {@link AgendamentoService}.
     * Todas as chamadas a este serviço dentro do controller serão interceptadas e controladas por este mock.
     */
    @MockBean
    private AgendamentoService agendamentoService;

    /**
     * Objeto utilitário da Jackson para serializar e desserializar objetos Java para/de JSON.
     */
    private ObjectMapper objectMapper;

    /**
     * Objeto de exemplo da entidade {@link Agendamento} para uso nos testes.
     * Representa um agendamento completo, incluindo as entidades Paciente e Médico associadas.
     */
    private Agendamento agendamentoExemplo;

    /**
     * Objeto de exemplo do DTO {@link AgendamentoDTO} para uso nos testes.
     * Representa os dados de entrada que seriam enviados em requisições HTTP (POST/PUT).
     */
    private AgendamentoDTO agendamentoDTOExemplo;

    /**
     * Objeto de exemplo da entidade {@link Paciente} para associações.
     */
    private Paciente pacienteExemplo;

    /**
     * Objeto de exemplo da entidade {@link Medico} para associações.
     */
    private Medico medicoExemplo;

    /**
     * Configuração inicial executada antes de cada método de teste.
     * Inicializa o {@link ObjectMapper} com suporte a tipos de data/hora do Java 8,
     * e prepara os objetos de exemplo (`pacienteExemplo`, `medicoExemplo`, `agendamentoExemplo`, `agendamentoDTOExemplo`)
     * para garantir um estado consistente para cada teste.
     */
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        pacienteExemplo = new Paciente(1L, "Paciente Teste", null, "11122233344", "Rua X", "11999999999");
        medicoExemplo = new Medico(10L, "Dr. Medico Teste", "CRM/SP 123456", "11987654321", "medico@test.com", "Cardiologia");

        agendamentoExemplo = new Agendamento(1L, LocalDateTime.of(2025, 8, 10, 10, 0),
                "Consulta de Rotina", StatusAgendamento.AGENDADO, pacienteExemplo, medicoExemplo);

        agendamentoDTOExemplo = new AgendamentoDTO(
                LocalDateTime.of(2025, 8, 10, 10, 0), "Consulta de Rotina",
                StatusAgendamento.AGENDADO, 1L, 10L);
    }

    /**
     * Testa o endpoint POST /api/agendamentos para criação bem-sucedida de um agendamento.
     * Verifica se o status HTTP 201 (Created) é retornado e se os dados do agendamento criado
     * no corpo da resposta estão corretos. Também valida se o método `salvarAgendamento`
     * do serviço foi chamado uma vez.
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("POST /api/agendamentos - Deve criar um novo agendamento e retornar 201 Created")
    void deveCriarAgendamentoERetornar201() throws Exception {
        when(agendamentoService.salvarAgendamento(any(AgendamentoDTO.class))).thenReturn(agendamentoExemplo);

        mockMvc.perform(post("/api/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoDTOExemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAgendamento").value(agendamentoExemplo.getIdAgendamento()))
                .andExpect(jsonPath("$.motivo").value(agendamentoExemplo.getMotivo()))
                .andExpect(jsonPath("$.paciente.idPaciente").value(pacienteExemplo.getIdPaciente()))
                .andExpect(jsonPath("$.medico.idMedico").value(medicoExemplo.getIdMedico()));

        verify(agendamentoService, times(1)).salvarAgendamento(any(AgendamentoDTO.class));
    }

    /**
     * Testa o endpoint POST /api/agendamentos com um DTO de agendamento inválido.
     * Espera um status HTTP 400 (Bad Request) e verifica a estrutura padrão do erro.
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("POST /api/agendamentos - Deve retornar 400 Bad Request se DTO inválido")
    void deveRetornar400SeDTOInvalidoAoCriar() throws Exception {
        AgendamentoDTO invalidoDTO = new AgendamentoDTO(
                LocalDateTime.of(2020, 1, 1, 10, 0), // Data no passado, inválida pela anotação @FutureOrPresent
                "Motivo", StatusAgendamento.AGENDADO, 1L, 10L);

        mockMvc.perform(post("/api/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").exists());
    }

    /**
     * Testa o endpoint POST /api/agendamentos quando o serviço indica que o paciente ou médico
     * referenciado no DTO não foi encontrado (retorna `null`).
     * Espera um status HTTP 400 (Bad Request).
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("POST /api/agendamentos - Deve retornar 400 Bad Request se paciente ou médico não encontrados")
    void deveRetornar400SePacienteOuMedicoNaoEncontradoAoCriar() throws Exception {
        when(agendamentoService.salvarAgendamento(any(AgendamentoDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoDTOExemplo)))
                .andExpect(status().isBadRequest());

        verify(agendamentoService, times(1)).salvarAgendamento(any(AgendamentoDTO.class));
    }

    /**
     * Testa o endpoint GET /api/agendamentos para listar todos os agendamentos.
     * Verifica se o status HTTP 200 (OK) é retornado e se o corpo da resposta
     * é um array JSON contendo os dados esperados.
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("GET /api/agendamentos - Deve retornar todos os agendamentos com 200 OK")
    void deveRetornarTodosAgendamentos() throws Exception {
        List<Agendamento> agendamentos = Arrays.asList(agendamentoExemplo,
                new Agendamento(2L, LocalDateTime.of(2025, 8, 11, 14, 30),
                        "Exame de Rotina", StatusAgendamento.PENDENTE, pacienteExemplo, medicoExemplo));
        when(agendamentoService.listarTodosAgendamentos()).thenReturn(agendamentos);

        mockMvc.perform(get("/api/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].motivo").value("Consulta de Rotina"))
                .andExpect(jsonPath("$[1].motivo").value("Exame de Rotina"));

        verify(agendamentoService, times(1)).listarTodosAgendamentos();
    }

    /**
     * Testa o endpoint GET /api/agendamentos/{id} para buscar um agendamento por ID existente.
     * Verifica se o status HTTP 200 (OK) é retornado e se os dados do agendamento encontrado
     * no corpo da resposta estão corretos.
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("GET /api/agendamentos/{id} - Deve retornar agendamento por ID com 200 OK")
    void deveRetornarAgendamentoPorId() throws Exception {
        when(agendamentoService.buscarAgendamentoPorId(1L)).thenReturn(Optional.of(agendamentoExemplo));

        mockMvc.perform(get("/api/agendamentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAgendamento").value(1L))
                .andExpect(jsonPath("$.motivo").value("Consulta de Rotina"));

        verify(agendamentoService, times(1)).buscarAgendamentoPorId(1L);
    }

    /**
     * Testa o endpoint GET /api/agendamentos/{id} para buscar um agendamento por ID inexistente.
     * Espera um status HTTP 404 (Not Found).
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("GET /api/agendamentos/{id} - Deve retornar 404 Not Found para ID inexistente")
    void deveRetornar404ParaIdInexistente() throws Exception {
        when(agendamentoService.buscarAgendamentoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/agendamentos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(agendamentoService, times(1)).buscarAgendamentoPorId(99L);
    }

    /**
     * Testa o endpoint PUT /api/agendamentos/{id} para atualizar um agendamento existente.
     * Verifica se o status HTTP 200 (OK) é retornado e se os dados do agendamento atualizado
     * no corpo da resposta estão corretos. Valida também se o método `atualizarAgendamento`
     * do serviço foi chamado uma vez.
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("PUT /api/agendamentos/{id} - Deve atualizar agendamento e retornar 200 OK")
    void deveAtualizarAgendamentoERetornar200() throws Exception {
        AgendamentoDTO agendamentoDTOAtualizado = new AgendamentoDTO(
                LocalDateTime.of(2025, 9, 1, 9, 0), "Retorno",
                StatusAgendamento.AGENDADO, 1L, 10L);

        Agendamento agendamentoAtualizadoRetorno = new Agendamento(1L, LocalDateTime.of(2025, 9, 1, 9, 0),
                "Retorno", StatusAgendamento.AGENDADO, pacienteExemplo, medicoExemplo);

        when(agendamentoService.atualizarAgendamento(eq(1L), any(AgendamentoDTO.class))).thenReturn(agendamentoAtualizadoRetorno);

        mockMvc.perform(put("/api/agendamentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoDTOAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAgendamento").value(1L))
                .andExpect(jsonPath("$.motivo").value("Retorno"))
                .andExpect(jsonPath("$.status").value("AGENDADO"));

        verify(agendamentoService, times(1)).atualizarAgendamento(eq(1L), any(AgendamentoDTO.class));
    }

    /**
     * Testa o endpoint PUT /api/agendamentos/{id} ao tentar atualizar um agendamento inexistente.
     * Espera um status HTTP 404 (Not Found).
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("PUT /api/agendamentos/{id} - Deve retornar 404 Not Found ao atualizar agendamento inexistente")
    void deveRetornar404AoAtualizarAgendamentoInexistente() throws Exception {
        when(agendamentoService.atualizarAgendamento(eq(99L), any(AgendamentoDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/agendamentos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoDTOExemplo)))
                .andExpect(status().isNotFound());

        verify(agendamentoService, times(1)).atualizarAgendamento(eq(99L), any(AgendamentoDTO.class));
    }

    /**
     * Testa o endpoint DELETE /api/agendamentos/{id} para deletar um agendamento existente.
     * Verifica se o status HTTP 204 (No Content) é retornado, indicando sucesso na deleção
     * sem conteúdo de resposta. Valida se o método `deletarAgendamento` do serviço
     * foi chamado uma vez.
     *
     * @throws Exception se ocorrer um erro durante a execução da requisição simulada.
     */
    @Test
    @DisplayName("DELETE /api/agendamentos/{id} - Deve deletar agendamento e retornar 204 No Content")
    void deveDeletarAgendamentoERetornar204() throws Exception {
        doNothing().when(agendamentoService).deletarAgendamento(1L);

        mockMvc.perform(delete("/api/agendamentos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(agendamentoService, times(1)).deletarAgendamento(1L);
    }
}
