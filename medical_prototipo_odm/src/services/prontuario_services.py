from models.prontuario import Prontuario, Exame # Importa o modelo Prontuario e Exame
from repositories.prontuario_repository import ProntuarioRepository # Importa o novo repositório
from datetime import datetime # Importa datetime para tratamento de datas se necessário

class ProntuarioService:
    
    @staticmethod
    def criar_prontuario(data: dict) -> Prontuario:
        """Cria e salva um novo prontuário."""
        # Se os 'exames' vierem como dicionários do DTO, precisam ser convertidos para objetos Exame
        # antes de instanciar Prontuario. Pydantic já faz isso se o DTO for bem definido,
        # mas aqui é uma camada de segurança.
        if 'exames' in data and isinstance(data['exames'], list):
            data['exames'] = [Exame(**exame_data) for exame_data in data['exames']]
            
        prontuario = Prontuario(**data) # Instancia o modelo
        return ProntuarioRepository.save(prontuario) # Usa o repositório para salvar

    @staticmethod
    def listar_prontuarios() -> List[Prontuario]:
        """Lista todos os prontuários."""
        return ProntuarioRepository.find_all() # Usa o repositório para buscar

    @staticmethod
    def buscar_prontuario(idProntuario: int) -> Optional[Prontuario]:
        """Busca um prontuário pelo ID."""
        return ProntuarioRepository.find_by_id(idProntuario) # Usa o repositório para buscar

    @staticmethod
    def atualizar_prontuario(idProntuario: int, dados: dict) -> int:
        """Atualiza um prontuário existente."""
        # Converte Exames de volta para objetos Exame se presentes e forem dicionários
        if 'exames' in dados and isinstance(dados['exames'], list):
            dados['exames'] = [Exame(**exame_data) for exame_data in dados['exames']]
            
        return ProntuarioRepository.update(idProntuario, dados) # Usa o repositório para atualizar

    @staticmethod
    def deletar_prontuario(idProntuario: int) -> int:
        """Deleta um prontuário."""
        return ProntuarioRepository.delete(idProntuario) # Usa o repositório para deletar

from typing import List, Optional