from mongoengine import Document, IntField, DateTimeField, StringField
from datetime import datetime

class Consulta(Document):
    idConsulta = IntField(required=True, unique=True)
    dataHora = DateTimeField(required=True, default=datetime.utcnow)
    motivo = StringField(required=True)
    idMedico = IntField(required=True)
    idPaciente = IntField(required=True)
    status = StringField(required=True, choices=["Agendada", "Realizada", "Cancelada"])

    # Getter para o campo idConsulta
    def get_idConsulta(self):
        return self.idConsulta

    # Setter para o campo idConsulta
    def set_idConsulta(self, id):
        if isinstance(id, int):
            self.idConsulta = id
        else:
            raise ValueError("ID da consulta deve ser um número inteiro.")

    # Getter para o campo dataHora
    def get_dataHora(self):
        return self.dataHora

    # Setter para o campo dataHora
    def set_dataHora(self, data):
        if isinstance(data, datetime):
            self.dataHora = data
        else:
            raise ValueError("Data e hora devem ser do tipo datetime.")

    # Getter para o campo motivo
    def get_motivo(self):
        return self.motivo

    # Setter para o campo motivo
    def set_motivo(self, motivo):
        if isinstance(motivo, str):
            self.motivo = motivo
        else:
            raise ValueError("Motivo deve ser uma string.")

    # Getter para o campo idMedico
    def get_idMedico(self):
        return self.idMedico

    # Setter para o campo idMedico
    def set_idMedico(self, id):
        if isinstance(id, int):
            self.idMedico = id
        else:
            raise ValueError("ID do médico deve ser um número inteiro.")

    # Getter para o campo idPaciente
    def get_idPaciente(self):
        return self.idPaciente

    # Setter para o campo idPaciente
    def set_idPaciente(self, id):
        if isinstance(id, int):
            self.idPaciente = id
        else:
            raise ValueError("ID do paciente deve ser um número inteiro.")

    # Getter para o campo status
    def get_status(self):
        return self.status

    # Setter para o campo status
    def set_status(self, status):
        if status in ["Agendada", "Realizada", "Cancelada"]:
            self.status = status
        else:
            raise ValueError("Status inválido. Escolha entre 'Agendada', 'Realizada' ou 'Cancelada'.")
    
    def to_json(self):
        return {
            "idConsulta": self.idConsulta,
            "dataHora": self.dataHora.strftime("%Y-%m-%d %H:%M:%S") if isinstance(self.dataHora, datetime) else self.dataHora,
            "motivo": self.motivo,
            "idMedico": self.idMedico,
            "idPaciente": self.idPaciente,
            "status": self.status
        }
