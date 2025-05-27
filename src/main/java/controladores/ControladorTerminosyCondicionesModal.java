package controladores;

import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 *
 * @author carmen_gordo
 */
public class ControladorTerminosyCondicionesModal implements Initializable {
    
    @FXML private Button btnCerrarModal, btnAceptarTerminos;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCerrarModal.setOnAction(event -> {
            cerrarModal();
        });
        btnAceptarTerminos.setOnAction(event -> cerrarModal());
    }
    
    public void cerrarModal() {
        if (stage != null) {
            stage.close();
        }
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
}
