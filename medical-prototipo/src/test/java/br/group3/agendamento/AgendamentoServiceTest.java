package br.group3.agendamento;

import br.group3.modules.medico.Medico;
import br.group3.modules.agendamento.AgendamentoService;
import br.group3.modules.agendamento.IAgendamentoRepository;
import br.group3.modules.agendamento.Agendamento;
import br.group3.modules.agendamento.AgendamentoDTO;
import br.group3.modules.medico.IMedicoRepository;
import br.group3.modules.paciente.IPacienteRepository;
import br.group3.modules.paciente.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.group3.modules.agendamento.Agendamento.StatusAgendamento;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes para {@link AgendamentoService}.
 * <p>
 * Esta classe utiliza o framework Mockito para testar a lógica de negócio
 * da classe {@link AgendamentoService} de forma isolada. Ao invés de interagir
 * com um banco de dados real ou outras dependências, os repositórios
 * ({@link IAgendamentoRepository}, {@link IPacienteRepository}, {@link IMedicoRepository})
 * são "mockados", permitindo controlar seu comportamento e verificar se os métodos
 * do serviço interagem com eles corretamente.
 * </p>
 *
 * @author Grupo 3
 * @see AgendamentoService
 * @see MockitoExtension
 */
@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {

    /**
     * Instância mock do repositório de agendamentos.
     * Captura interações e permite simular retornos de métodos de persistência.
     */
    @Mock
    private IAgendamentoRepository agendamentoRepository;

    /**
     * Instância mock do repositório de pacientes.
     * Usada para simular a busca por pacientes que serão associados a agendamentos.
     */
    @Mock
    private IPacienteRepository pacienteRepository;

    /**
     * Instância mock do repositório de médicos.
     * Usada para simular a busca por médicos que serão associados a agendamentos.
     */
    @Mock
    private IMedicoRepository medicoRepository;

    /**
     * Instância da classe {@link AgendamentoService} com as dependências mockadas injetadas.
     * Esta é a classe sob teste (System Under Test - SUT).
     */
    @InjectMocks
    private AgendamentoService agendamentoService;

    /**
     * Objeto de exemplo da entidade {@link Agendamento} para uso nos testes.
     * Representa um agendamento já existente no sistema.
     */
    private Agendamento agendamentoExemplo;

    /**
     * Objeto de exemplo do DTO {@link AgendamentoDTO} para uso nos testes.
     * Representa os dados de entrada para criação ou atualização de agendamentos.
     */
    private AgendamentoDTO agendamentoDTOExemplo;

    /**
     * Objeto de exemplo da entidade {@link Paciente} para uso nos testes.
     * Representa um paciente que pode ser associado a um agendamento.
     */
    private Paciente pacienteExemplo;

    /**
     * Objeto de exemplo da entidade {@link Medico} para uso nos testes.
     * Representa um médico que pode ser associado a um agendamento.
     */
    private Medico medicoExemplo;

    /**
     * Configuração inicial executada antes de cada método de teste.
     * Inicializa os objetos de exemplo (paciente, médico, agendamento e DTO)
     * para garantir um estado limpo para cada teste.
     */
    @BeforeEach
    void setUp() {
        pacienteExemplo = new Paciente(1L, "Paciente Teste", null, "11122233344", "Rua Y", "11999999999");
        medicoExemplo = new Medico(10L, "Dr. Medico Teste", "CRM/SP 123456", "11987654321", "medico@test.com", "Oftalmologia");

        agendamentoExemplo = new Agendamento(1L, LocalDateTime.of(2025, 8, 10, 10, 0),
                "Consulta Inicial", StatusAgendamento.AGENDADO, pacienteExemplo, medicoExemplo);

        agendamentoDTOExemplo = new AgendamentoDTO(
                LocalDateTime.of(2025, 8, 15, 14, 0), "Acompanhamento",
                StatusAgendamento.PENDENTE, 1L, 10L);
    }

    /**
     * Testa o cenário de sucesso para a criação de um novo agendamento.
     * Verifica se o método `salvarAgendamento` do serviço retorna o agendamento salvo
     * e se os métodos `findById` dos repositórios de paciente/médico e `save` do repositório
     * de agendamento foram chamados uma vez.
     */
    @Test
    @DisplayName("Deve salvar um novo agendamento com sucesso")
    void deveSalvarAgendamentoComSucesso() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExemplo));
        when(medicoRepository.findById(10L)).thenReturn(Optional.of(medicoExemplo));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoExemplo);

        Agendamento agendamentoSalvo = agendamentoService.salvarAgendamento(agendamentoDTOExemplo);

        assertNotNull(agendamentoSalvo);
        assertEquals(agendamentoExemplo.getMotivo(), agendamentoSalvo.getMotivo());
        assertEquals(agendamentoExemplo.getIdAgendamento(), agendamentoSalvo.getIdAgendamento());
        verify(pacienteRepository, times(1)).findById(1L);
        verify(medicoRepository, times(1)).findById(10L);
        verify(agendamentoRepository, times(1)).save(any(Agendamento.class));
    }

    /**
     * Testa o cenário onde um agendamento não deve ser salvo porque o paciente não existe.
     * Verifica se o método `salvarAgendamento` retorna `null` e se o método `save`
     * do repositório de agendamento nunca foi chamado.
     */
    @Test
    @DisplayName("Não deve salvar agendamento se paciente não existir")
    void naoDeveSalvarAgendamentoSePacienteNaoExistir() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());
        when(medicoRepository.findById(10L)).thenReturn(Optional.of(medicoExemplo)); // O médico pode até existir, mas o paciente não

        Agendamento agendamentoSalvo = agendamentoService.salvarAgendamento(agendamentoDTOExemplo);

        assertNull(agendamentoSalvo);
        verify(pacienteRepository, times(1)).findById(1L);
        verify(medicoRepository, times(1)).findById(10L);
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    /**
     * Testa o cenário onde um agendamento não deve ser salvo porque o médico não existe.
     * Verifica se o método `salvarAgendamento` retorna `null` e se o método `save`
     * do repositório de agendamento nunca foi chamado.
     */
    @Test
    @DisplayName("Não deve salvar agendamento se médico não existir")
    void naoDeveSalvarAgendamentoSeMedicoNaoExistir() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExemplo));
        when(medicoRepository.findById(10L)).thenReturn(Optional.empty());

        Agendamento agendamentoSalvo = agendamentoService.salvarAgendamento(agendamentoDTOExemplo);

        assertNull(agendamentoSalvo);
        verify(pacienteRepository, times(1)).findById(1L);
        verify(medicoRepository, times(1)).findById(10L);
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    /**
     * Testa a busca de um agendamento por um ID que existe.
     * Verifica se o método `buscarAgendamentoPorId` retorna um `Optional` contendo o agendamento
     * e se o método `findById` do repositório de agendamento foi chamado uma vez.
     */
    @Test
    @DisplayName("Deve buscar agendamento por ID existente")
    void deveBuscarAgendamentoPorIdExistente() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoExemplo));

        Optional<Agendamento> agendamentoEncontrado = agendamentoService.buscarAgendamentoPorId(1L);

        assertTrue(agendamentoEncontrado.isPresent());
        assertEquals(agendamentoExemplo.getMotivo(), agendamentoEncontrado.get().getMotivo());
        verify(agendamentoRepository, times(1)).findById(1L);
    }

    /**
     * Testa a busca de um agendamento por um ID que não existe.
     * Verifica se o método `buscarAgendamentoPorId` retorna um `Optional` vazio
     * e se o método `findById` do repositório de agendamento foi chamado uma vez.
     */
    @Test
    @DisplayName("Não deve encontrar agendamento por ID inexistente")
    void naoDeveEncontrarAgendamentoPorIdInexistente() {
        when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Agendamento> agendamentoEncontrado = agendamentoService.buscarAgendamentoPorId(99L);

        assertFalse(agendamentoEncontrado.isPresent());
        verify(agendamentoRepository, times(1)).findById(99L);
    }

    /**
     * Testa a listagem de todos os agendamentos.
     * Verifica se o método `listarTodosAgendamentos` retorna uma lista não vazia
     * com o número esperado de agendamentos e se o método `findAll`
     * do repositório de agendamento foi chamado uma vez.
     */
    @Test
    @DisplayName("Deve listar todos os agendamentos")
    void deveListarTodosAgendamentos() {
        List<Agendamento> agendamentos = Arrays.asList(
                agendamentoExemplo,
                new Agendamento(2L, LocalDateTime.of(2025, 8, 20, 9, 0),
                        "Check-up Anual", StatusAgendamento.AGENDADO, pacienteExemplo, medicoExemplo)
        );
        when(agendamentoRepository.findAll()).thenReturn(agendamentos);

        List<Agendamento> listaRetornada = agendamentoService.listarTodosAgendamentos();

        assertFalse(listaRetornada.isEmpty());
        assertEquals(2, listaRetornada.size());
        verify(agendamentoRepository, times(1)).findAll();
    }

    /**
     * Testa a atualização de um agendamento existente, onde apenas os dados do agendamento
     * (como data/hora e motivo) são alterados, mas os IDs de paciente e médico permanecem os mesmos.
     * Verifica se o agendamento é retornado com os dados atualizados e se as chamadas
     * aos repositórios de paciente/médico são evitadas.
     */
    @Test
    @DisplayName("Deve atualizar agendamento existente com sucesso sem alterar paciente/medico")
    void deveAtualizarAgendamentoExistenteComSucessoSemAlterarPacienteMedico() {
        AgendamentoDTO agendamentoDTOAtualizado = new AgendamentoDTO(
                LocalDateTime.of(2025, 8, 10, 11, 0),
                "Consulta de Rotina Remarcada", StatusAgendamento.AGENDADO,
                pacienteExemplo.getIdPaciente(), medicoExemplo.getIdMedico());

        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoExemplo));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoExemplo);

        Agendamento resultadoAtualizacao = agendamentoService.atualizarAgendamento(1L, agendamentoDTOAtualizado);

        assertNotNull(resultadoAtualizacao);
        assertEquals("Consulta de Rotina Remarcada", resultadoAtualizacao.getMotivo());
        assertEquals(LocalDateTime.of(2025, 8, 10, 11, 0), resultadoAtualizacao.getDataHora());
        verify(agendamentoRepository, times(1)).findById(1L);
        verify(pacienteRepository, never()).findById(anyLong()); // Verifica que findById não foi chamado para paciente
        verify(medicoRepository, never()).findById(anyLong());   // Verifica que findById não foi chamado para médico
        verify(agendamentoRepository, times(1)).save(any(Agendamento.class));
    }

    /**
     * Testa a atualização de um agendamento existente, incluindo a alteração
     * dos pacientes e/ou médicos associados.
     * Verifica se o agendamento é retornado com as novas associações e se as chamadas
     * aos repositórios de paciente/médico para os novos IDs ocorrem corretamente.
     */
    @Test
    @DisplayName("Deve atualizar agendamento existente e alterar paciente/medico")
    void deveAtualizarAgendamentoExistenteEAlterarPacienteMedico() {
        Paciente novoPaciente = new Paciente(2L, "Novo Paciente", null, "44455566677", "Rua Z", "22988887777");
        Medico novoMedico = new Medico(20L, "Dr. Novo Medico", "CRM/SP 987654", "11977776666", "novo.medico@test.com", "Pediatria");

        AgendamentoDTO agendamentoDTOAtualizado = new AgendamentoDTO(
                LocalDateTime.of(2025, 8, 10, 10, 0),
                "Consulta com novo médico e paciente", StatusAgendamento.AGENDADO,
                2L, 20L); // Novos IDs de paciente e médico

        Agendamento agendamentoOriginal = new Agendamento(1L, LocalDateTime.of(2025, 8, 10, 10, 0),
                "Consulta Inicial", StatusAgendamento.AGENDADO, pacienteExemplo, medicoExemplo);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoOriginal));

        when(pacienteRepository.findById(2L)).thenReturn(Optional.of(novoPaciente));
        when(medicoRepository.findById(20L)).thenReturn(Optional.of(novoMedico));

        Agendamento agendamentoAtualizadoEsperado = new Agendamento(1L, LocalDateTime.of(2025, 8, 10, 10, 0),
                "Consulta com novo médico e paciente", StatusAgendamento.AGENDADO, novoPaciente, novoMedico);
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoAtualizadoEsperado);

        Agendamento resultadoAtualizacao = agendamentoService.atualizarAgendamento(1L, agendamentoDTOAtualizado);

        assertNotNull(resultadoAtualizacao);
        assertEquals(novoPaciente.getIdPaciente(), resultadoAtualizacao.getPaciente().getIdPaciente());
        assertEquals(novoMedico.getIdMedico(), resultadoAtualizacao.getMedico().getIdMedico());
        verify(agendamentoRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).findById(2L);
        verify(medicoRepository, times(1)).findById(20L);
        verify(agendamentoRepository, times(1)).save(any(Agendamento.class));
    }

    /**
     * Testa o cenário de atualização de um agendamento com um ID inexistente.
     * Verifica se o método `atualizarAgendamento` retorna `null`
     * e se o método `save` do repositório de agendamento nunca é chamado.
     */
    @Test
    @DisplayName("Não deve atualizar agendamento se ID inexistente")
    void naoDeveAtualizarAgendamentoSeIdInexistente() {
        when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());

        Agendamento resultadoAtualizacao = agendamentoService.atualizarAgendamento(99L, agendamentoDTOExemplo);

        assertNull(resultadoAtualizacao);
        verify(agendamentoRepository, times(1)).findById(99L);
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
        verify(pacienteRepository, never()).findById(anyLong());
        verify(medicoRepository, never()).findById(anyLong());
    }

    /**
     * Testa o cenário de atualização de um agendamento onde o novo paciente
     * referenciado no DTO não existe.
     * Verifica se o método `atualizarAgendamento` retorna `null` e se as chamadas
     * aos repositórios são corretas (busca pelo paciente, mas não salva).
     */
    @Test
    @DisplayName("Não deve atualizar agendamento se novo paciente não existir")
    void naoDeveAtualizarAgendamentoSeNovoPacienteNaoExistir() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoExemplo));
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        AgendamentoDTO dtoComNovoPacienteInexistente = new AgendamentoDTO(
                agendamentoExemplo.getDataHora(), agendamentoExemplo.getMotivo(), agendamentoExemplo.getStatus(),
                99L, medicoExemplo.getIdMedico());

        Agendamento resultadoAtualizacao = agendamentoService.atualizarAgendamento(1L, dtoComNovoPacienteInexistente);

        assertNull(resultadoAtualizacao);
        verify(agendamentoRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).findById(99L);
        verify(medicoRepository, never()).findById(anyLong()); // Não deve tentar buscar o médico se o paciente já falhou
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    /**
     * Testa o cenário de atualização de um agendamento onde o novo médico
     * referenciado no DTO não existe.
     * Verifica se o método `atualizarAgendamento` retorna `null` e se as chamadas
     * aos repositórios são corretas (busca pelo paciente, busca pelo médico, mas não salva).
     */
    @Test
    @DisplayName("Não deve atualizar agendamento se novo médico não existir")
    void naoDeveAtualizarAgendamentoSeNovoMedicoNaoExistir() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoExemplo));
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.of(pacienteExemplo));
        when(medicoRepository.findById(anyLong())).thenReturn(Optional.empty());

        AgendamentoDTO dtoComNovoMedicoInexistente = new AgendamentoDTO(
                agendamentoExemplo.getDataHora(), agendamentoExemplo.getMotivo(), agendamentoExemplo.getStatus(),
                pacienteExemplo.getIdPaciente(), 99L);

        Agendamento resultadoAtualizacao = agendamentoService.atualizarAgendamento(1L, dtoComNovoMedicoInexistente);

        assertNull(resultadoAtualizacao);
        verify(agendamentoRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).findById(pacienteExemplo.getIdPaciente());
        verify(medicoRepository, times(1)).findById(99L);
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }


    /**
     * Testa a deleção de um agendamento existente por ID.
     * Verifica se o método `deletarAgendamento` invoca o método `deleteById`
     * do repositório de agendamento exatamente uma vez.
     */
    @Test
    @DisplayName("Deve deletar agendamento com sucesso")
    void deveDeletarAgendamentoComSucesso() {
        doNothing().when(agendamentoRepository).deleteById(1L);

        agendamentoService.deletarAgendamento(1L);

        verify(agendamentoRepository, times(1)).deleteById(1L);
    }
}