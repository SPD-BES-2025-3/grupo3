import unittest
from unittest.mock import patch, MagicMock
from src.repositories.consulta_repository import ConsultaRepository
from src.repositories.prontuario_repository import ProntuarioRepository
from src.models.consulta import Consulta
from src.models.prontuario import Prontuario, Exame
from datetime import datetime

class TestConsultaRepository(unittest.TestCase):

    # Patch para a classe Consulta no módulo consulta_repository.py
    # Isso significa que toda vez que Consulta.objects() for chamado,
    # ele será substituído pelo nosso mock.
    @patch('src.repositories.consulta_repository.Consulta') 
    def test_find_by_id(self, MockConsulta):
        mock_instance = MagicMock(spec=Consulta) # Um mock que se comporta como uma instância de Consulta
        # Configura o mock de Consulta.objects().first()
        MockConsulta.objects.return_value.first.return_value = mock_instance

        result = ConsultaRepository.find_by_id(1)

        # Verifica se Consulta.objects foi chamado e depois .first()
        MockConsulta.objects.assert_called_once_with(idConsulta=1)
        MockConsulta.objects.return_value.first.assert_called_once()
        self.assertEqual(result, mock_instance)

    @patch('src.repositories.consulta_repository.Consulta')
    def test_find_all(self, MockConsulta):
        mock_list = [MagicMock(spec=Consulta), MagicMock(spec=Consulta)]
        # Configura o mock para que Consulta.objects() retorne uma lista mockada
        MockConsulta.objects.return_value = mock_list

        result = ConsultaRepository.find_all()

        MockConsulta.objects.assert_called_once()
        self.assertEqual(result, mock_list)
        self.assertEqual(len(result), 2)

    @patch('src.repositories.consulta_repository.Consulta')
    def test_save_new_consulta(self, MockConsulta):
        mock_consulta_instance = MagicMock(spec=Consulta)
        # Quando o save for chamado no mock da instância, ele deve retornar None ou o próprio mock
        mock_consulta_instance.save.return_value = None 

        result = ConsultaRepository.save(mock_consulta_instance)

        mock_consulta_instance.save.assert_called_once()
        self.assertEqual(result, mock_consulta_instance) # O repositório retorna o objeto salvo

    @patch('src.repositories.consulta_repository.Consulta')
    def test_update_consulta(self, MockConsulta):
        update_data = {"status": "Realizada"}
        MockConsulta.objects.return_value.update_one.return_value = 1 # Simula 1 documento modificado

        result = ConsultaRepository.update(1, update_data)

        MockConsulta.objects.assert_called_once_with(idConsulta=1)
        MockConsulta.objects.return_value.update_one.assert_called_once_with(**update_data)
        self.assertEqual(result, 1)

    @patch('src.repositories.consulta_repository.Consulta')
    def test_delete_consulta(self, MockConsulta):
        MockConsulta.objects.return_value.delete.return_value = 1 # Simula 1 documento deletado

        result = ConsultaRepository.delete(1)

        MockConsulta.objects.assert_called_once_with(idConsulta=1)
        MockConsulta.objects.return_value.delete.assert_called_once()
        self.assertEqual(result, 1)

class TestProntuarioRepository(unittest.TestCase):

    @patch('src.repositories.prontuario_repository.Prontuario')
    def test_find_by_id(self, MockProntuario):
        mock_instance = MagicMock(spec=Prontuario)
        MockProntuario.objects.return_value.first.return_value = mock_instance

        result = ProntuarioRepository.find_by_id(1)

        MockProntuario.objects.assert_called_once_with(idProntuario=1)
        MockProntuario.objects.return_value.first.assert_called_once()
        self.assertEqual(result, mock_instance)

    @patch('src.repositories.prontuario_repository.Prontuario')
    def test_find_all(self, MockProntuario):
        mock_list = [MagicMock(spec=Prontuario), MagicMock(spec=Prontuario)]
        MockProntuario.objects.return_value = mock_list

        result = ProntuarioRepository.find_all()

        MockProntuario.objects.assert_called_once()
        self.assertEqual(result, mock_list)
        self.assertEqual(len(result), 2)

    @patch('src.repositories.prontuario_repository.Prontuario')
    def test_save_new_prontuario(self, MockProntuario):
        mock_prontuario_instance = MagicMock(spec=Prontuario)
        mock_prontuario_instance.save.return_value = None

        result = ProntuarioRepository.save(mock_prontuario_instance)

        mock_prontuario_instance.save.assert_called_once()
        self.assertEqual(result, mock_prontuario_instance)

    @patch('src.repositories.prontuario_repository.Prontuario')
    def test_update_prontuario(self, MockProntuario):
        update_data = {"tratamento": "Novo Tratamento"}
        MockProntuario.objects.return_value.update_one.return_value = 1

        result = ProntuarioRepository.update(1, update_data)

        MockProntuario.objects.assert_called_once_with(idProntuario=1)
        MockProntuario.objects.return_value.update_one.assert_called_once_with(**update_data)
        self.assertEqual(result, 1)

    @patch('src.repositories.prontuario_repository.Prontuario')
    def test_delete_prontuario(self, MockProntuario):
        MockProntuario.objects.return_value.delete.return_value = 1

        result = ProntuarioRepository.delete(1)

        MockProntuario.objects.assert_called_once_with(idProntuario=1)
        MockProntuario.objects.return_value.delete.assert_called_once()
        self.assertEqual(result, 1)

if __name__ == '__main__':
    unittest.main()