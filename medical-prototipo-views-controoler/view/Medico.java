package view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
 
public class Medico {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nomeCompleto;
    private final SimpleStringProperty crm;
    private final SimpleStringProperty especialidade;
    private final SimpleStringProperty email;
    private final SimpleStringProperty telefone;

    @JsonCreator  
    public Medico(
             
            @JsonProperty("idMedico") int id,
            @JsonProperty("nomeCompleto") String nomeCompleto,
            @JsonProperty("crm") String crm,
            @JsonProperty("especialidade") String especialidade,
            @JsonProperty("email") String email,
            @JsonProperty("telefone") String telefone) {
        this.id = new SimpleIntegerProperty(id);
        this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
        this.crm = new SimpleStringProperty(crm);
        this.especialidade = new SimpleStringProperty(especialidade);
        this.email = new SimpleStringProperty(email);
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

    public String getCrm() {
        return crm.get();
    }

    public void setCrm(String crm) {
        this.crm.set(crm);
    }

    public SimpleStringProperty crmProperty() {
        return crm;
    }

    public String getEspecialidade() {
        return especialidade.get();
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade.set(especialidade);
    }

    public SimpleStringProperty especialidadeProperty() {
        return especialidade;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public SimpleStringProperty emailProperty() {
        return email;
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