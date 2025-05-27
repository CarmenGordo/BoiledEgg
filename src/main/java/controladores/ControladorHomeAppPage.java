package controladores;

import com.github.sarxos.webcam.Webcam;
import conexion.ConexionBD;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.io.File;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import modelos.Alergeno;
import modelos.Favoritos;
import modelos.IconoPerfil;
import modelos.Ingrediente;
import modelos.Receta;
import modelos.RecetaIngrediente;
import modelos.Restaurante;
import modelos.RestauranteReceta;
import utils.FuncionesRepetidas;
import static utils.FuncionesRepetidas.obtenerListaRecetas;
import utils.ObservableListas;
import modelos.Valoracion;
import static utils.FuncionesRepetidas.actualizarValoracion;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javax.imageio.ImageIO;
import modelos.Codigo;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import modelos.UsuarioCodigo;

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
    @FXML private AnchorPane homePane, buscadorPane, infoCardPane, escanearPane, subirRecetaPane, perfilPane;
    @FXML private Button btnBuscar;
    @FXML private HBox btnHome, btnEscanear, btnSubirReceta, cajaBuscar ;
    @FXML private TextField inputBuscar;
    @FXML public FlowPane flowRecetas;
    @FXML public ScrollPane filtrosScroll, busquedasPane;
        
    // FXML de infoCardPane:
    @FXML private ImageView infoCardRecetaImagen, infoCardIngreImagen, infoCardRestauImagen;
    @FXML private Label infoCardRecetaNombre, infoCardRecetaDificultad, infoCardRecetaCocion, infoCardRecetaTiempo, infoCardRecetaValoracion, infoCardRecetaPasos, infoCardRecetaConsejos, infoCardIngreNombre, infoCardIngreTipo, infoCardRestauNombre, infoCardRestauTipo, infoCardRestauUrl, infoCardRestauCiudad, infoCardRestauDireccion;
    @FXML private HBox infoCardRecetaCajaTipo, infoCardRecetaCajaAlergenos, infoCardIngreCajaAlergenos, infoCardRecetaRestaurantes, infoCardRecetaValoracionCaja, infoCardRecetaTodasValoracionCaja, infoCardIngreRecetas, infoCardRestauRecetas;
    @FXML private VBox infoCardRecetaIngredientes, infoCardRecetaPane, infoCardIngredientePane, infoCardRestaurantePane;
    @FXML private Button btnAñadirFavReceta, btnAñadirValoracionReceta, btnAñadirFavIngre, btnAñadirFavRestau, btnAtrasInfoCardReceta,  btnAtrasInfoCardIngre, btnAtrasInfoCardRestau;
    @FXML private TextField inputAñadirValoracionReceta;
    @FXML private SVGPath svgAñadirFavReceta, svgAñadirFavIngre, svgAñadirFavRestau;

    
    // FXML de escanearPane:
    @FXML private HBox btnCamaraCodigoEscanearPane, btnSubirCodigoEscanearPane, todosCodigosInfoCodigo, infoCodigoEscanearPane, recomendarRecetasInfoCodigo, btnSubidaCodigoEscanearPane;
    @FXML private VBox codigoEscanearPane, inicioEscanearPane, camaraCodigoEscanearPane, subirCodigoEscanearPane, infoCardCodigoEscanearPane;
    @FXML private ImageView imgInfoCardCodigo, imgSubirCodigoEscanearPane;
    @FXML private Label nombreInfoCardCodigo, codigoInfoCardCodigo, tiendaInfoCardCodigo, marcaInfoCardCodigo, origenInfoCardCodigo;
    @FXML private ScrollPane scrollPaneInfoCardCodigo;
    @FXML private Button saberMasInfoCardCodigoEscanearPane;
    
    
    // FXML de subirRecetaPane:
    @FXML private ImageView imgSubirReceta;
    @FXML private Button btnImportarSubirReceta, btnSubirRecetaVal, btnCancelar;
    @FXML private TextField nombreSubirReceta, buscadorIngredientesSubirReceta, tiempoSubirReceta;
    @FXML private HBox listaIngredientesSubirReceta;
    @FXML private VBox ingredientesSeleccionadosSubirReceta;
    @FXML private Label lblNombreSubirReceta, lblTipoSubirReceta, lblDificultadSubirReceta, lblTipoCoccionSubirReceta, lblTiempoSubirReceta, lblPasosSubirReceta, etiqImgSubirReceta, etiqNombreSubirReceta, etiqTipoSubirReceta, etiqDificultadSubirReceta, etiqIngredientesSubirReceta, etiqAlergenosSubirReceta, etiqTipoCoccionSubirReceta, etiqTiempoSubirReceta, etiqPasosSubirReceta;
    @FXML private ComboBox tipoSubirReceta, dificultadSubirReceta, tipoCoccionSubirReceta;
    @FXML private ListView alergenosSubirReceta, alergenosSeleccionadosSubirReceta;
    @FXML private TextArea pasosSubirReceta;
            
    
    
    
            
    private Stage stage;
    public Connection conexion;
    public Statement st;
    private Usuario usuario;
    public String rutaSesionCache = "sesionCache.txt";
    public String rutaTemaClaro = "/style/Style.css";
    public String rutaTemaOscuro = "/style/StyleOscuro.css";
    IconoPerfil iconoUsuario = null;
    public Receta infoCardRecetaSelec;
    public Ingrediente infoCardIngredienteSelec;
    public Restaurante infoCardRestauranteSelec;
    public String ultimoCodigoEscaneado = null;
    public ObservableList<Ingrediente> ingredientesSeleccionadosList = FXCollections.observableArrayList();
    public ObservableList<Ingrediente> todosLosIngredientes;
    private String imagenBase64; // este es de SubirRecetaPane
    ImageView iconoOk = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("assets/iconos/icono_ok.png")));
    ImageView iconoError = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("assets/iconos/icono_error.png")));
    
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Por defecto:
        mostrarPane(homePane);
        infoCodigoEscanearPane.setVisible(false);
        camaraCodigoEscanearPane.setVisible(false);
        subirCodigoEscanearPane.setVisible(false);
        inicioEscanearPane.setVisible(false);
        subirRecetaPane.setVisible(false);
        perfilPane.setVisible(false);


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
        
        // Botones menu:ç
        btnEscanear.setOnMouseClicked(event -> {
            mostrarPane(escanearPane);
            inicioEscanearPane.setVisible(true);
            infoCodigoEscanearPane.setVisible(false);
        });
        btnHome.setOnMouseClicked(event -> mostrarPane(homePane));   
        
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
        
        //Botones CardInfo:
        inputAñadirValoracionReceta.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnAñadirValoracionReceta();
            }
        });
        
        
        svgAñadirFavReceta.getStyleClass().add("svgSoloBordes");
        svgAñadirFavIngre.getStyleClass().add("svgSoloBordes");
        svgAñadirFavRestau.getStyleClass().add("svgSoloBordes");

        btnAtrasInfoCardReceta.setOnMouseClicked(event -> {
            volverBuscador();
            actualizarCards();
        });
        btnAtrasInfoCardIngre.setOnMouseClicked(event -> {
            volverBuscador();
            actualizarCards();
        });
        btnAtrasInfoCardRestau.setOnMouseClicked(event -> {
            volverBuscador();
            actualizarCards();
        });

        FuncionesRepetidas.ponerHoverFavorito(btnAñadirFavReceta, svgAñadirFavReceta);
        FuncionesRepetidas.ponerHoverFavorito(btnAñadirFavIngre, svgAñadirFavIngre);
        FuncionesRepetidas.ponerHoverFavorito(btnAñadirFavRestau, svgAñadirFavRestau);
        
        
        // EscanearPane: 
        tipoSubirReceta.setItems(ObservableListas.listaTiposRecetas);
        dificultadSubirReceta.setItems(ObservableListas.listaDificultadRecetas);
        tipoCoccionSubirReceta.setItems(ObservableListas.listaTiposCoccion);
        
        nombreSubirReceta.textProperty().addListener((obs, oldVal, newVal) -> rellenarInfoSubirReceta());
        tipoSubirReceta.valueProperty().addListener((obs, oldVal, newVal) -> rellenarInfoSubirReceta());
        dificultadSubirReceta.valueProperty().addListener((obs, oldVal, newVal) -> rellenarInfoSubirReceta());
        tipoCoccionSubirReceta.valueProperty().addListener((obs, oldVal, newVal) -> rellenarInfoSubirReceta());
        tiempoSubirReceta.textProperty().addListener((obs, oldVal, newVal) -> rellenarInfoSubirReceta());
        pasosSubirReceta.textProperty().addListener((obs, oldVal, newVal) -> rellenarInfoSubirReceta());
        rellenarInfoSubirReceta();
    
        btnCamaraCodigoEscanearPane.setOnMouseClicked(event-> {
            
            inicioEscanearPane.setVisible(false);
            infoCodigoEscanearPane.setVisible(true);
            camaraCodigoEscanearPane.setVisible(true);
            subirCodigoEscanearPane.setVisible(false);
        });
        
        btnSubirCodigoEscanearPane.setOnMouseClicked(event-> {
            inicioEscanearPane.setVisible(false);
            infoCodigoEscanearPane.setVisible(true);
            camaraCodigoEscanearPane.setVisible(false);
            subirCodigoEscanearPane.setVisible(true);
        });
        
        
        btnSubidaCodigoEscanearPane.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona imagen con código de barras");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            File imagenArchivo = fileChooser.showOpenDialog(null);

            if (imagenArchivo != null) {
                Image fxImage = new Image(imagenArchivo.toURI().toString());
                imgSubirCodigoEscanearPane.setImage(fxImage);

                String codigo = leerCodigoBarrasDesdeImagen(imagenArchivo);
                if (codigo != null) {
                    ultimoCodigoEscaneado = codigo;
                    mostrarInfoCardCodigo(codigo);
                    infoCardCodigoEscanearPane.setVisible(true);
                    System.out.println("codigo:" + codigo);
                }
            }
        });
        
        btnSubirReceta.setOnMouseClicked(event -> {
            mostrarPane(subirRecetaPane);
        });
        
        // SubirRecetaPAne:
        configurarArrastrarImgSubirReceta();
        btnImportarSubirReceta.setOnAction(event -> importarImgSubirReceta());
        
        todosLosIngredientes = FuncionesRepetidas.obtenerListaIngredientes();
        cargarIngredientesAleatorios();
        
        buscadorIngredientesSubirReceta.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                if (ingredientesSeleccionadosList.isEmpty()) {
                    cargarIngredientesAleatorios();
                } else {
                    mostrarIngredientesSeleccionados();
                }
            } else {
                buscarIngredientes(newValue);
            }
        });
        
        btnCancelar.setOnAction(event -> limpiarSubirRecetaPane());
        //btnSubirRecetaVal
        
        // Tooltips
        iconoOk.setFitHeight(16);
        iconoOk.setFitWidth(16);
        iconoError.setFitHeight(16);
        iconoError.setFitWidth(16);
        ponerTooltips();
        
        // Validadores
        configurarValidacionesSubirReceta();
        
        
        btnPerfil.setOnMouseClicked(event -> { mostrarPane(perfilPane); });
     
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
        
        actualizarListaIngredientesEscaneados(usuario);
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
        mostrarPane(buscadorPane);
        actualizarCards();
    }
    
    public void mostrarPane(AnchorPane paneMostrar) {
        homePane.setVisible(false);
        buscadorPane.setVisible(false);
        infoCardPane.setVisible(false);
        escanearPane.setVisible(false);
        subirRecetaPane.setVisible(false);
        
        paneMostrar.setVisible(true);
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
        List<Alergeno> alergReceta = FuncionesRepetidas.obtenerRecetaAlergenos(receta.getId_receta());
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
        String tipoSeleccionado = getTipoSeleccionado();
        List<String> alergenosSeleccionados = getChecksSeleccionados(vboxAlergenos);
        List<String> dificultadSeleccionados = getChecksSeleccionados(vboxDificultad);
        List<String> tiposDietaSeleccionados = getChecksSeleccionados(vboxTipoReceta);
        List<String> tiposCoccionSeleccionados = getChecksSeleccionados(vboxTipoCoccion);
        List<String> valoracionesSeleccionadas = getChecksSeleccionados(vboxValoraciones);

        boolean buscarRecetas = tipoSeleccionado.equals("Receta") || tipoSeleccionado.isEmpty();
        boolean buscarIngredientes = tipoSeleccionado.equals("Ingrediente") || tipoSeleccionado.isEmpty();
        boolean buscarRestaurantes = tipoSeleccionado.equals("Restaurante") || tipoSeleccionado.isEmpty();

        if (buscarRecetas) {
            for (Receta receta : FuncionesRepetidas.obtenerListaRecetas()) {
                boolean coincideTexto = receta.getNombre_receta().toLowerCase().contains(textoBuscar);
                boolean pasaAlergenos = recetaNoTieneAlergenos(receta, alergenosSeleccionados);
                boolean pasaDificultad = dificultadSeleccionados.isEmpty() || dificultadSeleccionados.contains(receta.getDificultad_receta());
                boolean pasaTipoReceta = tiposDietaSeleccionados.isEmpty() || tiposDietaSeleccionados.contains(receta.getTipo_receta());
                boolean pasaTipoCoccion = tiposCoccionSeleccionados.isEmpty() || tiposCoccionSeleccionados.contains(receta.getTipo_coccion_receta());

                double mediaValoracion = FuncionesRepetidas.obtenerValoracionMedia(receta.getId_receta());
                boolean pasaValoracion = valoracionesSeleccionadas.isEmpty()
                        || valoracionesSeleccionadas.contains(String.valueOf(Math.round(mediaValoracion)));

                if (coincideTexto && pasaAlergenos && pasaDificultad && pasaTipoReceta && pasaTipoCoccion && pasaValoracion) {
                    VBox card = FuncionesRepetidas.crearCardReceta(usuario, receta);
                    if (card != null) {
                        card.setOnMouseClicked(event -> {
                            mostrarPane(infoCardPane);
                            infoCardRecetaPane.setVisible(true);
                            infoCardIngredientePane.setVisible(false);
                            infoCardRestaurantePane.setVisible(false);

                            infoCardRecetaSelec = receta;
                            mostrarInfoCardReceta();
                        });
                        flowRecetas.getChildren().add(card);
                    }
                }
            }
        }


        if (buscarIngredientes) {

            for (Ingrediente ingre : FuncionesRepetidas.obtenerListaIngredientes()) {
                boolean coincideTexto = ingre.getNombre_ingrediente().toLowerCase().contains(textoBuscar);
                boolean noValoracion = valoracionesSeleccionadas.isEmpty();
                boolean noTipoReceta = tiposDietaSeleccionados.isEmpty();
                boolean noDificultad = dificultadSeleccionados.isEmpty();
                boolean noTipoCoccion = tiposCoccionSeleccionados.isEmpty();
                boolean pasaAlergenos = true;

                if (!alergenosSeleccionados.isEmpty()) {
                    for (String alergNombre : alergenosSeleccionados) {
                        String tipoAlergeno = ingre.getTipo_alergeno_ingrediente();

                        if (tipoAlergeno != null && !tipoAlergeno.isEmpty() && tipoAlergeno.equalsIgnoreCase(alergNombre)) {
                            pasaAlergenos = false;
                            break;
                        }
                    }
                }

                if (alergenosSeleccionados.isEmpty()) {
                    pasaAlergenos = true;
                }

                if (coincideTexto && pasaAlergenos && noTipoReceta && noValoracion && noDificultad && noTipoCoccion) {
                    VBox card = FuncionesRepetidas.crearCardIngrediente(usuario, ingre);
                    if (card != null) {
                        card.setOnMouseClicked(event -> {
                            mostrarPane(infoCardPane);
                            infoCardIngredientePane.setVisible(true);
                            infoCardRecetaPane.setVisible(false);
                            infoCardRestaurantePane.setVisible(false);

                            infoCardIngredienteSelec = ingre;
                            mostrarInfoCardIngrediente();
                        });
                        flowRecetas.getChildren().add(card);
                    }
                }
            }
        }

        
        if (buscarRestaurantes) {
            for (Restaurante restaurante : FuncionesRepetidas.obtenerListaRestaurantes()) {
                boolean noTipoCoccion = tiposCoccionSeleccionados.isEmpty();
                boolean noDificultad = dificultadSeleccionados.isEmpty();
                boolean noAlergenos = alergenosSeleccionados.isEmpty();
                boolean noDieta = tiposDietaSeleccionados.isEmpty();
                boolean coincideTexto = restaurante.getNombre_restaurante().toLowerCase().contains(textoBuscar);
                double mediaValoracion = FuncionesRepetidas.obtenerValoracionMediaRestaurante(restaurante.getId_restaurante());
                boolean pasaValoracion = valoracionesSeleccionadas.isEmpty()
                        || valoracionesSeleccionadas.contains(String.valueOf(Math.round(mediaValoracion)));

                if (coincideTexto && pasaValoracion && noDieta && noTipoCoccion && noDificultad && noAlergenos) {
                    VBox card = FuncionesRepetidas.crearCardRestaurante(usuario, restaurante);
                    if (card != null) {
                        card.setOnMouseClicked(event -> {
                            mostrarPane(infoCardPane);
                            infoCardRestaurantePane.setVisible(true);
                            infoCardRecetaPane.setVisible(false);
                            infoCardIngredientePane.setVisible(false);

                            infoCardRestauranteSelec = restaurante;
                            mostrarInfoCardRestaurante();
                        });
                        flowRecetas.getChildren().add(card);
                    }
                }
            }
        }
    }
    
    
    // INFO CARD :
    public void mostrarInfoCardReceta() {

        if (infoCardRecetaSelec == null) return;

        String rutaImagen = infoCardRecetaSelec.getImagen_receta();
        infoCardRecetaImagen.setImage(FuncionesRepetidas.cargarImagenReceta(rutaImagen));

        infoCardRecetaNombre.setText(infoCardRecetaSelec.getNombre_receta());

        infoCardRecetaCajaTipo.getChildren().clear();
        ImageView imgTipo = FuncionesRepetidas.crearIconoDesdeRuta(infoCardRecetaSelec.getTipo_receta());
        if (imgTipo != null) {
            infoCardRecetaCajaTipo.getChildren().add(imgTipo);
        }

        infoCardRecetaDificultad.setText(infoCardRecetaSelec.getDificultad_receta());
        infoCardRecetaCocion.setText(infoCardRecetaSelec.getTipo_coccion_receta());
        infoCardRecetaTiempo.setText(Integer.toString(infoCardRecetaSelec.getTiempo_preparacion_receta())+" min.");


        infoCardRecetaCajaAlergenos.getChildren().clear();
        ObservableList<Alergeno> alergenos = FuncionesRepetidas.obtenerRecetaAlergenos(infoCardRecetaSelec.getId_receta());
        for (Alergeno alergeno : alergenos) {
            ImageView icono = FuncionesRepetidas.crearIconoDesdeRuta(alergeno.getImagen_alergeno());
            icono.setFitHeight(40);
            icono.setFitWidth(40);
            if (icono != null) {
                infoCardRecetaCajaAlergenos.getChildren().add(icono);
            }
        }

        double valoracion = FuncionesRepetidas.obtenerValoracionMedia(infoCardRecetaSelec.getId_receta());
        infoCardRecetaValoracion.setText(String.format("%.1f ★", valoracion));

        infoCardRecetaIngredientes.getChildren().clear();
        Map<Integer, String> cantidadesPorIngrediente = new HashMap<>();
                ObservableList<Ingrediente> ingredientes = FuncionesRepetidas.obtenerRecetaIngredientes(
            infoCardRecetaSelec.getId_receta(), cantidadesPorIngrediente
        );

        for (Ingrediente ingrediente : ingredientes) {
            String cantidad = cantidadesPorIngrediente.get(ingrediente.getId_ingrediente());

            HBox hboxIngrediente = new HBox(5);
            hboxIngrediente.setAlignment(Pos.CENTER_LEFT);
            
            ImageView imgIng = FuncionesRepetidas.crearIconoDesdeRuta(ingrediente.getImagen_ingrediente());
            if (imgIng != null) {
                imgIng.setFitHeight(40);
                imgIng.setFitWidth(40);
                hboxIngrediente.getChildren().add(imgIng);
            }

            Label label = new Label((cantidad != null ? cantidad + " " : "") + ingrediente.getNombre_ingrediente());
            hboxIngrediente.getChildren().add(label);

            infoCardRecetaIngredientes.getChildren().add(hboxIngrediente);
        }
        
        infoCardRecetaRestaurantes.getChildren().clear();
        
        ObservableList<RestauranteReceta> relaciones = FuncionesRepetidas.obtenerRestauranteReceta();
        ObservableList<Restaurante> todosRestaurantes = FuncionesRepetidas.obtenerListaRestaurantes();

        boolean encontrado = false;

        for (RestauranteReceta relacion : relaciones) {
            if (relacion.getId_receta() == infoCardRecetaSelec.getId_receta()) {
                Restaurante restau = todosRestaurantes.stream()
                    .filter(r -> r.getId_restaurante() == relacion.getId_restaurante())
                    .findFirst()
                    .orElse(null);

                if (restau != null) {
                    VBox card = FuncionesRepetidas.crearCardRestaurante(usuario, restau);
                    if (card != null) {
                        infoCardRecetaRestaurantes.getChildren().add(card);
                        encontrado = true;
                        card.setOnMouseClicked(event -> {
                            mostrarPane(infoCardPane);
                            infoCardIngredientePane.setVisible(false);
                            infoCardRecetaPane.setVisible(false);
                            infoCardRestaurantePane.setVisible(true);
                            
                            infoCardRestauranteSelec = restau; 
                            mostrarInfoCardRestaurante(); 
                        });
                    }
                }
            }
        }

        if (!encontrado) {
            Label noRestaurantes = new Label("No hay restaurantes asociados");
            infoCardRecetaRestaurantes.setAlignment(Pos.CENTER);
            infoCardRecetaRestaurantes.getChildren().add(noRestaurantes);
        }
        
        boolean valEcontrado = false;
        Valoracion valoracionUsuario = FuncionesRepetidas.obtenerValoracionUsuarioEnObjeto(
            usuario.getId_usuario(),
            infoCardRecetaSelec.getId_receta(),
            Valoracion.TipoObjeto.RECETA
        );

        FuncionesRepetidas.crearBotonesValoracion(infoCardRecetaValoracionCaja);
        if (valoracionUsuario != null) {
            FuncionesRepetidas.puntuacionSeleccionada = valoracionUsuario.getPuntuacion_valoracion();
            FuncionesRepetidas.actualizarEstrellas(infoCardRecetaValoracionCaja, valoracionUsuario.getPuntuacion_valoracion());

        } else {
            FuncionesRepetidas.puntuacionSeleccionada = 0;
            inputAñadirValoracionReceta.clear();
        }
        
        ObservableList<Valoracion> listaValoraciones = FuncionesRepetidas.obtenerListaValoraciones(
            Valoracion.TipoObjeto.RECETA.getValor(),
            infoCardRecetaSelec.getId_receta()
        );

        infoCardRecetaTodasValoracionCaja.getChildren().clear();
        for (Valoracion val : listaValoraciones) {
            valEcontrado = true;
            
            infoCardRecetaTodasValoracionCaja.getChildren().add(FuncionesRepetidas.crearCardValoraciones(val));
        } 
        
        if (!valEcontrado) {
            Label noHay = new Label("Esta receta aún no tiene valorariones");
            infoCardRecetaTodasValoracionCaja.setAlignment(Pos.CENTER);
            infoCardRecetaTodasValoracionCaja.getChildren().add(noHay);
        }
        
        //fav
        FuncionesRepetidas.actualizarEstadoFavoritos(usuario, infoCardRecetaSelec, Favoritos.TipoObjeto.RECETA, svgAñadirFavReceta);
        btnAñadirFavReceta.setOnMouseClicked(event -> {
            boolean esFavorito = FuncionesRepetidas.esFavorito(usuario.getId_usuario(), infoCardRecetaSelec.getId_receta(), Favoritos.TipoObjeto.RECETA);

            if (esFavorito) {
                if (FuncionesRepetidas.eliminarFavorito(usuario.getId_usuario(),infoCardRecetaSelec.getId_receta(), Favoritos.TipoObjeto.RECETA)) {
                    svgAñadirFavReceta.getStyleClass().remove("fillFav");
                }
            } else {
                
                if (FuncionesRepetidas.insertarFavorito(new Favoritos(0, Favoritos.TipoObjeto.RECETA, infoCardRecetaSelec.getId_receta(), usuario.getId_usuario(), new java.util.Date()))) {
                    svgAñadirFavReceta.getStyleClass().remove("svgSinBordes");
                    svgAñadirFavReceta.getStyleClass().add("fillFav");
                }
            }
        });
    }
    
    @FXML public void btnAñadirValoracionReceta() {
       
        String comentario = inputAñadirValoracionReceta.getText();
        int puntuacion = FuncionesRepetidas.puntuacionSeleccionada;

        if (puntuacion == 0) {
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Selecciona una puntuación, para mandar el comentario");
            return;
        }

        Valoracion val = new Valoracion();
        val.setTipo_objeto(Valoracion.TipoObjeto.RECETA);
        val.setId_objeto(infoCardRecetaSelec.getId_receta());
        val.setId_usuario(usuario.getId_usuario());
        val.setPuntuacion_valoracion(puntuacion);
        val.setComentario_valoracion(comentario);
        val.setFecha_valoracion(new Date());

        boolean existe = (FuncionesRepetidas.obtenerValoracionUsuarioEnObjeto(usuario.getId_usuario(), infoCardRecetaSelec.getId_receta(), Valoracion.TipoObjeto.RECETA) != null);
        boolean resultado;

        if (existe) {
            resultado = FuncionesRepetidas.actualizarValoracion(infoCardRecetaValoracionCaja, val);

        } else {
            resultado = FuncionesRepetidas.insertarValoracion(val);
        }

        if (resultado) {
            mostrarInfoCardReceta();
        } else {
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la valoración.");
        }
        inputAñadirValoracionReceta.clear();
    }

    
    public void mostrarInfoCardIngrediente() {
        if (infoCardIngredienteSelec == null) return;

        String rutaImagen = infoCardIngredienteSelec.getImagen_ingrediente();
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            infoCardIngreImagen.setImage(new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png")));
        } else {
            URL urlImagenReceta = getClass().getResource(rutaImagen);
            if (urlImagenReceta == null) {
                infoCardIngreImagen.setImage(new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png")));

            } else {
                infoCardIngreImagen.setImage(new Image(urlImagenReceta.toExternalForm()));
            }
        }
   
        infoCardIngreNombre.setText(infoCardIngredienteSelec.getNombre_ingrediente());
        infoCardIngreTipo.setText(infoCardIngredienteSelec.getTipo_ingrediente());

        infoCardIngreCajaAlergenos.getChildren().clear();
        ObservableList<Alergeno> alergenos = FuncionesRepetidas.obtenerListaAlergenos();
        boolean encontrado = false;

        String tipoAlergeno = infoCardIngredienteSelec.getTipo_alergeno_ingrediente();

        if (tipoAlergeno != null && !tipoAlergeno.isEmpty()) {
            for (Alergeno alergeno : alergenos) {
                if (alergeno.getNombre_alergeno().equalsIgnoreCase(tipoAlergeno)) {
                    String rutaIcono = alergeno.getImagen_alergeno();
                    URL urlAlergeno = FuncionesRepetidas.class.getResource(rutaIcono);
                    if (urlAlergeno != null) {
                        ImageView alergenoIcono = new ImageView(new Image(urlAlergeno.toExternalForm()));
                        alergenoIcono.setFitWidth(40);
                        alergenoIcono.setFitHeight(40);
                        infoCardIngreCajaAlergenos.getChildren().add(alergenoIcono);
                        encontrado = true;
                        break;
                    }
                }
            }
        }

        if (!encontrado) {
            Label noTiene = new Label("-");
            infoCardIngreCajaAlergenos.getChildren().add(noTiene);
        }
        
        infoCardIngreRecetas.getChildren().clear();

        ObservableList<Receta> recetas = FuncionesRepetidas.obtenerListaRecetas();
        int idIngrediente = infoCardIngredienteSelec.getId_ingrediente();
        Map<Integer, String> cantidadesPorIngrediente = new HashMap<>();

        for (Receta receta : recetas) {
            ObservableList<Ingrediente> ingredientes = FuncionesRepetidas.obtenerRecetaIngredientes(receta.getId_receta(), cantidadesPorIngrediente);

            for (Ingrediente ingrediente : ingredientes) {
                if (ingrediente.getId_ingrediente() == idIngrediente) {
                    VBox card = FuncionesRepetidas.crearCardReceta(usuario, receta);
                    if (card != null) {
                        card.setOnMouseClicked(event -> {
                            mostrarPane(infoCardPane);
                            infoCardRecetaPane.setVisible(true);
                            infoCardIngredientePane.setVisible(false);
                            infoCardRestaurantePane.setVisible(false);

                            infoCardRecetaSelec = receta;
                            mostrarInfoCardReceta(); 
                  
                        });

                        infoCardIngreRecetas.getChildren().add(card);
                    }
                    break;
                }
            }
        }
        
        //fav
        FuncionesRepetidas.actualizarEstadoFavoritos(usuario, infoCardIngredienteSelec, Favoritos.TipoObjeto.INGREDIENTE, svgAñadirFavIngre);
        btnAñadirFavIngre.setOnMouseClicked(event -> {
            boolean esFavorito = FuncionesRepetidas.esFavorito(usuario.getId_usuario(), infoCardIngredienteSelec.getId_ingrediente(), Favoritos.TipoObjeto.INGREDIENTE);

            if (esFavorito) {
                if (FuncionesRepetidas.eliminarFavorito(usuario.getId_usuario(),infoCardIngredienteSelec.getId_ingrediente(), Favoritos.TipoObjeto.INGREDIENTE)) {
                    svgAñadirFavIngre.getStyleClass().remove("fillFav");
                }
            } else {
                
                if (FuncionesRepetidas.insertarFavorito(new Favoritos(0, Favoritos.TipoObjeto.INGREDIENTE, infoCardIngredienteSelec.getId_ingrediente(), usuario.getId_usuario(), new java.util.Date()))) {
                    svgAñadirFavIngre.getStyleClass().remove("svgSinBordes");
                    svgAñadirFavIngre.getStyleClass().add("fillFav");
                }
            }
        });
    }
    
    public void mostrarInfoCardRestaurante() {
        if (infoCardRestauranteSelec == null) return;

        String rutaImagen = infoCardRestauranteSelec.getImagen_restaurante();
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            infoCardRestauImagen.setImage(new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png")));
        } else {
            URL urlImagenReceta = getClass().getResource(rutaImagen);
            if (urlImagenReceta == null) {
                infoCardRestauImagen.setImage(new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png")));

            } else {
                infoCardRestauImagen.setImage(new Image(urlImagenReceta.toExternalForm()));
            }
        }
   
        infoCardRestauNombre.setText(infoCardRestauranteSelec.getNombre_restaurante());
        infoCardRestauTipo.setText(infoCardRestauranteSelec.getTipo_restaurante());
        infoCardRestauUrl.setText(infoCardRestauranteSelec.getUrl_restaurante() == null || infoCardRestauranteSelec.getUrl_restaurante().isEmpty() ? "-" : infoCardRestauranteSelec.getUrl_restaurante());
        infoCardRestauCiudad.setText(infoCardRestauranteSelec.getCiudad_restaurante() == null || infoCardRestauranteSelec.getCiudad_restaurante().isEmpty() ? "-" : infoCardRestauranteSelec.getCiudad_restaurante());
        infoCardRestauDireccion.setText(infoCardRestauranteSelec.getDireccion_restaurante() == null || infoCardRestauranteSelec.getDireccion_restaurante().isEmpty() ? "-" : infoCardRestauranteSelec.getDireccion_restaurante());
        
 
        infoCardRestauRecetas.getChildren().clear();
        
        ObservableList<RestauranteReceta> relaciones = FuncionesRepetidas.obtenerRestauranteReceta();
        ObservableList<Receta> recetas = FuncionesRepetidas.obtenerListaRecetas();
        boolean encontrado = false;

        for (RestauranteReceta relacion : relaciones) {
            if (relacion.getId_restaurante()== infoCardRestauranteSelec.getId_restaurante()) {
                Receta receta = recetas.stream()
                    .filter(r -> r.getId_receta() == relacion.getId_receta())
                    .findFirst()
                    .orElse(null);

                if (receta != null) {
                    VBox card = FuncionesRepetidas.crearCardReceta(usuario, receta);
                    if (card != null) {
                        infoCardRestauRecetas.getChildren().add(card);
                        encontrado = true;

                        card.setOnMouseClicked(event -> {
                            mostrarPane(infoCardPane);
                            infoCardIngredientePane.setVisible(false);
                            infoCardRecetaPane.setVisible(true);
                            infoCardRestaurantePane.setVisible(false);

                            infoCardRecetaSelec = receta;
                            mostrarInfoCardReceta();
                        });
                    }
                }
            }
        }

        if (!encontrado) {
            Label noRecetas = new Label("Este restaurante aún no ha incluido su carta");
            infoCardRestauRecetas.setAlignment(Pos.CENTER);
            infoCardRestauRecetas.getChildren().add(noRecetas);
        }
        
        //fav
        FuncionesRepetidas.actualizarEstadoFavoritos(usuario, infoCardRestauranteSelec, Favoritos.TipoObjeto.RESTAURANTE, svgAñadirFavRestau);
        btnAñadirFavRestau.setOnMouseClicked(event -> {
            boolean esFavorito = FuncionesRepetidas.esFavorito(usuario.getId_usuario(), infoCardRestauranteSelec.getId_restaurante(), Favoritos.TipoObjeto.RESTAURANTE);

            if (esFavorito) {
                if (FuncionesRepetidas.eliminarFavorito(usuario.getId_usuario(),infoCardRestauranteSelec.getId_restaurante(), Favoritos.TipoObjeto.RESTAURANTE)) {
                    svgAñadirFavRestau.getStyleClass().remove("fillFav");
                }
            } else {
                
                if (FuncionesRepetidas.insertarFavorito(new Favoritos(0, Favoritos.TipoObjeto.RESTAURANTE, infoCardRestauranteSelec.getId_restaurante(), usuario.getId_usuario(), new java.util.Date()))) {
                    svgAñadirFavRestau.getStyleClass().remove("svgSinBordes");
                    svgAñadirFavRestau.getStyleClass().add("fillFav");
                }
            }
        });
    }
    
    public void volverBuscador(){
        mostrarPane(buscadorPane);
        infoCardIngredientePane.setVisible(false);
        infoCardRecetaPane.setVisible(false);
        infoCardRestaurantePane.setVisible(false);
    }
    
    
    // ESCANEAR PANE:
    public void actualizarListaIngredientesEscaneados(Usuario usuario) {
        todosCodigosInfoCodigo.getChildren().clear();
        recomendarRecetasInfoCodigo.getChildren().clear();

        ObservableList<UsuarioCodigo> listaUsuarioCodigo = FuncionesRepetidas.obtenerListaUsuarioCodigo(usuario);

        if (listaUsuarioCodigo.isEmpty()) {
            Label noEscaneados = new Label("Aún no has escaneado ningún código");
            todosCodigosInfoCodigo.getChildren().add(noEscaneados);
            todosCodigosInfoCodigo.setAlignment(Pos.CENTER); 
            
            mostrarRecetasAleatorias(usuario);
        } else {
            for (UsuarioCodigo uc : listaUsuarioCodigo) {

                ObservableList<Codigo> listaCodigos = FuncionesRepetidas.obtenerCodigoDeImagen(ultimoCodigoEscaneado);
                if (!listaCodigos.isEmpty()) {
                    Codigo codigo = listaCodigos.get(0);
                    ObservableList<Ingrediente> listaIngredientes = FuncionesRepetidas.obtenerListaIngredientes();

                    for (Ingrediente ing : listaIngredientes) {
                        if (ing.getId_ingrediente() == codigo.getId_ingrediente()) {
                            VBox card = FuncionesRepetidas.crearCardIngrediente(usuario, ing);
                            todosCodigosInfoCodigo.getChildren().add(card);
                            break;
                        }
                    }
                }
            }
            
            mostrarRecetasConIngredientesEscaneados(usuario, listaUsuarioCodigo);
        }
    }
    
    public void mostrarRecetasAleatorias(Usuario usuario) {
        ObservableList<Receta> todasLasRecetas = FuncionesRepetidas.obtenerListaRecetas();

        if (!todasLasRecetas.isEmpty()) {
            List<Receta> listaRecetas = new ArrayList<>(todasLasRecetas);

            int contador = 0;
            while (contador < 3 && contador < listaRecetas.size()) {

                int indiceAleatorio = (int) (Math.random() * listaRecetas.size());
                Receta receta = listaRecetas.get(indiceAleatorio);

                VBox card = FuncionesRepetidas.crearCardReceta(usuario, receta);
                recomendarRecetasInfoCodigo.getChildren().add(card);

                contador++;
            }
        }
    }
    
    public void mostrarRecetasConIngredientesEscaneados(Usuario usuario, ObservableList<UsuarioCodigo> listaUsuarioCodigo) {
        ObservableList<Receta> todasLasRecetas = FuncionesRepetidas.obtenerListaRecetas();
        ObservableList<Receta> recetasConIngredientes = FXCollections.observableArrayList();

        for (Receta receta : todasLasRecetas) {
            Map<Integer, String> cantidades = new HashMap<>();
            ObservableList<Ingrediente> ingredientesReceta = FuncionesRepetidas.obtenerRecetaIngredientes(receta.getId_receta(), cantidades);

            boolean contieneIngredienteEscaneado = false;

            for (UsuarioCodigo uc : listaUsuarioCodigo) {
                ObservableList<Codigo> codigos = FuncionesRepetidas.obtenerCodigoDeImagen(ultimoCodigoEscaneado);
                if (!codigos.isEmpty()) {
                    int idIngredienteEscaneado = codigos.get(0).getId_ingrediente();

                    for (Ingrediente ing : ingredientesReceta) {
                        if (ing.getId_ingrediente() == idIngredienteEscaneado) {
                            contieneIngredienteEscaneado = true;
                            break;
                        }
                    }
                }
                if (contieneIngredienteEscaneado) break;
            }

            if (contieneIngredienteEscaneado) {
                recetasConIngredientes.add(receta);
            }
        }

        int contador = 0;
        for (Receta receta : recetasConIngredientes) {
            if (contador < 3) {
                VBox card = FuncionesRepetidas.crearCardReceta(usuario, receta);
                recomendarRecetasInfoCodigo.getChildren().add(card);
                contador++;
            } else {
                break;
            }
        }
    }
    
    public String leerCodigoBarrasDesdeImagen(File imagenArchivo) {
        try {
            BufferedImage bufferedImage = ImageIO.read(imagenArchivo);
            LuminanceSource fuente = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(fuente));

            Result resultado = new MultiFormatReader().decode(bitmap);
            return resultado.getText(); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Ingrediente ingredienteEncontrado;
    public void mostrarInfoCardCodigo(String codigoLeido) {
        infoCardCodigoEscanearPane.setVisible(true);
        ObservableList<Codigo> listaCodigos = FuncionesRepetidas.obtenerCodigoDeImagen(codigoLeido);

        infoCardCodigoEscanearPane.getChildren().clear();

        if (listaCodigos.isEmpty()) {
            Label noEncontrado = new Label("No se ha encontrado ninguna referencia");
            infoCardCodigoEscanearPane.getChildren().add(noEncontrado);
        } else {
            Codigo cod = listaCodigos.get(0);

            codigoInfoCardCodigo.setText(cod.getCodigo_barras());
            tiendaInfoCardCodigo.setText(cod.getNombre_tienda());
            marcaInfoCardCodigo.setText(cod.getNombre_marca());
            origenInfoCardCodigo.setText(cod.getPais_origen());

            ObservableList<Ingrediente> listaIngredientes = FuncionesRepetidas.obtenerListaIngredientes();
            String rutaImagen = null;
            String nombreIngrediente = "Ingrediente desconocido";

            for (Ingrediente ing : listaIngredientes) {
                if (ing.getId_ingrediente() == cod.getId_ingrediente()) {
                    rutaImagen = ing.getImagen_ingrediente();
                    nombreIngrediente = ing.getNombre_ingrediente();
                    ingredienteEncontrado = ing;
                    break;
                }
            }

            nombreInfoCardCodigo.setText(nombreIngrediente);

            if (rutaImagen != null) {
                Image img = new Image(getClass().getResourceAsStream(rutaImagen));
                imgInfoCardCodigo.setImage(img);
            }

            infoCardCodigoEscanearPane.getChildren().add(scrollPaneInfoCardCodigo);
            saberMasInfoCardCodigoEscanearPane.setOnMouseClicked(event -> {
                if (ingredienteEncontrado != null) {
                    infoCardIngredienteSelec = ingredienteEncontrado;
                    escanearPane.setVisible(false);
                    infoCardPane.setVisible(true);
                    infoCardIngredientePane.setVisible(true);
                    mostrarInfoCardIngrediente();
                }
            });
        }
    }
    
    @FXML public void volverAtrasSubirImagen() {
        inicioEscanearPane.setVisible(true);
        infoCodigoEscanearPane.setVisible(false);
        camaraCodigoEscanearPane.setVisible(false);
        subirCodigoEscanearPane.setVisible(false);
    }
    
    // SUBIR RECETA PANE:
    @FXML public void btnVerTerminosSubirReceta(){
        try {
            URL url = getClass().getResource("/vistas/TerminosyCondicionesModal.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            ControladorTerminosyCondicionesModal controlador = loader.getController();

            Stage modalStage = new Stage();
            // paar bloquear la ventana
            modalStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            modalStage.setScene(scene);
            controlador.setStage(modalStage);

            // Centrar el modal en la pantalla
            modalStage.centerOnScreen();
            modalStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void rellenarInfoSubirReceta() {
        lblNombreSubirReceta.setText(nombreSubirReceta.getText());
        lblTipoSubirReceta.setText(tipoSubirReceta.getValue() != null ? tipoSubirReceta.getValue().toString() : "");   
        lblDificultadSubirReceta.setText(dificultadSubirReceta.getValue() != null ? dificultadSubirReceta.getValue().toString() : "");
        lblTipoCoccionSubirReceta.setText(tipoCoccionSubirReceta.getValue() != null ? tipoCoccionSubirReceta.getValue().toString() : "");        
        lblTiempoSubirReceta.setText(tiempoSubirReceta.getText());
        lblPasosSubirReceta.setText(pasosSubirReceta.getText());        
    }

    public void configurarArrastrarImgSubirReceta() {
        subirRecetaPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                String extension = getFileExtension(event.getDragboard().getFiles().get(0).getName());
                if (extension != null && (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"))) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
            }
            event.consume();
        });

        subirRecetaPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                String extension = getFileExtension(file.getName());

                if (extension != null && (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"))) {
                    try {
                        Image image = new Image(file.toURI().toString());
                        imgSubirReceta.setImage(image);

                        imagenBase64 = convertirImagenABase64(file);

                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void importarImgSubirReceta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Archivos de imagen", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(subirRecetaPane.getScene().getWindow());

        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                imgSubirReceta.setImage(image);

                imagenBase64 = convertirImagenABase64(file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(String fileName) {
        if (fileName == null) return null;
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) return null;
        return fileName.substring(lastIndexOf + 1).toLowerCase();
    }

    public String convertirImagenABase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void cargarIngredientesAleatorios() {
        listaIngredientesSubirReceta.getChildren().clear();
        ObservableList<Ingrediente> aleatorios = FXCollections.observableArrayList();
        List<Integer> indicesUsados = new ArrayList<>();
        
        int contador = 0;
        while (contador < 5 && contador < todosLosIngredientes.size()) {
            int indiceAleatorio = (int) (Math.random() * todosLosIngredientes.size());

            if (!indicesUsados.contains(indiceAleatorio)) {
                indicesUsados.add(indiceAleatorio);
                aleatorios.add(todosLosIngredientes.get(indiceAleatorio));
                contador++;
            }
        }

        for (Ingrediente ingrediente : aleatorios) {
            añadirIngredienteALista(ingrediente);
        }
    }

    public void buscarIngredientes(String busqueda) {
        listaIngredientesSubirReceta.getChildren().clear();
        
        String busquedaMinusculas = busqueda.toLowerCase();
        
        for (Ingrediente ingrediente : todosLosIngredientes) {
            String nombreIngrediente = ingrediente.getNombre_ingrediente().toLowerCase();
            if (nombreIngrediente.contains(busquedaMinusculas)) {
                añadirIngredienteALista(ingrediente);
            }
        }
    }
    
    private void añadirIngredienteALista(Ingrediente ingrediente) {
        VBox ingredienteBox = new VBox(5);
        ingredienteBox.setAlignment(Pos.CENTER);

        ImageView imagen = new ImageView();
        try {
            String rutaImagen = ingrediente.getImagen_ingrediente();
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
               
                Image img = new Image(getClass().getResourceAsStream(rutaImagen));
                imagen.setImage(img);
            } else {
                
                Image img = new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png"));
                imagen.setImage(img);
            }
        } catch (Exception e) {
            
            try {
                Image img = new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png"));
                imagen.setImage(img);
            } catch (Exception ex) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(Color.PLUM);
                ingredienteBox.getChildren().add(rect);
            }
        }

        if (imagen.getImage() != null) {
            imagen.setFitHeight(40);
            imagen.setFitWidth(40);
            imagen.setPreserveRatio(true);
            ingredienteBox.getChildren().add(imagen);
        }
        Label nombre = new Label(ingrediente.getNombre_ingrediente());
        nombre.setWrapText(true);
        nombre.setMaxWidth(40);
        nombre.setAlignment(Pos.CENTER);

        ingredienteBox.getChildren().add(nombre);

        ingredienteBox.setOnMouseClicked(e -> seleccionarIngrediente(ingrediente));

        ingredienteBox.setStyle("-fx-padding: 5; -fx-background-color: white; -fx-background-radius: 5;");
        ingredienteBox.setOnMouseEntered(event -> ingredienteBox.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;"));
        ingredienteBox.setOnMouseExited(event -> ingredienteBox.setStyle("-fx-padding: 5; -fx-background-color: white; -fx-background-radius: 5;"));

        listaIngredientesSubirReceta.getChildren().add(ingredienteBox);
    }
    
    private void mostrarIngredientesSeleccionados() {
        listaIngredientesSubirReceta.getChildren().clear();
        for (Ingrediente ingrediente : ingredientesSeleccionadosList) {
            añadirIngredienteALista(ingrediente);
        }
    }
    
    private void seleccionarIngrediente(Ingrediente ingrediente) {
        if (!ingredientesSeleccionadosList.contains(ingrediente)) {
            ingredientesSeleccionadosList.add(ingrediente);

            HBox seleccionadoBox = new HBox(10);
            seleccionadoBox.setAlignment(Pos.CENTER_LEFT);

            ImageView imagen = new ImageView();
            try {
                String rutaImagen = ingrediente.getImagen_ingrediente();
                if (rutaImagen != null && !rutaImagen.isEmpty()) {
                    if (!rutaImagen.startsWith("/")) {
                        rutaImagen = "/" + rutaImagen;
                    }
                    Image img = new Image(getClass().getResourceAsStream(rutaImagen));
                    imagen.setImage(img);
                } else {
                    Image img = new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png"));
                    imagen.setImage(img);
                }
            } catch (Exception e) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(Color.PLUM);
                seleccionadoBox.getChildren().add(rect);
            }

            if (imagen.getImage() != null) {
                imagen.setFitHeight(30);
                imagen.setFitWidth(30);
                imagen.setPreserveRatio(true);
                seleccionadoBox.getChildren().add(imagen);
            }

            Label nombre = new Label(ingrediente.getNombre_ingrediente());

            Button btnMenos = new Button("-");
            btnMenos.setStyle("-fx-background-radius: 50%; -fx-min-width: 25px; -fx-min-height: 25px;");

            Label cantidad = new Label("1");
            cantidad.setMinWidth(30);
            cantidad.setAlignment(Pos.CENTER);

            Button btnMas = new Button("+");
            btnMas.setStyle("-fx-background-radius: 50%; -fx-min-width: 25px; -fx-min-height: 25px;");

            Button btnEliminar = new Button("X");
            btnEliminar.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");

            btnMenos.setOnAction(e -> {
                int cantidadActual = Integer.parseInt(cantidad.getText());
                if (cantidadActual > 1) {
                    cantidad.setText(String.valueOf(cantidadActual - 1));
                } else {
                    // Si la cantidad llega a 0, eliminar el ingrediente
                    ingredientesSeleccionadosList.remove(ingrediente);
                    ingredientesSeleccionadosSubirReceta.getChildren().remove(seleccionadoBox);
                    if (buscadorIngredientesSubirReceta.getText().isEmpty()) {
                        mostrarIngredientesSeleccionados();
                    }
                }
            });

            btnMas.setOnAction(e -> {
                int cantidadActual = Integer.parseInt(cantidad.getText());
                cantidad.setText(String.valueOf(cantidadActual + 1));
            });

            btnEliminar.setOnAction(e -> {
                ingredientesSeleccionadosList.remove(ingrediente);
                ingredientesSeleccionadosSubirReceta.getChildren().remove(seleccionadoBox);
                if (buscadorIngredientesSubirReceta.getText().isEmpty()) {
                    mostrarIngredientesSeleccionados();
                }
            });

            seleccionadoBox.getChildren().addAll(nombre, btnMenos, cantidad, btnMas, btnEliminar);
            ingredientesSeleccionadosSubirReceta.getChildren().add(seleccionadoBox);

            System.out.println("Ingrediente seleccionado: " + ingrediente.getNombre_ingrediente());
            System.out.println("Ingredientes seleccionados: " + ingredientesSeleccionadosList);
        }
    }
    
    public void limpiarSubirRecetaPane(){
        imgSubirReceta.setImage(null);
        nombreSubirReceta.clear();
        buscadorIngredientesSubirReceta.clear();
        tiempoSubirReceta.clear();
        pasosSubirReceta.clear();
        tipoSubirReceta.setValue(null);
        dificultadSubirReceta.setValue(null);
        tipoCoccionSubirReceta.setValue(null);
        listaIngredientesSubirReceta.getChildren().clear();
        alergenosSubirReceta.getSelectionModel().clearSelection();
        etiqImgSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqNombreSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqTipoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqDificultadSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIngredientesSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqAlergenosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqTipoCoccionSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqTiempoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqPasosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        lblNombreSubirReceta.setText("");
        lblTipoSubirReceta.setText("");
        lblDificultadSubirReceta.setText("");
        lblTipoCoccionSubirReceta.setText("");
        lblTiempoSubirReceta.setText("");
        lblPasosSubirReceta.setText("");
        ingredientesSeleccionadosSubirReceta.getChildren().clear();
        //alergenosSeleccionadosSubirReceta.getChildren().clear();
    }
    
    public ListCell<Alergeno> crearListCellAlergeno() {
        return new ListCell<Alergeno>() {
            private ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                setGraphicTextGap(10);
            }

            @Override
            protected void updateItem(Alergeno alergeno, boolean empty) {
                super.updateItem(alergeno, empty);
                if (empty || alergeno == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(alergeno.getImagen_alergeno()));
                    setText(alergeno.getNombre_alergeno());
                    setGraphic(imageView);
                }
            }
        };
    }
    
    private ListCell<Alergeno> crearListCellAlergenoSeleccionado() {
        return new ListCell<Alergeno>() {
            private ImageView imageView = new ImageView();
            private Button btnEliminar = new Button("X");

            {
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                setGraphicTextGap(10);

                btnEliminar.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnEliminar.setOnAction(e -> {
                    Alergeno alergeno = getItem();
                    if (alergeno != null) {
                        alergenosSeleccionadosSubirReceta.getItems().remove(alergeno);
                        if (alergenosSeleccionadosSubirReceta.getItems().isEmpty()) {
                            etiqAlergenosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Alergeno alergeno, boolean empty) {
                super.updateItem(alergeno, empty);
                if (empty || alergeno == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(alergeno.getImagen_alergeno()));

                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.getChildren().addAll(imageView, new Label(alergeno.getNombre_alergeno()), btnEliminar);

                    setGraphic(hbox);
                }
            }
        };
    }
    
    @FXML private void btnSubirRecetaVal() {
        if (comprobarValidacionesSubirReceta()) {
            Receta receta = new Receta();
        receta.setNombre_receta(nombreSubirReceta.getText());
        receta.setConsejos_receta(null);
        receta.setPasos_receta(pasosSubirReceta.getText());
        receta.setImagen_receta(imagenBase64);
        receta.setTiempo_preparacion_receta(Integer.parseInt(tiempoSubirReceta.getText()));
        receta.setDificultad_receta(dificultadSubirReceta.getValue().toString());
        receta.setId_autor(usuario.getId_usuario());
        receta.setTipo_receta(tipoSubirReceta.getValue().toString());
        receta.setTipo_coccion_receta(tipoCoccionSubirReceta.getValue().toString());
        receta.setPublicada_por_restaurante(0);
        receta.setVisible_receta(0);

        if (FuncionesRepetidas.insertarSubirReceta(receta)) {
            int idReceta = FuncionesRepetidas.obtenerUltimoIdReceta();

            // Insertamos los alérgenos
            for (var item : alergenosSeleccionadosSubirReceta.getItems()) {
                Alergeno alergeno = (Alergeno) item;
                if (!FuncionesRepetidas.insertarRecetaAlergeno(idReceta, alergeno.getId_alergeno())) {
                    FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar los alérgenos", 
                        "No se pudieron guardar todos los alérgenos de la receta");
                    return;
                }
            }

            // Insertamos los ingredientes con sus cantidades
            for (int i = 0; i < ingredientesSeleccionadosList.size(); i++) {
                Ingrediente ingrediente = ingredientesSeleccionadosList.get(i);
                HBox hbox = (HBox) ingredientesSeleccionadosSubirReceta.getChildren().get(i);
                
                // Imprimir la estructura del HBox
                System.out.println("HBox " + i + " contiene:");
                for (int j = 0; j < hbox.getChildren().size(); j++) {
                    System.out.println("  Elemento " + j + ": " + hbox.getChildren().get(j).getClass().getSimpleName());
                }
                
                // Buscar el Label de cantidad
                String cantidad = "1"; // valor por defecto
                for (Node node : hbox.getChildren()) {
                    if (node instanceof Label) {
                        Label label = (Label) node;
                        if (label.getText().matches("\\d+")) { // si es un número
                            cantidad = label.getText();
                            break;
                        }
                    }
                }
                
                if (!FuncionesRepetidas.insertarRecetaIngrediente(
                    idReceta, 
                    ingrediente.getId_ingrediente(),
                    cantidad)) {

                    FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar los ingredientes", 
                        "No se pudieron guardar todos los ingredientes de la receta");
                    return;
                }
            }

                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "¡Éxito! Has subido tu receta", 
                    "Te recordamos que te avisaremos cuando el servicio compruebe que todos los datos son correctos");

                limpiarSubirRecetaPane();

            } else {
                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al intentar subir tu receta", 
                    "Por favor, revisa los Términos y condiciones para subir una receta o comprueba que todos los campos estén bien");
            }
        } else {
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al intentar subir tu receta", 
                "Por favor, revisa los Términos y condiciones para subir una receta o comprueba que todos los campos estén bien");
        }
    }
    
    
    // Tooltips
    private void ponerTooltips() {
        etiqImgSubirReceta.setTooltip(new Tooltip("Debes subir una imagen de la receta"));
        etiqNombreSubirReceta.setTooltip(new Tooltip("Introduce el nombre de la receta"));
        etiqTipoSubirReceta.setTooltip(new Tooltip("Selecciona el tipo de receta"));
        etiqDificultadSubirReceta.setTooltip(new Tooltip("Selecciona el nivel de dificultad"));
        etiqIngredientesSubirReceta.setTooltip(new Tooltip("Añade al menos un ingrediente"));
        etiqAlergenosSubirReceta.setTooltip(new Tooltip("Selecciona los alérgenos presentes"));
        etiqTipoCoccionSubirReceta.setTooltip(new Tooltip("Selecciona el tipo de cocción"));
        etiqTiempoSubirReceta.setTooltip(new Tooltip("Introduce el tiempo de preparación en minutos"));
        etiqPasosSubirReceta.setTooltip(new Tooltip("Describe los pasos a seguir"));
    }
    
    // Validadores
    private void configurarValidacionesSubirReceta() {
        etiqImgSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqNombreSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqTipoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqDificultadSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIngredientesSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqAlergenosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqTipoCoccionSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqTiempoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqPasosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));

        imgSubirReceta.imageProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (imgSubirReceta.getImage() != null) {
                    etiqImgSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqImgSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });
        
        nombreSubirReceta.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (!newVal.trim().isEmpty()) {
                    etiqNombreSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqNombreSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        tipoSubirReceta.valueProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal != null) {
                    etiqTipoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqTipoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        dificultadSubirReceta.valueProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal != null) {
                    etiqDificultadSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqDificultadSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        ingredientesSeleccionadosSubirReceta.getChildren().addListener((ListChangeListener<Node>) change -> {
            Platform.runLater(() -> {
                if (!ingredientesSeleccionadosSubirReceta.getChildren().isEmpty()) {
                    etiqIngredientesSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIngredientesSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        alergenosSubirReceta.setItems(FuncionesRepetidas.obtenerListaAlergenos());
        alergenosSubirReceta.setCellFactory(param -> crearListCellAlergeno());
        alergenosSeleccionadosSubirReceta.setCellFactory(param -> crearListCellAlergenoSeleccionado());

        alergenosSubirReceta.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Platform.runLater(() -> {
                    if (!alergenosSeleccionadosSubirReceta.getItems().contains(newVal)) {
                        alergenosSeleccionadosSubirReceta.getItems().add(newVal);
                    }
                    alergenosSubirReceta.getSelectionModel().clearSelection();
                    etiqAlergenosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                });
            }
        });

        tipoCoccionSubirReceta.valueProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal != null) {
                    etiqTipoCoccionSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqTipoCoccionSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        tiempoSubirReceta.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (!newVal.trim().isEmpty() && newVal.matches("\\d+")) {
                    etiqTiempoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqTiempoSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        pasosSubirReceta.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (!newVal.trim().isEmpty()) {
                    etiqPasosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqPasosSubirReceta.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });
    }
   
    public boolean comprobarValidacionesSubirReceta() {
        System.out.println(nombreSubirReceta.getText()+", " +tipoSubirReceta.getValue().toString()+", " +
                dificultadSubirReceta.getValue().toString()+", " +listaIngredientesSubirReceta.getChildren().toString()+", " +alergenosSubirReceta.getSelectionModel().getSelectedItems()+", " +tipoCoccionSubirReceta.getValue().toString()+", " +tiempoSubirReceta.getText()+", " +pasosSubirReceta.getText()
                );
        boolean ingredientesValidos = false;
        for (int i = 0; i < listaIngredientesSubirReceta.getChildren().size(); i++) {
            VBox vbox = (VBox) listaIngredientesSubirReceta.getChildren().get(i);
            System.out.println("VBox " + i + " contiene:");
            for (int j = 0; j < vbox.getChildren().size(); j++) {
                System.out.println("  Elemento " + j + ": " + vbox.getChildren().get(j).getClass().getSimpleName());
            }
        }

        return imgSubirReceta.getImage() != null &&
              !nombreSubirReceta.getText().trim().isEmpty() &&
              tipoSubirReceta.getValue() != null &&
              dificultadSubirReceta.getValue() != null &&
              !listaIngredientesSubirReceta.getChildren().isEmpty() && 
              !alergenosSeleccionadosSubirReceta.getItems().isEmpty() &&
              tipoCoccionSubirReceta.getValue() != null &&
              !tiempoSubirReceta.getText().trim().isEmpty() &&
              !pasosSubirReceta.getText().trim().isEmpty();
    }

    
    
    
    // JuegosPane:
    @FXML private void btnJuego() {
        try {
            URL url = getClass().getResource("/vistas/JuegosPage.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            ControladorJuegosPage controlador = loader.getController();
            controlador.setUsuario(usuario);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    
}
