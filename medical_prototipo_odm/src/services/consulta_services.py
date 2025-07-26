from models.consulta import Consulta
from repositories.consulta_repository import ConsultaRepository # Importa o novo repositório

class ConsultaService:

    @staticmethod
    def criar_consulta(data: dict) -> Consulta:
        """Cria e salva uma nova consulta."""
        consulta = Consulta(**data) # Instancia o modelo
        return ConsultaRepository.save(consulta) # Usa o repositório para salvar

    @staticmethod
    def listar_consultas() -> List[Consulta]:
        """Lista todas as consultas."""
        return ConsultaRepository.find_all() # Usa o repositório para buscar

    @staticmethod
    def buscar_consulta(idConsulta: int) -> Optional[Consulta]:
        """Busca uma consulta pelo ID."""
        return ConsultaRepository.find_by_id(idConsulta) # Usa o repositório para buscar

    @staticmethod
    def atualizar_consulta(idConsulta: int, dados: dict) -> int:
        """Atualiza uma consulta existente."""
        # A validação dos 'dados' já vem do DTO na camada de Controller (views)
        return ConsultaRepository.update(idConsulta, dados) # Usa o repositório para atualizar

    @staticmethod
    def deletar_consulta(idConsulta: int) -> int:
        """Deleta uma consulta."""
        return ConsultaRepository.delete(idConsulta) # Usa o repositório para deletar

from typing import List, Optional