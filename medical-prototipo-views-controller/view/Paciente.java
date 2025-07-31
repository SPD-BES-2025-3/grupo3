package view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class Paciente {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nomeCompleto;
    private final SimpleStringProperty cpf;
    private final SimpleObjectProperty<LocalDate> dataNascimento;
    private final SimpleStringProperty endereco;
    private final SimpleStringProperty telefone;

    @JsonCreator
    public Paciente(
            @JsonProperty("idPaciente") int id,  
            @JsonProperty("nomeCompleto") String nomeCompleto,
            @JsonProperty("cpf") String cpf,
            @JsonProperty("dataNascimento") LocalDate dataNascimento,
            @JsonProperty("endereco") String endereco,
            @JsonProperty("telefone") String telefone) {
        this.id = new SimpleIntegerProperty(id);
        this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
        this.cpf = new SimpleStringProperty(cpf);
        this.dataNascimento = new SimpleObjectProperty<>(dataNascimento);
        this.endereco = new SimpleStringProperty(endereco);
        this.telefone = new SimpleStringProperty(telefone);
    }

 
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto.get();
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto.set(nomeCompleto);
    }

    public SimpleStringProperty nomeCompletoProperty() {
        return nomeCompleto;
    }

    public String getCpf() {
        return cpf.get();
    }

    public void setCpf(String cpf) {
        this.cpf.set(cpf);
    }

    public SimpleStringProperty cpfProperty() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento.get();
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento.set(dataNascimento);
    }

    public SimpleObjectProperty<LocalDate> dataNascimentoProperty() {
        return dataNascimento;
    }

    public String getEndereco() {
        return endereco.get();
    }

    public void setEndereco(String endereco) {
        this.endereco.set(endereco);
    }

    public SimpleStringProperty enderecoProperty() {
        return endereco;
    }

    public String getTelefone() {
        return telefone.get();
    }

    public void setTelefone(String telefone) {
        this.telefone.set(telefone);
    }

    public SimpleStringProperty telefoneProperty() {
        return telefone;
    }
}