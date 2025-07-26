import unittest
from datetime import datetime
from src.models.consulta import Consulta 
from src.models.prontuario import Prontuario, Exame

class TestConsultaModel(unittest.TestCase):

    def test_consulta_to_json(self):
        # Garante que o to_json converte o objeto Consulta para o dicionário esperado
        now = datetime.now().replace(microsecond=0) # Remove microssegundos para evitar inconsistências de string
        consulta = Consulta(
            idConsulta=1,
            dataHora=now,
            motivo="Dor de cabeça",
            idMedico=101,
            idPaciente=201,
            status="Agendada"
        )
        
        expected_json = {
            "idConsulta": 1,
            "dataHora": now.strftime("%Y-%m-%dT%H:%M:%S"), # Formato esperado do to_json
            "motivo": "Dor de cabeça",
            "idMedico": 101,
            "idPaciente": 201,
            "status": "Agendada"
        }
        self.assertEqual(consulta.to_json(), expected_json)

    def test_consulta_field_assignment(self):
        # Testa a atribuição direta de campos (sem getters/setters)
        consulta = Consulta()
        consulta.idConsulta = 2
        consulta.motivo = "Febre"
        self.assertEqual(consulta.idConsulta, 2)
        self.assertEqual(consulta.motivo, "Febre")

class TestProntuarioModel(unittest.TestCase):

    def test_exame_to_json(self):
        now = datetime.now().replace(microsecond=0)
        exame = Exame(
            nomeExame="Raio-X",
            dataRealizacao=now
        )
        expected_json = {
            "nomeExame": "Raio-X",
            "dataRealizacao": now.strftime("%Y-%m-%dT%H:%M:%S")
        }
        self.assertEqual(exame.to_json(), expected_json)

    def test_prontuario_to_json(self):
        now_pront = datetime.now().replace(microsecond=0)
        now_exame = datetime.now().replace(microsecond=0)
        exame1 = Exame(nomeExame="Hemograma", dataRealizacao=now_exame)
        exame2 = Exame(nomeExame="Urinálise", dataRealizacao=now_exame)

        prontuario = Prontuario(
            idProntuario=1,
            dataCriacao=now_pront,
            historicoMedico="Paciente com histórico de asma.",
            diagnostico="Resfriado comum",
            idPaciente=301,
            tratamento="Repouso e medicação",
            exames=[exame1, exame2]
        )

        expected_json = {
            "idProntuario": 1,
            "dataCriacao": now_pront.strftime("%Y-%m-%dT%H:%M:%S"),
            "historicoMedico": "Paciente com histórico de asma.",
            "diagnostico": "Resfriado comum",
            "idPaciente": 301,
            "tratamento": "Repouso e medicação",
            "exames": [
                exame1.to_json(), # Já usa o método to_json do Exame
                exame2.to_json()
            ]
        }
        self.assertEqual(prontuario.to_json(), expected_json)

    def test_prontuario_field_assignment_and_exames_list(self):
        prontuario = Prontuario()
        prontuario.idProntuario = 2
        prontuario.historicoMedico = "Sem histórico relevante."
        prontuario.exames = [Exame(nomeExame="Glicemia", dataRealizacao=datetime.now())]

        self.assertEqual(prontuario.idProntuario, 2)
        self.assertEqual(prontuario.historicoMedico, "Sem histórico relevante.")
        self.assertEqual(len(prontuario.exames), 1)
        self.assertIsInstance(prontuario.exames[0], Exame)
        self.assertEqual(prontuario.exames[0].nomeExame, "Glicemia")

if __name__ == '__main__':
    unittest.main()