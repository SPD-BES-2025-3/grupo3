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
import view.Paciente;
import view.PacienteDTO;
import view.ErrorResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PacienteController implements Initializable {

    @FXML
    private TableView<Paciente> tabela;

    @FXML
    private TableColumn<Paciente, Integer> idCol;
    @FXML
    private TableColumn<Paciente, String> nomeCol;
    @FXML
    private TableColumn<Paciente, String> cpfCol;
    @FXML
    private TableColumn<Paciente, LocalDate> dataNascimentoCol;
    @FXML
    private TableColumn<Paciente, String> enderecoCol;
    @FXML
    private TableColumn<Paciente, String> telefoneCol;

    @FXML
    private TextField idField;
    @FXML
    private TextField nomeCompletoField;
    @FXML
    private TextField cpfField;
    @FXML
    private DatePicker dataNascimentoPicker;
    @FXML
    private TextField enderecoField;
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

    private final String API_BASE_URL = "http://localhost:8083/api/pacientes";
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean isAdding = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        cpfCol.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        dataNascimentoCol.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        enderecoCol.setCellValueFactory(new PropertyValueFactory<>("endereco"));
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

        carregarPacientes();
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
            PacienteDTO pacienteDtoParaAPI;
            HttpRequest request;

            if (isAdding) {
                pacienteDtoParaAPI = new PacienteDTO(
                    nomeCompletoField.getText(),
                    dataNascimentoPicker.getValue(),
                    cpfField.getText(),
                    enderecoField.getText(),
                    telefoneField.getText()
                );
                String json = objectMapper.writeValueAsString(pacienteDtoParaAPI);
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            } else {
                Paciente selectedPaciente = tabela.getSelectionModel().getSelectedItem();
                if (selectedPaciente == null || selectedPaciente.getId() == 0) {
                    exibirAlerta(Alert.AlertType.ERROR, "Erro", "Selecione um paciente para atualizar ou o ID é inválido.");
                    return;
                }
                pacienteDtoParaAPI = new PacienteDTO(
                    Long.valueOf(selectedPaciente.getId()),
                    nomeCompletoField.getText(),
                    dataNascimentoPicker.getValue(),
                    cpfField.getText(),
                    enderecoField.getText(),
                    telefoneField.getText()
                );
                String json = objectMapper.writeValueAsString(pacienteDtoParaAPI);
                request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE_URL + "/" + selectedPaciente.getId()))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            }

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Paciente salvo com sucesso!");
                carregarPacientes();
                limparCampos();
                desabilitarCampos(true);
                desabilitarBotoes(false, true, true, true, true);
                isAdding = false;
            } else {
                String errorMessage = "Erro desconhecido ao salvar paciente.";
                if (response.body() != null && !response.body().isEmpty()) {
                    try {
                        ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
                        if (errorResponse.getErrors() != null && !errorResponse.getErrors().isEmpty()) {
                            // CORREÇÃO APLICADA AQUI: Quebrar mensagens de erro em novas linhas
                            errorMessage = "Erros de Validação:\n" +
                                           errorResponse.getErrors().entrySet().stream()
                                                   .map(entry -> {
                                                        return "- " + entry.getKey() + ": " + entry.getValue().replace("; ", "\n- ");
                                                   })
                                                   .collect(Collectors.joining("\n"));
                            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", errorMessage);
                            return;
                        } else if (errorResponse.getMessage() != null && !errorResponse.getMessage().isEmpty()) {
                            errorMessage = errorResponse.getMessage();
                        } else if (errorResponse.getError() != null && !errorResponse.getError().isEmpty()) {
                            errorMessage = "Erro: " + errorResponse.getError();
                        }
                    } catch (IOException eJson) {
                        errorMessage = "Erro ao salvar paciente. Resposta do servidor: " + response.body();
                        System.err.println("Erro ao parsear JSON de erro: " + eJson.getMessage());
                    }
                }
                exibirAlerta(Alert.AlertType.ERROR, "Erro", errorMessage);
            }

        } catch (IOException | InterruptedException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar à API: " + e.getMessage());
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Entrada", "ID inválido. Certifique-se de que é um número.");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao salvar o paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onAtualizar() {
        Paciente selectedPaciente = tabela.getSelectionModel().getSelectedItem();
        if (selectedPaciente != null) {
            desabilitarCampos(false);
            desabilitarBotoes(true, true, true, false, false);
            isAdding = false;
        } else {
            exibirAlerta(Alert.AlertType.WARNING, "Nenhum Paciente Selecionado", "Por favor, selecione um paciente para atualizar.");
        }
    }

    @FXML
    private void onDeletar() {
        Paciente selectedPaciente = tabela.getSelectionModel().getSelectedItem();
        if (selectedPaciente != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmação de Exclusão");
            confirmAlert.setHeaderText("Tem certeza que deseja excluir este paciente?");
            confirmAlert.setContentText("Paciente: " + selectedPaciente.getNomeCompleto());

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(API_BASE_URL + "/" + selectedPaciente.getId()))
                            .DELETE()
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 204) {
                        exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Paciente excluído com sucesso!");
                        carregarPacientes();
                        limparCampos();
                        desabilitarCampos(true);
                        desabilitarBotoes(false, true, true, true, true);
                    } else {
                        String errorMessage = "Erro desconhecido ao excluir paciente.";
                        if (response.body() != null && !response.body().isEmpty()) {
                            try {
                                ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
                                if (errorResponse.getMessage() != null && !errorResponse.getMessage().isEmpty()) {
                                    errorMessage = errorResponse.getMessage();
                                } else if (errorResponse.getError() != null && !errorResponse.getError().isEmpty()) {
                                    errorMessage = "Erro: " + errorResponse.getError();
                                }
                            } catch (IOException eJson) {
                                errorMessage = "Erro ao excluir paciente. Resposta do servidor: " + response.body();
                                System.err.println("Erro ao parsear JSON de erro na deleção: " + eJson.getMessage());
                            }
                        }
                        exibirAlerta(Alert.AlertType.ERROR, "Erro", errorMessage);
                    }
                } catch (IOException | InterruptedException e) {
                    exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar à API: " + e.getMessage());
                }
            }
        } else {
            exibirAlerta(Alert.AlertType.WARNING, "Nenhum Paciente Selecionado", "Por favor, selecione um paciente para deletar.");
        }
    }

    @FXML
    private void onCancelar() {
        limparCampos();
        desabilitarCampos(true);
        desabilitarBotoes(false, true, true, true, true);
        isAdding = false;
    }

    private void carregarPacientes() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Paciente[] pacientesArray = objectMapper.readValue(response.body(), Paciente[].class);
                ObservableList<Paciente> pacientes = FXCollections.observableArrayList(Arrays.asList(pacientesArray));
                tabela.setItems(pacientes);
            } else {
                String errorMessage = "Erro desconhecido ao carregar pacientes.";
                if (response.body() != null && !response.body().isEmpty()) {
                    try {
                        ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
                        if (errorResponse.getMessage() != null && !errorResponse.getMessage().isEmpty()) {
                            errorMessage = errorResponse.getMessage();
                        } else if (errorResponse.getError() != null && !errorResponse.getError().isEmpty()) {
                            errorMessage = "Erro: " + errorResponse.getError();
                        }
                    } catch (IOException eJson) {
                        errorMessage = "Erro ao carregar pacientes. Resposta do servidor: " + response.body();
                        System.err.println("Erro ao parsear JSON de erro no carregamento: " + eJson.getMessage());
                    }
                }
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Carregar", errorMessage);
            }
        } catch (IOException | InterruptedException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar à API: " + e.getMessage());
        }
    }

    private void preencherCampos(Paciente pac) {
        idField.setText(String.valueOf(pac.getId()));
        nomeCompletoField.setText(pac.getNomeCompleto());
        cpfField.setText(pac.getCpf());
        dataNascimentoPicker.setValue(pac.getDataNascimento());
        enderecoField.setText(pac.getEndereco());
        telefoneField.setText(pac.getTelefone());
    }

    private void limparCampos() {
        idField.clear();
        nomeCompletoField.clear();
        cpfField.clear();
        dataNascimentoPicker.setValue(null);
        enderecoField.clear();
        telefoneField.clear();
    }

    private void desabilitarCampos(boolean desabilitado) {
        nomeCompletoField.setDisable(desabilitado);
        cpfField.setDisable(desabilitado);
        dataNascimentoPicker.setDisable(desabilitado);
        enderecoField.setDisable(desabilitado);
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