import sys
import requests
import json
from PyQt5.QtWidgets import (
    QApplication, QWidget, QVBoxLayout, QHBoxLayout, QPushButton, 
    QLabel, QLineEdit, QMessageBox, QTextEdit, QFormLayout, QTabWidget,
    QScrollArea, QGroupBox
)
from PyQt5.QtCore import Qt # Para alinhamento e outras constantes Qt
from datetime import datetime

# URL base do seu backend Flask
# Certifique-se de que o backend Flask esteja rodando nesta URL/porta
BASE_URL = "http://127.0.0.1:5000" 

class MainApp(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Sistema de Gestão de Consultas e Prontuários")
        self.setGeometry(100, 100, 1200, 800) 
        self.init_ui()

    def init_ui(self):
        main_layout = QVBoxLayout()

        # Criar um widget de abas para organizar melhor a interface
        tab_widget = QTabWidget()
        
        # Aba de Consultas
        consulta_tab = QWidget()
        self.setup_consulta_tab(consulta_tab)
        tab_widget.addTab(consulta_tab, "Consultas")
        
        # Aba de Prontuários
        prontuario_tab = QWidget()
        self.setup_prontuario_tab(prontuario_tab)
        tab_widget.addTab(prontuario_tab, "Prontuários")
        
        main_layout.addWidget(tab_widget)
        
        # Área de saída comum para ambas as abas
        self.text_output = QTextEdit(self)
        self.text_output.setReadOnly(True)
        self.text_output.setMaximumHeight(400)
        main_layout.addWidget(QLabel("Saída:"))
        main_layout.addWidget(self.text_output)

        self.setLayout(main_layout)

    def setup_consulta_tab(self, tab):
        """Configura a aba de consultas."""
        layout = QVBoxLayout()
        
        # Grupo para criar consulta
        criar_group = QGroupBox("Criar Nova Consulta")
        criar_layout = QFormLayout()
        
        # Campo ID da consulta removido - será gerado automaticamente pelo backend

        self.consulta_motivo_input = QLineEdit()
        self.consulta_motivo_input.setPlaceholderText("Ex: Dor de cabeça, Check-up")
        criar_layout.addRow("Motivo:", self.consulta_motivo_input)
        
        self.consulta_datahora_input = QLineEdit()
        # Corrigido: Usar timespec para isoformat
        self.consulta_datahora_input.setPlaceholderText(f"Data e Hora (Ex: {datetime.now().strftime("%Y-%m-%dT%H:%M:%S")})")
        self.consulta_datahora_input.setText(datetime.now().isoformat(timespec='seconds'))
        criar_layout.addRow("Data e Hora:", self.consulta_datahora_input)

        self.consulta_idmedico_input = QLineEdit()
        self.consulta_idmedico_input.setPlaceholderText("ID do Médico (número inteiro)")
        criar_layout.addRow("ID do Médico:", self.consulta_idmedico_input)

        self.consulta_idpaciente_input = QLineEdit()
        self.consulta_idpaciente_input.setPlaceholderText("ID do Paciente (número inteiro)")
        criar_layout.addRow("ID do Paciente:", self.consulta_idpaciente_input)

        self.consulta_status_input = QLineEdit()
        self.consulta_status_input.setPlaceholderText("Status (Agendada, Realizada, Cancelada)")
        self.consulta_status_input.setText("Agendada")
        criar_layout.addRow("Status:", self.consulta_status_input)
        
        self.btn_criar_consulta = QPushButton("Criar Consulta")
        self.btn_criar_consulta.clicked.connect(self.criar_consulta)
        criar_layout.addRow(self.btn_criar_consulta)
        
        criar_group.setLayout(criar_layout)
        layout.addWidget(criar_group)
        
        # Grupo para atualizar/deletar consulta
        crud_consulta_group = QGroupBox("Atualizar/Deletar Consulta")
        crud_consulta_layout = QFormLayout()

        self.consulta_id_crud_input = QLineEdit()
        self.consulta_id_crud_input.setPlaceholderText("ID da Consulta para Atualizar/Deletar")
        crud_consulta_layout.addRow("ID da Consulta:", self.consulta_id_crud_input)

        self.consulta_motivo_update_input = QLineEdit()
        self.consulta_motivo_update_input.setPlaceholderText("Novo Motivo (opcional)")
        crud_consulta_layout.addRow("Novo Motivo:", self.consulta_motivo_update_input)

        self.consulta_datahora_update_input = QLineEdit()
        self.consulta_datahora_update_input.setPlaceholderText("Nova Data e Hora (opcional)")
        crud_consulta_layout.addRow("Nova Data/Hora:", self.consulta_datahora_update_input)

        self.consulta_status_update_input = QLineEdit()
        self.consulta_status_update_input.setPlaceholderText("Novo Status (opcional)")
        crud_consulta_layout.addRow("Novo Status:", self.consulta_status_update_input)

        btn_update_consulta = QPushButton("Atualizar Consulta")
        btn_update_consulta.clicked.connect(self.atualizar_consulta)
        crud_consulta_layout.addRow(btn_update_consulta)

        btn_delete_consulta = QPushButton("Deletar Consulta")
        btn_delete_consulta.clicked.connect(self.deletar_consulta)
        crud_consulta_layout.addRow(btn_delete_consulta)

        crud_consulta_group.setLayout(crud_consulta_layout)
        layout.addWidget(crud_consulta_group)

        # Grupo para listar consultas
        listar_group = QGroupBox("Listar Consultas")
        listar_layout = QVBoxLayout()
        
        self.btn_listar_consultas = QPushButton("Listar Todas as Consultas")
        self.btn_listar_consultas.clicked.connect(self.listar_consultas)
        listar_layout.addWidget(self.btn_listar_consultas)
        
        listar_group.setLayout(listar_layout)
        layout.addWidget(listar_group)
        
        layout.addStretch()  # Adiciona espaço flexível no final
        tab.setLayout(layout)

    def setup_prontuario_tab(self, tab):
        """Configura a aba de prontuários."""
        layout = QVBoxLayout()
        
        # Grupo para criar prontuário
        criar_group = QGroupBox("Criar Novo Prontuário")
        criar_layout = QFormLayout()
        
        self.prontuario_id_input = QLineEdit()
        self.prontuario_id_input.setPlaceholderText("ID único do Prontuário (número inteiro)")
        criar_layout.addRow("ID do Prontuário:", self.prontuario_id_input)
        
        self.prontuario_datacriacao_input = QLineEdit()
        # Corrigido: Usar timespec para isoformat
        self.prontuario_datacriacao_input.setPlaceholderText(f"Data de Criação (Ex: {datetime.now().strftime("%Y-%m-%dT%H:%M:%S")})")
        self.prontuario_datacriacao_input.setText(datetime.now().isoformat(timespec='seconds'))
        criar_layout.addRow("Data de Criação:", self.prontuario_datacriacao_input)
        
        self.prontuario_historico_input = QTextEdit()
        self.prontuario_historico_input.setPlaceholderText("Histórico médico do paciente...")
        self.prontuario_historico_input.setMaximumHeight(80)
        criar_layout.addRow("Histórico Médico:", self.prontuario_historico_input)
        
        self.prontuario_diagnostico_input = QTextEdit()
        self.prontuario_diagnostico_input.setPlaceholderText("Diagnóstico médico...")
        self.prontuario_diagnostico_input.setMaximumHeight(80)
        criar_layout.addRow("Diagnóstico:", self.prontuario_diagnostico_input)
        
        self.prontuario_idpaciente_input = QLineEdit()
        self.prontuario_idpaciente_input.setPlaceholderText("ID do Paciente (número inteiro)")
        criar_layout.addRow("ID do Paciente:", self.prontuario_idpaciente_input)
        
        self.prontuario_tratamento_input = QTextEdit()
        self.prontuario_tratamento_input.setPlaceholderText("Tratamento prescrito...")
        self.prontuario_tratamento_input.setMaximumHeight(80)
        criar_layout.addRow("Tratamento:", self.prontuario_tratamento_input)
        
        # Campos para exames (simplificado - apenas um exame por vez)
        self.prontuario_exame_nome_input = QLineEdit()
        self.prontuario_exame_nome_input.setPlaceholderText("Nome do Exame (opcional)")
        criar_layout.addRow("Nome do Exame:", self.prontuario_exame_nome_input)
        
        self.prontuario_exame_data_input = QLineEdit()
        self.prontuario_exame_data_input.setPlaceholderText(f"Data do Exame (Ex: {datetime.now().strftime("%Y-%m-%dT%H:%M:%S")})")
        criar_layout.addRow("Data do Exame:", self.prontuario_exame_data_input)
        
        self.btn_criar_prontuario = QPushButton("Criar Prontuário")
        self.btn_criar_prontuario.clicked.connect(self.criar_prontuario)
        criar_layout.addRow(self.btn_criar_prontuario)
        
        criar_group.setLayout(criar_layout)
        layout.addWidget(criar_group)
        
        # Grupo para atualizar/deletar prontuário
        crud_prontuario_group = QGroupBox("Atualizar/Deletar Prontuário")
        crud_prontuario_layout = QFormLayout()

        self.prontuario_id_crud_input = QLineEdit()
        self.prontuario_id_crud_input.setPlaceholderText("ID do Prontuário para Atualizar/Deletar")
        crud_prontuario_layout.addRow("ID do Prontuário:", self.prontuario_id_crud_input)

        self.prontuario_historico_update_input = QTextEdit()
        self.prontuario_historico_update_input.setPlaceholderText("Novo Histórico Médico (opcional)")
        self.prontuario_historico_update_input.setMaximumHeight(80)
        crud_prontuario_layout.addRow("Novo Histórico:", self.prontuario_historico_update_input)

        self.prontuario_diagnostico_update_input = QTextEdit()
        self.prontuario_diagnostico_update_input.setPlaceholderText("Novo Diagnóstico (opcional)")
        self.prontuario_diagnostico_update_input.setMaximumHeight(80)
        crud_prontuario_layout.addRow("Novo Diagnóstico:", self.prontuario_diagnostico_update_input)

        self.prontuario_tratamento_update_input = QTextEdit()
        self.prontuario_tratamento_update_input.setPlaceholderText("Novo Tratamento (opcional)")
        self.prontuario_tratamento_update_input.setMaximumHeight(80)
        crud_prontuario_layout.addRow("Novo Tratamento:", self.prontuario_tratamento_update_input)

        btn_update_prontuario = QPushButton("Atualizar Prontuário")
        btn_update_prontuario.clicked.connect(self.atualizar_prontuario)
        crud_prontuario_layout.addRow(btn_update_prontuario)

        btn_delete_prontuario = QPushButton("Deletar Prontuário")
        btn_delete_prontuario.clicked.connect(self.deletar_prontuario)
        crud_prontuario_layout.addRow(btn_delete_prontuario)

        crud_prontuario_group.setLayout(crud_prontuario_layout)
        layout.addWidget(crud_prontuario_group)

        # Grupo para listar prontuários
        listar_group = QGroupBox("Listar Prontuários")
        listar_layout = QVBoxLayout()
        
        self.btn_listar_prontuarios = QPushButton("Listar Todos os Prontuários")
        self.btn_listar_prontuarios.clicked.connect(self.listar_prontuarios)
        listar_layout.addWidget(self.btn_listar_prontuarios)
        
        listar_group.setLayout(listar_layout)
        layout.addWidget(listar_group)
        
        layout.addStretch()  # Adiciona espaço flexível no final
        tab.setLayout(layout)

    def _display_message(self, title, message, is_error=False):
        """Função auxiliar para exibir mensagens ao usuário."""
        msg_box = QMessageBox(self)
        msg_box.setWindowTitle(title)
        msg_box.setText(message)
        if is_error:
            msg_box.setIcon(QMessageBox.Critical)
        else:
            msg_box.setIcon(QMessageBox.Information)
        msg_box.exec_()

    def criar_consulta(self):
        """Cria uma nova consulta."""
        self.text_output.clear()
        try:
            # Coleta e valida os dados dos campos da UI (ID será gerado automaticamente)
            motivo = self.consulta_motivo_input.text()
            data_hora_str = self.consulta_datahora_input.text()
            id_medico = int(self.consulta_idmedico_input.text())
            id_paciente = int(self.consulta_idpaciente_input.text())
            status = self.consulta_status_input.text()

            # Validações básicas
            if not motivo or not data_hora_str or not status:
                raise ValueError("Todos os campos obrigatórios devem ser preenchidos.")
            if status not in ["Agendada", "Realizada", "Cancelada"]:
                raise ValueError("Status inválido. Escolha entre \'Agendada\', \'Realizada\' ou \'Cancelada\'.")

            # Monta o payload JSON (sem idConsulta - será gerado automaticamente)
            consulta_data = {
                "dataHora": data_hora_str,
                "motivo": motivo,
                "idMedico": id_medico,
                "idPaciente": id_paciente,
                "status": status
            }

            headers = {"Content-Type": "application/json"}
            
            # Envia a requisição POST para o backend
            response = requests.post(f"{BASE_URL}/consultas/", json=consulta_data, headers=headers)
            response.raise_for_status()

            if response.status_code == 201:
                self._display_message("Sucesso", "Consulta criada com sucesso!")
                self.text_output.setText(f"Consulta Criada:\n{json.dumps(response.json(), indent=2)}")
                self._clear_consulta_fields()
                self.listar_consultas() # Adicionado: Chama a função de listar após criar
            else:
                self._display_message("Erro", f"Erro ao criar consulta: {response.text}", is_error=True)

        except ValueError as e:
            self._display_message("Erro de Entrada", f"Dados inválidos: {e}", is_error=True)
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if 'response' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def atualizar_consulta(self):
        """Atualiza uma consulta existente."""
        self.text_output.clear()
        try:
            id_consulta = int(self.consulta_id_crud_input.text())
            update_data = {}
            if self.consulta_motivo_update_input.text():
                update_data["motivo"] = self.consulta_motivo_update_input.text()
            if self.consulta_datahora_update_input.text():
                update_data["dataHora"] = self.consulta_datahora_update_input.text()
            if self.consulta_status_update_input.text():
                status = self.consulta_status_update_input.text()
                if status not in ["Agendada", "Realizada", "Cancelada"]:
                    raise ValueError("Status inválido. Escolha entre \'Agendada\', \'Realizada\' ou \'Cancelada\'.")
                update_data["status"] = status

            if not update_data:
                self._display_message("Aviso", "Nenhum dado para atualizar fornecido.")
                return

            headers = {"Content-Type": "application/json"}
            response = requests.put(f"{BASE_URL}/consultas/{id_consulta}", json=update_data, headers=headers)
            response.raise_for_status()

            if response.status_code == 200:
                self._display_message("Sucesso", "Consulta atualizada com sucesso!")
                self.text_output.setText(f"Consulta Atualizada:\n{json.dumps(response.json(), indent=2)}")
                self._clear_consulta_crud_fields()
                self.listar_consultas() # Adicionado: Chama a função de listar após atualizar
            else:
                self._display_message("Erro", f"Erro ao atualizar consulta: {response.text}", is_error=True)

        except ValueError as e:
            self._display_message("Erro de Entrada", f"Dados inválidos: {e}", is_error=True)
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if 'response' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def deletar_consulta(self):
        """Deleta uma consulta existente."""
        self.text_output.clear()
        try:
            id_consulta = int(self.consulta_id_crud_input.text())

            response = requests.delete(f"{BASE_URL}/consultas/{id_consulta}")
            response.raise_for_status()

            if response.status_code == 200:
                self._display_message("Sucesso", "Consulta deletada com sucesso!")
                self.text_output.setText(f"Consulta Deletada: ID {id_consulta}")
                self._clear_consulta_crud_fields()
                self.listar_consultas() # Adicionado: Chama a função de listar após deletar
            else:
                self._display_message("Erro", f"Erro ao deletar consulta: {response.text}", is_error=True)

        except ValueError:
            self._display_message("Erro de Entrada", "Por favor, insira um ID de consulta válido.", is_error=True)
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if 'response' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def listar_consultas(self):
        """Lista todas as consultas e exibe no text_output."""
        self.text_output.clear()
        try:
            response = requests.get(f"{BASE_URL}/consultas/")
            response.raise_for_status()

            consultas = response.json()
            if consultas:
                output_text = "Consultas:\n"
                for consulta in consultas:
                    output_text += (
                        f"  ID: {consulta.get('idConsulta', 'N/A')}, "
                        f"Motivo: {consulta.get('motivo', 'N/A')}, "
                        f"Data/Hora: {consulta.get('dataHora', 'N/A')}, "
                        f"Status: {consulta.get('status', 'N/A')}\n"
                    )
                self.text_output.setText(output_text)
                self._display_message("Sucesso", f"Foram encontradas {len(consultas)} consultas.")
            else:
                self.text_output.setText("Nenhuma consulta encontrada.")
                self._display_message("Informação", "Nenhuma consulta encontrada no sistema.")

        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", f"Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em {BASE_URL}", is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if 'response' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def criar_prontuario(self):
        """Cria um novo prontuário."""
        self.text_output.clear()
        try:
            id_prontuario = int(self.prontuario_id_input.text())
            data_criacao_str = self.prontuario_datacriacao_input.text()
            historico_medico = self.prontuario_historico_input.toPlainText()
            diagnostico = self.prontuario_diagnostico_input.toPlainText()
            id_paciente = int(self.prontuario_idpaciente_input.text())
            tratamento = self.prontuario_tratamento_input.toPlainText()
            exame_nome = self.prontuario_exame_nome_input.text()
            exame_data = self.prontuario_exame_data_input.text()

            if not all([id_prontuario, data_criacao_str, historico_medico, diagnostico, id_paciente, tratamento]):
                raise ValueError("Todos os campos obrigatórios do prontuário devem ser preenchidos.")

            prontuario_data = {
                "idProntuario": id_prontuario,
                "dataCriacao": data_criacao_str,
                "historicoMedico": historico_medico,
                "diagnostico": diagnostico,
                "idPaciente": id_paciente,
                "tratamento": tratamento,
                "exames": []
            }

            if exame_nome and exame_data:
                prontuario_data["exames"].append({"nome": exame_nome, "data": exame_data})

            headers = {"Content-Type": "application/json"}
            response = requests.post(f"{BASE_URL}/prontuarios/", json=prontuario_data, headers=headers)
            response.raise_for_status()

            if response.status_code == 201:
                self._display_message("Sucesso", "Prontuário criado com sucesso!")
                self.text_output.setText(f"Prontuário Criado:\n{json.dumps(response.json(), indent=2)}")
                self._clear_prontuario_fields()
                self.listar_prontuarios() # Adicionado: Chama a função de listar após criar
            else:
                self._display_message("Erro", f"Erro ao criar prontuário: {response.text}", is_error=True)

        except ValueError as e:
            self._display_message("Erro de Entrada", f"Dados inválidos: {e}", is_error=True)
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if 'response' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def atualizar_prontuario(self):
        """Atualiza um prontuário existente."""
        self.text_output.clear()
        try:
            id_prontuario = int(self.prontuario_id_crud_input.text())
            update_data = {}
            if self.prontuario_historico_update_input.toPlainText():
                update_data["historicoMedico"] = self.prontuario_historico_update_input.toPlainText()
            if self.prontuario_diagnostico_update_input.toPlainText():
                update_data["diagnostico"] = self.prontuario_diagnostico_update_input.toPlainText()
            if self.prontuario_tratamento_update_input.toPlainText():
                update_data["tratamento"] = self.prontuario_tratamento_update_input.toPlainText()

            if not update_data:
                self._display_message("Aviso", "Nenhum dado para atualizar fornecido.")
                return

            headers = {"Content-Type": "application/json"}
            response = requests.put(f"{BASE_URL}/prontuarios/{id_prontuario}", json=update_data, headers=headers)
            response.raise_for_status()

            if response.status_code == 200:
                self._display_message("Sucesso", "Prontuário atualizado com sucesso!")
                self.text_output.setText(f"Prontuário Atualizado:\n{json.dumps(response.json(), indent=2)}")
                self._clear_prontuario_crud_fields()
                self.listar_prontuarios() # Adicionado: Chama a função de listar após atualizar
            else:
                self._display_message("Erro", f"Erro ao atualizar prontuário: {response.text}", is_error=True)

        except ValueError as e:
            self._display_message("Erro de Entrada", f"Dados inválidos: {e}", is_error=True)
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if 'response' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def deletar_prontuario(self):
        """Deleta um prontuário existente."""
        self.text_output.clear()
        try:
            id_prontuario = int(self.prontuario_id_crud_input.text())

            response = requests.delete(f"{BASE_URL}/prontuarios/{id_prontuario}")
            response.raise_for_status()

            if response.status_code == 204:
                self._display_message("Sucesso", "Prontuário deletado com sucesso!")
                self.text_output.setText(f"Prontuário Deletado: ID {id_prontuario}")
                self._clear_prontuario_crud_fields()
                self.listar_prontuarios() # Adicionado: Chama a função de listar após deletar
            else:
                self._display_message("Erro", f"Erro ao deletar prontuário: {response.text}", is_error=True)

        except ValueError:
            self._display_message("Erro de Entrada", "Por favor, insira um ID de prontuário válido.", is_error=True)
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if '\'response\'' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def listar_prontuarios(self):
        """Lista todos os prontuários e exibe no text_output."""
        self.text_output.clear()
        try:
            response = requests.get(f"{BASE_URL}/prontuarios/")
            response.raise_for_status()

            prontuarios = response.json()
            if prontuarios:
                output_text = "Prontuários:\n"
                for prontuario in prontuarios:
                    output_text += f"  ID: {prontuario.get("idProntuario")}, Historico: {prontuario.get("historicoMedico")}, Data Criação: {prontuario.get("dataCriacao")}, Paciente: {prontuario.get("idPaciente")}\n"
                self.text_output.setText(output_text)
            else:
                self.text_output.setText("Nenhum prontuário encontrado.")

        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if 'response' in locals():
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def _clear_consulta_fields(self):
        """Limpa os campos de entrada da consulta após criação bem-sucedida."""
        # Campo ID removido - não precisa ser limpo
        self.consulta_motivo_input.clear()
        self.consulta_datahora_input.setText(datetime.now().isoformat(timespec='seconds'))
        self.consulta_idmedico_input.clear()
        self.consulta_idpaciente_input.clear()
        self.consulta_status_input.setText("Agendada")

    def _clear_consulta_crud_fields(self):
        self.consulta_id_crud_input.clear()
        self.consulta_motivo_update_input.clear()
        self.consulta_datahora_update_input.clear()
        self.consulta_status_update_input.clear()

    def _clear_prontuario_fields(self):
        self.prontuario_id_input.clear()
        self.prontuario_datacriacao_input.setText(datetime.now().isoformat(timespec='seconds')) # Resetar para data/hora atual
        self.prontuario_historico_input.clear()
        self.prontuario_diagnostico_input.clear()
        self.prontuario_idpaciente_input.clear()
        self.prontuario_tratamento_input.clear()
        self.prontuario_exame_nome_input.clear()
        self.prontuario_exame_data_input.clear()

    def _clear_prontuario_crud_fields(self):
        self.prontuario_id_crud_input.clear()
        self.prontuario_historico_update_input.clear()
        self.prontuario_diagnostico_update_input.clear()
        self.prontuario_tratamento_update_input.clear()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    main_app = MainApp()
    main_app.show()
    sys.exit(app.exec_())

