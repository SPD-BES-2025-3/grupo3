from models.consulta import Consulta
from repositories.consulta_repository import ConsultaRepository # Importa o novo repositório
from typing import List, Optional
from mongoengine.errors import NotUniqueError

class ConsultaService:

    @staticmethod
    def criar_consulta(data: dict) -> Consulta:
        id_consulta = data.get("idConsulta")
        if id_consulta is None:
            raise ValueError("idConsulta não fornecido nos dados.")

        consulta_existente = ConsultaRepository.find_by_id(id_consulta)
        if consulta_existente:
            raise ValueError(f"Consulta com idConsulta {id_consulta} já existe.")

        consulta = Consulta(**data)
        try:
            return ConsultaRepository.save(consulta)
        except NotUniqueError as e:
            raise ValueError(f"Consulta com idConsulta {id_consulta} já existe.") from e


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

