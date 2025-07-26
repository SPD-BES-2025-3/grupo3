from models.prontuario import Prontuario # Importa o modelo Prontuario

class ProntuarioRepository:
    """
    Classe de Repositório para a entidade Prontuario.
    Gerencia as operações de persistência (CRUD) para Prontuarios no MongoDB,
    abstraindo o uso direto do MongoEngine.
    """

    @staticmethod
    def find_by_id(idProntuario: int) -> Optional[Prontuario]:
        """Busca um prontuário pelo seu ID."""
        return Prontuario.objects(idProntuario=idProntuario).first() #

    @staticmethod
    def find_all() -> List[Prontuario]:
        """Lista todos os prontuários."""
        return list(Prontuario.objects()) #

    @staticmethod
    def save(prontuario: Prontuario) -> Prontuario:
        """Salva um novo prontuário ou atualiza um existente."""
        prontuario.save()
        return prontuario

    @staticmethod
    def update(idProntuario: int, dados: dict) -> int:
        """
        Atualiza um prontuário existente.
        Retorna o número de documentos modificados (0 ou 1).
        """
        return Prontuario.objects(idProntuario=idProntuario).update_one(**dados) #

    @staticmethod
    def delete(idProntuario: int) -> int:
        """
        Deleta um prontuário pelo seu ID.
        Retorna o número de documentos deletados (0 ou 1).
        """
        return Prontuario.objects(idProntuario=idProntuario).delete() #

from typing import List, Optional