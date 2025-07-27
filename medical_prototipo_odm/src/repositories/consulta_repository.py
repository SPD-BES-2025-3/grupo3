from models.consulta import Consulta 
from typing import List, Optional

class ConsultaRepository:
    """
    Classe de Repositório para a entidade Consulta.
    Gerencia as operações de persistência (CRUD) para Consultas no MongoDB,
    abstraindo o uso direto do MongoEngine.
    """

    @staticmethod
    def find_by_id(idConsulta: int) -> Optional[Consulta]:
        """Busca uma consulta pelo seu ID."""
        return Consulta.objects(idConsulta=idConsulta).first() #

    @staticmethod
    def find_all() -> List[Consulta]:
        """Lista todas as consultas."""
        return list(Consulta.objects()) #

    @staticmethod
    def save(consulta: Consulta) -> Consulta:
        """Salva uma nova consulta ou atualiza uma existente."""
        consulta.save()
        return consulta

    @staticmethod
    def update(idConsulta: int, dados: dict) -> int:
        """
        Atualiza uma consulta existente.
        Retorna o número de documentos modificados (0 ou 1).
        """
        return Consulta.objects(idConsulta=idConsulta).update_one(**dados) #

    @staticmethod
    def delete(idConsulta: int) -> int:
        """
        Deleta uma consulta pelo seu ID.
        Retorna o número de documentos deletados (0 ou 1).
        """
        return Consulta.objects(idConsulta=idConsulta).delete() #

