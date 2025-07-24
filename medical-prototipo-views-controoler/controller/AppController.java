package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.Parent;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private Tab tabConsulta;
    @FXML
    private Tab tabPaciente;
    @FXML
    private Tab tabMedico;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Carrega paciente.fxml
            Parent pacienteContent = FXMLLoader.load(getClass().getResource("/view/paciente.fxml"));
            tabPaciente.setContent(pacienteContent);
            
            // Carrega medico.fxml
            Parent medicoContent = FXMLLoader.load(getClass().getResource("/view/medico.fxml"));
            tabMedico.setContent(medicoContent);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}