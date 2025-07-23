package br.group3.medico;

import br.group3.modules.medico.IMedicoRepository;
import br.group3.modules.medico.Medico;
import br.group3.modules.medico.MedicoDTO;
import br.group3.modules.medico.MedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes para {@link MedicoService}.
 * Testa as operações de CRUD da lógica de negócio, mockando o repositório.
 *
 * @author Grupo 3
 */
@ExtendWith(MockitoExtension.class)
public class MedicoServiceTest {

    @Mock
    private IMedicoRepository medicoRepository;

    @InjectMocks
    private MedicoService medicoService;

    private Medico medicoExemplo;
    private MedicoDTO medicoDTOExemplo;

    /**
     * Configuração inicial antes de cada teste.
     * Prepara objetos Medico e MedicoDTO de exemplo.
     */
    @BeforeEach
    void setUp() {
        medicoExemplo = new Medico(1L, "Dr. Carlos Eduardo", "CRM/MG 789012", "3199887766",
                "carlos.eduardo@example.com", "Ortopedia");

        medicoDTOExemplo = new MedicoDTO(
                "Dr. Fernanda Alves", "CRM/PR 345678", "41987654321",
                "fernanda.alves@example.com", "Dermatologia");
    }

    @Test
    @DisplayName("Deve salvar um novo médico com sucesso")
    void deveSalvarMedicoComSucesso() {
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoExemplo);

        Medico medicoSalvo = medicoService.salvarMedico(medicoDTOExemplo);

        assertNotNull(medicoSalvo);
        assertEquals(medicoExemplo.getNomeCompleto(), medicoSalvo.getNomeCompleto());
        assertEquals(medicoExemplo.getIdMedico(), medicoSalvo.getIdMedico());
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    @DisplayName("Deve buscar médico por ID existente")
    void deveBuscarMedicoPorIdExistente() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoExemplo));

        Optional<Medico> medicoEncontrado = medicoService.buscarMedicoPorId(1L);

        assertTrue(medicoEncontrado.isPresent());
        assertEquals(medicoExemplo.getNomeCompleto(), medicoEncontrado.get().getNomeCompleto());
        verify(medicoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Não deve encontrar médico por ID inexistente")
    void naoDeveEncontrarMedicoPorIdInexistente() {
        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Medico> medicoEncontrado = medicoService.buscarMedicoPorId(99L);

        assertFalse(medicoEncontrado.isPresent());
        verify(medicoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve listar todos os médicos")
    void deveListarTodosMedicos() {
        List<Medico> medicos = Arrays.asList(
                medicoExemplo,
                new Medico(2L, "Dr. Gabriela Rocha", "CRM/RS 987654", "51991122334",
                        "gabriela.rocha@example.com", "Ginecologia")
        );
        when(medicoRepository.findAll()).thenReturn(medicos);

        List<Medico> listaRetornada = medicoService.listarTodosMedicos();

        assertFalse(listaRetornada.isEmpty());
        assertEquals(2, listaRetornada.size());
        verify(medicoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve atualizar médico existente com sucesso")
    void deveAtualizarMedicoExistenteComSucesso() {
        MedicoDTO medicoDTOAtualizado = new MedicoDTO(
                "Dr. Carlos Eduardo Lima", "CRM/MG 789012", "31998877660",
                "carlos.eduardo.lima@example.com", "Ortopedia e Traumatologia");

        Medico medicoAntigo = new Medico(1L, "Dr. Carlos Eduardo", "CRM/MG 789012", "3199887766",
                "carlos.eduardo@example.com", "Ortopedia");
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoAntigo));

        Medico medicoAtualizadoRetorno = new Medico(1L, "Dr. Carlos Eduardo Lima", "CRM/MG 789012", "31998877660",
                "carlos.eduardo.lima@example.com", "Ortopedia e Traumatologia");
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoAtualizadoRetorno);

        Medico resultadoAtualizacao = medicoService.atualizarMedico(1L, medicoDTOAtualizado);

        assertNotNull(resultadoAtualizacao);
        assertEquals("Dr. Carlos Eduardo Lima", resultadoAtualizacao.getNomeCompleto());
        assertEquals("Ortopedia e Traumatologia", resultadoAtualizacao.getEspecialidade());
        verify(medicoRepository, times(1)).findById(1L);
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    @DisplayName("Não deve atualizar médico inexistente")
    void naoDeveAtualizarMedicoInexistente() {
        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        Medico resultadoAtualizacao = medicoService.atualizarMedico(99L, medicoDTOExemplo);

        assertNull(resultadoAtualizacao);
        verify(medicoRepository, times(1)).findById(99L);
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    @DisplayName("Deve deletar médico com sucesso")
    void deveDeletarMedicoComSucesso() {
        doNothing().when(medicoRepository).deleteById(1L);

        medicoService.deletarMedico(1L);

        verify(medicoRepository, times(1)).deleteById(1L);
    }
}