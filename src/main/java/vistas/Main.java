package vistas;

import controladores.ControladorCargaPage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author carmen_gordo
 */
public class Main extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CargaPage.fxml"));
        
        Parent root = loader.load();
        
        //pasar el stage para usar el modal:
        ControladorCargaPage controlador = loader.getController();
        controlador.setStage(stage);
        Image icon = new Image(getClass().getResource("/assets/iconos_perfil/huevin.png").toExternalForm());
        stage.getIcons().add(icon);

        
        
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/Style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Boiled Egg");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
