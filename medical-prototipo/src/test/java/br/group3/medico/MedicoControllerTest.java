package br.group3.medico;

import br.group3.modules.medico.Medico;
import br.group3.modules.medico.MedicoController;
import br.group3.modules.medico.MedicoDTO;
import br.group3.modules.medico.MedicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de testes para {@link MedicoController}.
 * Testa os endpoints da API REST para Médico, simulando requisições HTTP.
 *
 * @author Grupo 3
 */
@WebMvcTest(MedicoController.class)
public class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicoService medicoService;

    private ObjectMapper objectMapper;
    private Medico medicoExemplo;
    private MedicoDTO medicoDTOExemplo;

    /**
     * Configuração inicial antes de cada teste.
     * Prepara objetos Medico e MedicoDTO de exemplo, e configura o ObjectMapper.
     */
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        medicoExemplo = new Medico(1L, "Dr. Ana Paula", "CRM/SP 123456", "11987654321",
                "ana.paula@example.com", "Cardiologia");

        medicoDTOExemplo = new MedicoDTO(
                "Dr. Ana Paula", "CRM/SP 123456", "11987654321",
                "ana.paula@example.com", "Cardiologia");
    }

    @Test
    @DisplayName("POST /api/medicos - Deve criar um novo médico e retornar 201 Created")
    void deveCriarMedicoERetornar201() throws Exception {
        when(medicoService.salvarMedico(any(MedicoDTO.class))).thenReturn(medicoExemplo);

        mockMvc.perform(post("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicoDTOExemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMedico").value(medicoExemplo.getIdMedico()))
                .andExpect(jsonPath("$.nomeCompleto").value(medicoExemplo.getNomeCompleto()));

        verify(medicoService, times(1)).salvarMedico(any(MedicoDTO.class));
    }

    @Test
    @DisplayName("POST /api/medicos - Deve retornar 400 Bad Request se DTO inválido")
    void deveRetornar400SeDTOInvalidoAoCriar() throws Exception {
        MedicoDTO invalidoDTO = new MedicoDTO("", "", "tel", "email@invalido", "especialidade"); // Nome e CRM vazios

        mockMvc.perform(post("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").exists());
    }

    @Test
    @DisplayName("GET /api/medicos - Deve retornar todos os médicos com 200 OK")
    void deveRetornarTodosMedicos() throws Exception {
        List<Medico> medicos = Arrays.asList(medicoExemplo,
                new Medico(2L, "Dr. Bruno Costa", "CRM/RJ 654321", "21998877665",
                        "bruno.costa@example.com", "Pediatria"));
        when(medicoService.listarTodosMedicos()).thenReturn(medicos);

        mockMvc.perform(get("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeCompleto").value("Dr. Ana Paula"))
                .andExpect(jsonPath("$[1].nomeCompleto").value("Dr. Bruno Costa"));

        verify(medicoService, times(1)).listarTodosMedicos();
    }

    @Test
    @DisplayName("GET /api/medicos/{id} - Deve retornar médico por ID com 200 OK")
    void deveRetornarMedicoPorId() throws Exception {
        when(medicoService.buscarMedicoPorId(1L)).thenReturn(Optional.of(medicoExemplo));

        mockMvc.perform(get("/api/medicos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMedico").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Dr. Ana Paula"));

        verify(medicoService, times(1)).buscarMedicoPorId(1L);
    }

    @Test
    @DisplayName("GET /api/medicos/{id} - Deve retornar 404 Not Found para ID inexistente")
    void deveRetornar404ParaIdInexistente() throws Exception {
        when(medicoService.buscarMedicoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/medicos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(medicoService, times(1)).buscarMedicoPorId(99L);
    }

    @Test
    @DisplayName("PUT /api/medicos/{id} - Deve atualizar médico e retornar 200 OK")
    void deveAtualizarMedicoERetornar200() throws Exception {
        MedicoDTO medicoDTOAtualizado = new MedicoDTO(
                "Dr. Ana Paula Silva", "CRM/SP 123456", "11987654322",
                "ana.paula.silva@example.com", "Cardiologia");

        Medico medicoAtualizadoRetorno = new Medico(1L, "Dr. Ana Paula Silva", "CRM/SP 123456", "11987654322",
                "ana.paula.silva@example.com", "Cardiologia");

        when(medicoService.atualizarMedico(eq(1L), any(MedicoDTO.class))).thenReturn(medicoAtualizadoRetorno);

        mockMvc.perform(put("/api/medicos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicoDTOAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMedico").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Dr. Ana Paula Silva"))
                .andExpect(jsonPath("$.email").value("ana.paula.silva@example.com"));

        verify(medicoService, times(1)).atualizarMedico(eq(1L), any(MedicoDTO.class));
    }

    @Test
    @DisplayName("PUT /api/medicos/{id} - Deve retornar 404 Not Found ao atualizar médico inexistente")
    void deveRetornar404AoAtualizarMedicoInexistente() throws Exception {
        when(medicoService.atualizarMedico(eq(99L), any(MedicoDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/medicos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicoDTOExemplo)))
                .andExpect(status().isNotFound());

        verify(medicoService, times(1)).atualizarMedico(eq(99L), any(MedicoDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/medicos/{id} - Deve deletar médico e retornar 204 No Content")
    void deveDeletarMedicoERetornar204() throws Exception {
        doNothing().when(medicoService).deletarMedico(1L);

        mockMvc.perform(delete("/api/medicos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(medicoService, times(1)).deletarMedico(1L);
    }
}