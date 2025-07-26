package view;

public class MedicoDTO {

    private Long idMedico; 
    private String nomeCompleto;
    private String crm;
    private String telefone;
    private String email;
    private String especialidade;

    public MedicoDTO() {
    }

     public MedicoDTO(String nomeCompleto, String crm, String telefone, String email, String especialidade) {
        this.nomeCompleto = nomeCompleto;
        this.crm = crm;
        this.telefone = telefone;
        this.email = email;
        this.especialidade = especialidade;
    }

     public MedicoDTO(Long idMedico, String nomeCompleto, String crm, String telefone, String email, String especialidade) {
        this.idMedico = idMedico;
        this.nomeCompleto = nomeCompleto;
        this.crm = crm;
        this.telefone = telefone;
        this.email = email;
        this.especialidade = especialidade;
    }

     public Long getIdMedico() {
        return idMedico;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getCrm() {
        return crm;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    // Setters
    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}
