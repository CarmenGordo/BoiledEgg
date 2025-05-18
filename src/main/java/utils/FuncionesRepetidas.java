package utils;

import conexion.ConexionBD;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import modelos.IconoPerfil;
import modelos.Restaurante;
import modelos.Usuario;
import java.util.Base64;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelos.Alergeno;
import modelos.Ingrediente;
import modelos.Receta;

/**
 *
 * @author carmen_gordo
 */
public class FuncionesRepetidas {
    
    //Variables: 
    public Connection conexion;
    
    // Rutas/archivos:
    public static String rutaSesionCache = "sesionCache.txt";
    public static String rutaTemaClaro = "/style/Style.css";
    public static String rutaTemaOscuro = "/style/StyleOscuro.css";
    
    
   
    
    
    public static Connection iniciarConexion() throws SQLException {
        Connection conexion = ConexionBD.getConexion();
        if (conexion == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error en la conexión", "No se pudo establecer la conexión, por favo rponga se en contacto con los distribuidores de la app");
        }
        return conexion;
    }
    
    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(mensaje);
        alerta.showAndWait();
    }
    
    

    // Funciones de Cache:
    public static void guardarSesionCache(String clave, String valor) {
        Map<String, String> cacheData = leerSesionCache();

        cacheData.put(clave, valor);

        StringBuilder contenido = new StringBuilder();
        cacheData.forEach((k, v) -> contenido.append(k).append(":").append(v).append("\n"));

        try {
            Files.writeString(Paths.get(rutaSesionCache), contenido.toString().trim(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error guardando en sesión cache: " + e.getMessage());
        }
    }

    public static Map<String, String> leerSesionCache() {
        Map<String, String> cacheData = new HashMap<>();
        Path sessionPath = Paths.get("sesionCache.txt");

        if (Files.exists(sessionPath)) {
            try {
                List<String> lineas = Files.readAllLines(sessionPath);
                for (String linea : lineas) {
                    if (linea.contains(":")) {
                        String[] partes = linea.split(":", 2);
                        String clave = partes[0].trim();
                        String valor = partes[1].trim();
                        cacheData.put(clave, valor);
                    }
                }
            } catch (IOException e) {
                System.err.println("No se pudo leer la sesión: " + e.getMessage());
            }
        }
        return cacheData;
    }


    
    
    public static boolean validarContraseña(String password) {
        if (password.length() < 8) return false;
        boolean mayus = false, minus = false, num = false, especial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) mayus = true;
            else if (Character.isLowerCase(c)) minus = true;
            else if (Character.isDigit(c)) num = true;
            else if ("!@#$%^&*()_+[]{}|;:,.<>?".indexOf(c) >= 0) especial = true;
        }
        return mayus && minus && num && especial;
    }
    
    public static ImageView clonarIcono(Image iconoBase) {
        ImageView icono = new ImageView(iconoBase);
        icono.setFitHeight(16);
        icono.setFitWidth(16);
        return icono;
    }
    
    
    
    // Funciones con Select:
    public static Usuario obtenerUsuarioPorNombre(String nombre) {
        String query = "SELECT * FROM Usuario WHERE nombre_usuario = ?";

        try (Connection conexion = iniciarConexion()) {
            if (conexion == null) {
                System.err.println("------error en obtenerUsuarioPorNombre(). FuncionesRepetidas");
                return null;
            }

            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setString(1, nombre);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_usuario"),
                            rs.getString("email_usuario"),
                            rs.getString("contraseña_usuario"),
                            rs.getInt("nivel_usuario"),
                            rs.getInt("puntos_usuario"),
                            rs.getInt("icono_perfil_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return null;
    }

    public static Restaurante obtenerRestaurantePorEmail(String email) {
        String query = "SELECT * FROM Restaurante WHERE email_restaurante = ?";

        try (Connection conexion = iniciarConexion()) {
            if (conexion == null) {
                System.err.println("------error en obtenerRestaurantePorEmail(). FuncionesRepetidas");
                return null; 
            }

            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setString(1, email);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new Restaurante(
                            rs.getInt("id_restaurante"),
                            rs.getString("nombre_restaurante"),
                            rs.getString("email_restaurante"),
                            rs.getString("contraseña_restaurante"),
                            rs.getString("ciudad_restaurante"),
                            rs.getString("tipo_restaurante"),
                            rs.getString("url_restaurante"),
                            rs.getInt("usuario_id")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener restaurante: " + e.getMessage());
        }
        return null;
    }
    
    public static ObservableList<IconoPerfil> obtenerListaIconos() {
        ObservableList<IconoPerfil> listaIconos = FXCollections.observableArrayList();

        try (Connection conexion = iniciarConexion()) {
            if (conexion == null) {
                System.err.println("------error en obtenerListaIconos(). FuncionesRepetidas");
            }

            String query = "SELECT id_icono, nombre_icono, imagen_icono FROM IconoPerfil";
            try (PreparedStatement ps = conexion.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    listaIconos.add(new IconoPerfil(
                        rs.getInt("id_icono"),
                        rs.getString("nombre_icono"),
                        rs.getString("imagen_icono"),
                        1, 1 // nivel requerido
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cargando iconos: " + e.getMessage());
        }

        return listaIconos;
    }
    
    
    public static ObservableList<Receta> obtenerListaRecetas() {
        ObservableList<Receta> recetas = FXCollections.observableArrayList();

        try (Connection conexion = iniciarConexion()) {
            if (conexion == null) {
                System.err.println("------error en obtenerListaRecetas(). FuncionesRepetidas");
            }

            String query = "SELECT * FROM Receta";
            try (PreparedStatement ps = conexion.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Receta receta = new Receta(
                        rs.getInt("id_receta"),
                        rs.getString("nombre_receta"),
                        rs.getString("descripcion_receta"),
                        rs.getString("pasos_receta"),
                        rs.getString("imagen_receta"),
                        rs.getInt("tiempo_preparacion_receta"),
                        rs.getString("dificultad_receta"),
                        rs.getInt("autor_id"),
                        rs.getString("tipo_receta"),
                        rs.getString("tipo_coccion_receta"),
                        rs.getInt("publicada_por_restaurante")
                    );
                    recetas.add(receta);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cargando recetas: " + e.getMessage());
        }

        return recetas;
    }
    
    public static ObservableList<Alergeno> obtenerListaAlergenos() {
        ObservableList<Alergeno> lista = FXCollections.observableArrayList();

        String query = "SELECT * FROM Alergeno";

        try (Connection conexion = iniciarConexion();
             PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Alergeno a = new Alergeno(
                    rs.getInt("id_alergeno"),
                    rs.getString("nombre_alergeno"),
                    rs.getString("icono_alergeno"),
                    rs.getString("tipo_alergeno")   
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo alérgenos: " + e.getMessage());
        }

        return lista;
    }
    
    public static ObservableList<Alergeno> obtenerAlergenosReceta(int recetaId) {
        //de RecetaAlergeno
        ObservableList<Alergeno> listaAlergenos = FXCollections.observableArrayList();

        String query = "SELECT Alergeno.* FROM Alergeno "
                       + "JOIN RecetaAlergeno ON Alergeno.id_alergeno = RecetaAlergeno.id_alergeno "
                       + "WHERE RecetaAlergeno.id_receta = ?";

        try (Connection conexion = iniciarConexion();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setInt(1, recetaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Alergeno alergeno = new Alergeno(
                        rs.getInt("id_alergeno"),
                        rs.getString("nombre_alergeno"),
                        rs.getString("imagen_alergeno"),
                        rs.getString("tipo_alergeno")  
                    );
                    listaAlergenos.add(alergeno);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo alérgenos para la receta " + recetaId + ": " + e.getMessage());
        }

        return listaAlergenos;
    }
    
    
    public static ObservableList<Ingrediente> obtenerListaIngredientes() {
        ObservableList<Ingrediente> listaIngredientes = FXCollections.observableArrayList();

        String query = "SELECT * FROM Ingrediente";

        try (Connection conexion = iniciarConexion();
             PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingrediente a = new Ingrediente(
                    rs.getInt("id_ingrediente"),
                    rs.getString("nombre_ingrediente"),
                    rs.getString("imagen_ingrediente"),
                    rs.getString("tipo_ingrediente"),
                    rs.getInt("alergeno_ingrediente"),
                    rs.getString("tipo_alergeno_ingrediente")
                );
                listaIngredientes.add(a);
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo lista Ingrediente: " + e.getMessage());
        }

        return listaIngredientes;
    }

    
    
    // recoger el icono/img 
    public static ImageView obtenerImageBD(Object imagenOrigen) {
        Image imagen = null;

        try {
            if (imagenOrigen instanceof byte[]) {
                byte[] bytes = (byte[]) imagenOrigen;
                imagen = new Image(new ByteArrayInputStream(bytes));
                
            } else if (imagenOrigen instanceof String) {
                
                String str = (String) imagenOrigen;
                
                if (str.startsWith("data:image") && str.contains("base64,")) {
                    
                    // Cadena base64 con prefijo data:image/...
                    String base64 = str.substring(str.indexOf("base64,") + 7);
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    imagen = new Image(new ByteArrayInputStream(bytes));
                    
                } else if (str.matches("^[A-Za-z]:\\\\.*") || str.startsWith("/") || str.startsWith("./") || str.startsWith("../") || str.startsWith("assets/")) {
                    
                    // Ruta local (Windows o Unix) o assets relativa
                    imagen = new Image("file:" + str);
                } else if (str.startsWith("http://") || str.startsWith("https://")) {
                    
                    // URL remota
                    imagen = new Image(str);
                } else {
                    
                    // Desde recursos
                    imagen = new Image(FuncionesRepetidas.class.getResourceAsStream(str));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            // fallback
            imagen = new Image(FuncionesRepetidas.class.getResourceAsStream("/assets/iconos_perfil/huevin.png"));
        }

        ImageView iv = new ImageView(imagen);
        return iv;
    }
    
    public static String obtenerRutaIconoUsuario(int iconoPerfilId) {
        ObservableList<IconoPerfil> listaIconos = obtenerListaIconos();

        for (IconoPerfil icono : listaIconos) {
            if (icono.getId_icono() == iconoPerfilId) {
                return icono.getImagen_icono();
            }
        }

        return "/assets/iconos_perfil/huevin.png";
    }
    
    

    
    private boolean verificarUsuarioExistente(String nombreUsuario) {
        String query = "SELECT COUNT(*) FROM Usuario WHERE nombre_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    
    
    
    
    // Crear Card:
    public static VBox crearCardReceta(Receta receta) {
        try {
            List<Alergeno> alergenos = obtenerAlergenosReceta(receta.getId_receta());
            
            FXMLLoader loader = new FXMLLoader(FuncionesRepetidas.class.getResource("/vistas/CardReceta.fxml"));
            VBox card = loader.load();

            // card.lookup("#...) sirve para busrcar por el id en el fxml enlazadp
            ImageView img = (ImageView) card.lookup("#imgCard");
            Label nombre = (Label) card.lookup("#lblNombreCard");
            Label tipo = (Label) card.lookup("#lblTipoCard");
            HBox tipoIconos = (HBox) card.lookup("#tipoIconosCard");
            Label alergenosLabel = (Label) card.lookup("#lblAlergenosCard");
            HBox alergenosIconosCard = (HBox) card.lookup("#alergenosIconosCard");

            nombre.setText(receta.getNombre_receta());
            //Image image = new Image(receta.getImagen_receta());
            //img.setImage(image);
            tipo.setText("Tipo: " + receta.getTipo_receta());

            for (Alergeno alergeno : alergenos) {
                ImageView alergenoIcono = new ImageView();
                alergenoIcono.setImage(new Image(FuncionesRepetidas.class.getResource(alergeno.getImagen_alergeno()).toExternalForm()));
                alergenoIcono.setFitWidth(20);
                alergenoIcono.setFitHeight(20);
                alergenosIconosCard.getChildren().add(alergenoIcono);
            }

            return card;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
}
