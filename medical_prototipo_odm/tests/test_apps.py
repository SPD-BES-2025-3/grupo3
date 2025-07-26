import unittest
from unittest.mock import patch, MagicMock
import json

# Importar o app e os blueprints de onde eles estão no seu projeto
# Assumindo a estrutura controllers/
from src.app import app
from src.models.consulta import Consulta
from src.models.prontuario import Prontuario

class TestAPI(unittest.TestCase):

    def setUp(self):
        self.app = app
        self.client = app.test_client

        self.app.testing = True

    # Patch para o ConsultaService no contexto do controlador de consulta
    @patch('src.controllers.consulta_controller.ConsultaService')
    def test_criar_consulta_sucesso(self, MockConsultaService):
        mock_data = {
            "idConsulta": 1,
            "dataHora": "2025-07-25T10:00:00",
            "motivo": "Check-up",
            "idMedico": 101,
            "idPaciente": 201,
            "status": "Agendada"
        }
        
        # O to_json é chamado pelo controller antes do jsonify
        mock_consulta_obj = MagicMock(spec=Consulta)
        mock_consulta_obj.to_json.return_value = mock_data
        MockConsultaService.criar_consulta.return_value = mock_consulta_obj

        response = self.app.post('/consultas/',
                                 data=json.dumps(mock_data),
                                 content_type='application/json')

        self.assertEqual(response.status_code, 201)
        self.assertEqual(response.json, mock_data)
        MockConsultaService.criar_consulta.assert_called_once()

    @patch('src.controllers.consulta_controller.ConsultaService')
    def test_criar_consulta_dados_invalidos(self, MockConsultaService):
        invalid_data = {
            "idConsulta": "invalido", # ID deveria ser int
            "motivo": "Curto"
        }
        # O controller vai pegar a ValidationError do Pydantic
        MockConsultaService.criar_consulta.side_effect = Exception("Erro de validação mockado") # Não será chamado

        response = self.app.post('/consultas/',
                                 data=json.dumps(invalid_data),
                                 content_type='application/json')

        self.assertEqual(response.status_code, 400)
        self.assertIn("erro", response.json)
        # Verifica se o serviço NÃO foi chamado, pois o DTO já falharia antes
        MockConsultaService.criar_consulta.assert_not_called()

    @patch('src.controllers.consulta_controller.ConsultaService')
    def test_listar_consultas_sucesso(self, MockConsultaService):
        mock_consultas_data = [
            {"idConsulta": 1, "dataHora": "2025-07-25T10:00:00", "motivo": "Check-up", "idMedico": 101, "idPaciente": 201, "status": "Agendada"},
            {"idConsulta": 2, "dataHora": "2025-07-26T11:00:00", "motivo": "Retorno", "idMedico": 102, "idPaciente": 202, "status": "Agendada"}
        ]
        # Mocks para o to_json de cada objeto Consulta retornado
        mock_consulta_obj1 = MagicMock(spec=Consulta)
        mock_consulta_obj1.to_json.return_value = mock_consultas_data[0]
        mock_consulta_obj2 = MagicMock(spec=Consulta)
        mock_consulta_obj2.to_json.return_value = mock_consultas_data[1]

        MockConsultaService.listar_consultas.return_value = [mock_consulta_obj1, mock_consulta_obj2]

        response = self.app.get('/consultas/')

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json, mock_consultas_data)
        MockConsultaService.listar_consultas.assert_called_once()

    @patch('src.controllers.prontuario_controller.ProntuarioService')
    def test_buscar_prontuario_nao_encontrado(self, MockProntuarioService):
        MockProntuarioService.buscar_prontuario.return_value = None

        response = self.app.get('/prontuarios/999') # ID que não existe

        self.assertEqual(response.status_code, 404)
        self.assertIn("erro", response.json)
        MockProntuarioService.buscar_prontuario.assert_called_once_with(999)


if __name__ == '__main__':
    unittest.main()