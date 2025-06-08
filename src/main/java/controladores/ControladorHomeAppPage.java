package controladores;

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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javax.imageio.ImageIO;
import modelos.Codigo;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import modelos.UsuarioCodigo;
import eu.hansolo.tilesfx.Section;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.skins.DonutChartTileSkin;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
    @FXML private AnchorPane homePane, buscadorPane, infoCardPane, escanearPane, subirRecetaPane, perfilPane, ajustesPane;
    @FXML private Button btnBuscar, btnVerFavoritos;
    @FXML private HBox btnHome, btnEscanear, btnSubirReceta, btnAjustes, cajaBuscar;
    @FXML private TextField inputBuscar;
    @FXML public FlowPane flowRecetas;
    @FXML public ScrollPane filtrosScroll, busquedasPane;
        
    // FXML de homePane:
    @FXML private HBox recetasRecomendadasCaja, restaurantesCaja;
    @FXML private VBox valoracionesCaja;
    @FXML private Label lblNivelUsuario, lblPuntosUsuario, lblProximoNivel;
    @FXML private ProgressBar progresoNivel;
    @FXML private Button btnExplorarRecetas;

    
    // FXML de infoCardPane:
    @FXML private ImageView infoCardRecetaImagen, infoCardIngreImagen, infoCardRestauImagen;
    @FXML private Label infoCardRecetaNombre, infoCardRecetaDificultad, infoCardRecetaCocion, infoCardRecetaTiempo, infoCardRecetaValoracion, infoCardRecetaConsejos, infoCardIngreNombre, infoCardIngreTipo, infoCardRestauNombre, infoCardRestauTipo, infoCardRestauUrl, infoCardRestauCiudad, infoCardRestauDireccion;
    @FXML private HBox infoCardRecetaCajaTipo, infoCardRecetaCajaAlergenos, infoCardIngreCajaAlergenos, infoCardRecetaRestaurantes, infoCardRecetaValoracionCaja, infoCardIngreRecetas, infoCardRestauRecetas;
    @FXML private VBox infoCardRecetaTodasValoracionCaja, infoCardRecetaIngredientes, infoCardRecetaPane, infoCardIngredientePane, infoCardRestaurantePane;
    @FXML private Button btnAñadirFavReceta, btnAñadirValoracionReceta, btnAñadirFavIngre, btnAñadirFavRestau, btnAtrasInfoCardReceta,  btnAtrasInfoCardIngre, btnAtrasInfoCardRestau;
    @FXML private TextField inputAñadirValoracionReceta;
    @FXML private SVGPath svgAñadirFavReceta, svgAñadirFavIngre, svgAñadirFavRestau;
    @FXML private TextArea infoCardRecetaPasos;

    
    // FXML de escanearPane:
    @FXML private HBox btnSubirCodigoEscanearPane, todosCodigosInfoCodigo, infoCodigoEscanearPane, recomendarRecetasInfoCodigo, btnSubidaCodigoEscanearPane;
    @FXML private VBox codigoEscanearPane, inicioEscanearPane, subirCodigoEscanearPane, infoCardCodigoEscanearPane, hbInfoCardCodigoEscanearPane;
    @FXML private ImageView imgInfoCardCodigo, imgSubirCodigoEscanearPane;
    @FXML private Label nombreInfoCardCodigo, codigoInfoCardCodigo, tiendaInfoCardCodigo, marcaInfoCardCodigo, origenInfoCardCodigo;
    @FXML private ScrollPane scrollPaneInfoCardCodigo;
    @FXML private Button saberMasInfoCardCodigoEscanearPane;
    
    
    // FXML subirRecetaPane:
    @FXML private ImageView imgSubirReceta;
    @FXML private Button btnImportarSubirReceta, btnSubirRecetaVal, btnCancelar;
    @FXML private TextField nombreSubirReceta, buscadorIngredientesSubirReceta, tiempoSubirReceta;
    @FXML private HBox listaIngredientesSubirReceta;
    @FXML private VBox ingredientesSeleccionadosSubirReceta;
    @FXML private Label lblNombreSubirReceta, lblTipoSubirReceta, lblDificultadSubirReceta, lblTipoCoccionSubirReceta, lblTiempoSubirReceta, lblPasosSubirReceta, etiqImgSubirReceta, etiqNombreSubirReceta, etiqTipoSubirReceta, etiqDificultadSubirReceta, etiqIngredientesSubirReceta, etiqAlergenosSubirReceta, etiqTipoCoccionSubirReceta, etiqTiempoSubirReceta, etiqPasosSubirReceta;
    @FXML private ComboBox tipoSubirReceta, dificultadSubirReceta, tipoCoccionSubirReceta;
    @FXML private ListView alergenosSubirReceta, alergenosSeleccionadosSubirReceta;
    @FXML private TextArea pasosSubirReceta;
    
    // FXML ajustesPane:
    @FXML private Label lblReestablecerPreferenias, lblCerrarSesion, lblBorrarPerfil, lblAjustesJuego, lblManualPdf, lblVolverFundidoPage, lblContactarSoporte, lblReportar, lblCreditos, lblTerminosyCondiciones, lblPoliticayPrivacidad;
    @FXML private VBox inicioAjustesPane,condicionesPane, ajustesJuegoPane, correoSoportePane, manualPane, informacionAppSoportePane;
    @FXML private Label lblCondiciones, lvlTextoDescriptivo, lblTextoDescripcion, lblAyudaTexto, lblAyudaTextoInfo;
    @FXML private Button btnAceptarCondiciones, btnCancelarCondiciones, btnReproducirAjustesJuego, btnSonidoAjustesJuego, btnEnviarCorreoSoportePane;
    @FXML private SVGPath svgSonidoAjustesJuego;
    @FXML private ProgressBar volumenProgressBar;
    @FXML private TextField asuntoCorreoSoportePane;
    @FXML private TextArea mensajeCorreoSoportePane;
    @FXML private WebView webViewManual;
    
    
    
    // FXML perfilPane:
    @FXML private HBox inicioPerfilPane, misFavPerfilPane, cajaGuardarEditarPerfilPane, cajaVariosFavPerfilPane; 
    @FXML private VBox misAlergiasCajaPerfilPane, vboxTipoFiltrarFav, vboxTipoDietaFiltrarFav, vboxAlergenosFiltrarFav;
    @FXML private Label nombrePerfilPane, correoPerfilPane, contraseñaPerfilPane, ciudadPerfilPane, btnVerFavoPerfilPane;
    @FXML private Button btnEditarPerfilPane, btnGuardarPerfilPane, btnCancelarPerfilPane, btnVolverPerfilPane;
    @FXML private ChoiceBox selectCiudadPerfilPane; 
    @FXML private Label lblInputNombrePerfilPane, lblInputCorreoPerfilPane, lblInputContraseñaPerfilPane, lblSelectCiudadPerfilPane, lblPuntosUsuarioPerfil, lblNivelUsuarioPerfil, lblProximoNivelPerfil;
    @FXML private ListView<Alergeno> editarMisAlergiasPerfilPane;
    @FXML private TextField inputNombrePerfilPane, inputCorreoPerfilPane, inputContraseñaPerfilPane;
    @FXML private ImageView imgPerfilPane;
    @FXML public FlowPane flowRecetasFavoritosPerfilPane;
    @FXML private AnchorPane graficoCajaPerfilPane;
    @FXML private ProgressBar progresoNivelPerfil;
    
    
            
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
    private String imagenBase64; // de SubirRecetaPane
    ImageView iconoOk = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("assets/iconos/icono_ok.png")));
    ImageView iconoError = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("assets/iconos/icono_error.png")));
    private String accionActual = "";
    private MediaPlayer mediaPlayer;
    private boolean sonidoActivado = true;
    private double volumen = 1.0;
    private String accionActualCorreo = "";
    
    // Svgs:
    private String svgTemaClaro = "M480-360q50 0 85-35t35-85q0-50-35-85t-85-35q-50 0-85 35t-35 85q0 50 35 85t85 35Zm0 80q-83 0-141.5-58.5T280-480q0-83 58.5-141.5T480-680q83 0 141.5 58.5T680-480q0 83-58.5 141.5T480-280ZM80-440q-17 0-28.5-11.5T40-480q0-17 11.5-28.5T80-520h80q17 0 28.5 11.5T200-480q0 17-11.5 28.5T160-440H80Zm720 0q-17 0-28.5-11.5T760-480q0-17 11.5-28.5T800-520h80q17 0 28.5 11.5T920-480q0 17-11.5 28.5T880-440h-80ZM480-760q-17 0-28.5-11.5T440-800v-80q0-17 11.5-28.5T480-920q17 0 28.5 11.5T520-880v80q0 17-11.5 28.5T480-760Zm0 720q-17 0-28.5-11.5T440-80v-80q0-17 11.5-28.5T480-200q17 0 28.5 11.5T520-160v80q0 17-11.5 28.5T480-40ZM226-678l-43-42q-12-11-11.5-28t11.5-29q12-12 29-12t28 12l42 43q11 12 11 28t-11 28q-11 12-27.5 11.5T226-678Zm494 495-42-43q-11-12-11-28.5t11-27.5q11-12 27.5-11.5T734-282l43 42q12 11 11.5 28T777-183q-12 12-29 12t-28-12Zm-42-495q-12-11-11.5-27.5T678-734l42-43q11-12 28-11.5t29 11.5q12 12 12 29t-12 28l-43 42q-12 11-28 11t-28-11ZM183-183q-12-12-12-29t12-28l43-42q12-11 28.5-11t27.5 11q12 11 11.5 27.5T282-226l-42 43q-11 12-28 11.5T183-183Zm297-297Z";
    private String svgTemaOscuro = "M484-80q-84 0-157.5-32t-128-86.5Q144-253 112-326.5T80-484q0-128 72-232t193-146q22-8 41 5.5t18 36.5q-3 85 27 162t90 137q60 60 137 90t162 27q26-1 38.5 17.5T863-345q-44 120-147.5 192.5T484-80Zm0-80q88 0 163-44t118-121q-86-8-163-43.5T464-465q-61-61-97-138t-43-163q-77 43-120.5 118.5T160-484q0 135 94.5 229.5T484-160Zm-20-305Z";
    
    private String svgConSonido = "M400-120q-66 0-113-47t-47-113q0-66 47-113t113-47q23 0 42.5 5.5T480-418v-382q0-17 11.5-28.5T520-840h160q17 0 28.5 11.5T720-800v80q0 17-11.5 28.5T680-680H560v400q0 66-47 113t-113 47Z";
    private String svgSinSonido = "M764-84 84-764q-11-11-11-28t11-28q11-11 28-11t28 11l680 680q11 11 11 28t-11 28q-11 11-28 11t-28-11ZM560-680v70q0 20-12.5 29.5T520-571q-15 0-27.5-10T480-611v-189q0-17 11.5-28.5T520-840h160q17 0 28.5 11.5T720-800v80q0 17-11.5 28.5T680-680H560ZM400-120q-66 0-113-47t-47-113q0-66 47-113t113-47q23 0 42.5 5.5T480-418v-62l80 80v120q0 66-47 113t-113 47Z";
    
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Por defecto:
        mostrarPane(homePane);
        infoCodigoEscanearPane.setVisible(false);
        subirCodigoEscanearPane.setVisible(false);
        inicioEscanearPane.setVisible(false);
        subirRecetaPane.setVisible(false);
        perfilPane.setVisible(false);
        ajustesPane.setVisible(false);
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
        
        Map<String, String> cache = FuncionesRepetidas.leerSesionCache();
        String temaGuardado = cache.getOrDefault("tema", "claro");

        if ("oscuro".equals(temaGuardado)) {
            btnTema.setSelected(true);
            aplicarTemaOscuro();
        } else {
            btnTema.setSelected(false);
            aplicarTemaClaro();
        }
        
        Platform.runLater(() -> {
            cargarEstiloInicial();
        });

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
        
        Platform.runLater(() -> {
            cargarDatosUsuario();
            cargarRecetasRecomendadas();
            cargarRestaurantesRecomendados();
            cargarValoracionesUsuario();
        });
        btnExplorarRecetas.setOnMouseClicked(event-> verBuscadorPane());
        
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
        
        btnSubirCodigoEscanearPane.setOnMouseClicked(event-> {
            inicioEscanearPane.setVisible(false);
            infoCodigoEscanearPane.setVisible(true);
            subirCodigoEscanearPane.setVisible(true);
        });
        
        hbInfoCardCodigoEscanearPane.setVisible(false);
        
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
     
        // AjustesPage
        btnAjustes.setOnMouseClicked(event -> {
            mostrarPane(ajustesPane);
            mostrarAjustesPane(inicioAjustesPane);
        });   
        lblReestablecerPreferenias.setOnMouseClicked(event -> {
            mostrarAjustesPane(condicionesPane);
            accionActual = "restablecer";
            lblCondiciones.setText("Reestablecer preferencias del sistema");
            lvlTextoDescriptivo.setText("El restablecimiento de los ajustes del sistema borrará todas las preferencias " +
                "como el tema de la app, los ajustes del sonido de los juegos, y otras configuraciones personalizadas. " +
                "Todo volverá a su configuración por defecto.");
        });
        
        btnAceptarCondiciones.setOnAction(event -> {
            if ("cerrarSesion".equals(accionActual)) {
                cerrarSesion();
            } else if ("restablecer".equals(accionActual)) {
                restablecerPreferencias();
            } else if ("borrarPerfil".equals(accionActual)) {
                borrarPerfil();
            }
            condicionesPane.setVisible(false);
        });
        
        btnCancelarCondiciones.setOnAction(event -> {
            mostrarAjustesPane(inicioAjustesPane);
        });
        
        lblCerrarSesion.setOnMouseClicked(event -> {
            mostrarAjustesPane(condicionesPane);
            accionActual = "cerrarSesion";
            lblCondiciones.setText("Cerrar sesión");
            lvlTextoDescriptivo.setText("Si cierras sesión ahora, perderás todo aquello que no hayas guardado. La sesión se cerrará y deberás de ingresar tus datos para poder entrar.");
        });
        
        lblBorrarPerfil.setOnMouseClicked(event -> {
            mostrarAjustesPane(condicionesPane);
            accionActual = "borrarPerfil";
            lblCondiciones.setText("Borrar perfil");
            lvlTextoDescriptivo.setText("Al borrar por completo tu perfil, no podrás recuperar los datos. Por lo que se borrará toda aquella información incluso si decides volver a registrarte.");
        });
        
        lblAjustesJuego.setOnMouseClicked(event -> {
            mostrarAjustesPane(ajustesJuegoPane);
        });
        
        cargarEstadoSonido();

        btnReproducirAjustesJuego.setOnAction(event -> reproducirMusica());
        btnSonidoAjustesJuego.setOnAction(event -> toggleSonido());
        volumenProgressBar.setOnMouseDragged(event -> ajustarVolumen(event.getX()));
        volumenProgressBar.setOnMouseClicked(event -> ajustarVolumen(event.getX()));
        volumenProgressBar.setProgress(volumen);
        
        lblContactarSoporte.setOnMouseClicked(event -> {
            mostrarAjustesPane(correoSoportePane);
            accionActualCorreo = "contactar";
            lblTextoDescripcion.setText("¿Quieres contactar con nosotros? Puedes hacerlo a traves del correo: soporteboiledegg@gmial.com o simplemente escribiendonos en el apartado de acontinuación. Recuerda que si usas este método, ");
        });
        
        lblReportar.setOnMouseClicked(event -> {
            mostrarAjustesPane(correoSoportePane);
            accionActualCorreo = "reportar";
            lblTextoDescripcion.setText("¿Ha sucedido alguna incidencia? Por favor, descríbela detalladamente para poder ayudarte lo antes posible.");
        });
        
        btnEnviarCorreoSoportePane.setOnAction(event -> enviarCorreo());
        
        lblManualPdf.setOnMouseClicked(event -> {
            mostrarAjustesPane(manualPane);
            
            WebEngine webEngine = webViewManual.getEngine();
    
            URL urlManual = getClass().getResource("/manual/ManualBoiledEgg.html");
            System.out.println("urlManual del recurso: " + (urlManual != null ? urlManual.toExternalForm() : "null"));

            if (urlManual != null) {
                webEngine.load(urlManual.toExternalForm());
            } else {
                try {
                    File file = new File("src/main/resources/manual/ManualBoiledEgg.html");
                    System.out.println("Buscando archivo en: " + file.getAbsolutePath());

                    if (file.exists()) {
                        System.out.println("Archivo encontrado!");
                        webEngine.load(file.toURI().toURL().toExternalForm());
                    }
                } catch (Exception e) {
                    System.out.println("Error al cargar el archivo: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        
        lblCreditos.setOnMouseClicked(event ->{
            mostrarAjustesPane(informacionAppSoportePane);
            lblAyudaTexto.setText("Créditos");
            lblAyudaTextoInfo.setText("Versión: 1.0 Beta\n\n" +
                            "Desarrollado por: Carmen Gordo-Ureña Toro\n" +
                            "Trabajo de Fin de Grado\n" +
                            "Desarrollo de Aplicaciones Multiplataforma\n" +
                            "Universidad de I.E.S Politécnico Hermenegildo Lanz ");
        });
        
        lblTerminosyCondiciones.setOnMouseClicked(event ->{
            mostrarAjustesPane(informacionAppSoportePane);
            lblAyudaTexto.setText("Términos y Condiciones");
            lblAyudaTextoInfo.setText("1. Aceptación de los Términos\n\n" +
                            "Al acceder y utilizar esta aplicación, usted acepta estar sujeto a estos términos y condiciones.\n\n" +
                            "2. Uso de la Aplicación\n\n" +
                            "La aplicación está diseñada para proporcionar información sobre recetas y productos alimenticios. " +
                            "El usuario se compromete a utilizar la aplicación de manera responsable y ética.\n\n" +
                            "3. Cuentas de Usuario\n\n" +
                            "Los usuarios son responsables de mantener la confidencialidad de sus credenciales de acceso.\n\n" +
                            "4. Contenido del Usuario\n\n" +
                            "Los usuarios pueden compartir contenido, pero deben respetar los derechos de autor y la propiedad intelectual.\n\n" +
                            "5. Limitaciones de Responsabilidad\n\n" +
                            "La aplicación se proporciona 'tal cual', sin garantías de ningún tipo.\n\n" +
                            "6. Modificaciones\n\n" +
                            "Nos reservamos el derecho de modificar estos términos en cualquier momento.");
        });
        
        lblPoliticayPrivacidad.setOnMouseClicked(event -> {
            mostrarAjustesPane(informacionAppSoportePane);
            lblAyudaTexto.setText("Política de Privacidad");
            lblAyudaTextoInfo.setText("1. Recopilación de Información\n\n" +
                            "Recopilamos información que usted nos proporciona directamente, como su nombre, correo electrónico y preferencias.\n\n"
                            +"2. Uso de la Información\n\n" +
                            "Utilizamos su información para:\n" +
                            "- Proporcionar y mantener nuestros servicios\n" +
                            "- Personalizar su experiencia\n" +
                            "- Comunicarnos con usted\n\n" +
                            "3. Protección de Datos\n\n" +
                            "Implementamos medidas de seguridad para proteger su información personal.\n\n" +
                            "4. Cookies\n\n" +
                            "Utilizamos cookies para mejorar su experiencia de usuario.\n\n" +
                            "5. Sus Derechos\n\n" +
                            "Usted tiene derecho a acceder, corregir o eliminar sus datos personales.\n\n" +
                            "6. Cambios en la Política\n\n" +
                            "Nos reservamos el derecho de actualizar esta política de privacidad en cualquier momento.");
        });
        
        
        // perfilPage
        inputNombrePerfilPane.setVisible(false);
        inputCorreoPerfilPane.setVisible(false);
        inputContraseñaPerfilPane.setVisible(false);
        selectCiudadPerfilPane.setVisible(false);
        cajaGuardarEditarPerfilPane.setVisible(false);
        selectCiudadPerfilPane.setItems(ObservableListas.listaCiudades);
    
        btnPerfil.setOnMouseClicked(event -> {
            mostrarPane(perfilPane);
            mostrarPerfilPane(inicioPerfilPane);
            nombrePerfilPane.setText(usuario.getNombre_usuario());
            correoPerfilPane.setText(usuario.getEmail_usuario());
            cargarFavoritosPerfil(); 
            
            List<Alergeno> alergiasUsuario = FuncionesRepetidas.obtenerUsuarioAlergenos(usuario.getId_usuario());
            misAlergiasCajaPerfilPane.getChildren().clear();

            if (alergiasUsuario.isEmpty()) {
                Label lblNoAlergias = new Label("Aún no has añadido tus alergias");
                misAlergiasCajaPerfilPane.getChildren().add(lblNoAlergias);
            } else {
                for (Alergeno alergeno : alergiasUsuario) {
                    HBox alergenosBox = new HBox(10);
                    alergenosBox.setAlignment(Pos.CENTER_LEFT);
                    alergenosBox.setPadding(new Insets(5));

                    ImageView imgAlergeno = new ImageView(new Image(getClass().getResource(alergeno.getImagen_alergeno()).toExternalForm()));
                    imgAlergeno.setFitHeight(30);
                    imgAlergeno.setFitWidth(30);

                    Label lblAlergeno = new Label(alergeno.getNombre_alergeno());
                    lblAlergeno.setAlignment(Pos.CENTER_LEFT);

                    alergenosBox.getChildren().addAll(imgAlergeno, lblAlergeno);

                    misAlergiasCajaPerfilPane.getChildren().add(alergenosBox);
                }
            }
                        
            mostrarDonutFavoritos();
        });
        
        btnVerFavoritos.setOnMouseClicked(event -> {
            mostrarPane(perfilPane);
            mostrarPerfilPane(misFavPerfilPane);
            cargarFiltrosFavoritos();
            actualizarCardsFavoritos();
        });
        
        btnVerFavoPerfilPane.setOnMouseClicked(event -> {
            mostrarPane(perfilPane);
            mostrarPerfilPane(misFavPerfilPane);
            cargarFiltrosFavoritos();
            actualizarCardsFavoritos();
        });
        
        btnVolverPerfilPane.setOnMouseClicked(event -> {
            mostrarPane(perfilPane);
            mostrarPerfilPane(inicioPerfilPane);
            
            nombrePerfilPane.setText(usuario.getNombre_usuario());
            correoPerfilPane.setText(usuario.getEmail_usuario());
            cargarFavoritosPerfil(); 
            
            List<Alergeno> alergiasUsuario = FuncionesRepetidas.obtenerUsuarioAlergenos(usuario.getId_usuario());
            misAlergiasCajaPerfilPane.getChildren().clear();

            if (alergiasUsuario.isEmpty()) {
                Label lblNoAlergias = new Label("Aún no has añadido tus alergias");
                misAlergiasCajaPerfilPane.getChildren().add(lblNoAlergias);
            } else {
                for (Alergeno alergeno : alergiasUsuario) {
                    HBox alergenosBox = new HBox(10);
                    alergenosBox.setAlignment(Pos.CENTER_LEFT);
                    alergenosBox.setPadding(new Insets(5));

                    ImageView imgAlergeno = new ImageView(new Image(getClass().getResource(alergeno.getImagen_alergeno()).toExternalForm()));
                    imgAlergeno.setFitHeight(30);
                    imgAlergeno.setFitWidth(30);

                    Label lblAlergeno = new Label(alergeno.getNombre_alergeno());
                    lblAlergeno.setAlignment(Pos.CENTER_LEFT);

                    alergenosBox.getChildren().addAll(imgAlergeno, lblAlergeno);

                    misAlergiasCajaPerfilPane.getChildren().add(alergenosBox);
                }
            }
        });
        
        btnEditarPerfilPane.setOnMouseClicked(event -> toggleEdicionPerfil());
        configurarEdicionAlergias();
        
        ingredientesSeleccionadosSubirReceta.setPrefHeight(Region.USE_COMPUTED_SIZE);
        ingredientesSeleccionadosSubirReceta.setMinHeight(Region.USE_COMPUTED_SIZE);
        ingredientesSeleccionadosSubirReceta.setMaxHeight(Region.USE_COMPUTED_SIZE);
        
        ingredientesSeleccionadosSubirReceta.getChildren().addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    ingredientesSeleccionadosSubirReceta.requestLayout();
                }
            }
        });
    }
    
    private void cargarEstiloInicial() {
        try {
            Map<String, String> cacheData = FuncionesRepetidas.leerSesionCache();
            String tema = cacheData.getOrDefault("tema", "claro");
                        
            Scene scene = btnTema.getScene();
            if (scene != null) {
                scene.getStylesheets().clear();
                if ("oscuro".equals(tema)) {
                    URL styleUrl = getClass().getResource(rutaTemaOscuro);
                    if (styleUrl != null) {
                        scene.getStylesheets().add(styleUrl.toExternalForm());
                    }
                } else {
                    URL styleUrl = getClass().getResource(rutaTemaClaro);
                    if (styleUrl != null) {
                        scene.getStylesheets().add(styleUrl.toExternalForm());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el estilo inicial: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void animarIconoTema(double destinoX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), iconoTema);
        transition.setToX(destinoX);
        transition.play();
    }
    
    private void aplicarTemaClaro() {
        iconoTema.setContent(svgTemaClaro);
        animarIconoTema(-20);
        Scene scene = btnTema.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(rutaTemaClaro).toExternalForm());
        }
    }

    private void aplicarTemaOscuro() {
        iconoTema.setContent(svgTemaOscuro);
        animarIconoTema(20);
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
            imgPerfilPane.setImage(img);
        } else {
            System.err.println("No se pudo cargar el icono desde: " + rutaIcono);
        }
        
        
        actualizarListaIngredientesEscaneados(usuario);
    }
   
   
    // HOME PANE:
    private void comprobarNivelUsuario() {
        if (usuario != null) {
            int puntosActuales = usuario.getPuntos_usuario();
            int nivelActual = usuario.getNivel_usuario();
            int nuevoNivel = nivelActual;

            if (puntosActuales >= 10 && nivelActual == 1) {
                nuevoNivel = 2;
            } else if (puntosActuales >= 25 && nivelActual == 2) {
                nuevoNivel = 3;
            } else if (puntosActuales >= 50 && nivelActual == 3) {
                nuevoNivel = 4;
            } else if (puntosActuales >= 75 && nivelActual == 4) {
                nuevoNivel = 5;
            } else if (puntosActuales >= 100 && nivelActual == 5) {
                nuevoNivel = 6;
            } else if (puntosActuales >= 125 && nivelActual == 6) {
                nuevoNivel = 7;
            } else if (puntosActuales >= 150 && nivelActual == 7) {
                nuevoNivel = 8;
            } else if (puntosActuales >= 175 && nivelActual == 8) {
                nuevoNivel = 9;
            } else if (puntosActuales >= 200 && nivelActual == 9) {
                nuevoNivel = 10;
            }

            if (nuevoNivel != nivelActual) {
                usuario.setNivel_usuario(nuevoNivel);
                FuncionesRepetidas.actualizarUsuarioPuntos(usuario);
                cargarDatosUsuario();
            }
        }
    }

    private void cargarDatosUsuario() {
        if (usuario != null) {
            lblNivelUsuario.setText(" " + String.valueOf(usuario.getNivel_usuario()));
            lblPuntosUsuario.setText(" " + String.valueOf(usuario.getPuntos_usuario()));
            
            lblNivelUsuarioPerfil.setText(" " + String.valueOf(usuario.getNivel_usuario()));
            lblPuntosUsuarioPerfil.setText(" " + String.valueOf(usuario.getPuntos_usuario()));

            int puntosActuales = usuario.getPuntos_usuario();
            int nivelActual = usuario.getNivel_usuario();

            int puntosParaSiguienteNivel = 10;
            if (nivelActual > 1) {
                puntosParaSiguienteNivel += (nivelActual - 1) * 25;
            }

            int puntosParaNivelActual = 0;
            if (nivelActual > 1) {
                puntosParaNivelActual = 10 + (nivelActual - 2) * 25;
            }

            double progreso = (double) (puntosActuales - puntosParaNivelActual) / (puntosParaSiguienteNivel - puntosParaNivelActual);
            progresoNivel.setProgress(progreso);
            progresoNivelPerfil.setProgress(progreso);

            int puntosFaltantes = puntosParaSiguienteNivel - puntosActuales;
            String textoProximo = "Siguiente nivel en: " + (puntosFaltantes > 0 ? puntosFaltantes : 0) + " puntos";
            lblProximoNivel.setText(textoProximo);
            lblProximoNivelPerfil.setText(textoProximo);
        }
    }
    
    private void cargarRecetasRecomendadas() {
        recetasRecomendadasCaja.getChildren().clear();

        ObservableList<Favoritos> favoritos = FuncionesRepetidas.obtenerFavoritosUsuario(usuario.getId_usuario());
        ObservableList<Receta> todasLasRecetas = FuncionesRepetidas.obtenerListaRecetas();
        ObservableList<Receta> recetasFavoritas = FXCollections.observableArrayList();
        ObservableList<Receta> recetasRecomendadas = FXCollections.observableArrayList();

        for (Favoritos fav : favoritos) {
            if (fav.getTipo_objeto() == Favoritos.TipoObjeto.RECETA) {
                for (Receta receta : todasLasRecetas) {
                    if (receta.getId_receta() == fav.getId_objeto()) {
                        recetasFavoritas.add(receta);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < 8 && i < recetasFavoritas.size(); i++) {
            recetasRecomendadas.add(recetasFavoritas.get(i));
        }

        if (recetasRecomendadas.size() < 8) {
            Set<String> alergenosFavoritos = new HashSet<>();
            for (Receta favorita : recetasFavoritas) {
                ObservableList<Alergeno> alergenos = FuncionesRepetidas.obtenerRecetaAlergenos(favorita.getId_receta());
                for (Alergeno alergeno : alergenos) {
                    alergenosFavoritos.add(alergeno.getNombre_alergeno());
                }
            }

            List<Receta> recetasDisponibles = new ArrayList<>();
            for (Receta receta : todasLasRecetas) {
                boolean esFavorita = false;
                for (Receta favorita : recetasFavoritas) {
                    if (receta.getId_receta() == favorita.getId_receta()) {
                        esFavorita = true;
                        break;
                    }
                }
                if (!esFavorita) {
                    recetasDisponibles.add(receta);
                }
            }

            for (int i = 0; i < recetasDisponibles.size(); i++) {
                for (int j = i + 1; j < recetasDisponibles.size(); j++) {
                    Receta r1 = recetasDisponibles.get(i);
                    Receta r2 = recetasDisponibles.get(j);
                    int coincidencias1 = 0;
                    int coincidencias2 = 0;

                    ObservableList<Alergeno> alergenos1 = FuncionesRepetidas.obtenerRecetaAlergenos(r1.getId_receta());
                    ObservableList<Alergeno> alergenos2 = FuncionesRepetidas.obtenerRecetaAlergenos(r2.getId_receta());

                    for (Alergeno alergeno : alergenos1) {
                        if (alergenosFavoritos.contains(alergeno.getNombre_alergeno())) {
                            coincidencias1++;
                        }
                    }

                    for (Alergeno alergeno : alergenos2) {
                        if (alergenosFavoritos.contains(alergeno.getNombre_alergeno())) {
                            coincidencias2++;
                        }
                    }

                    if (coincidencias2 > coincidencias1) {
                        Receta temp = recetasDisponibles.get(i);
                        recetasDisponibles.set(i, recetasDisponibles.get(j));
                        recetasDisponibles.set(j, temp);
                    }
                }
            }

            for (Receta receta : recetasDisponibles) {
                if (recetasRecomendadas.size() >= 8) break;
                recetasRecomendadas.add(receta);
            }

            if (recetasRecomendadas.size() < 8) {
                List<Receta> recetasRestantes = new ArrayList<>();
                for (Receta receta : recetasDisponibles) {
                    boolean yaRecomendada = false;
                    for (Receta recomendada : recetasRecomendadas) {
                        if (receta.getId_receta() == recomendada.getId_receta()) {
                            yaRecomendada = true;
                            break;
                        }
                    }
                    if (!yaRecomendada) {
                        recetasRestantes.add(receta);
                    }
                }

                while (recetasRecomendadas.size() < 8 && !recetasRestantes.isEmpty()) {
                    int indiceAleatorio = (int) (Math.random() * recetasRestantes.size());
                    recetasRecomendadas.add(recetasRestantes.get(indiceAleatorio));
                    recetasRestantes.remove(indiceAleatorio);
                }
            }
        }

        for (Receta receta : recetasRecomendadas) {
            VBox card = FuncionesRepetidas.crearCardReceta(usuario, receta);
            if (card != null) {
                card.setOnMouseClicked(event -> {
                    infoCardRecetaSelec = receta;
                    
                    mostrarPane(infoCardPane);
                    infoCardRecetaPane.setVisible(true);
                    infoCardIngredientePane.setVisible(false);
                    infoCardRestaurantePane.setVisible(false);

                    infoCardRecetaSelec = receta;
                    mostrarInfoCardReceta();
                });

                recetasRecomendadasCaja.getChildren().add(card);
            }
        }
    }
    
    private void cargarRestaurantesRecomendados() {
        restaurantesCaja.getChildren().clear();

        ObservableList<Favoritos> favoritos = FuncionesRepetidas.obtenerFavoritosUsuario(usuario.getId_usuario());
        ObservableList<Restaurante> todosRestaurantes = FuncionesRepetidas.obtenerListaRestaurantes();
        ObservableList<Restaurante> restaurantesFavoritos = FXCollections.observableArrayList();

        for (Favoritos fav : favoritos) {
            if (fav.getTipo_objeto() == Favoritos.TipoObjeto.RESTAURANTE) {
                for (Restaurante restaurante : todosRestaurantes) {
                    if (restaurante.getId_restaurante() == fav.getId_objeto()) {
                        restaurantesFavoritos.add(restaurante);
                        break;
                    }
                }
            }
        }

        if (restaurantesFavoritos.isEmpty()) {
            List<Restaurante> listaRestaurantes = new ArrayList<>(todosRestaurantes);

            for (int i = 0; i < 8 && i < listaRestaurantes.size(); i++) {
                int indiceAleatorio = (int) (Math.random() * listaRestaurantes.size());
                Restaurante restaurante = listaRestaurantes.get(indiceAleatorio);
                listaRestaurantes.remove(indiceAleatorio);

                VBox card = FuncionesRepetidas.crearCardRestaurante(usuario, restaurante);
                if (card != null) {
                    card.setOnMouseClicked(event -> {
                        infoCardRestauranteSelec = restaurante;
                        mostrarInfoCardRestaurante();
                        mostrarPane(infoCardPane);
                    });
                    restaurantesCaja.getChildren().add(card);
                }
            }
        } else {
            for (int i = 0; i < 8 && i < restaurantesFavoritos.size(); i++) {
                Restaurante restaurante = restaurantesFavoritos.get(i);
                VBox card = FuncionesRepetidas.crearCardRestaurante(usuario, restaurante);
                if (card != null) {
                    card.setOnMouseClicked(event -> {
                        infoCardRestauranteSelec = restaurante;
                        mostrarInfoCardRestaurante();
                        mostrarPane(infoCardPane);
                    });
                    restaurantesCaja.getChildren().add(card);
                }
            }
        }
    }
    
    public void cargarValoracionesUsuario() {
        valoracionesCaja.getChildren().clear();

        ObservableList<Valoracion> valoracionesUsuario = FuncionesRepetidas.obtenerListaValoraciones("Receta", usuario.getId_usuario());
        Set<Integer> recetasComentadasPorUsuario = new HashSet<>();
        for (Valoracion val : valoracionesUsuario) {
            recetasComentadasPorUsuario.add(val.getId_objeto());
        }

        List<Valoracion> valoracionesDeRecetas = new ArrayList<>();
        for (Integer idReceta : recetasComentadasPorUsuario) {
            ObservableList<Valoracion> valoracionesReceta = FuncionesRepetidas.obtenerListaValoraciones("Receta", idReceta);
            valoracionesDeRecetas.addAll(valoracionesReceta);
        }

        valoracionesDeRecetas.sort((v1, v2) -> v2.getFecha_valoracion().compareTo(v1.getFecha_valoracion()));

        for (Valoracion val : valoracionesDeRecetas) {
            HBox card = FuncionesRepetidas.crearCardValoraciones(val);
            if (card != null) {
                valoracionesCaja.getChildren().add(card);
            }
        }
    }

    
    
    // FILTROS:
    public void cargarFiltros(ObservableList<String> opciones, VBox destino) {
        for (String nombre : opciones) {
            CheckBox check = new CheckBox(nombre);
            check.getStyleClass().add("checkBox");
            check.setOnAction(e -> actualizarCards());
            destino.getChildren().add(check);
        }
    }

    public void cargarFiltrosNumericos(ObservableList<Integer> opciones, VBox destino) {
        for (Integer val : opciones) {
            CheckBox check = new CheckBox(String.valueOf(val));
            check.getStyleClass().add("checkBox");
            check.setOnAction(e ->  actualizarCards());
            destino.getChildren().add(check);
        }
    }
    
    private List<CheckBox> listaCheckTipo = new ArrayList<>();
    public void cargarFiltrosTipo(List<String> opciones) {
        for (String texto : opciones) {
            CheckBox cb = new CheckBox(texto);
            cb.getStyleClass().add("checkBox");
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
        perfilPane.setVisible(false);
        ajustesPane.setVisible(false);
        perfilPane.setVisible(false);
        
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
        ImageView imgTipo = FuncionesRepetidas.crearIconoDesdeRuta("/assets/img_tipo_receta/" + infoCardRecetaSelec.getTipo_receta() + ".png");
        if (imgTipo != null) {
            infoCardRecetaCajaTipo.getChildren().addAll(
                imgTipo,
                new Label(infoCardRecetaSelec.getTipo_receta())
            );
        } else {
            infoCardRecetaCajaTipo.getChildren().add(new Label(infoCardRecetaSelec.getTipo_receta()));
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
        
        String pasos = infoCardRecetaSelec.getPasos_receta().replace("\\n", "\n");
        infoCardRecetaPasos.setText(pasos);
        infoCardRecetaPasos.setEditable(false);
        infoCardRecetaPasos.setWrapText(true);
        infoCardRecetaPasos.setPrefRowCount(1);
        infoCardRecetaPasos.textProperty().addListener((obs, oldText, newText) -> {
            int numLines = newText.split("\n").length;
            infoCardRecetaPasos.setPrefRowCount(numLines);
        });
        
        if (infoCardRecetaSelec.getConsejos_receta() == null || infoCardRecetaSelec.getConsejos_receta().trim().isEmpty()) {
            infoCardRecetaConsejos.setText("Huevin no tiene consejos sobre esta receta");
            infoCardRecetaConsejos.setAlignment(Pos.CENTER);
        }else{
            infoCardRecetaConsejos.setText(infoCardRecetaSelec.getConsejos_receta());
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
            todosCodigosInfoCodigo.setAlignment(Pos.CENTER_LEFT);
            ObservableList<Ingrediente> listaIngredientes = FuncionesRepetidas.obtenerListaIngredientes();

            for (UsuarioCodigo uc : listaUsuarioCodigo) {
                for (Ingrediente ing : listaIngredientes) {
                    if (ing.getId_ingrediente() == uc.getId_ingrediente()) {
                        VBox card = FuncionesRepetidas.crearCardIngrediente(usuario, ing);
                        card.setOnMouseClicked(event -> {
                            infoCardIngredienteSelec = ing;
                            escanearPane.setVisible(false);
                            infoCardPane.setVisible(true);
                            infoCardIngredientePane.setVisible(true);
                            mostrarInfoCardIngrediente();
                        });
                        
                        todosCodigosInfoCodigo.getChildren().add(card);
                        break;
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
                card.setOnMouseClicked(event -> {
                    infoCardRecetaSelec = receta;
                    escanearPane.setVisible(false);
                    infoCardPane.setVisible(true);
                    infoCardIngredientePane.setVisible(true);
                    mostrarInfoCardReceta();
                });
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
                for (Ingrediente ing : ingredientesReceta) {
                    if (ing.getId_ingrediente() == uc.getId_ingrediente()) {
                        contieneIngredienteEscaneado = true;
                        break;
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
                card.setOnMouseClicked(event -> {
                    infoCardRecetaSelec = receta;
                    escanearPane.setVisible(false);
                    infoCardPane.setVisible(true);
                    infoCardIngredientePane.setVisible(true);
                    mostrarInfoCardReceta();
                });
                
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
        hbInfoCardCodigoEscanearPane.setVisible(true);

        if (listaCodigos.isEmpty()) {
            Label noEncontrado = new Label("No se ha encontrado ninguna referencia");
            infoCardCodigoEscanearPane.getChildren().add(noEncontrado);
        } else {
            Codigo cod = listaCodigos.get(0);

            if (FuncionesRepetidas.insertarUsuarioCodigo(usuario, codigoLeido)) {
                actualizarListaIngredientesEscaneados(usuario);
            }

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
            Image img = new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png"));
            imagen.setImage(img);
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
                Image img = new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png"));
                imagen.setImage(img);
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

            for (var item : alergenosSeleccionadosSubirReceta.getItems()) {
                Alergeno alergeno = (Alergeno) item;
                if (!FuncionesRepetidas.insertarRecetaAlergeno(idReceta, alergeno.getId_alergeno())) {
                    FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar los alérgenos", 
                        "No se pudieron guardar todos los alérgenos de la receta");
                    return;
                }
            }

            for (int i = 0; i < ingredientesSeleccionadosList.size(); i++) {
                Ingrediente ingrediente = ingredientesSeleccionadosList.get(i);
                HBox hbox = (HBox) ingredientesSeleccionadosSubirReceta.getChildren().get(i);
                
                System.out.println("HBox " + i + " contiene:");
                for (int j = 0; j < hbox.getChildren().size(); j++) {
                    System.out.println("  Elemento " + j + ": " + hbox.getChildren().get(j).getClass().getSimpleName());
                }
                
                String cantidad = "1";
                for (Node node : hbox.getChildren()) {
                    if (node instanceof Label) {
                        Label label = (Label) node;
                        if (label.getText().matches("\\d+")) {
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
        
        lblInputNombrePerfilPane.setTooltip(new Tooltip("No puede contener caracteres especiales"));
        lblInputCorreoPerfilPane.setTooltip(new Tooltip("Tiene que ser un correo válido"));
        lblInputContraseñaPerfilPane.setTooltip(new Tooltip("Tiene que tener un mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 1 número y 1 símbolo"));
        lblSelectCiudadPerfilPane.setTooltip(new Tooltip("Añade tu ciudad"));
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

    
    // JUEGOS PANE:
    @FXML private void btnJuego() {
        try {
            
            URL url = getClass().getResource("/vistas/JuegosPage.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            ControladorJuegosPage controlador = loader.getController();
            controlador.setUsuario(usuario);
            Thread musicaThread = new Thread(() -> {
                try {
                    Thread.sleep(500);

                    Platform.runLater(() -> {
                        if (controlador != null) {
                            controlador.iniciarMusica();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            musicaThread.start();
            stage.setOnCloseRequest(event -> {
                if (controlador != null) {
                    controlador.pararMusica();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // AJUSTES PANE:
    public void mostrarAjustesPane(VBox paneMostrar) {
        inicioAjustesPane.setVisible(false);
        condicionesPane.setVisible(false);
        ajustesJuegoPane.setVisible(false);
        correoSoportePane.setVisible(false);
        informacionAppSoportePane.setVisible(false);
        manualPane.setVisible(false);
        
        paneMostrar.setVisible(true);
    } 
    
    private void restablecerPreferencias() {
        try {
            Map<String, String> cacheData = FuncionesRepetidas.leerSesionCache();
            String usuario = cacheData.get("Usuario");
            
            if (usuario != null) {
                FuncionesRepetidas.guardarSesionCache("Usuario", usuario);
                FuncionesRepetidas.guardarSesionCache("tema", "claro");
                
                aplicarTemaClaro();
                
                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "Preferencias restablecidas", "¡Éxito! /n Las preferencias han sido restablecidas correctamente.");
            }
            
        } catch (Exception e) {
            
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al restablecer las referencias", "Error al restablecer preferencias. /n Ha ocurrido un error al restablecer las preferencias.");
            System.err.println("Error al restablecer preferencias: " + e.getMessage());
        }
    }
    
    private void cerrarSesion() {
        try {
            Path sessionPath = Paths.get("sesionCache.txt");
            if (Files.exists(sessionPath)) {
                Files.delete(sessionPath);
            }
            
            Stage stage = (Stage) btnAceptarCondiciones.getScene().getWindow();
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/LoginPage.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.show();
                
                Platform.runLater(() -> {
                    stage.close();
                });
                
            } catch (IOException e) {
                System.err.println("Error al cargar la página de inicio: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {     
            e.printStackTrace();
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al cerrar sesión", "Ha ocurrido un error al cerrar la sesión.");
        }
    }
    
    private void borrarPerfil(){
        try {
            Path sessionPath = Paths.get("sesionCache.txt");
            if (Files.exists(sessionPath)) {
                Files.delete(sessionPath);
            }
            
            
            FuncionesRepetidas.borrarFavoritos(usuario.getId_usuario());
            FuncionesRepetidas.borrarUsuarioAlergeno(usuario.getId_usuario());
            FuncionesRepetidas.borrarUsuarioCodigo(usuario.getId_usuario());
            FuncionesRepetidas.actualizarDarBajaUsuario(usuario.getId_usuario());
            
            Stage stage = (Stage) btnAceptarCondiciones.getScene().getWindow();
            
            Platform.exit();
            
        } catch (Exception e) {     
            e.printStackTrace();
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al borrar el perfil", "Ha ocurrido un error, no se ha podido borrar este perfil.");
        }
    }
    
    private void cargarEstadoSonido() {
        Map<String, String> cacheData = FuncionesRepetidas.leerSesionCache();
        
        if (!cacheData.containsKey("sonido_juegos")) {
            sonidoActivado = true;
            FuncionesRepetidas.guardarSesionCache("sonido_juegos", "true");
        } else {
            sonidoActivado = Boolean.parseBoolean(cacheData.get("sonido_juegos"));
        }
        
        if (!cacheData.containsKey("volumen_juegos")) {
            volumen = 1.0;
            FuncionesRepetidas.guardarSesionCache("volumen_juegos", "1.0");
        } else {
            volumen = Double.parseDouble(cacheData.get("volumen_juegos"));
        }
        
        volumenProgressBar.setProgress(volumen);
        actualizarIconoSonido();
    }
    
    private void reproducirMusica() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            
            String projectPath = System.getProperty("user.dir");
            String musicPath = projectPath + "/src/main/resources/assets/musica/musicaJuego.mp3";
            File musicFile = new File(musicPath);
            
            if (!musicFile.exists()) {
                System.out.println("No se encontró el archivo de música");
                return;
            }
            
            Media media = new Media(musicFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(sonidoActivado ? volumen : 0);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al reproducir música: " + e.getMessage());
        }
    }
    
    private void toggleSonido() {
        sonidoActivado = !sonidoActivado;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(sonidoActivado ? volumen : 0);
        }
        actualizarIconoSonido();
        guardarEstadoSonido();
    }
    
    private void actualizarIconoSonido() {
        svgSonidoAjustesJuego.setContent(sonidoActivado ? svgConSonido : svgSinSonido);
    }
    
    private void ajustarVolumen(double x) {
        double width = volumenProgressBar.getWidth();

        // x / width da un valor entre 0 y 1
        // Math.max(0, Math.min(1, ...)) asegura que el valor esté entre 0 y 1
        volumen = Math.max(0, Math.min(1, x / width));

        volumenProgressBar.setProgress(volumen);

        if (mediaPlayer != null && sonidoActivado) {
            mediaPlayer.setVolume(volumen);
        }

        guardarEstadoSonido();
    }
    
    private void guardarEstadoSonido() {
        FuncionesRepetidas.guardarSesionCache("sonido_juegos", String.valueOf(sonidoActivado));
        FuncionesRepetidas.guardarSesionCache("volumen_juegos", String.valueOf(volumen));
    }
    
    public void pararMusica() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
    
    @FXML private void btnGuardarAjustesJuego() {
        guardarEstadoSonido();
        FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "¡Éxito!", "Has guardado los ajustes. ");
        pararMusica();
    }
    
    @FXML private void btnCancelarAjustesJuego() {
        cargarEstadoSonido();
        mostrarAjustesPane(inicioAjustesPane);
        pararMusica();
    }
    
    private void enviarCorreo() {
        String mensaje = mensajeCorreoSoportePane.getText();
        String asunto = asuntoCorreoSoportePane.getText();

        if (mensaje == null || mensaje.trim().isEmpty()) {
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, escribe un mensaje");
            return;
        }

        if (asunto == null || asunto.trim().isEmpty()) {
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, escribe un asunto");
            return;
        }

        FuncionesRepetidas.enviarCorreoSoporte(mensaje, asunto, accionActualCorreo, usuario);
    }
    
    
    // PERFIL PANE:
    private void mostrarPerfilPane(HBox paneHbox){
        inicioPerfilPane.setVisible(false);
        misFavPerfilPane.setVisible(false);

        paneHbox.setVisible(true);
    }
    
    private void toggleEdicionPerfil() {
        inputNombrePerfilPane.clear();
        inputCorreoPerfilPane.clear();
        inputContraseñaPerfilPane.clear();
        
        boolean modoEdicion = !inputNombrePerfilPane.isVisible();

        inputNombrePerfilPane.setVisible(modoEdicion);
        inputNombrePerfilPane.setManaged(modoEdicion);
        lblInputNombrePerfilPane.setVisible(modoEdicion);
        lblInputNombrePerfilPane.setManaged(modoEdicion);
 
        inputCorreoPerfilPane.setVisible(modoEdicion);
        inputCorreoPerfilPane.setManaged(modoEdicion);
        lblInputCorreoPerfilPane.setVisible(modoEdicion);
        lblInputCorreoPerfilPane.setVisible(modoEdicion);
        
        inputContraseñaPerfilPane.setVisible(modoEdicion);
        inputContraseñaPerfilPane.setManaged(modoEdicion);
        lblInputContraseñaPerfilPane.setVisible(modoEdicion);
        lblInputContraseñaPerfilPane.setManaged(modoEdicion);
        
        selectCiudadPerfilPane.setVisible(modoEdicion);
        selectCiudadPerfilPane.setManaged(modoEdicion);
        lblSelectCiudadPerfilPane.setVisible(modoEdicion);
        lblSelectCiudadPerfilPane.setManaged(modoEdicion);
        
        cajaGuardarEditarPerfilPane.setVisible(modoEdicion);
        cajaGuardarEditarPerfilPane.setManaged(modoEdicion);
        
        editarMisAlergiasPerfilPane.setVisible(modoEdicion);
        editarMisAlergiasPerfilPane.setManaged(modoEdicion);
    
        nombrePerfilPane.setVisible(!modoEdicion);
        nombrePerfilPane.setManaged(!modoEdicion);
        correoPerfilPane.setVisible(!modoEdicion);
        correoPerfilPane.setManaged(!modoEdicion);
        contraseñaPerfilPane.setVisible(!modoEdicion);
        contraseñaPerfilPane.setManaged(!modoEdicion);
        ciudadPerfilPane.setVisible(!modoEdicion);
        ciudadPerfilPane.setManaged(!modoEdicion);

        if (modoEdicion) {
            inputNombrePerfilPane.setText(nombrePerfilPane.getText());
            inputCorreoPerfilPane.setText(correoPerfilPane.getText());
            selectCiudadPerfilPane.setValue(ciudadPerfilPane.getText());
            
            editarMisAlergiasPerfilPane.setPrefHeight(200);
            editarMisAlergiasPerfilPane.setMinHeight(200);
            editarMisAlergiasPerfilPane.setMaxHeight(200);
            
            configuracionesValicionesEditarPerfil();
            
            List<Alergeno> alergiasUsuario = FuncionesRepetidas.obtenerUsuarioAlergenos(usuario.getId_usuario());
            misAlergiasCajaPerfilPane.getChildren().clear();
            if (alergiasUsuario.isEmpty()) {
                Label lblNoAlergias = new Label("Aún no has añadido tus alergias");
                misAlergiasCajaPerfilPane.getChildren().add(lblNoAlergias);
            } else {
                for (Alergeno alergeno : alergiasUsuario) {
                    HBox alergenosBox = new HBox(10);
                    alergenosBox.setAlignment(Pos.CENTER_LEFT);
                    alergenosBox.setPadding(new Insets(5));

                    ImageView imgAlergeno = new ImageView(new Image(getClass().getResource(alergeno.getImagen_alergeno()).toExternalForm()));
                    imgAlergeno.setFitHeight(30);
                    imgAlergeno.setFitWidth(30);

                    Label lblAlergeno = new Label(alergeno.getNombre_alergeno());
                    lblAlergeno.setAlignment(Pos.CENTER_LEFT);

                    alergenosBox.getChildren().addAll(imgAlergeno, lblAlergeno);
                    misAlergiasCajaPerfilPane.getChildren().add(alergenosBox);
                }
            }
        }
    }
    
    private void configuracionesValicionesEditarPerfil() {
        String nombreActual = nombrePerfilPane.getText();
        String correoActual = correoPerfilPane.getText();
        String ciudadActual = ciudadPerfilPane.getText();

        if (nombreActual.isEmpty() || (!nombreActual.contains(" ") && nombreActual.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9_]+"))) {
            lblInputNombrePerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
        } else {
            lblInputNombrePerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        }

        if (correoActual.isEmpty() || correoActual.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            lblInputCorreoPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
        } else {
            lblInputCorreoPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        }

        lblSelectCiudadPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));

        lblInputContraseñaPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));

        inputNombrePerfilPane.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal.isEmpty() || (!newVal.contains(" ") && newVal.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9_]+"))) {
                    lblInputNombrePerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    lblInputNombrePerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
                comprobarValidacionesEditarPerfil();
            });
        });

        inputCorreoPerfilPane.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal.isEmpty() || newVal.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    lblInputCorreoPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    lblInputCorreoPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
                comprobarValidacionesEditarPerfil();
            });
        });

        inputContraseñaPerfilPane.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal.isEmpty() || FuncionesRepetidas.validarContraseña(newVal)) {
                    lblInputContraseñaPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    lblInputContraseñaPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
                comprobarValidacionesEditarPerfil();
            });
        });

        selectCiudadPerfilPane.valueProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                lblSelectCiudadPerfilPane.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                comprobarValidacionesEditarPerfil();
            });
        });
    }
    
    private boolean comprobarValidacionesEditarPerfil() {
        boolean nombreOk = lblInputNombrePerfilPane.getGraphic() != null && 
                          ((ImageView)lblInputNombrePerfilPane.getGraphic()).getImage().equals(iconoOk.getImage());
        boolean correoOk = lblInputCorreoPerfilPane.getGraphic() != null && 
                          ((ImageView)lblInputCorreoPerfilPane.getGraphic()).getImage().equals(iconoOk.getImage());
        boolean contraseñaOk = lblInputContraseñaPerfilPane.getGraphic() != null && 
                              ((ImageView)lblInputContraseñaPerfilPane.getGraphic()).getImage().equals(iconoOk.getImage());
        boolean ciudadOk = lblSelectCiudadPerfilPane.getGraphic() != null && 
                          ((ImageView)lblSelectCiudadPerfilPane.getGraphic()).getImage().equals(iconoOk.getImage());


        return (nombreOk && correoOk && contraseñaOk && ciudadOk);
    }
    

    private void configurarEdicionAlergias() {
        ObservableList<Alergeno> listaAlergenos = FuncionesRepetidas.obtenerListaAlergenos();
        editarMisAlergiasPerfilPane.setItems(listaAlergenos);
        editarMisAlergiasPerfilPane.setCellFactory(param -> crearCeldaAlergeno());

        editarMisAlergiasPerfilPane.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Platform.runLater(() -> {
                    if (misAlergiasCajaPerfilPane.getChildren().size() == 1 && 
                        misAlergiasCajaPerfilPane.getChildren().get(0) instanceof Label) {
                        misAlergiasCajaPerfilPane.getChildren().clear();
                    }
                    
                    boolean yaSeleccionado = false;
                    for (int i = 0; i < misAlergiasCajaPerfilPane.getChildren().size(); i++) {
                        HBox hbox = (HBox) misAlergiasCajaPerfilPane.getChildren().get(i);
                        Label lbl = (Label) hbox.getChildren().get(1);
                        if (lbl.getText().equals(newVal.getNombre_alergeno())) {
                            misAlergiasCajaPerfilPane.getChildren().remove(hbox);
                            yaSeleccionado = true;
                            break;
                        }
                    }

                    if (!yaSeleccionado) {
                        HBox alergenosBox = new HBox(10);
                        alergenosBox.setAlignment(Pos.CENTER_LEFT);
                        alergenosBox.setPadding(new Insets(5));

                        ImageView imgAlergeno = new ImageView(new Image(getClass().getResource(newVal.getImagen_alergeno()).toExternalForm()));
                        imgAlergeno.setFitHeight(30);
                        imgAlergeno.setFitWidth(30);

                        Label lblAlergeno = new Label(newVal.getNombre_alergeno());
                        lblAlergeno.setAlignment(Pos.CENTER_LEFT);

                        alergenosBox.getChildren().addAll(imgAlergeno, lblAlergeno);

                        misAlergiasCajaPerfilPane.getChildren().add(alergenosBox);
                    }
                    
                    if (misAlergiasCajaPerfilPane.getChildren().isEmpty()) {
                        Label lblNoAlergias = new Label("Aún no has añadido tus alergias");
                        misAlergiasCajaPerfilPane.getChildren().add(lblNoAlergias);
                    }
                    
                    editarMisAlergiasPerfilPane.getSelectionModel().clearSelection();
                });
            }
        });
    }
    
    private ListCell<Alergeno> crearCeldaAlergeno() {
        return new ListCell<Alergeno>() {
            @Override
            protected void updateItem(Alergeno alergeno, boolean empty) {
                super.updateItem(alergeno, empty);
                if (empty || alergeno == null) {
                    setGraphic(null);
                } else {
                    VBox alergenoBox = new VBox(5);
                    alergenoBox.setAlignment(Pos.CENTER);
                    alergenoBox.setPadding(new Insets(5));

                    HBox alergenosBox = new HBox(10);
                    alergenosBox.setAlignment(Pos.CENTER_LEFT);
                    alergenosBox.setPadding(new Insets(5));

                    ImageView imgAlergeno = new ImageView(new Image(getClass().getResource(alergeno.getImagen_alergeno()).toExternalForm()));
                    imgAlergeno.setFitHeight(30);
                    imgAlergeno.setFitWidth(30);

                    Label lblAlergeno = new Label(alergeno.getNombre_alergeno());
                    lblAlergeno.setAlignment(Pos.CENTER_LEFT);

                    alergenosBox.getChildren().addAll(imgAlergeno, lblAlergeno);
                    alergenoBox.getChildren().add(alergenosBox);

                    setGraphic(alergenoBox);
                }
            }
        };
    }
    
    @FXML  private void btnGuardarPerfilPane() {
        if (comprobarValidacionesEditarPerfil()) {
            boolean hayCambios = false;
            StringBuilder camposModificados = new StringBuilder();

            String nombreActual = nombrePerfilPane.getText();
            String emailActual = correoPerfilPane.getText();
            String contraseñaActual = usuario.getContraseña_usuario();
            String ciudadActual = ciudadPerfilPane.getText();

            if (!inputNombrePerfilPane.getText().equals(nombreActual)) {
                nombreActual = inputNombrePerfilPane.getText();
                nombrePerfilPane.setText(nombreActual);
                hayCambios = true;
                camposModificados.append("- Nombre\n");
            }
        
            if (!inputCorreoPerfilPane.getText().equals(emailActual)) {
                emailActual = inputCorreoPerfilPane.getText();
                correoPerfilPane.setText(emailActual);
                hayCambios = true;
                camposModificados.append("- Correo\n");
            }

            if (!inputContraseñaPerfilPane.getText().isEmpty()) {
                contraseñaActual = inputContraseñaPerfilPane.getText();
                hayCambios = true;
                camposModificados.append("- Contraseña\n");
            }
        
            String nuevaCiudad = selectCiudadPerfilPane.getValue() != null ? 
                               selectCiudadPerfilPane.getValue().toString() : null;
            if (!nuevaCiudad.equals(ciudadActual)) {
                ciudadActual = nuevaCiudad;
                ciudadPerfilPane.setText(ciudadActual != null ? ciudadActual : "-");
                hayCambios = true;
                camposModificados.append("- Ciudad\n");
            }

            if (editarMisAlergiasPerfilPane.isVisible()) {
                List<Alergeno> alergenosSeleccionados = new ArrayList<>();
                for (int i = 0; i < misAlergiasCajaPerfilPane.getChildren().size(); i++) {
                    HBox hbox = (HBox) misAlergiasCajaPerfilPane.getChildren().get(i);
                    Label lbl = (Label) hbox.getChildren().get(1);
                    for (Alergeno a : editarMisAlergiasPerfilPane.getItems()) {
                        if (a.getNombre_alergeno().equals(lbl.getText())) {
                            alergenosSeleccionados.add(a);
                            break;
                        }
                    }
                }

                FuncionesRepetidas.guardarAlergenosUsuario(usuario.getId_usuario(), alergenosSeleccionados);
                hayCambios = true;
                camposModificados.append("- Alergias\n");
            }

            
            if (hayCambios) {
                FuncionesRepetidas.actualizarUsuarioDatos(usuario.getId_usuario(), nombreActual, emailActual, contraseñaActual, ciudadActual);

                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "Cambios guardados", "Se han guardado los siguientes cambios:\n"+camposModificados.toString());
                toggleEdicionPerfil();
                
            } else {
                inputNombrePerfilPane.setText(nombrePerfilPane.getText());
                inputCorreoPerfilPane.setText(correoPerfilPane.getText());
                inputContraseñaPerfilPane.setText("");
                selectCiudadPerfilPane.setValue(null);
                toggleEdicionPerfil();
            }
        }
    }
    
    @FXML  private void btnCancelarPerfilPane() {
        inputNombrePerfilPane.setText(nombrePerfilPane.getText());
        inputCorreoPerfilPane.setText(correoPerfilPane.getText());
        inputContraseñaPerfilPane.setText("");
        selectCiudadPerfilPane.setValue(ciudadPerfilPane.getText());
        
        if (editarMisAlergiasPerfilPane.isVisible()) {
            misAlergiasCajaPerfilPane.getChildren().clear();

            editarMisAlergiasPerfilPane.setVisible(false);

            List<Alergeno> alergiasUsuario = FuncionesRepetidas.obtenerUsuarioAlergenos(usuario.getId_usuario());
            for (Alergeno alergeno : alergiasUsuario) {
                HBox alergenoBox = new HBox(5);
                ImageView imgAlergeno = new ImageView(new Image(getClass().getResource(alergeno.getImagen_alergeno()).toExternalForm()));
                imgAlergeno.setFitHeight(20);
                imgAlergeno.setFitWidth(20);
                Label lblAlergeno = new Label(alergeno.getNombre_alergeno());
                alergenoBox.getChildren().addAll(imgAlergeno, lblAlergeno);
                misAlergiasCajaPerfilPane.getChildren().add(alergenoBox);
            }
        }

        toggleEdicionPerfil();
    }
    
    private void cargarFavoritosPerfil() {
        ObservableList<Favoritos> favoritos = FuncionesRepetidas.obtenerFavoritosUsuario(usuario.getId_usuario());
        HBox contenedorFavoritos = (HBox) ((ScrollPane)cajaVariosFavPerfilPane.getChildren().get(0)).getContent();
        contenedorFavoritos.getChildren().clear();

        if (favoritos.isEmpty()) {
            Label lblNoFavoritos = new Label("Aún no tienes favoritos");
            contenedorFavoritos.getChildren().add(lblNoFavoritos);
            return;
        }

        ObservableList<Receta> todasRecetas = FuncionesRepetidas.obtenerListaRecetas();
        ObservableList<Ingrediente> todosIngredientes = FuncionesRepetidas.obtenerListaIngredientes();
        ObservableList<Restaurante> todosRestaurantes = FuncionesRepetidas.obtenerListaRestaurantes();

        int contador = 0;
        for (Favoritos favorito : favoritos) {
            if (contador >= 8) break;

            switch (favorito.getTipo_objeto()) {
                case RECETA:
                    for (Receta receta : todasRecetas) {
                        if (receta.getId_receta() == favorito.getId_objeto()) {
                            VBox cardReceta = FuncionesRepetidas.crearCardReceta(usuario, receta);
                            contenedorFavoritos.getChildren().add(cardReceta);
                            contador++;
                            break;
                        }
                    }
                    break;
                case INGREDIENTE:
                    for (Ingrediente ingrediente : todosIngredientes) {
                        if (ingrediente.getId_ingrediente() == favorito.getId_objeto()) {
                            VBox cardIngrediente = FuncionesRepetidas.crearCardIngrediente(usuario, ingrediente);
                            contenedorFavoritos.getChildren().add(cardIngrediente);
                            contador++;
                            break;
                        }
                    }
                    break;
                case RESTAURANTE:
                    for (Restaurante restaurante : todosRestaurantes) {
                        if (restaurante.getId_restaurante() == favorito.getId_objeto()) {
                            VBox cardRestaurante = FuncionesRepetidas.crearCardRestaurante(usuario, restaurante);
                            contenedorFavoritos.getChildren().add(cardRestaurante);
                            contador++;
                            break;
                        }
                    }
                    break;
            }
        }
    }
    
    private List<CheckBox> listaCheckTipoFav = new ArrayList<>(); 
    public void cargarFiltrosTipoFav(List<String> opciones) {
        for (String texto : opciones) {
            CheckBox cb = new CheckBox(texto);
            cb.setOnAction(e -> actualizarCardsFavoritos());
            listaCheckTipoFav.add(cb);
            vboxTipoFiltrarFav.getChildren().add(cb);
        }
    }
    
    private void cargarFiltrosFavoritos() {
        vboxTipoFiltrarFav.getChildren().clear();
        vboxTipoDietaFiltrarFav.getChildren().clear();
        vboxAlergenosFiltrarFav.getChildren().clear();
        listaCheckTipoFav.clear();
        
        for (String texto : ObservableListas.listaFiltrar) {
            CheckBox cb = new CheckBox(texto);
            cb.getStyleClass().add("checkBox");
            cb.setOnAction(e -> actualizarCardsFavoritos());
            listaCheckTipoFav.add(cb);
            vboxTipoFiltrarFav.getChildren().add(cb);
        }

        cargarFiltros(ObservableListas.listaTiposRecetas, vboxTipoDietaFiltrarFav);
        cargarFiltros(ObservableListas.listaAlergenos, vboxAlergenosFiltrarFav);

        for (CheckBox cb : getChecksFromVBox(vboxTipoDietaFiltrarFav)) {
            cb.setOnAction(e -> actualizarCardsFavoritos());
        }
        for (CheckBox cb : getChecksFromVBox(vboxAlergenosFiltrarFav)) {
            cb.setOnAction(e -> actualizarCardsFavoritos());
        }
    }
    
    public String getTipoSeleccionadoFav() {
        for (CheckBox cb : listaCheckTipoFav) {
            if (cb.isSelected()) {
                return cb.getText();
            }
        }
        return "";
    }
    
    private List<CheckBox> getChecksFromVBox(VBox vbox) {
        List<CheckBox> checks = new ArrayList<>();
        for (Object child : vbox.getChildren()) {
            if (child instanceof CheckBox) {
                checks.add((CheckBox) child);
            }
        }
        return checks;
    }
    
    public void actualizarCardsFavoritos() {
        flowRecetasFavoritosPerfilPane.getChildren().clear();

        String tipoSeleccionado = getTipoSeleccionadoFav();
        List<String> tiposDietaSeleccionados = getChecksSeleccionados(vboxTipoDietaFiltrarFav);
        List<String> alergenosSeleccionados = getChecksSeleccionados(vboxAlergenosFiltrarFav);

        boolean buscarRecetas = tipoSeleccionado.equals("Receta") || tipoSeleccionado.isEmpty();
        boolean buscarIngredientes = tipoSeleccionado.equals("Ingrediente") || tipoSeleccionado.isEmpty();
        boolean buscarRestaurantes = tipoSeleccionado.equals("Restaurante") || tipoSeleccionado.isEmpty();

        ObservableList<Favoritos> favoritos = FuncionesRepetidas.obtenerFavoritosUsuario(usuario.getId_usuario());
        ObservableList<Receta> todasRecetas = FuncionesRepetidas.obtenerListaRecetas();
        ObservableList<Ingrediente> todosIngredientes = FuncionesRepetidas.obtenerListaIngredientes();
        ObservableList<Restaurante> todosRestaurantes = FuncionesRepetidas.obtenerListaRestaurantes();
        
        for (Favoritos favorito : favoritos) {
        switch (favorito.getTipo_objeto()) {
            case RECETA:
                if (buscarRecetas) {
                    for (Receta receta : todasRecetas) {
                        if (receta.getId_receta() == favorito.getId_objeto()) {
                            boolean pasaAlergenos = recetaNoTieneAlergenos(receta, alergenosSeleccionados);
                            boolean pasaTipoReceta = tiposDietaSeleccionados.isEmpty() || 
                                tiposDietaSeleccionados.contains(receta.getTipo_receta());

                            if (pasaAlergenos && pasaTipoReceta) {
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
                                    flowRecetasFavoritosPerfilPane.getChildren().add(card);
                                }
                            }
                            break;
                        }
                    }
                }
                break;

                case INGREDIENTE:
                if (buscarIngredientes) {
                    for (Ingrediente ingrediente : todosIngredientes) {
                        if (ingrediente.getId_ingrediente() == favorito.getId_objeto()) {
                            boolean noTipoReceta = tiposDietaSeleccionados.isEmpty();
                            boolean pasaAlergenos = true;

                            if (!alergenosSeleccionados.isEmpty()) {
                                for (String alergNombre : alergenosSeleccionados) {
                                    String tipoAlergeno = ingrediente.getTipo_alergeno_ingrediente();
                                    if (tipoAlergeno != null && !tipoAlergeno.isEmpty() && 
                                        tipoAlergeno.equalsIgnoreCase(alergNombre)) {
                                        pasaAlergenos = false;
                                        break;
                                    }
                                }
                            }

                            if (pasaAlergenos && noTipoReceta) {
                                VBox card = FuncionesRepetidas.crearCardIngrediente(usuario, ingrediente);
                                if (card != null) {
                                    card.setOnMouseClicked(event -> {
                                        mostrarPane(infoCardPane);
                                        infoCardIngredientePane.setVisible(true);
                                        infoCardRecetaPane.setVisible(false);
                                        infoCardRestaurantePane.setVisible(false);

                                        infoCardIngredienteSelec = ingrediente;
                                        mostrarInfoCardIngrediente();
                                    });
                                    flowRecetasFavoritosPerfilPane.getChildren().add(card);
                                }
                            }
                            break;
                        }
                    }
                }
                break;
                case RESTAURANTE:
                if (buscarRestaurantes) {
                    for (Restaurante restaurante : todosRestaurantes) {
                        if (restaurante.getId_restaurante() == favorito.getId_objeto()) {
                            boolean noDieta = tiposDietaSeleccionados.isEmpty();
                            boolean noAlergenos = alergenosSeleccionados.isEmpty();

                            if (noDieta && noAlergenos) {
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
                                    flowRecetasFavoritosPerfilPane.getChildren().add(card);
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    public void mostrarDonutFavoritos() {
        ObservableList<Favoritos> favoritos = FuncionesRepetidas.obtenerFavoritosUsuario(usuario.getId_usuario());
        ObservableList<Receta> todasRecetas = FuncionesRepetidas.obtenerListaRecetas();

        Map<String, Integer> datos = new HashMap<>();
        int total = 0;

        for (Favoritos favorito : favoritos) {
            if (favorito.getTipo_objeto() == Favoritos.TipoObjeto.RECETA) {
                for (Receta receta : todasRecetas) {
                    if (receta.getId_receta() == favorito.getId_objeto()) {
                        String tipo = receta.getTipo_receta();
                        if (tipo == null || tipo.isEmpty()) tipo = "Sin tipo";
                        datos.put(tipo, datos.getOrDefault(tipo, 0) + 1);
                        total++;
                        break;
                    }
                }
            }
        }

        graficoCajaPerfilPane.getChildren().removeIf(node -> node instanceof Canvas || node instanceof HBox);

        if (total == 0) {
            return;
        }

        double donutSize = 160;
        double width = graficoCajaPerfilPane.getWidth() > 0 ? graficoCajaPerfilPane.getWidth() : 200;

        double labelHeight = 0;
        if (!graficoCajaPerfilPane.getChildren().isEmpty() && graficoCajaPerfilPane.getChildren().get(0) instanceof Label) {
            Label label = (Label) graficoCajaPerfilPane.getChildren().get(0);
            labelHeight = label.getHeight() > 0 ? label.getHeight() : 30;
        }

        double centerX = width / 2;
        double centerY = labelHeight + 20 + donutSize / 2;

        double radius = donutSize / 2;
        double innerRadius = radius * 0.7;

        Canvas canvas = new Canvas(width, donutSize + 80 + labelHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double startAngle = 90;
        Color[] colores = { Color.MEDIUMPURPLE, Color.ORANGE, Color.LIGHTGREEN, Color.CORNFLOWERBLUE, Color.PINK, Color.DARKCYAN, Color.GOLD, Color.BROWN, Color.DARKRED, Color.DARKMAGENTA };

        int idx = 0;
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            double porcentaje = (entry.getValue() * 360.0) / total;
            gc.setFill(colores[idx % colores.length]);
            gc.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, -porcentaje, javafx.scene.shape.ArcType.ROUND);
            startAngle -= porcentaje;
            idx++;
        }

        gc.setFill(Color.WHITE);
        gc.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);

        gc.setFill(Color.GRAY);
        gc.setFont(javafx.scene.text.Font.font("Arial", radius * 0.4));
        String totalStr = String.valueOf(total);
        double textWidth = totalStr.length() * radius * 0.4 * 0.6;
        gc.fillText(totalStr, centerX - textWidth / 2, centerY + 10);

        graficoCajaPerfilPane.getChildren().add(canvas);

        HBox leyenda = new HBox(15);
        leyenda.setLayoutY(centerY + radius + 10);
        leyenda.setLayoutX(centerX - (datos.size() * 60) / 2.0);

        idx = 0;
        for (String tipo : datos.keySet()) {
            HBox item = new HBox(5);
            Circle punto = new Circle(7, colores[idx % colores.length]);
            Label lbl = new Label(tipo);
            lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            item.getChildren().addAll(punto, lbl);
            leyenda.getChildren().add(item);
            idx++;
        }
        graficoCajaPerfilPane.getChildren().add(leyenda);
    }
    
    
    
    
    
    
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
}
