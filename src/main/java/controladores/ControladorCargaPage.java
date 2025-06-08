package controladores;

import conexion.ConexionBD;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelos.Restaurante;
import modelos.Usuario;
import utils.FuncionesRepetidas;
import static utils.FuncionesRepetidas.mostrarAlerta;

/**
 *
 * @author carmen_gordo
 */
public class ControladorCargaPage implements Initializable {
    
    @FXML private BorderPane rootCargaPane;
    private Stage stage;
    public Connection conexion;
    public Statement st;
    private boolean haySesion = false;
    private Usuario usuarioEnSesion = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {


        try {
            conexion = FuncionesRepetidas.iniciarConexion();
            if (conexion != null) {
                st = conexion.createStatement();
                System.out.println("Cargando elementos...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error en la conexión", "No se pudo establecer la conexión, por favor ponga se en contacto con los distribuidores de la app");
        }
        
        comprobarSessionCache();
        
        /*Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(3), event -> cambiarVista())
        );
        timeline.setCycleCount(1);
        timeline.play();*/
        
    }
    
    public void comprobarSessionCache(){

        Path sessionPath = Paths.get("sesionCache.txt");

        if (Files.exists(sessionPath)) {
            Map<String, String> cacheData = FuncionesRepetidas.leerSesionCache();

            if (cacheData.containsKey("UsuarioId")) {
                try {
                    int idUsuario = Integer.parseInt(cacheData.get("UsuarioId"));
                    Usuario usuario = FuncionesRepetidas.obtenerUsuarioPorId(idUsuario);
                    if (usuario != null) {
                        this.haySesion = true;
                        this.usuarioEnSesion = usuario;
                        abrirVentanaApp("/vistas/HomeAppPage.fxml", "Inicio", usuarioEnSesion);
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir ID de usuario: " + e.getMessage());
                }
            }

            abrirVentanaApp("/vistas/LoginPage.fxml", "Login", null);
        } else {
            abrirVentanaApp("/vistas/LoginPage.fxml", "Login", null);
        }
    }
    
    private void abrirVentanaApp(String rutaFXML, String titulo, Usuario usuario) {
        try {
            URL url = getClass().getResource(rutaFXML);
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            if (usuario != null) {
                ControladorHomeAppPage controlador = loader.getController();
                controlador.inicializarUsuario(usuario);
            }

            Stage nuevaStage = new Stage();
            establecerIconoStage(nuevaStage);
            nuevaStage.setScene(new Scene(root));
            nuevaStage.setTitle(titulo);
            nuevaStage.show();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                if (this.stage != null) {
                    this.stage.close();
                }
            }));
            timeline.setCycleCount(1);
            timeline.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private void establecerIconoStage(Stage stage) {
        Image icon = new Image(getClass().getResource("/assets/iconos_perfil/huevin.png").toExternalForm());
        stage.getIcons().add(icon);
    }
    
}
