package controladores;

import conexion.ConexionBD;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import modelos.Usuario;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modelos.Alergeno;
import modelos.IconoPerfil;
import modelos.Ingrediente;
import modelos.Receta;
import utils.FuncionesRepetidas;
import static utils.FuncionesRepetidas.obtenerListaRecetas;
import utils.ObservableListas;

/**
 *
 * @author carmen_gordo
 */
public class ControladorHomeAppPage implements Initializable {
    
    @FXML private Parent root;
    @FXML private Label saludoLabel;
    @FXML private ToggleButton btnTema;
    @FXML private SVGPath iconoTema;
    @FXML private ImageView btnPerfil;
    @FXML private VBox vboxTipoFiltrar, vboxTipoReceta, vboxAlergenos, vboxDificultad, vboxValoraciones, vboxTipoCoccion;
    @FXML private AnchorPane HomePane,buscadorPane, infoCardPane;
    @FXML private Button btnBuscar;
    @FXML private HBox cajaBuscar;
    @FXML private TextField inputBuscar;
    @FXML public FlowPane flowRecetas;
     @FXML public ScrollPane filtrosScroll, busquedasPane;
        
    
    private Stage stage;
    public Connection conexion;
    public Statement st;
    private Usuario usuario;
    public String rutaSesionCache = "sesionCache.txt";
    public String rutaTemaClaro = "/style/Style.css";
    public String rutaTemaOscuro = "/style/StyleOscuro.css";
    IconoPerfil iconoUsuario = null;



        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Por defecto:
        buscadorPane.setVisible(false);
        infoCardPane.setVisible(false);


        try {
            conexion = FuncionesRepetidas.iniciarConexion();
            if (conexion != null) {
                st = conexion.createStatement();
                System.out.println("ControladorHomeAppPage bd");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("--noconexion a bd");
        }   
        
        // Leer y poner le tema de sesionCache
        Map<String, String> cache = FuncionesRepetidas.leerSesionCache();
        String temaGuardado = cache.getOrDefault("tema", "claro");

        if ("oscuro".equals(temaGuardado)) {
            btnTema.setSelected(true);
            aplicarTemaOscuro();
        } else {
            btnTema.setSelected(false);
            aplicarTemaClaro();
        }

        btnTema.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                aplicarTemaOscuro();
                FuncionesRepetidas.guardarSesionCache("tema", "oscuro");
            } else {
                aplicarTemaClaro();
                FuncionesRepetidas.guardarSesionCache("tema", "claro");
            }
        });
        
        
        //Filtros: 
        cargarFiltrosTipo(ObservableListas.listaFiltrar);
        cargarFiltros(ObservableListas.listaTiposRecetas, vboxTipoReceta);
        cargarFiltros(ObservableListas.listaAlergenos, vboxAlergenos);
        cargarFiltros(ObservableListas.listaDificultadRecetas, vboxDificultad);
        cargarFiltrosNumericos(ObservableListas.listaValoraciones, vboxValoraciones);
        cargarFiltros(ObservableListas.listaTiposCoccion, vboxTipoCoccion);

        
        cajaBuscar.setOnMouseClicked(e -> verBuscadorPane());
        inputBuscar.setOnMouseClicked(e -> verBuscadorPane());
        inputBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            actualizarCards();
        });
        
        
    }
    

    
    private void aplicarTemaClaro() {
        iconoTema.setContent("M480-360q50 0 85-35t35-85q0-50-35-85t-85-35q-50 0-85 35t-35 85q0 50 35 85t85 35Zm0 80q-83 0-141.5-58.5T280-480q0-83 58.5-141.5T480-680q83 0 141.5 58.5T680-480q0 83-58.5 141.5T480-280ZM80-440q-17 0-28.5-11.5T40-480q0-17 11.5-28.5T80-520h80q17 0 28.5 11.5T200-480q0 17-11.5 28.5T160-440H80Zm720 0q-17 0-28.5-11.5T760-480q0-17 11.5-28.5T800-520h80q17 0 28.5 11.5T920-480q0 17-11.5 28.5T880-440h-80ZM480-760q-17 0-28.5-11.5T440-800v-80q0-17 11.5-28.5T480-920q17 0 28.5 11.5T520-880v80q0 17-11.5 28.5T480-760Zm0 720q-17 0-28.5-11.5T440-80v-80q0-17 11.5-28.5T480-200q17 0 28.5 11.5T520-160v80q0 17-11.5 28.5T480-40ZM226-678l-43-42q-12-11-11.5-28t11.5-29q12-12 29-12t28 12l42 43q11 12 11 28t-11 28q-11 12-27.5 11.5T226-678Zm494 495-42-43q-11-12-11-28.5t11-27.5q11-12 27.5-11.5T734-282l43 42q12 11 11.5 28T777-183q-12 12-29 12t-28-12Zm-42-495q-12-11-11.5-27.5T678-734l42-43q11-12 28-11.5t29 11.5q12 12 12 29t-12 28l-43 42q-12 11-28 11t-28-11ZM183-183q-12-12-12-29t12-28l43-42q12-11 28.5-11t27.5 11q12 11 11.5 27.5T282-226l-42 43q-11 12-28 11.5T183-183Zm297-297Z");
        Scene scene = btnTema.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(rutaTemaClaro).toExternalForm());
        }
    }

    private void aplicarTemaOscuro() {
        iconoTema.setContent("M484-80q-84 0-157.5-32t-128-86.5Q144-253 112-326.5T80-484q0-128 72-232t193-146q22-8 41 5.5t18 36.5q-3 85 27 162t90 137q60 60 137 90t162 27q26-1 38.5 17.5T863-345q-44 120-147.5 192.5T484-80Zm0-80q88 0 163-44t118-121q-86-8-163-43.5T464-465q-61-61-97-138t-43-163q-77 43-120.5 118.5T160-484q0 135 94.5 229.5T484-160Zm-20-305Z");
        Scene scene = btnTema.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(rutaTemaOscuro).toExternalForm());
        }
    }

    
    public void inicializarUsuario(Usuario usu) {
        this.usuario = usu;
        System.err.println("Usuario: "+usu.getId_usuario()+", "+usu.getNombre_usuario()+", "+usu.getEmail_usuario()+", "+usu.getContraseña_usuario()+", nivel: "+usu.getNivel_usuario()+", puntos: "+usu.getPuntos_usuario()+", id icono:"+usu.getIcono_perfil_id());
        
        //Poner datos del usu en el menu top
        saludoLabel.setText("¡Hola "+usu.getNombre_usuario()+"!");
        String rutaIcono = FuncionesRepetidas.obtenerRutaIconoUsuario(usu.getIcono_perfil_id());

        InputStream is = getClass().getResourceAsStream(rutaIcono);
        if (is != null) {
            Image img = new Image(is);
            btnPerfil.setImage(img);
        } else {
            System.err.println("No se pudo cargar el icono desde: " + rutaIcono);
        }
    }
   
   
    // FILTROS:
    public void cargarFiltros(ObservableList<String> opciones, VBox destino) {
        for (String nombre : opciones) {
            CheckBox check = new CheckBox(nombre);
            check.setOnAction(e -> actualizarCards());
            destino.getChildren().add(check);
        }
    }

    public void cargarFiltrosNumericos(ObservableList<Integer> opciones, VBox destino) {
        for (Integer val : opciones) {
            CheckBox check = new CheckBox(String.valueOf(val));
            check.setOnAction(e ->  actualizarCards());
            destino.getChildren().add(check);
        }
    }
    
    private List<CheckBox> listaCheckTipo = new ArrayList<>();
    public void cargarFiltrosTipo(List<String> opciones) {
        for (String texto : opciones) {
            CheckBox cb = new CheckBox(texto);
            cb.setOnAction(e -> actualizarCards());
            listaCheckTipo.add(cb);
            vboxTipoFiltrar.getChildren().add(cb);
        }
    }
   
   // Cambiar a BuscadorPAne:
    public void verBuscadorPane() {
        HomePane.setVisible(false);
        buscadorPane.setVisible(true);
        infoCardPane.setVisible(false);
        actualizarCards();
    }
      
    // Para saber si es Receta o Ingre
    public String getTipoSeleccionado() {
        for (CheckBox cb : listaCheckTipo) {
            if (cb.isSelected()) {
                return cb.getText();
            }
        }
        return "";
    }
    
    public List<String> getChecksSeleccionados(VBox vbox) {
        List<String> seleccionados = new ArrayList<>();
        for (int i = 0; i < vbox.getChildren().size(); i++) {
            CheckBox cb = (CheckBox) vbox.getChildren().get(i);
            if (cb.isSelected()) {
                seleccionados.add(cb.getText());
            }
        }
        return seleccionados;
    }

    public boolean recetaNoTieneAlergenos(Receta receta, List<String> alergenosFiltro) {
        List<Alergeno> alergReceta = FuncionesRepetidas.obtenerAlergenosReceta(receta.getId_receta());
        for (Alergeno a : alergReceta) {
            if (alergenosFiltro.contains(a.getNombre_alergeno())) {
                return false;
            }
        }
        return true;
    }
    
    public void actualizarCards() {
        flowRecetas.getChildren().clear();

        String textoBuscar = inputBuscar.getText().toLowerCase();
        String tipoSeleccionado = getTipoSeleccionado(); // "Receta", "Ingrediente", o "" si ninguno
        List<String> alergenosSeleccionados = getChecksSeleccionados(vboxAlergenos);
        List<String> dificultadSeleccionados = getChecksSeleccionados(vboxDificultad);
        List<String> tiposRecetaSeleccionados = getChecksSeleccionados(vboxTipoReceta);
        List<String> tiposCoccionSeleccionados = getChecksSeleccionados(vboxTipoCoccion);

        boolean buscarRecetas = tipoSeleccionado.equals("Receta") || tipoSeleccionado.isEmpty();
        boolean buscarIngredientes = tipoSeleccionado.equals("Ingrediente") || tipoSeleccionado.isEmpty();

        // Filtrar recetas
        if (buscarRecetas) {
            for (Receta receta : FuncionesRepetidas.obtenerListaRecetas()) {
                boolean coincideTexto = receta.getNombre_receta().toLowerCase().contains(textoBuscar);
                boolean pasaAlergenos = recetaNoTieneAlergenos(receta, alergenosSeleccionados);
                boolean pasaDificultadReceta = dificultadSeleccionados.isEmpty() || dificultadSeleccionados.contains(receta.getDificultad_receta());
                boolean pasaTipoReceta = tiposRecetaSeleccionados.isEmpty() || tiposRecetaSeleccionados.contains(receta.getTipo_receta());
                boolean pasaTipoCoccion = tiposCoccionSeleccionados.isEmpty() || tiposCoccionSeleccionados.contains(receta.getTipo_coccion_receta());

                if (coincideTexto && pasaAlergenos && pasaDificultadReceta && pasaTipoReceta && pasaTipoCoccion) {
                    VBox card = FuncionesRepetidas.crearCardReceta(receta);
                    if (card != null) {

                        card.setOnMouseClicked(event -> {
                            buscadorPane.setVisible(false);
                            infoCardPane.setVisible(true);

                            System.out.println("ID: " + receta.getId_receta());
                            System.out.println("Tipo: Receta. nombre: "+receta.getNombre_receta());
                           
                        });

                        flowRecetas.getChildren().add(card);
                    }
                }
            }
        }

        // Filtrar ingredientes
        if (buscarIngredientes) {
            for (Ingrediente ing : FuncionesRepetidas.obtenerListaIngredientes()) {
                // Los ingredientes no deben filtrar por tipo_receta ni tipo_coccion_receta
                if (ing.getNombre_ingrediente().toLowerCase().contains(textoBuscar)) {
                    //VBox card = FuncionesRepetidas.crearCardIngrediente(ing);
                    //if (card != null) flowRecetas.getChildren().add(card);
                }
            }
        }
    }
    
    
    
    

    
    
    
    
    
    
    
    
   

    
    
   
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    
}
