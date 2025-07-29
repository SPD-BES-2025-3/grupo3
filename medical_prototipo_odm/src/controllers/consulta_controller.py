from flask import Blueprint, request, jsonify
from services.consulta_services import ConsultaService
from dto.consulta_dto import ConsultaCreateDTO, ConsultaUpdateDTO # Importe os DTOs
from pydantic import ValidationError # Importe ValidationError do Pydantic
import traceback
consulta_bp = Blueprint('consulta_bp', __name__, url_prefix='/consultas')

@consulta_bp.route('/', methods=['POST'])
def criar_consulta():
    data = request.get_json()
    if not data:
        print("Erro: Dados da consulta não fornecidos.")
        return jsonify({"erro": "Dados da consulta não fornecidos."}), 400

    try:
        print(f"DEBUG: Dados JSON recebidos do frontend: {data}") #
        
        consulta_data = ConsultaCreateDTO(**data).model_dump()
        print(f"DEBUG: Dados após validação Pydantic (dataHora deve ser datetime): {consulta_data}") #
        print(f"DEBUG: Tipo de 'dataHora' após Pydantic: {type(consulta_data.get('dataHora'))}") #
        
        consulta = ConsultaService.criar_consulta(consulta_data)
        print(f"DEBUG: Objeto Consulta retornado pelo Service: {consulta}") #
        print(f"DEBUG: Tipo de 'dataHora' no objeto Consulta (antes de to_json()): {type(consulta.dataHora)}") #

        json_response_payload = consulta.to_json()
        print(f"DEBUG: Payload JSON gerado por .to_json(): {json_response_payload}") #
        print(f"DEBUG: Tipo de 'dataHora' no payload de resposta: {type(json_response_payload.get('dataHora'))}") #
        traceback.print_exc()
        return jsonify(json_response_payload), 201
    except ValidationError as e:
        print("Erro de validação Pydantic:", e) #
        return jsonify({"erro": e.errors()}), 400
    except ValueError as e:
        print("Erro de valor (provavelmente do Service/Repository):", e) #
        return jsonify({"erro": str(e)}), 400
    except Exception as e:
        print("Erro inesperado ao criar consulta:", e) #
        traceback.print_exc() # Imprime o traceback completo no console do servidor
        return jsonify({"erro": f"Erro interno do servidor: {str(e)}"}), 500

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
