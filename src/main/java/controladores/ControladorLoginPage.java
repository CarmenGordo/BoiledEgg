package controladores;

import conexion.ConexionBD;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelos.IconoPerfil;
import modelos.Restaurante;
import modelos.Usuario;
import org.controlsfx.validation.Severity;

import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import utils.FuncionesRepetidas;
import utils.ObservableListas;


/**
 *
 * @author carmen_gordo
 */
public class ControladorLoginPage implements Initializable {
    
    @FXML private Parent root;
    @FXML private StackPane stackPane;
    @FXML private Pane loginPage, registroUsuarioPage, registroRestaurantePage, inputsPane, cambiarIconoPane;
    @FXML private Label enlaceRegistro, enlaceRegistroRestaurante, enlaceLogin,enlaceLogin2, etiqIconoNombre, etiqIconoEmail, etiqIconoContraseña, etiqIconoRepContraseña, etiqIconoTipo,lblPez, lblDefecto, lblColiflor, lblUva, lblSandwich, lblAlmendra, etiqIconoNombreRestaurante, etiqIconoEmailRestaurante,etiqIconoContraseñaRestaurante,etiqIconoRepContraseñaRestaurante, etiqIconoTipoRestaurante;
    @FXML private ImageView iconoPerfil, iconoVerContraseña, iconoVerContraseñaRestaurante, imgPez, imgDefecto, imgColiflor, imgUva, imgSandwich, imgAlmendra;
    @FXML private TextField nombreInput, emailInput, contraseñaInput, textoVisibleInput, repetirContraseñaInput, nombreInputLogin, contraseñaInputLogin, nombreRestauranteInput,emailRestauranteInput, contraseñaRestauranteInput, repetirContraseñaRestauranteInput;
    @FXML private ComboBox tipoRestauranteSelect, ciudadRestaurante;
    @FXML private Button btnRegistro, btnLogin, btnConfirmarCambiarIcono;
    @FXML private VBox iconoPez, iconoDefecto, iconoColiflor, iconoUva, iconoSandwich, iconoAlmendra;
    


    public Connection conexion;
    public Statement st;
    public ResultSet rs;
    private Stage stage;
    private Image imagenSeleccionada = null;
    private Map<VBox, IconoPerfil> iconosMap = new HashMap<>();
    private IconoPerfil iconoSeleccionado;
    private boolean mostrar = false;
    // Iconos:
    ImageView iconoOk = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("assets/iconos/icono_ok.png")));
    ImageView iconoError = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("assets/iconos/icono_error.png")));
    Image imgCerrado = new Image(getClass().getResourceAsStream("/assets/iconos/candado_cerrado.png"));
    Image imgAbierto = new Image(getClass().getResourceAsStream("/assets/iconos/candado_abierto.png"));

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conexion = ConexionBD.getConexion();  
            if (conexion != null) {
                st = conexion.createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error de conexión");
            alert.setContentText("No se pudo establecer la conexión con la base de datos");
            alert.showAndWait();
        }
        
        // Por defecto:
        loginPage.setVisible(true);
        registroUsuarioPage.setVisible(false);
        registroRestaurantePage.setVisible(false);
        inputsPane.setVisible(true);
        cambiarIconoPane.setVisible(false);
        

        // Cambio de pane para mostrar la pagina de Registro o la de Login
        enlaceRegistro.setOnMouseClicked(event -> {
            loginPage.setVisible(false);
            registroRestaurantePage.setVisible(false);
            registroUsuarioPage.setVisible(true);
        });
        
        enlaceRegistroRestaurante.setOnMouseClicked(event -> {
            loginPage.setVisible(false);
            registroUsuarioPage.setVisible(false);
            registroRestaurantePage.setVisible(true);
        });

        enlaceLogin.setOnMouseClicked(event -> {
            registroUsuarioPage.setVisible(false);
            registroRestaurantePage.setVisible(false);
            loginPage.setVisible(true);
        });
        
        enlaceLogin2.setOnMouseClicked(event -> {
            registroUsuarioPage.setVisible(false);
            registroRestaurantePage.setVisible(false);
            loginPage.setVisible(true);
        });
        
        tipoRestauranteSelect.setItems(ObservableListas.listaTiposRestaurantes);
        ciudadRestaurante.setItems(ObservableListas.listaCiudades);
        
        cargarIconos();
        
        // Hacer clic en el icono de perfil = mostrar panel de cambio
        iconoPerfil.setOnMouseClicked(event -> {
            cambiarIconoPane.setVisible(true);
            inputsPane.setVisible(false);
        }); 
        
        
        ponerTooltips();
        
        // Validadores
        iconoOk.setFitHeight(16);
        iconoOk.setFitWidth(16);
        iconoError.setFitHeight(16);
        iconoError.setFitWidth(16);
        configurarValidaciones();
        configurarValidacionesRestaurante();

        configurarIconoVerContraseña();
    }
    
    //Para recoger el Stage del Main
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    

    private void cargarIconos() {
        try {
            Connection conexion = FuncionesRepetidas.iniciarConexion();
            ObservableList<IconoPerfil> listaIconos = FuncionesRepetidas.obtenerListaIconos();

            List<VBox> vboxList = List.of(iconoPez, iconoDefecto, iconoColiflor, iconoUva, iconoSandwich, iconoAlmendra);
            List<ImageView> imageList = List.of(imgPez, imgDefecto, imgColiflor, imgUva, imgSandwich, imgAlmendra);
            List<Label> labelList = List.of(lblPez, lblDefecto, lblColiflor, lblUva, lblSandwich, lblAlmendra);
            // Nuevo orden para el pane con  los icons
            Map<String, Integer> ordenIconos = Map.of("Pezqueñín", 0, "Huevín", 1,"Coli", 2, "Uvina", 3, "Mordisquitos", 4, "Almond", 5);

            for (int i = 0; i < listaIconos.size(); i++) {
                IconoPerfil icono = listaIconos.get(i);

                int index = ordenIconos.getOrDefault(icono.getNombre_icono(), -1);

                if (index >= 0 && index < vboxList.size()) {
                    VBox vbox = vboxList.get(index);
                    ImageView img = imageList.get(index);
                    Label lbl = labelList.get(index);

                    Image image = new Image(getClass().getResource(icono.getImagen_icono()).toExternalForm());
                    img.setImage(image);
                    lbl.setText(icono.getNombre_icono());

                    configurarSeleccion(vbox, icono);
                }
            }
        } catch (Exception e){
            System.err.println("Error al cargar iconos: "+e.getMessage());

        }
    }

    // Indicar que icono se ha elegido
    private void configurarSeleccion(VBox contenedor, IconoPerfil icono) {
        contenedor.setOnMouseClicked(event -> {
            Image img = new Image(getClass().getResource(icono.getImagen_icono()).toExternalForm());
            imagenSeleccionada = img;
            iconoSeleccionado = icono;
            System.out.println("imagenSeleccionada "+iconoSeleccionado);
                        
            borrarBordes();
            contenedor.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2px;");
            iconoPerfil.setImage(imagenSeleccionada);
        });
    }
    
    private void borrarBordes() {
        iconoPez.setStyle("");
        iconoDefecto.setStyle("");
        iconoColiflor.setStyle("");
        iconoUva.setStyle("");
        iconoSandwich.setStyle("");
        iconoAlmendra.setStyle("");
    }

    @FXML private void btnConfirmarCambiarIcono() {
        if (imagenSeleccionada != null) {
            iconoPerfil.setImage(imagenSeleccionada);
            System.out.println("Icono seleccionado: " + imagenSeleccionada.getUrl());
        }
        cambiarIconoPane.setVisible(false);
        inputsPane.setVisible(true);
    }

    // Tooltips:
    private void ponerTooltips(){
        Tooltip tooltip = new Tooltip("Cambia el icono");
        Tooltip.install(iconoPerfil, tooltip);
        etiqIconoNombre.setTooltip(new Tooltip("Solo puede tener letras, numeros y '_', y no puede estar vacío"));
        etiqIconoEmail.setTooltip(new Tooltip("No puede tener caracteres especiales o estar vacío"));
        etiqIconoContraseña.setTooltip(new Tooltip("Mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 1 número y 1 símbolo"));
        etiqIconoRepContraseña.setTooltip(new Tooltip("Debe de coincidir con la contraseña"));
        
        etiqIconoNombreRestaurante.setTooltip(new Tooltip("Introduce el nombre del restaurante"));
        etiqIconoEmailRestaurante.setTooltip(new Tooltip("Introduce el correo de la empresa"));
        etiqIconoContraseñaRestaurante.setTooltip(new Tooltip("Mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 1 número y 1 símbolo"));
        etiqIconoRepContraseñaRestaurante.setTooltip(new Tooltip("Debe de coincidir con la contraseña"));
        etiqIconoTipoRestaurante.setTooltip(new Tooltip("Tienes de elegir el tipo del restaurante"));
    }
    
    // Validaciones:
    private void configurarValidaciones() {

        etiqIconoNombre.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIconoEmail.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIconoContraseña.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIconoRepContraseña.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));

        nombreInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                String texto = newVal.trim();
                if (!texto.isEmpty() && !newVal.contains(" ") && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9_]+")) {
                    etiqIconoNombre.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIconoNombre.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        emailInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal.trim().isEmpty() || !newVal.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    etiqIconoEmail.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                } else {
                    etiqIconoEmail.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                }
            });
        });

        contraseñaInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (FuncionesRepetidas.validarContraseña(newVal)) {
                    etiqIconoContraseña.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIconoContraseña.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        repetirContraseñaInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (FuncionesRepetidas.validarContraseña(contraseñaInput.getText()) && newVal.equals(contraseñaInput.getText())) {
                    etiqIconoRepContraseña.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIconoRepContraseña.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });
    }
    
    
    private void configurarValidacionesRestaurante() {
        etiqIconoNombreRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIconoEmailRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIconoContraseñaRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIconoRepContraseñaRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
        etiqIconoTipoRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));

        nombreRestauranteInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                String texto = newVal.trim();
                if (!texto.isEmpty() && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9_',!&]+")) {
                    etiqIconoNombreRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIconoNombreRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        emailRestauranteInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal.trim().isEmpty() || !newVal.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    etiqIconoEmailRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                } else {
                    etiqIconoEmailRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                }
            });
        });

        contraseñaRestauranteInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (FuncionesRepetidas.validarContraseña(newVal)) {
                    etiqIconoContraseñaRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIconoContraseñaRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        repetirContraseñaRestauranteInput.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (FuncionesRepetidas.validarContraseña(contraseñaRestauranteInput.getText()) && 
                    newVal.equals(contraseñaRestauranteInput.getText())) {
                    etiqIconoRepContraseñaRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIconoRepContraseñaRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });

        tipoRestauranteSelect.valueProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                String valorSeleccionado = (String) newVal;
                if (valorSeleccionado != null && !valorSeleccionado.trim().isEmpty()) {
                    etiqIconoTipoRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoOk.getImage()));
                } else {
                    etiqIconoTipoRestaurante.setGraphic(FuncionesRepetidas.clonarIcono(iconoError.getImage()));
                }
            });
        });
    }
    
    private void configurarIconoVerContraseña() {
        iconoVerContraseña.setImage(imgCerrado);
        contraseñaInput.textProperty().bindBidirectional(textoVisibleInput.textProperty());

        iconoVerContraseña.setOnMouseClicked(event -> {
            mostrar = !mostrar;

            if (mostrar) {
                iconoVerContraseña.setImage(imgAbierto);
                textoVisibleInput.setVisible(true);
                textoVisibleInput.setManaged(true);
                contraseñaInput.setVisible(false);
                contraseñaInput.setManaged(false);
            } else {
                iconoVerContraseña.setImage(imgCerrado);
                textoVisibleInput.setVisible(false);
                textoVisibleInput.setManaged(false);
                contraseñaInput.setVisible(true);
                contraseñaInput.setManaged(true);
            }
        });
    }

    @FXML private void btnRegistro(){
        configurarValidaciones();
        
        String nombre = nombreInput.getText().trim();
        String email = emailInput.getText().trim();
        String contraseña = contraseñaInput.getText().trim();
        String repetirContraseña = repetirContraseñaInput.getText().trim();
        
        registrarUsuario(nombre, email, contraseña);
    }
    
    private void registrarUsuario(String nombre, String email, String contraseña) {
        
        if (conexion != null) {
            if (verificarUsuarioExistente(email)) {
                System.err.println("-----------------error registrar usuario: ya existe un usuario con ese email");
                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ya existe un usuario con ese correo");
                return;
            }
            
            int icono = (iconoSeleccionado != null) ? iconoSeleccionado.getId_icono(): getIdPorNombre("Huevín");
            String query = "INSERT INTO Usuario (nombre_usuario, email_usuario, contraseña_usuario, nivel_usuario, "+"                                            puntos_usuario, icono_perfil_id) VALUES (?, ?, ?, ?, ?, ?)";
            
            try {
                PreparedStatement ps = conexion.prepareStatement(query);

                ps.setString(1, nombre);
                ps.setString(2, email);
                ps.setString(3, contraseña);
                ps.setInt(4, 1);
                ps.setInt(5, 0);
                ps.setInt(6, icono);

                int rowsInserted = ps.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Usuario registrado con exito");
                    FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "¡Perfecto!", "Te has registrado con éxito");
                } else {
                    System.err.println("No se ha registrado el usuario.");
                    FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se ha podido registrar el usuario");
                }

            } catch (SQLException e) {
                System.err.println("-----------------error registrar usuario: " + e.getMessage());
                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al registrar usuario, por favor compruba los campos");
            }

        } else {
            System.err.println("No se pudo conectar a la base de datos");
        }
    }
    
    private int getIdPorNombre(String nombreIcono) {
        try {
            String query = "SELECT id_icono FROM IconoPerfil WHERE nombre_icono = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, nombreIcono);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_icono");
        } catch (SQLException e) {
            System.err.println("Error obteniendo id de icono: " + e.getMessage());
        }
        return 1;
    }
    
    
    @FXML private void btnRegistroRestaurante() {
        configurarValidacionesRestaurante();

        String nombre = nombreRestauranteInput.getText().trim();
        String email = emailRestauranteInput.getText().trim();
        String contraseña = contraseñaRestauranteInput.getText().trim();
        String repetirContraseña = repetirContraseñaRestauranteInput.getText().trim();
        String tipo = (String) tipoRestauranteSelect.getValue();
        String ciudad = (String) ciudadRestaurante.getValue();
        String url = "";

        registrarRestaurante(nombre, email, contraseña, repetirContraseña, ciudad, tipo, url);
    }

    private void registrarRestaurante(String nombre, String email, String contraseña, String repetirContraseña,
                                  String ciudad, String tipo, String url) {

        if (conexion != null) {
            if (verificarRestauranteExistente(email)) {
                System.err.println("-----------------error registrar restaurante: ya existe un restaurante con ese email");
                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ya existe un restaurante con ese correo");
                return;
            }

            if (!contraseña.equals(repetirContraseña)) {
                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Las contraseñas no coinciden");
                return;
            }

            String query = "INSERT INTO Restaurante (nombre_restaurante, email_restaurante, contraseña_restaurante, ciudad_restaurante, tipo_restaurante, url_restaurante) VALUES (?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement ps = conexion.prepareStatement(query);

                ps.setString(1, nombre);
                ps.setString(2, email);
                ps.setString(3, contraseña);
                ps.setString(4, ciudad);
                ps.setString(5, (tipo != null && !tipo.trim().isEmpty()) ? tipo : null);
                ps.setString(6, (url != null && !url.trim().isEmpty()) ? url : null);

                int rowsInserted = ps.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Restaurante registrado con éxito");
                    FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "¡Perfecto!", "Te has registrado con éxito");
                } else {
                    FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se ha podido registrar el restaurante");
                }

            } catch (SQLException e) {
                System.err.println("-----------------error registrar restaurante: " + e.getMessage());
                FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al registrar restaurante");
            }

        } else {
            System.err.println("No se pudo conectar a la base de datos");
        }
    }


    private boolean verificarUsuarioExistente(String email) {
        String query = "SELECT email_usuario FROM Usuario WHERE email_usuario = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de usuario: " + e.getMessage());
            return false;
        }
    } 
    
    private boolean verificarRestauranteExistente(String email) {
        String query = "SELECT email_restaurante FROM Restaurante WHERE email_restaurante = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia del restaurante: " + e.getMessage());
            return false;
        }
    } 
    
    
    // LOGIN:
    @FXML private void btnLogin() {
        String nombre = nombreInputLogin.getText().trim();
        String contraseña = contraseñaInputLogin.getText().trim();

        if (nombre.isEmpty() || contraseña.isEmpty()) {
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, introduce usuario y contraseña");
            return;
        }

        Usuario usuario = loginComoUsuario(nombre, contraseña);
        if (usuario != null) {
            FuncionesRepetidas.guardarSesionCache("Usuario", usuario.getNombre_usuario());
            abrirVentanaApp("/vistas/HomeAppPage.fxml", "Inicio - Home", usuario, null);
            return;
        }

        Restaurante restaurante = loginComoRestaurante(nombre, contraseña);
        if (restaurante != null) {
            FuncionesRepetidas.guardarSesionCache("Restau", restaurante.getEmail_restaurante());
            abrirVentanaApp("/vistas/HomeAppRestaurantePage.fxml", "Inicio - Home Restaurante", null,restaurante);
            return;
        }

        FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "Error al iniciar sesión", "Por favor, verifica tus credenciales e inténtalo de nuevo");
    }
    
    // Buscar primero si es un usuario
    private Usuario loginComoUsuario(String nombre, String contraseña) {
        try {
            String query = "SELECT * FROM Usuario WHERE (email_usuario = ? OR nombre_usuario = ?) AND contraseña_usuario = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, nombre);
            ps.setString(3, contraseña);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre_usuario(rs.getString("nombre_usuario"));
                usuario.setEmail_usuario(rs.getString("email_usuario"));
                usuario.setContraseña_usuario(rs.getString("contraseña_usuario"));
                usuario.setNivel_usuario(rs.getInt("nivel_usuario"));
                usuario.setPuntos_usuario(rs.getInt("puntos_usuario"));
                usuario.setIcono_perfil_id(rs.getInt("icono_perfil_id"));
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("--------------------error login usuario: " + e.getMessage());
        }
        return null;
    }

    // Si no buscar como restaurante
    private Restaurante loginComoRestaurante(String email, String contraseña) {
        try {
            String query = "SELECT * FROM Restaurante WHERE email_restaurante = ? AND contraseña_restaurante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, contraseña);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Restaurante restaurante = new Restaurante();
                restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                restaurante.setNombre_restaurante(rs.getString("nombre_restaurante"));
                restaurante.setEmail_restaurante(rs.getString("email_restaurante"));
                restaurante.setContraseña_restaurante(rs.getString("contraseña_restaurante"));
                restaurante.setTipo_restaurante(rs.getString("tipo_restaurante"));
                restaurante.setCiudad_restaurante(rs.getString("ciudad_restaurante"));

                return restaurante;
            }

        } catch (SQLException e) {
            System.err.println("--------------------error login restaurante: " + e.getMessage());
        }
        return null;
    }



    //Si el registro o el login va bien, se abre directamente la ventana HomePage.fxml
    private void abrirVentanaApp(String rutaFXML, String tituloVentana, Usuario usuario, Restaurante restaurante) {
        URL fxml = getClass().getResource(rutaFXML);
            System.out.println("FXML path resolved to: " + fxml);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            

            if (usuario != null) {
                ControladorHomeAppPage controlador = loader.getController();
                controlador.inicializarUsuario(usuario);
            }
            
            if (restaurante != null) {
                ControladorHomeAppRestaurantePage controlador = loader.getController();
                controlador.inicializarRestaurante(restaurante);
            }

            Stage nuevaVentana = new Stage();
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.setTitle(tituloVentana);
            nuevaVentana.show();
            
            Stage stageActual = (Stage) btnRegistro.getScene().getWindow();
            stageActual.close();

        } catch (IOException e) {
            System.err.println("Error al cargar " + rutaFXML + ": " + e.getMessage());
        }
    }
    
    

    
}