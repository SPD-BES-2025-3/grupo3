package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import view.Medico;
import view.MedicoDTO; 

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class MedicoController implements Initializable {

    @FXML
    private TableView<Medico> tabela;

    @FXML
    private TableColumn<Medico, Integer> idCol;
    @FXML
    private TableColumn<Medico, String> nomeCol;
    @FXML
    private TableColumn<Medico, String> crmCol;
    @FXML
    private TableColumn<Medico, String> especialidadeCol;
    @FXML
    private TableColumn<Medico, String> emailCol;
    @FXML
    private TableColumn<Medico, String> telefoneCol;

    @FXML
    private TextField idField;
    @FXML
    private TextField nomeCompletoField;
    @FXML
    private TextField crmField;
    @FXML
    private TextField especialidadeField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telefoneField;

    @FXML
    private Button adicionarButton;
    @FXML
    private Button atualizarButton;
    @FXML
    private Button deletarButton;
    @FXML
    private Button cancelarButton;
    @FXML
    private Button salvarButton;

    private final String API_BASE_URL = "http://localhost:8083/api/medicos";
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean isAdding = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        crmCol.setCellValueFactory(new PropertyValueFactory<>("crm"));
        especialidadeCol.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefoneCol.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        tabela.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        preencherCampos(newValue);
                        desabilitarBotoes(false, false, false, false, true);
                    } else {
                        limparCampos();
                        desabilitarBotoes(false, true, true, false, true);
                    }
                });

        desabilitarCampos(true);
        desabilitarBotoes(false, true, true, true, true);

        carregarMedicos();
    }

    @FXML
    private void onAdicionar() {
        limparCampos();
        desabilitarCampos(false);
        desabilitarBotoes(true, true, true, false, false);
        isAdding = true;
    }

    @FXML
    private void onSalvar() {
        try {
            MedicoDTO medicoDtoParaAPI;  
            HttpRequest request;

            if (isAdding) {
                medicoDtoParaAPI = new MedicoDTO(
                    nomeCompletoField.getText(),
                    crmField.getText(),
                    telefoneField.getText(),
                    emailField.getText(),
                    especialidadeField.getText()
                );
                String json = objectMapper.writeValueAsString(medicoDtoParaAPI);
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            } else {
                // Para atualização, precisamos do ID do médico selecionado
                Medico selectedMedico = tabela.getSelectionModel().getSelectedItem();
                if (selectedMedico == null || selectedMedico.getId() == 0) {
                    exibirAlerta(Alert.AlertType.ERROR, "Erro", "Selecione um médico para atualizar ou o ID é inválido.");
                    return;
                }
                // Cria um MedicoDTO com o ID para a atualização (PUT)
                medicoDtoParaAPI = new MedicoDTO(
                    Long.valueOf(selectedMedico.getId()), // Converte o ID da view para Long
                    nomeCompletoField.getText(),
                    crmField.getText(),
                    telefoneField.getText(),
                    emailField.getText(),
                    especialidadeField.getText()
                );
                String json = objectMapper.writeValueAsString(medicoDtoParaAPI);
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE_URL + "/" + selectedMedico.getId()))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            }

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Médico salvo com sucesso!");
                carregarMedicos();
                limparCampos();
                desabilitarCampos(true);
                desabilitarBotoes(false, true, true, true, true);
                isAdding = false;
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar médico: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar à API: " + e.getMessage());
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Entrada", "ID inválido. Certifique-se de que é um número.");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao salvar o médico: " + e.getMessage());
        }
    }

    @FXML
    private void onAtualizar() {
        Medico selectedMedico = tabela.getSelectionModel().getSelectedItem();
        if (selectedMedico != null) {
            desabilitarCampos(false);
            desabilitarBotoes(true, true, true, false, false);
            isAdding = false;
        } else {
            exibirAlerta(Alert.AlertType.WARNING, "Nenhum Médico Selecionado", "Por favor, selecione um médico para atualizar.");
        }
    }

    @FXML
    private void onDeletar() {
        Medico selectedMedico = tabela.getSelectionModel().getSelectedItem();
        if (selectedMedico != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmação de Exclusão");
            confirmAlert.setHeaderText("Tem certeza que deseja excluir este médico?");
            confirmAlert.setContentText("Médico: " + selectedMedico.getNomeCompleto());

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(API_BASE_URL + "/" + selectedMedico.getId()))
                            .DELETE()
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 204) {
                        exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Médico excluído com sucesso!");
                        carregarMedicos();
                        limparCampos();
                        desabilitarCampos(true);
                        desabilitarBotoes(false, true, true, true, true);
                    } else {
                        exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir médico: " + response.body());
                    }
                } catch (IOException | InterruptedException e) {
                    exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar à API: " + e.getMessage());
                }
            }
        } else {
            exibirAlerta(Alert.AlertType.WARNING, "Nenhum Médico Selecionado", "Por favor, selecione um médico para deletar.");
        }
    }

    @FXML
    private void onCancelar() {
        limparCampos();
        desabilitarCampos(true);
        desabilitarBotoes(false, true, true, true, true);
        isAdding = false;
    }

    private void carregarMedicos() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Medico[] medicosArray = objectMapper.readValue(response.body(), Medico[].class);
                ObservableList<Medico> medicos = FXCollections.observableArrayList(Arrays.asList(medicosArray));
                tabela.setItems(medicos);
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Carregar", "Erro ao carregar médicos: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar à API: " + e.getMessage());
        }
    }

    
    private void preencherCampos(Medico med) {
        idField.setText(String.valueOf(med.getId()));
        nomeCompletoField.setText(med.getNomeCompleto());
        crmField.setText(med.getCrm());
        especialidadeField.setText(med.getEspecialidade());
        emailField.setText(med.getEmail());
        telefoneField.setText(med.getTelefone());
    }

    private void limparCampos() {
        idField.clear();
        nomeCompletoField.clear();
        crmField.clear();
        especialidadeField.clear();
        emailField.clear();
        telefoneField.clear();
    }

    private void desabilitarCampos(boolean desabilitado) {
        nomeCompletoField.setDisable(desabilitado);
        crmField.setDisable(desabilitado);
        especialidadeField.setDisable(desabilitado);
        emailField.setDisable(desabilitado);
        telefoneField.setDisable(desabilitado);
        idField.setDisable(true);
    }

    private void desabilitarBotoes(boolean adicionar, boolean atualizar, boolean deletar, boolean cancelar, boolean salvar) {
        adicionarButton.setDisable(adicionar);
        atualizarButton.setDisable(atualizar);
        deletarButton.setDisable(deletar);
        cancelarButton.setDisable(cancelar);
        salvarButton.setDisable(salvar);
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

   
}