package view;

import java.time.LocalDate;

public class PacienteDTO {

    private Long idPaciente; 
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private String cpf;
    private String endereco;
    private String telefone;

    public PacienteDTO() {
    }

     public PacienteDTO(String nomeCompleto, LocalDate dataNascimento, String cpf, String endereco, String telefone) {
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
    }

     public PacienteDTO(Long idPaciente, String nomeCompleto, LocalDate dataNascimento, String cpf, String endereco, String telefone) {
        this.idPaciente = idPaciente;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
    }

     public Long getIdPaciente() {
        return idPaciente;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

     public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
