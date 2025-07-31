from models.consulta import Consulta
from repositories.consulta_repository import ConsultaRepository # Importa o repositório
from typing import List, Optional
from mongoengine.errors import NotUniqueError

class ConsultaService:

    @staticmethod
    def _gerar_proximo_id() -> int:
        """Gera o próximo ID disponível, começando em 1."""
        consultas_existentes = ConsultaRepository.find_all()
        if not consultas_existentes:
            return 1
        
        # Obtém todos os IDs existentes e ordena
        ids_existentes = sorted([consulta.idConsulta for consulta in consultas_existentes])
        
        # Encontra o menor ID disponível
        proximo_id = 1
        for id_atual in ids_existentes:
            if proximo_id < id_atual:
                break
            proximo_id = id_atual + 1
        
        return proximo_id

    @staticmethod
    def criar_consulta(data: dict) -> Consulta:
        # Remove idConsulta dos dados se estiver presente (será gerado automaticamente)
        data_copia = data.copy()
        data_copia.pop("idConsulta", None)
        
        # Gera automaticamente o próximo ID disponível
        id_consulta = ConsultaService._gerar_proximo_id()
        data_copia["idConsulta"] = id_consulta

        consulta = Consulta(**data_copia)
        try:
            return ConsultaRepository.save(consulta)
        except NotUniqueError as e:
            # Em caso de conflito (muito raro), tenta novamente com o próximo ID
            id_consulta = ConsultaService._gerar_proximo_id()
            data_copia["idConsulta"] = id_consulta
            consulta = Consulta(**data_copia)
            return ConsultaRepository.save(consulta)


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

