package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class AppView extends Application {

    @Override
    public void start(Stage stage) {
        try {
            URL fxmlUrl = getClass().getResource("app.fxml");
            
            if (fxmlUrl == null) {
                System.err.println("Erro: O arquivo 'app.fxml' não foi encontrado. Verifique o caminho.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Minha aplicação JavaFX");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}