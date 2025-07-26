import sys
import requests
import json
from PyQt5.QtWidgets import (
    QApplication, QWidget, QVBoxLayout, QHBoxLayout, QPushButton, 
    QLabel, QLineEdit, QMessageBox, QTextEdit, QFormLayout
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
        self.setGeometry(100, 100, 800, 600) # Ajusta o tamanho da janela
        self.init_ui()

    def init_ui(self):
        main_layout = QVBoxLayout()

        # --- Seção para Criar Consulta ---
        consulta_group_layout = QFormLayout()
        
        self.consulta_id_input = QLineEdit(self)
        self.consulta_id_input.setPlaceholderText("ID único da Consulta (número inteiro)")
        consulta_group_layout.addRow("ID da Consulta:", self.consulta_id_input)

        self.consulta_motivo_input = QLineEdit(self)
        self.consulta_motivo_input.setPlaceholderText("Ex: Dor de cabeça, Check-up")
        consulta_group_layout.addRow("Motivo:", self.consulta_motivo_input)
        
        # Para data e hora, idealmente usaria QDateTimeEdit, mas para simplicidade, uma string
        self.consulta_datahora_input = QLineEdit(self)
        # Sugestão de formato para o usuário
        self.consulta_datahora_input.setPlaceholderText(f"Data e Hora (Ex: {datetime.now().strftime('%Y-%m-%dT%H:%M:%S')})")
        # Preenche com a data/hora atual para facilitar
        self.consulta_datahora_input.setText(datetime.now().isoformat(timespec='seconds'))
        consulta_group_layout.addRow("Data e Hora:", self.consulta_datahora_input)

        self.consulta_idmedico_input = QLineEdit(self)
        self.consulta_idmedico_input.setPlaceholderText("ID do Médico (número inteiro)")
        consulta_group_layout.addRow("ID do Médico:", self.consulta_idmedico_input)

        self.consulta_idpaciente_input = QLineEdit(self)
        self.consulta_idpaciente_input.setPlaceholderText("ID do Paciente (número inteiro)")
        consulta_group_layout.addRow("ID do Paciente:", self.consulta_idpaciente_input)

        self.consulta_status_input = QLineEdit(self)
        self.consulta_status_input.setPlaceholderText("Status (Agendada, Realizada, Cancelada)")
        self.consulta_status_input.setText("Agendada") # Default para nova consulta
        consulta_group_layout.addRow("Status:", self.consulta_status_input)
        
        self.btn_criar_consulta = QPushButton("Criar Consulta", self)
        self.btn_criar_consulta.clicked.connect(self.criar_consulta)
        consulta_group_layout.addRow(self.btn_criar_consulta)

        main_layout.addLayout(consulta_group_layout)
        main_layout.addSpacing(20) # Espaçamento entre as seções

        # --- Seção para Listar Consultas ---
        self.btn_listar_consultas = QPushButton("Listar Todas as Consultas", self)
        self.btn_listar_consultas.clicked.connect(self.listar_consultas)
        main_layout.addWidget(self.btn_listar_consultas)

        self.text_output = QTextEdit(self)
        self.text_output.setReadOnly(True)
        main_layout.addWidget(self.text_output)

        self.setLayout(main_layout)

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
        self.text_output.clear() # Limpa a saída anterior
        try:
            # Coleta e valida os dados dos campos da UI
            id_consulta = int(self.consulta_id_input.text())
            motivo = self.consulta_motivo_input.text()
            data_hora_str = self.consulta_datahora_input.text()
            id_medico = int(self.consulta_idmedico_input.text())
            id_paciente = int(self.consulta_idpaciente_input.text())
            status = self.consulta_status_input.text()

            # Validações básicas (mais validações seriam feitas pelos DTOs no backend)
            if not motivo or not data_hora_str or not status:
                raise ValueError("Todos os campos obrigatórios devem ser preenchidos.")
            if status not in ["Agendada", "Realizada", "Cancelada"]:
                raise ValueError("Status inválido. Escolha entre 'Agendada', 'Realizada' ou 'Cancelada'.")

            # Monta o payload JSON
            consulta_data = {
                "idConsulta": id_consulta,
                "dataHora": data_hora_str, # Enviamos como string, Pydantic vai converter
                "motivo": motivo,
                "idMedico": id_medico,
                "idPaciente": id_paciente,
                "status": status
            }

            headers = {"Content-Type": "application/json"}
            
            # Envia a requisição POST para o backend
            response = requests.post(f"{BASE_URL}/consultas/", json=consulta_data, headers=headers)
            response.raise_for_status() # Levanta um erro para códigos de status HTTP 4xx ou 5xx

            if response.status_code == 201:
                self._display_message("Sucesso", "Consulta criada com sucesso!")
                self.text_output.setText(f"Consulta Criada:\n{json.dumps(response.json(), indent=2)}")
            else:
                # Isso seria capturado por raise_for_status na maioria dos casos, mas para segurança
                self._display_message("Erro", f"Erro ao criar consulta: {response.text}", is_error=True)

        except ValueError as e:
            self._display_message("Erro de Entrada", f"Dados inválidos: {e}", is_error=True)
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            # Captura erros de requisição, incluindo HTTPError de raise_for_status()
            error_message = f"Ocorreu um erro na requisição: {e}"
            if response is not None:
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

    def listar_consultas(self):
        self.text_output.clear() # Limpa a saída anterior
        try:
            # Envia a requisição GET para o backend
            response = requests.get(f"{BASE_URL}/consultas/")
            response.raise_for_status()
            
            consultas = response.json()
            if consultas:
                formatted_consultas = "\n".join([json.dumps(c, indent=2) for c in consultas])
                self.text_output.setText(f"Consultas Encontradas:\n{formatted_consultas}")
                self._display_message("Sucesso", f"Foram encontradas {len(consultas)} consultas.")
            else:
                self.text_output.setText("Nenhuma consulta encontrada.")
                self._display_message("Informação", "Nenhuma consulta encontrada no sistema.")
                
        except requests.exceptions.ConnectionError:
            self._display_message("Erro de Conexão", "Não foi possível conectar ao servidor Flask. Verifique se o backend está rodando em " + BASE_URL, is_error=True)
        except requests.exceptions.RequestException as e:
            error_message = f"Ocorreu um erro na requisição: {e}"
            if response is not None:
                try:
                    error_json = response.json()
                    error_message += f"\nDetalhes do Backend:\n{json.dumps(error_json, indent=2)}"
                except json.JSONDecodeError:
                    error_message += f"\nResposta bruta do Backend: {response.text}"
            self._display_message("Erro de Requisição", error_message, is_error=True)
        except Exception as e:
            self._display_message("Erro Inesperado", f"Ocorreu um erro inesperado: {e}", is_error=True)

if __name__ == "__main__":
    app = QApplication(sys.argv) # Passa sys.argv para permitir argumentos de linha de comando
    main_app = MainApp()
    main_app.show()
    sys.exit(app.exec_()) # Garante uma saída limpa do aplicativo