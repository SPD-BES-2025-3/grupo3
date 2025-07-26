from mongoengine import Document, IntField, DateTimeField, StringField, EmbeddedDocument, EmbeddedDocumentListField
from datetime import datetime

# Documento embutido para Exames
class Exame(EmbeddedDocument):
    nomeExame = StringField(required=True)
    dataRealizacao = DateTimeField(default=datetime)

# Documento principal: Prontuario
class Prontuario(Document):
    idProntuario = IntField(required=True, unique=True)
    dataCriacao = DateTimeField(required=True, default=datetime)
    historicoMedico = StringField(required=True)
    diagnostico = StringField(required=True)
    idPaciente = IntField(required=True)
    tratamento = StringField(required=True)
    exames = EmbeddedDocumentListField(Exame)  # Novo campo para os exames

    # Getters e Setters
    def get_idProntuario(self):
        return self.idProntuario

    def set_idProntuario(self, id):
        if isinstance(id, int):
            self.idProntuario = id
        else:
            raise ValueError("ID do Prontuário deve ser um número inteiro.")

    def get_dataCriacao(self):
        return self.dataCriacao

    def set_dataCriacao(self, data):
        if isinstance(data, datetime):
            self.dataCriacao = data
        else:
            raise ValueError("Data de criação deve ser do tipo datetime.")

    def get_historicoMedico(self):
        return self.historicoMedico

    def set_historicoMedico(self, historico):
        if isinstance(historico, str):
            self.historicoMedico = historico
        else:
            raise ValueError("Histórico médico deve ser uma string.")

    def get_diagnostico(self):
        return self.diagnostico

    def set_diagnostico(self, diagnostico):
        if isinstance(diagnostico, str):
            self.diagnostico = diagnostico
        else:
            raise ValueError("Diagnóstico deve ser uma string.")

    def get_idPaciente(self):
        return self.idPaciente

    def set_idPaciente(self, id):
        if isinstance(id, int):
            self.idPaciente = id
        else:
            raise ValueError("ID do paciente deve ser um número inteiro.")

    def get_tratamento(self):
        return self.tratamento

    def set_tratamento(self, tratamento):
        if isinstance(tratamento, str):
            self.tratamento = tratamento
        else:
            raise ValueError("Tratamento deve ser uma string.")

    def get_exames(self):
        return self.exames
    def set_exames(self, lista_exames):
        if all(isinstance(e, Exame) for e in lista_exames):
            self.exames = lista_exames
        else:
            raise ValueError("Todos os exames devem ser instâncias da classe Exame.")

    def adicionar_exame(self, exame):
        if isinstance(exame, Exame):
            self.exames.append(exame)
        else:
            raise ValueError("Exame deve ser uma instância da classe Exame.")

    def remover_exame(self, nome_exame):
        self.exames = [e for e in self.exames if e.nomeExame != nome_exame]


    def to_json(self):
        return {
            "idProntuario": self.idProntuario,
            "dataCriacao": self.dataCriacao.strftime("%Y-%m-%d %H:%M:%S"),
            "historicoMedico": self.historicoMedico,
            "diagnostico": self.diagnostico,
            "idPaciente": self.idPaciente,
            "tratamento": self.tratamento,
            "exames": [
                {
                    "nomeExame": exame.nomeExame,
                    "dataRealizacao": exame.dataRealizacao.strftime("%Y-%m-%d %H:%M:%S")
                }
                for exame in self.exames
            ]
        }
