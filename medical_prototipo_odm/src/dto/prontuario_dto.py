from pydantic import BaseModel, Field, ValidationError
from datetime import datetime
from typing import List, Optional

# DTO para o documento embutido Exame
class ExameDTO(BaseModel):
    nomeExame: str = Field(min_length=2, max_length=100)
    dataRealizacao: datetime

# DTO para criação de um novo prontuário
class ProntuarioCreateDTO(BaseModel):
    idProntuario: int
    dataCriacao: datetime
    historicoMedico: str = Field(min_length=10)
    diagnostico: str = Field(min_length=5)
    idPaciente: int
    tratamento: str = Field(min_length=5)
    exames: List[ExameDTO] = [] # Lista de ExameDTOs

# DTO para atualização de um prontuário
class ProntuarioUpdateDTO(BaseModel):
    dataCriacao: Optional[datetime] = None
    historicoMedico: Optional[str] = Field(None, min_length=10)
    diagnostico: Optional[str] = Field(None, min_length=5)
    idPaciente: Optional[int] = None
    tratamento: Optional[str] = Field(None, min_length=5)
    exames: Optional[List[ExameDTO]] = None