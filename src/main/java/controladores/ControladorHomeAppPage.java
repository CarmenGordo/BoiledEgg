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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import modelos.Codigo;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
    @FXML private AnchorPane homePane,buscadorPane, infoCardPane, escanearPane;
    @FXML private Button btnBuscar;
    @FXML private HBox btnHome, btnEscanear, cajaBuscar;
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
    @FXML private HBox btnCamaraCodigoEscanearPane, btnSubirCodigoEscanearPane, infoCodigoEscanearPane, btnSubidaCodigoEscanearPane;
    @FXML private VBox codigoEscanearPane, inicioEscanearPane, camaraCodigoEscanearPane, subirCodigoEscanearPane, infoCardCodigoEscanearPane;
    @FXML private ImageView imgInfoCardCodigo, imgSubirCodigoEscanearPane;
    @FXML private Label nombreInfoCardCodigo, codigoInfoCardCodigo, tiendaInfoCardCodigo, marcaInfoCardCodigo, origenInfoCardCodigo;
    @FXML private ScrollPane scrollPaneInfoCardCodigo;
            
            
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
    
    
    
    @FXML
    private ImageView cameraImageView;
    private Webcam webcam;
        private ExecutorService executor = Executors.newSingleThreadExecutor();

        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Por defecto:
        mostrarPane(homePane);
        infoCodigoEscanearPane.setVisible(false);
        camaraCodigoEscanearPane.setVisible(false);
        subirCodigoEscanearPane.setVisible(false);
        inicioEscanearPane.setVisible(true);


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
                    mostrarInfoCardCodigo(codigo);
                    System.out.println("codigo:" + codigo);
                }
            }
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
        mostrarPane(buscadorPane);
        actualizarCards();
    }
    
    public void mostrarPane(AnchorPane paneMostrar) {
        homePane.setVisible(false);
        buscadorPane.setVisible(false);
        infoCardPane.setVisible(false);
        escanearPane.setVisible(false);
        
        
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

        // Filtrar recetas
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

        // Filtrar ingredientes
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

        
        // Filtar por restau
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
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            infoCardRecetaImagen.setImage(new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png")));
        } else {
            URL urlImagenReceta = getClass().getResource(rutaImagen);
            if (urlImagenReceta == null) {
                infoCardRecetaImagen.setImage(new Image(getClass().getResourceAsStream("/assets/img_otros/noImagen.png")));

            } else {
                infoCardRecetaImagen.setImage(new Image(urlImagenReceta.toExternalForm()));
            }
        }
   
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
    
    @FXML private void btnAñadirValoracionReceta() {
       
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
    
    public void mostrarInfoCardCodigo(String codigoLeido) {
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
                    break;
                }
            }

            nombreInfoCardCodigo.setText(nombreIngrediente);

            if (rutaImagen != null) {
                Image img = new Image(getClass().getResourceAsStream(rutaImagen));
                imgInfoCardCodigo.setImage(img);
            }

            infoCardCodigoEscanearPane.getChildren().add(scrollPaneInfoCardCodigo);
        }
    }
    




 @FXML
    public void escanearConCamara() {
        System.out.println("Botón presionado");
        Platform.runLater(() -> {
            webcam = Webcam.getDefault();
            webcam.open();

            executor.submit(() -> {
                while (webcam.isOpen()) {
                    BufferedImage imagen = webcam.getImage();
                    String codigo = escanearCodigo(imagen);

                    Platform.runLater(() -> actualizarUI(imagen, codigo));

                    if (codigo != null) {
                        webcam.close();
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    private String escanearCodigo(BufferedImage imagen) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(imagen);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result resultado = new MultiFormatReader().decode(bitmap);
            return resultado.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    private void actualizarUI(BufferedImage imagen, String codigo) {
        Image fxImage = SwingFXUtils.toFXImage(imagen, null);
        cameraImageView.setImage(fxImage);
        if (codigo != null) {
            System.out.println("Código detectado: " + codigo);
        }
    }


   
   

    
    
   
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    
}
