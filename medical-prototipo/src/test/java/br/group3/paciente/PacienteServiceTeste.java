package br.group3.paciente;

import br.group3.modules.paciente.Paciente;
import br.group3.modules.paciente.PacienteRepository;
import br.group3.modules.paciente.PacienteService;
import br.group3.modules.paciente.PacienteDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes para {@link PacienteService}.
 * Testa as operações de CRUD da lógica de negócio, mockando o repositório.
 *
 * @author Grupo 3
 */
@ExtendWith(MockitoExtension.class)
public class PacienteServiceTeste {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente pacienteExemplo;
    private PacienteDTO pacienteDTOExemplo;

    /**
     * Configuração inicial antes de cada teste.
     * Prepara objetos Paciente e PacienteDTO de exemplo.
     */
    @BeforeEach
    void setUp() {
        pacienteExemplo = new Paciente(1L, "João da Silva", LocalDate.of(1990, 5, 15),
                "12345678901", "Rua A, 123", "99999999999");

        pacienteDTOExemplo = new PacienteDTO(
                "Maria Souza", LocalDate.of(1985, 10, 20),
                "98765432109", "Av. B, 456", "88888888888");
    }

    @Test
    @DisplayName("Deve salvar um novo paciente com sucesso")
    void deveSalvarPacienteComSucesso() {
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteExemplo);

        Paciente pacienteSalvo = pacienteService.salvarPaciente(pacienteDTOExemplo);

        assertNotNull(pacienteSalvo);
        assertEquals(pacienteExemplo.getNomeCompleto(), pacienteSalvo.getNomeCompleto());
        assertEquals(pacienteExemplo.getIdPaciente(), pacienteSalvo.getIdPaciente());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve buscar paciente por ID existente")
    void deveBuscarPacientePorIdExistente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExemplo));

        Optional<Paciente> pacienteEncontrado = pacienteService.buscarPacientePorId(1L);

        assertTrue(pacienteEncontrado.isPresent());
        assertEquals(pacienteExemplo.getNomeCompleto(), pacienteEncontrado.get().getNomeCompleto());
        verify(pacienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Não deve encontrar paciente por ID inexistente")
    void naoDeveEncontrarPacientePorIdInexistente() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Paciente> pacienteEncontrado = pacienteService.buscarPacientePorId(99L);

        assertFalse(pacienteEncontrado.isPresent());
        verify(pacienteRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve listar todos os pacientes")
    void deveListarTodosPacientes() {
        List<Paciente> pacientes = Arrays.asList(
                pacienteExemplo,
                new Paciente(2L, "Pedro Costa", LocalDate.of(1992, 1, 1),
                        "11122233344", "Rua B, 456", "77777777777")
        );
        when(pacienteRepository.findAll()).thenReturn(pacientes);

        List<Paciente> listaRetornada = pacienteService.listarTodosPacientes();

        assertFalse(listaRetornada.isEmpty());
        assertEquals(2, listaRetornada.size());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve atualizar paciente existente com sucesso")
    void deveAtualizarPacienteExistenteComSucesso() {
        PacienteDTO pacienteDTOAtualizado = new PacienteDTO(
                "João da Silva Atualizado", LocalDate.of(1990, 5, 15),
                "12345678901", "Rua A, 123 - Novo", "99999999999");

        Paciente pacienteAntigo = new Paciente(1L, "João da Silva", LocalDate.of(1990, 5, 15),
                "12345678901", "Rua A, 123", "99999999999");
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteAntigo));

        Paciente pacienteAtualizadoRetorno = new Paciente(1L, "João da Silva Atualizado", LocalDate.of(1990, 5, 15),
                "12345678901", "Rua A, 123 - Novo", "99999999999");
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteAtualizadoRetorno);

        Paciente resultadoAtualizacao = pacienteService.atualizarPaciente(1L, pacienteDTOAtualizado);

        assertNotNull(resultadoAtualizacao);
        assertEquals("João da Silva Atualizado", resultadoAtualizacao.getNomeCompleto());
        assertEquals("Rua A, 123 - Novo", resultadoAtualizacao.getEndereco());
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Não deve atualizar paciente inexistente")
    void naoDeveAtualizarPacienteInexistente() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        Paciente resultadoAtualizacao = pacienteService.atualizarPaciente(99L, pacienteDTOExemplo);

        assertNull(resultadoAtualizacao);
        verify(pacienteRepository, times(1)).findById(99L);
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve deletar paciente com sucesso")
    void deveDeletarPacienteComSucesso() {
        doNothing().when(pacienteRepository).deleteById(1L);

        pacienteService.deletarPaciente(1L);

        verify(pacienteRepository, times(1)).deleteById(1L);
    }
}