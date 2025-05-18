package controladores;

import conexion.ConexionBD;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modelos.Restaurante;

/**
 *
 * @author carmen_gordo
 */
public class ControladorHomeAppRestaurantePage implements Initializable {
    
    @FXML private Parent root;
    private Stage stage;
    public Connection conexion;
    public Statement st;
    private Restaurante restau;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        try {
            conexion = ConexionBD.getConexion();  
            if (conexion != null) {
                st = conexion.createStatement();
                System.out.println("Conexion a la bd hecha en ControladorHomeAppPage");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("--noconexion a bd");
        }
        
        
        
    }
    
    public void inicializarRestaurante(Restaurante restaurante) {
        this.restau = restaurante;
        System.err.println("Restaurante: "+restau.getId_restaurante()+", "+restau.getNombre_restaurante()+", "+restau.email_restaurante+
                ", "+restau.contraseña_restaurante+", "+restau.tipo_restaurante+", "+restau.getCiudad_restaurante()+", "
                +restau.getUrl_restaurante());
    }
    
   
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
}
