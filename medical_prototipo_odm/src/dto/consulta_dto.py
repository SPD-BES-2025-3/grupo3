from pydantic import BaseModel, Field
from datetime import datetime
from typing import Optional

# DTO para criação de uma nova consulta
class ConsultaCreateDTO(BaseModel):
    idConsulta: Optional[int] = None  # Agora é opcional, será gerado automaticamente
    dataHora: datetime
    motivo: str = Field(min_length=3, max_length=255) 
    idMedico: int
    idPaciente: int
    status: str = Field(pattern="^(Agendada|Realizada|Cancelada)$")
    
# DTO para atualização de uma consulta (todos os campos são opcionais)
class ConsultaUpdateDTO(BaseModel):
    dataHora: Optional[datetime] = None
    motivo: Optional[str] = Field(None, min_length=3, max_length=255)
    idMedico: Optional[int] = None
    idPaciente: Optional[int] = None
    status: Optional[str] = Field(None, pattern="^(Agendada|Realizada|Cancelada)$")
