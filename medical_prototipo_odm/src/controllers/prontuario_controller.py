from flask import Blueprint, request, jsonify
from services.prontuario_services import ProntuarioService
from dto.prontuario_dto import ProntuarioCreateDTO, ProntuarioUpdateDTO # Importe os DTOs
from pydantic import ValidationError

prontuario_bp = Blueprint('prontuario_bp', __name__, url_prefix='/prontuarios')

@prontuario_bp.route('/', methods=['POST'])
def criar_prontuario():
    data = request.get_json()
    if not data:
        return jsonify({"erro": "Dados do prontuário não fornecidos."}), 400
    try:
        # Valida e converte a entrada JSON para um objeto DTO, depois para um dicionário
        prontuario_data = ProntuarioCreateDTO(**data).model_dump()
        
        prontuario = ProntuarioService.criar_prontuario(prontuario_data)
        return jsonify(prontuario.to_json()), 201
    except ValidationError as e:
        return jsonify({"erro": e.errors()}), 400
    except ValueError as e:
        return jsonify({"erro": str(e)}), 400
    except Exception as e:
        return jsonify({"erro": f"Erro ao criar prontuário: {str(e)}"}), 500

@prontuario_bp.route('/', methods=['GET'])
def listar_prontuarios():
    prontuarios = ProntuarioService.listar_prontuarios()
    return jsonify([prontuario.to_json() for prontuario in prontuarios]), 200

@prontuario_bp.route('/<int:idProntuario>', methods=['GET'])
def buscar_prontuario(idProntuario):
    prontuario = ProntuarioService.buscar_prontuario(idProntuario)
    if prontuario:
        return jsonify(prontuario.to_json()), 200
    return jsonify({"mensagem": "Prontuário não encontrado."}), 404

@prontuario_bp.route('/<int:idProntuario>', methods=['PUT'])
def atualizar_prontuario(idProntuario):
    data = request.get_json()
    if not data:
        return jsonify({"erro": "Dados para atualização não fornecidos."}), 400
    try:
        update_data = ProntuarioUpdateDTO(**data).model_dump(exclude_unset=True)

        resultado = ProntuarioService.atualizar_prontuario(idProntuario, update_data)
        if resultado:
            prontuario_atualizado = ProntuarioService.buscar_prontuario(idProntuario)
            if prontuario_atualizado:
                return jsonify(prontuario_atualizado.to_json()), 200
            else:
                return jsonify({"mensagem": "Prontuário atualizado, mas não encontrado para retorno."}), 200
        return jsonify({"mensagem": "Prontuário não encontrado para atualização."}), 404
    except ValidationError as e:
        return jsonify({"erro": e.errors()}), 400
    except ValueError as e:
        return jsonify({"erro": str(e)}), 400
    except Exception as e:
        return jsonify({"erro": f"Erro ao atualizar prontuário: {str(e)}"}), 500

@prontuario_bp.route('/<int:idProntuario>', methods=['DELETE'])
def deletar_prontuario(idProntuario):
    resultado = ProntuarioService.deletar_prontuario(idProntuario)
    if resultado:
        return jsonify({"mensagem": "Prontuário deletado com sucesso."}), 200
    return jsonify({"mensagem": "Prontuário não encontrada para deleção."}), 404
