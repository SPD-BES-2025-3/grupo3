from mongoengine import Document, IntField, DateTimeField, StringField
from datetime import datetime

class Prontuario(Document):
    idProntuario = IntField(required=True, unique=True)
    dataCriacao = DateTimeField(required=True, default=datetime.utcnow)
    historicoMedico = StringField(required=True)
    diagnostico = StringField(required=True)
    idPaciente = IntField(required=True)
    tratamento = StringField(required=True)

    # Getter para o campo idProntuario
    def get_idProntuario(self):
        return self.idProntuario

    # Setter para o campo idProntuario
    def set_idProntuario(self, id):
        if isinstance(id, int):
            self.idProntuario = id
        else:
            raise ValueError("ID do Prontuário deve ser um número inteiro.")

    # Getter para o campo dataCriacao
    def get_dataCriacao(self):
        return self.dataCriacao

    # Setter para o campo dataCriacao
    def set_dataCriacao(self, data):
        if isinstance(data, datetime):
            self.dataCriacao = data
        else:
            raise ValueError("Data de criação deve ser do tipo datetime.")

    # Getter para o campo historicoMedico
    def get_historicoMedico(self):
        return self.historicoMedico

    # Setter para o campo historicoMedico
    def set_historicoMedico(self, historico):
        if isinstance(historico, str):
            self.historicoMedico = historico
        else:
            raise ValueError("Histórico médico deve ser uma string.")

    # Getter para o campo diagnostico
    def get_diagnostico(self):
        return self.diagnostico

    # Setter para o campo diagnostico
    def set_diagnostico(self, diagnostico):
        if isinstance(diagnostico, str):
            self.diagnostico = diagnostico
        else:
            raise ValueError("Diagnóstico deve ser uma string.")

    # Getter para o campo idPaciente
    def get_idPaciente(self):
        return self.idPaciente

    # Setter para o campo idPaciente
    def set_idPaciente(self, id):
        if isinstance(id, int):
            self.idPaciente = id
        else:
            raise ValueError("ID do paciente deve ser um número inteiro.")

    # Getter para o campo tratamento
    def get_tratamento(self):
        return self.tratamento

    # Setter para o campo tratamento
    def set_tratamento(self, tratamento):
        if isinstance(tratamento, str):
            self.tratamento = tratamento
        else:
            raise ValueError("Tratamento deve ser uma string.")
    
    def to_json(self):
        return {
            "idProntuario": self.idProntuario,
            "dataCriacao": self.dataCriacao.strftime("%Y-%m-%d %H:%M:%S"),
            "historicoMedico": self.historicoMedico,
            "diagnostico": self.diagnostico,
            "idPaciente": self.idPaciente,
            "tratamento": self.tratamento
        }
