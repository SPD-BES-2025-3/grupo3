from mongoengine import Document, StringField, DateTimeField, IntField, ReferenceField
from datetime import datetime
from .prontuario import Prontuario  # Importando a classe Prontuario

class Exame(Document):
    # Atributos da classe Exame
    idExame = IntField(required=True, unique=True)  # ID do exame
    nomeExame = StringField(required=True)  # Nome do exame
    dataRealizacao = DateTimeField(default=datetime.utcnow)  # Data de realização do exame
    prontuario = ReferenceField(Prontuario, required=True)  # Referência ao prontuário

    # Getter para o campo idExame
    def get_idExame(self):
        return self.idExame

    # Setter para o campo idExame
    def set_idExame(self, id):
        if isinstance(id, int):
            self.idExame = id
        else:
            raise ValueError("ID do Exame deve ser um número inteiro.")

    # Getter para o campo nomeExame
    def get_nomeExame(self):
        return self.nomeExame

    # Setter para o campo nomeExame
    def set_nomeExame(self, nome):
        if isinstance(nome, str):
            self.nomeExame = nome
        else:
            raise ValueError("Nome do exame deve ser uma string.")

    # Getter para o campo dataRealizacao
    def get_dataRealizacao(self):
        return self.dataRealizacao

    # Setter para o campo dataRealizacao
    def set_dataRealizacao(self, data):
        if isinstance(data, datetime):
            self.dataRealizacao = data
        else:
            raise ValueError("Data de realização do exame deve ser do tipo datetime.")

    # Getter para o campo prontuario
    def get_prontuario(self):
        return self.prontuario

    # Setter para o campo prontuario
    def set_prontuario(self, prontuario):
        if isinstance(prontuario, Prontuario):
            self.prontuario = prontuario
        else:
            raise ValueError("Prontuário deve ser uma instância de Prontuario.")

    # Método para retornar os dados como JSON
    def to_json(self):
        return {
            "idExame": self.idExame,
            "nomeExame": self.nomeExame,
            "dataRealizacao": self.dataRealizacao.strftime("%Y-%m-%d %H:%M:%S"),
            "idProntuario": self.prontuario.idProntuario if self.prontuario else None
        }
