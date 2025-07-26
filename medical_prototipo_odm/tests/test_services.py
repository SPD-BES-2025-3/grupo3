import unittest
from unittest.mock import patch, MagicMock
from src.services.consulta_services import ConsultaService
from src.services.prontuario_services import ProntuarioService
from src.models.consulta import Consulta
from src.models.prontuario import Prontuario, Exame
from src.dto.prontuario_dto import ExameDTO # Precisamos do DTO de Exame para criar dados mock
from datetime import datetime
class TestConsultaService(unittest.TestCase):

    # O patch.object substitui o ConsultaRepository dentro de ConsultaService
    # com um mock durante a execução dos testes desta classe.
    @patch('src.services.consulta_services.ConsultaRepository')
    def test_criar_consulta(self, MockConsultaRepository):
        mock_consulta_data = {
            "idConsulta": 1,
            "dataHora": "2025-07-25T10:00:00",
            "motivo": "Check-up",
            "idMedico": 101,
            "idPaciente": 201,
            "status": "Agendada"
        }
        # Configura o mock para retornar uma instância de Consulta quando .save() for chamado
        MockConsultaRepository.save.return_value = MagicMock(spec=Consulta) 
        MockConsultaRepository.save.return_value.to_json.return_value = mock_consulta_data

        result = ConsultaService.criar_consulta(mock_consulta_data)

        # Verifica se o método save do repositório foi chamado uma vez
        MockConsultaRepository.save.assert_called_once()
        # Verifica se o retorno é uma instância de Consulta (mesmo que mockada)
        self.assertIsInstance(result, MagicMock)
        # Verifica se o ID da consulta no resultado mockado corresponde
        self.assertEqual(result.to_json()['idConsulta'], mock_consulta_data['idConsulta'])

    @patch('src.services.consulta_services.ConsultaRepository')
    def test_listar_consultas(self, MockConsultaRepository):
        mock_consultas = [MagicMock(spec=Consulta), MagicMock(spec=Consulta)]
        MockConsultaRepository.find_all.return_value = mock_consultas

        result = ConsultaService.listar_consultas()

        MockConsultaRepository.find_all.assert_called_once()
        self.assertEqual(len(result), 2)
        self.assertIsInstance(result[0], MagicMock)

    @patch('src.services.consulta_services.ConsultaRepository')
    def test_buscar_consulta(self, MockConsultaRepository):
        mock_consulta = MagicMock(spec=Consulta)
        MockConsultaRepository.find_by_id.return_value = mock_consulta

        result = ConsultaService.buscar_consulta(1)

        MockConsultaRepository.find_by_id.assert_called_once_with(1)
        self.assertIsInstance(result, MagicMock)

    @patch('src.services.consulta_services.ConsultaRepository')
    def test_atualizar_consulta(self, MockConsultaRepository):
        update_data = {"status": "Realizada"}
        MockConsultaRepository.update.return_value = 1 # 1 documento modificado

        result = ConsultaService.atualizar_consulta(1, update_data)

        MockConsultaRepository.update.assert_called_once_with(1, update_data)
        self.assertEqual(result, 1) # O serviço retorna o resultado do repositório

    @patch('src.services.consulta_services.ConsultaRepository')
    def test_deletar_consulta(self, MockConsultaRepository):
        MockConsultaRepository.delete.return_value = 1 # 1 documento deletado

        result = ConsultaService.deletar_consulta(1)

        MockConsultaRepository.delete.assert_called_once_with(1)
        self.assertEqual(result, 1)

class TestProntuarioService(unittest.TestCase):

    @patch('src.services.prontuario_services.ProntuarioRepository')
    def test_criar_prontuario(self, MockProntuarioRepository):
        # Data de prontuário com exame mockado
        mock_exame_data = ExameDTO(nomeExame="Hemograma", dataRealizacao=datetime.strptime("2025-07-20T09:00:00", "%Y-%m-%dT%H:%M:%S"))
        mock_prontuario_data = {
            "idProntuario": 1,
            "dataCriacao": "2025-07-25T10:00:00",
            "historicoMedico": "Histórico mock",
            "diagnostico": "Diagnóstico mock",
            "idPaciente": 301,
            "tratamento": "Tratamento mock",
            "exames": [mock_exame_data.model_dump()] # Passe como dicionário
        }
        
        MockProntuarioRepository.save.return_value = MagicMock(spec=Prontuario) 
        # Simula o to_json se o controller precisar para retorno
        MockProntuarioRepository.save.return_value.to_json.return_value = mock_prontuario_data

        result = ProntuarioService.criar_prontuario(mock_prontuario_data)

        MockProntuarioRepository.save.assert_called_once()
        self.assertIsInstance(result, MagicMock)
        self.assertEqual(result.to_json()['idProntuario'], mock_prontuario_data['idProntuario'])
        
        # Verificar se os exames foram convertidos para objetos Exame antes de salvar
        # O mock.call_args.args[0] acessa o primeiro argumento passado para o método save (o objeto Prontuario)
        saved_prontuario = MockProntuarioRepository.save.call_args.args[0]
        self.assertIsInstance(saved_prontuario.exames[0], Exame)
        self.assertEqual(saved_prontuario.exames[0].nomeExame, mock_exame_data.nomeExame)


    @patch('src.services.prontuario_services.ProntuarioRepository')
    def test_listar_prontuarios(self, MockProntuarioRepository):
        mock_prontuarios = [MagicMock(spec=Prontuario), MagicMock(spec=Prontuario)]
        MockProntuarioRepository.find_all.return_value = mock_prontuarios

        result = ProntuarioService.listar_prontuarios()

        MockProntuarioRepository.find_all.assert_called_once()
        self.assertEqual(len(result), 2)

    @patch('src.services.prontuario_services.ProntuarioRepository')
    def test_atualizar_prontuario(self, MockProntuarioRepository):
        update_data = {"tratamento": "Novo Tratamento"}
        MockProntuarioRepository.update.return_value = 1

        result = ProntuarioService.atualizar_prontuario(1, update_data)

        MockProntuarioRepository.update.assert_called_once_with(1, update_data)
        self.assertEqual(result, 1)

    @patch('src.services.prontuario_services.ProntuarioRepository')
    def test_deletar_prontuario(self, MockProntuarioRepository):
        MockProntuarioRepository.delete.return_value = 1

        result = ProntuarioService.deletar_prontuario(1)

        MockProntuarioRepository.delete.assert_called_once_with(1)
        self.assertEqual(result, 1)

if __name__ == '__main__':
    unittest.main()