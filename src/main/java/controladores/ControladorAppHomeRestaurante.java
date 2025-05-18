package controladores;

import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author carmen_gordo
 */
public class ControladorAppHomeRestaurante implements Initializable {
    
    @FXML private Parent root;
    @FXML private Pane loginPage, registroPage;
    @FXML private Button btnCambiarIcono;
    @FXML private Label enlaceRegistro, enlaceLogin;
    
    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Cambio de pane para mostrar la pagina de Registro o la de Login
        loginPage.setVisible(false);
        registroPage.setVisible(true);

        enlaceRegistro.setOnMouseClicked(event -> {
            loginPage.setVisible(false);
            registroPage.setVisible(true);
        });

        enlaceLogin.setOnMouseClicked(event -> {
            registroPage.setVisible(false);
            loginPage.setVisible(true);
        });
    }
    
    
    // Para recoger el Stage del Main
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
}
