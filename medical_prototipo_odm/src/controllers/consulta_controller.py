from flask import Blueprint, request, jsonify
from services.consulta_services import ConsultaService
from dto.consulta_dto import ConsultaCreateDTO, ConsultaUpdateDTO # Importe os DTOs
from pydantic import ValidationError # Importe ValidationError do Pydantic

consulta_bp = Blueprint('consulta_bp', __name__, url_prefix='/consultas')

@consulta_bp.route('/', methods=['POST'])
def criar_consulta():
    data = request.get_json()
    if not data:
        return jsonify({"erro": "Dados da consulta não fornecidos."}), 400
    try:
        # Valida e converte a entrada JSON para um objeto DTO, depois para um dicionário
        # Pydantic automaticamente converte a string de data para datetime
        consulta_data = ConsultaCreateDTO(**data).model_dump() 
        
        consulta = ConsultaService.criar_consulta(consulta_data)
        return jsonify(consulta.to_json()), 201
    except ValidationError as e:
        # Captura erros de validação do Pydantic e retorna 400 Bad Request
        return jsonify({"erro": e.errors()}), 400
    except ValueError as e:
        # Captura erros de validação nos setters dos modelos (se ainda existirem e forem diferentes)
        return jsonify({"erro": str(e)}), 400
    except Exception as e:
        return jsonify({"erro": f"Erro ao criar consulta: {str(e)}"}), 500

@consulta_bp.route('/', methods=['GET'])
def listar_consultas():
    consultas = ConsultaService.listar_consultas()
    return jsonify([consulta.to_json() for consulta in consultas]), 200

@consulta_bp.route('/<int:idConsulta>', methods=['GET'])
def buscar_consulta(idConsulta):
    consulta = ConsultaService.buscar_consulta(idConsulta)
    if consulta:
        return jsonify(consulta.to_json()), 200
    return jsonify({"mensagem": "Consulta não encontrada."}), 404

@consulta_bp.route('/<int:idConsulta>', methods=['PUT'])
def atualizar_consulta(idConsulta):
    data = request.get_json()
    if not data:
        return jsonify({"erro": "Dados para atualização não fornecidos."}), 400
    try:
        # Valida e converte a entrada JSON para um objeto DTO de atualização
        # `exclude_unset=True` garante que apenas os campos fornecidos na requisição sejam incluídos no dicionário
        update_data = ConsultaUpdateDTO(**data).model_dump(exclude_unset=True) 

        resultado = ConsultaService.atualizar_consulta(idConsulta, update_data)
        if resultado:
            consulta_atualizada = ConsultaService.buscar_consulta(idConsulta)
            if consulta_atualizada:
                return jsonify(consulta_atualizada.to_json()), 200
            else:
                return jsonify({"mensagem": "Consulta atualizada, mas não encontrada para retorno."}), 200
        return jsonify({"mensagem": "Consulta não encontrada para atualização."}), 404
    except ValidationError as e:
        return jsonify({"erro": e.errors()}), 400
    except ValueError as e:
        return jsonify({"erro": str(e)}), 400
    except Exception as e:
        return jsonify({"erro": f"Erro ao atualizar consulta: {str(e)}"}), 500

@consulta_bp.route('/<int:idConsulta>', methods=['DELETE'])
def deletar_consulta(idConsulta):
    resultado = ConsultaService.deletar_consulta(idConsulta)
    if resultado:
        return jsonify({"mensagem": "Consulta deletada com sucesso."}), 200
    return jsonify({"mensagem": "Consulta não encontrada para deleção."}), 404