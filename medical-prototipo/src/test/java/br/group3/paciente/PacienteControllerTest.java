package br.group3.paciente;

import br.group3.modules.paciente.Paciente;
import br.group3.modules.paciente.PacienteController;
import br.group3.modules.paciente.PacienteDTO;
import br.group3.modules.paciente.PacienteService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de testes para {@link PacienteController}.
 * Testa os endpoints da API REST para Paciente, simulando requisições HTTP.
 *
 * @author Grupo 3
 */
@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    private ObjectMapper objectMapper;
    private Paciente pacienteExemplo;
    private PacienteDTO pacienteDTOExemplo;

    /**
     * Configuração inicial antes de cada teste.
     * Prepara objetos Paciente e PacienteDTO de exemplo, e configura o ObjectMapper.
     */
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        pacienteExemplo = new Paciente(1L, "Carlos Lima", LocalDate.of(1995, 8, 25),
                "11122233344", "Rua C, 789", "66666666666");

        pacienteDTOExemplo = new PacienteDTO(
                "Carlos Lima", LocalDate.of(1995, 8, 25),
                "11122233344", "Rua C, 789", "66666666666");
    }

    @Test
    @DisplayName("POST /api/pacientes - Deve criar um novo paciente e retornar 201 Created")
    void deveCriarPacienteERetornar201() throws Exception {
        when(pacienteService.salvarPaciente(any(PacienteDTO.class))).thenReturn(pacienteExemplo);

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTOExemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPaciente").value(pacienteExemplo.getIdPaciente()))
                .andExpect(jsonPath("$.nomeCompleto").value(pacienteExemplo.getNomeCompleto()));

        verify(pacienteService, times(1)).salvarPaciente(any(PacienteDTO.class));
    }

    @Test
    @DisplayName("POST /api/pacientes - Deve retornar 400 Bad Request se DTO inválido")
    void deveRetornar400SeDTOInvalidoAoCriar() throws Exception {
        PacienteDTO invalidoDTO = new PacienteDTO("", LocalDate.of(2000, 1, 1), "12345678901", "End", "Tel");

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidoDTO)))
                .andExpect(status().isBadRequest()) // Verifica apenas o status 400
                // .andExpect(jsonPath("$").isArray()); // LINHA REMOVIDA OU MODIFICADA
                .andExpect(jsonPath("$.type").value("about:blank")) // Assumindo ProblemDetail padrão do Spring Boot
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists()) // Verifica que há algum detalhe sobre o erro
                .andExpect(jsonPath("$.instance").exists());
    }

    @Test
    @DisplayName("GET /api/pacientes - Deve retornar todos os pacientes com 200 OK")
    void deveRetornarTodosPacientes() throws Exception {
        List<Paciente> pacientes = Arrays.asList(pacienteExemplo,
                new Paciente(2L, "Fernanda Dias", LocalDate.of(1988, 3, 10),
                        "99988877766", "Rua D, 10", "55555555555"));
        when(pacienteService.listarTodosPacientes()).thenReturn(pacientes);

        mockMvc.perform(get("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeCompleto").value("Carlos Lima"))
                .andExpect(jsonPath("$[1].nomeCompleto").value("Fernanda Dias"));

        verify(pacienteService, times(1)).listarTodosPacientes();
    }

    @Test
    @DisplayName("GET /api/pacientes/{id} - Deve retornar paciente por ID com 200 OK")
    void deveRetornarPacientePorId() throws Exception {
        when(pacienteService.buscarPacientePorId(1L)).thenReturn(Optional.of(pacienteExemplo));

        mockMvc.perform(get("/api/pacientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPaciente").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Carlos Lima"));

        verify(pacienteService, times(1)).buscarPacientePorId(1L);
    }

    @Test
    @DisplayName("GET /api/pacientes/{id} - Deve retornar 404 Not Found para ID inexistente")
    void deveRetornar404ParaIdInexistente() throws Exception {
        when(pacienteService.buscarPacientePorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pacientes/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(pacienteService, times(1)).buscarPacientePorId(99L);
    }

    @Test
    @DisplayName("PUT /api/pacientes/{id} - Deve atualizar paciente e retornar 200 OK")
    void deveAtualizarPacienteERetornar200() throws Exception {
        PacienteDTO pacienteDTOAtualizado = new PacienteDTO(
                "Carlos Lima Junior", LocalDate.of(1995, 8, 25),
                "11122233344", "Rua C, 789 - Apto 101", "66666666666");

        Paciente pacienteAtualizadoRetorno = new Paciente(1L, "Carlos Lima Junior", LocalDate.of(1995, 8, 25),
                "11122233344", "Rua C, 789 - Apto 101", "66666666666");

        when(pacienteService.atualizarPaciente(eq(1L), any(PacienteDTO.class))).thenReturn(pacienteAtualizadoRetorno);

        mockMvc.perform(put("/api/pacientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTOAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPaciente").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Carlos Lima Junior"))
                .andExpect(jsonPath("$.endereco").value("Rua C, 789 - Apto 101"));

        verify(pacienteService, times(1)).atualizarPaciente(eq(1L), any(PacienteDTO.class));
    }

    @Test
    @DisplayName("PUT /api/pacientes/{id} - Deve retornar 404 Not Found ao atualizar paciente inexistente")
    void deveRetornar404AoAtualizarPacienteInexistente() throws Exception {
        when(pacienteService.atualizarPaciente(eq(99L), any(PacienteDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/pacientes/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTOExemplo)))
                .andExpect(status().isNotFound());

        verify(pacienteService, times(1)).atualizarPaciente(eq(99L), any(PacienteDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/pacientes/{id} - Deve deletar paciente e retornar 204 No Content")
    void deveDeletarPacienteERetornar204() throws Exception {
        doNothing().when(pacienteService).deletarPaciente(1L);

        mockMvc.perform(delete("/api/pacientes/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(pacienteService, times(1)).deletarPaciente(1L);
    }
}