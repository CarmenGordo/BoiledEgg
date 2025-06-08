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
import java.sql.Timestamp;
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
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import modelos.Alergeno;
import modelos.Codigo;
import modelos.Favoritos;
import modelos.Ingrediente;
import modelos.Receta;
import modelos.RecetaIngrediente;
import modelos.RestauranteReceta;
import modelos.UsuarioCodigo;
import modelos.Valoracion;

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
    public static String emailSoporte = "soporteboiledegg@gmail.com";
    
    
   
    
    
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
    
    
    public static ImageView crearIconoDesdeRuta(String rutaImagen) {
        try {
            System.err.println("");
            Image img = new Image(FuncionesRepetidas.class.getResourceAsStream(rutaImagen));
            ImageView icono = new ImageView(img);
            icono.setFitHeight(45);
            icono.setFitWidth(45);
            return icono;
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + rutaImagen);
            e.printStackTrace(); 
            return null;
        }
    }
    
    // Funciones con Select / Listas:
    public static ObservableList<Usuario> obtenerListaUsuarios() {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Usuario";

        try (Connection conn = iniciarConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("email_usuario"),
                        rs.getString("contraseña_usuario"),
                        rs.getInt("nivel_usuario"),
                        rs.getInt("puntos_usuario"),
                        rs.getInt("icono_perfil_id"),
                        rs.getString("ciudad_usuario"),
                        rs.getInt("juego_completado_usuario")
                    );
                listaUsuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaUsuarios;
    }

    public static Usuario obtenerUsuarioPorId(int id) {
        String sql = "SELECT * FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = iniciarConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("email_usuario"),
                        rs.getString("contraseña_usuario"),
                        rs.getInt("nivel_usuario"),
                        rs.getInt("puntos_usuario"),
                        rs.getInt("icono_perfil_id"),
                        rs.getString("ciudad_usuario"),
                        rs.getInt("juego_completado_usuario")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
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
                        rs.getInt("icono_perfil_id"),
                        rs.getString("ciudad_usuario"),
                        rs.getInt("juego_completado_usuario")
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
                            rs.getString("imagen_restaurante"),
                            rs.getString("ciudad_restaurante"),
                            rs.getString("direccion_restaurante"),
                            rs.getString("tipo_restaurante"),
                            rs.getString("url_restaurante"),
                            rs.getInt("id_usuario")
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

            String query = "SELECT * FROM Receta WHERE visible_receta = 1";
            try (PreparedStatement ps = conexion.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Receta receta = new Receta(
                        rs.getInt("id_receta"),
                        rs.getString("nombre_receta"),
                        rs.getString("consejos_receta"),
                        rs.getString("pasos_receta"),
                        rs.getString("imagen_receta"),
                        rs.getInt("tiempo_preparacion_receta"),
                        rs.getString("dificultad_receta"),
                        rs.getInt("id_autor"),
                        rs.getString("tipo_receta"),
                        rs.getString("tipo_coccion_receta"),
                        rs.getInt("publicada_por_restaurante"),
                        rs.getInt("visible_receta")
                    );
                    recetas.add(receta);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error cargando recetas: " + e.getMessage());
        }

        return recetas;
    }
    
    public static int obtenerUltimoIdReceta() {
        int idReceta = -1;
        String sql = "SELECT MAX(id_receta) as ultimo_id FROM Receta";

        try (Connection conn = iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                idReceta = rs.getInt("ultimo_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idReceta;
    }
    
    public static double obtenerValoracionMedia(int recetaId) {
        String query = "SELECT AVG(puntuacion_valoracion) as media FROM Valoracion " +
                        "WHERE tipo_objeto = 'Receta' AND id_objeto = ?";
        double media = 0.0;

        try (Connection conexion = iniciarConexion();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setInt(1, recetaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    media = rs.getDouble("media");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo valoración de receta" + recetaId + ": " + e.getMessage());
        }

        return media;
    }
    
    public static double obtenerValoracionMediaRestaurante(int restauranteId) {
        String query = "SELECT AVG(puntuacion_valoracion) as media FROM Valoracion " +
                       "WHERE tipo_objeto = 'Restaurante' AND id_objeto = ?";
        double media = 0.0;

        try (Connection conexion = iniciarConexion();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setInt(1, restauranteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    media = rs.getDouble("media");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo valoración de restaurante " + restauranteId + ": " + e.getMessage());
        }

        return media;
    }
    
    public static ObservableList<Valoracion> obtenerListaValoraciones(String tipoObjeto, int idObjeto) {
        ObservableList<Valoracion> lista = FXCollections.observableArrayList();

        try (Connection conn = iniciarConexion()) {
            String sql = "SELECT * FROM Valoracion WHERE tipo_objeto = ? AND id_objeto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipoObjeto);
            stmt.setInt(2, idObjeto);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Valoracion val = new Valoracion();
                val.setId_valoracion(rs.getInt("id_valoracion"));
                val.setTipo_objeto(Valoracion.TipoObjeto.fromString(rs.getString("tipo_objeto")));
                val.setId_objeto(rs.getInt("id_objeto"));
                val.setId_usuario(rs.getInt("id_usuario"));
                val.setPuntuacion_valoracion(rs.getInt("puntuacion_valoracion"));
                val.setComentario_valoracion(rs.getString("comentario_valoracion"));
                val.setFecha_valoracion(rs.getDate("fecha_valoracion"));
                lista.add(val);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public static Valoracion obtenerValoracionUsuarioEnObjeto(int idUsuario, int objetoId, Valoracion.TipoObjeto tipoObjeto) {
        try (Connection conn =  iniciarConexion()){
            String sql = "SELECT * FROM Valoracion WHERE id_usuario = ? AND id_objeto = ? AND tipo_objeto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, objetoId);
            stmt.setString(3, tipoObjeto.getValor());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Valoracion val = new Valoracion();
                val.setId_valoracion(rs.getInt("id_valoracion"));
                val.setTipo_objeto(tipoObjeto);
                val.setId_objeto(objetoId);
                val.setId_usuario(idUsuario);
                val.setPuntuacion_valoracion(rs.getInt("puntuacion_valoracion"));
                val.setComentario_valoracion(rs.getString("comentario_valoracion"));
                val.setFecha_valoracion(rs.getDate("fecha_valoracion"));
                return val;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static ObservableList<Favoritos> obtenerFavoritosUsuario(int idUsuario) {
        ObservableList<Favoritos> lista = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Favoritos WHERE id_usuario = ?";

        try (Connection conn = iniciarConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Favoritos.TipoObjeto tipo = Favoritos.TipoObjeto.fromString(rs.getString("tipo_objeto"));

                Favoritos favorito = new Favoritos(
                    rs.getInt("id_favorito"),
                    tipo,
                    rs.getInt("id_objeto"),
                    rs.getInt("id_usuario"),
                    rs.getDate("fecha_favorito")
                );

                lista.add(favorito);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener favoritos del usuario: " + e.getMessage());
        }

        return lista;
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
                    rs.getString("imagen_alergeno"),
                    rs.getString("tipo_alergeno")   
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo alérgenos: " + e.getMessage());
        }

        return lista;
    }
    
    public static ObservableList<Alergeno> obtenerRecetaAlergenos(int recetaId) {
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
    
    public static ObservableList<Ingrediente> obtenerRecetaIngredientes(int recetaId, Map<Integer, String> cantidadesPorIngrediente) {
        ObservableList<Ingrediente> listaIngredientes = FXCollections.observableArrayList();

        String query = "SELECT Ingrediente.*, RecetaIngrediente.cantidad_ingrediente AS cantidad " +
                       "FROM Ingrediente " +
                       "JOIN RecetaIngrediente ON Ingrediente.id_ingrediente = RecetaIngrediente.id_ingrediente " +
                       "WHERE RecetaIngrediente.id_receta = ?";

        try (Connection conexion = iniciarConexion();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setInt(1, recetaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingrediente ingrediente = new Ingrediente(
                        rs.getInt("id_ingrediente"),
                        rs.getString("nombre_ingrediente"),
                        rs.getString("imagen_ingrediente"),
                        rs.getString("tipo_ingrediente"),
                        rs.getInt("alergeno_ingrediente"),
                        rs.getString("tipo_alergeno_ingrediente")
                    );

                    String cantidad = rs.getString("cantidad");
                    listaIngredientes.add(ingrediente);

                    if (cantidad != null) {
                        cantidadesPorIngrediente.put(ingrediente.getId_ingrediente(), cantidad);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo ingredientes de la receta " + recetaId + ": " + e.getMessage());
        }

        return listaIngredientes;
    }
    
    public static ObservableList<Restaurante> obtenerListaRestaurantes() {
        ObservableList<Restaurante> lista = FXCollections.observableArrayList();

        String query = "SELECT * FROM Restaurante";

        try (Connection con = iniciarConexion();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Restaurante restau = new Restaurante(
                    rs.getInt("id_restaurante"),
                    rs.getString("nombre_restaurante"),
                    rs.getString("email_restaurante"),
                    rs.getString("contraseña_restaurante"),
                    rs.getString("imagen_restaurante"),
                    rs.getString("ciudad_restaurante"),
                     rs.getString("direccion_restaurante"),
                    rs.getString("tipo_restaurante"),
                    rs.getString("url_restaurante"),
                    rs.getInt("id_usuario")
                );
                lista.add(restau);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ObservableList<RestauranteReceta> obtenerRestauranteReceta() {
        ObservableList<RestauranteReceta> lista = FXCollections.observableArrayList();

        String query = "SELECT * FROM RestauranteReceta";

        try (Connection con = iniciarConexion();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RestauranteReceta relacion = new RestauranteReceta(
                    rs.getInt("id_restaurante"),
                    rs.getInt("id_receta")
                );
                lista.add(relacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    
    public static ObservableList<Codigo> obtenerCodigoDeImagen(String cod) {
        ObservableList<Codigo> lista = FXCollections.observableArrayList();

        String query = "SELECT Codigo.*, Ingrediente.nombre_ingrediente FROM Codigo " +
                   "JOIN Ingrediente ON Codigo.id_ingrediente = Ingrediente.id_ingrediente " +
                   "WHERE Codigo.codigo_barras = ?";

        try (Connection con = iniciarConexion();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, cod);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Codigo codigo = new Codigo(
                        rs.getInt("id_codigo"),
                        rs.getString("codigo_barras"),
                        rs.getInt("id_ingrediente"),
                        rs.getString("nombre_tienda"),
                        rs.getString("nombre_marca"),
                        rs.getString("pais_origen"),
                        rs.getString("descripcion_opcional")
                    );
                    lista.add(codigo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public static ObservableList<UsuarioCodigo> obtenerListaUsuarioCodigo(Usuario usuario) {
        ObservableList<UsuarioCodigo> listaUsuarioCodigo = FXCollections.observableArrayList();

        String query = "SELECT * FROM UsuarioCodigo WHERE id_usuario = ? ORDER BY fecha_escaneo DESC";

        try (Connection conexion = iniciarConexion();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setInt(1, usuario.getId_usuario());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UsuarioCodigo uc = new UsuarioCodigo(
                    rs.getInt("id_escaneo"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_codigo"),
                    rs.getInt("id_ingrediente"),
                    rs.getTimestamp("fecha_escaneo")
                );
                listaUsuarioCodigo.add(uc);
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo lista UsuarioCodigo: " + e.getMessage());
        }

        return listaUsuarioCodigo;
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
    
    public static List<Alergeno> obtenerUsuarioAlergenos(int idUsuario) {
        List<Alergeno> alergias = new ArrayList<>();
        String sql = "SELECT a.* FROM Alergeno a " +
                     "INNER JOIN UsuarioAlergeno ua ON a.id_alergeno = ua.id_alergeno " +
                     "WHERE ua.id_usuario = ?";

        try (Connection conn = iniciarConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alergeno alergeno = new Alergeno();
                alergeno.setId_alergeno(rs.getInt("id_alergeno"));
                alergeno.setNombre_alergeno(rs.getString("nombre_alergeno"));
                alergeno.setImagen_alergeno(rs.getString("imagen_alergeno"));
                alergeno.setTipo_alergeno(rs.getString("tipo_alergeno"));
                alergias.add(alergeno);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener alergias del usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return alergias;
    }
    
    
    // Funciones con insert:
    public static boolean insertarSubirReceta(Receta receta) {
        try (Connection conexion = iniciarConexion()) {
    
            String query = "INSERT INTO Receta (nombre_receta, consejos_receta, pasos_receta, imagen_receta, tiempo_preparacion_receta, dificultad_receta, id_autor, tipo_receta, tipo_coccion_receta, publicada_por_restaurante, visible_receta) " +
                          "VALUES (?, NULL, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
            
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setString(1, receta.getNombre_receta());
            stmt.setString(2, receta.getPasos_receta());
            stmt.setString(3, receta.getImagen_receta());
            stmt.setInt(4, receta.getTiempo_preparacion_receta());
            stmt.setString(5, receta.getDificultad_receta());
            stmt.setInt(6, receta.getId_autor());
            stmt.setString(7, receta.getTipo_receta());
            stmt.setString(8, receta.getTipo_coccion_receta());
            stmt.setInt(9, receta.getPublicada_por_restaurante());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertarValoracion(Valoracion val) {
        try (Connection conn = iniciarConexion()){
            String insertSql = "INSERT INTO Valoracion (tipo_objeto, id_objeto, id_usuario, puntuacion_valoracion, comentario_valoracion, fecha_valoracion) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, val.getTipo_objeto().getValor());
            stmt.setInt(2, val.getId_objeto());
            stmt.setInt(3, val.getId_usuario());
            stmt.setInt(4, val.getPuntuacion_valoracion());
            stmt.setString(5, val.getComentario_valoracion());
            stmt.setDate(6, new java.sql.Date(val.getFecha_valoracion().getTime()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean insertarFavorito(Favoritos favorito){
         String insertSql = "INSERT INTO Favoritos (tipo_objeto, id_objeto, id_usuario, fecha_favorito) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = iniciarConexion()){
            PreparedStatement stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, favorito.getTipo_objeto().getValor());
            stmt.setInt(2, favorito.getId_objeto());
            stmt.setInt(3, favorito.getId_usuario());
            stmt.setDate(4, new java.sql.Date(favorito.getFecha_favorito().getTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar favorito: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean insertarRecetaAlergeno(int idReceta, int idAlergeno) {
        String sql = "INSERT INTO RecetaAlergeno (id_receta, id_alergeno) VALUES (?, ?)";

        try (Connection conn = iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReceta);
            ps.setInt(2, idAlergeno);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean insertarRecetaIngrediente(int idReceta, int idIngrediente, String cantidad) {
        String sql = "INSERT INTO RecetaIngrediente (id_receta, id_ingrediente, cantidad_ingrediente) VALUES (?, ?, ?)";

        try (Connection conn = iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReceta);
            ps.setInt(2, idIngrediente);
            ps.setString(3, cantidad);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean insertarUsuarioCodigo(Usuario usuario, String codigoBarras) {
        String queryVerificar = "SELECT COUNT(*) FROM UsuarioCodigo uc " +
                           "JOIN Codigo c ON uc.id_codigo = c.id_codigo " +
                           "WHERE uc.id_usuario = ? AND c.codigo_barras = ?";

        try (Connection conexion = iniciarConexion();
            PreparedStatement ps = conexion.prepareStatement(queryVerificar)) {

            ps.setInt(1, usuario.getId_usuario());
            ps.setString(2, codigoBarras);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            String queryInsert = "INSERT INTO UsuarioCodigo (id_usuario, id_codigo, id_ingrediente, fecha_escaneo) " +
                               "SELECT ?, c.id_codigo, c.id_ingrediente, ? FROM Codigo c WHERE c.codigo_barras = ?";

            try (PreparedStatement psInsert = conexion.prepareStatement(queryInsert)) {
                psInsert.setInt(1, usuario.getId_usuario());
                psInsert.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                psInsert.setString(3, codigoBarras);

                return psInsert.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error en insertarUsuarioCodigo: " + e.getMessage());
            return false;
        }
    }
   
    
    
    // Funciones con update:
    public static void actualizarUsuarioDatos(int idUsuario, String nombre, String email, String contraseña, String ciudad) {
        try (Connection conn = iniciarConexion()) {
            String sql = "UPDATE Usuario SET nombre_usuario = ?, email_usuario = ?, contraseña_usuario = ?, ciudad_usuario = ? WHERE id_usuario = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, contraseña);
            stmt.setString(4, ciudad);
            stmt.setInt(5, idUsuario);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void actualizarUsuarioPuntos(Usuario usuario) {
        try (Connection conn = iniciarConexion()) {
            String sql = "UPDATE Usuario SET nivel_usuario = ?, puntos_usuario = ?, juego_completado_usuario = ? WHERE id_usuario = ?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuario.getNivel_usuario());
            stmt.setInt(2, usuario.getPuntos_usuario());
            stmt.setInt(3, usuario.getJuego_completado_usuario());
            stmt.setInt(4, usuario.getId_usuario());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean actualizarValoracion(HBox contenedor, Valoracion nuevaVal) {
        Valoracion actual = obtenerValoracionUsuarioEnObjeto(
            nuevaVal.getId_usuario(),
            nuevaVal.getId_objeto(),
            nuevaVal.getTipo_objeto()
        );

        if (actual == null) {
            return false;
        }

        boolean cambioPuntuacion = actual.getPuntuacion_valoracion() != nuevaVal.getPuntuacion_valoracion();
        boolean cambioComentario = (actual.getComentario_valoracion() == null && nuevaVal.getComentario_valoracion() != null)
            || (actual.getComentario_valoracion() != null && !actual.getComentario_valoracion().equals(nuevaVal.getComentario_valoracion()));

        if (!cambioPuntuacion && !cambioComentario) {
            //naada cambia
            return true;
        }

        try (Connection conn = iniciarConexion()) {
            String updateSql = "UPDATE Valoracion SET puntuacion_valoracion = ?, comentario_valoracion = ?, fecha_valoracion = ? WHERE id_valoracion = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setInt(1, nuevaVal.getPuntuacion_valoracion());
            stmt.setString(2, nuevaVal.getComentario_valoracion());
            stmt.setDate(3, new java.sql.Date(nuevaVal.getFecha_valoracion().getTime()));
            stmt.setInt(4, actual.getId_valoracion());

            boolean actualizado = stmt.executeUpdate() > 0;

            if (actualizado && cambioPuntuacion) {
                actualizarEstrellas(contenedor, nuevaVal.getPuntuacion_valoracion()); // Actualiza fill visualmente
            }

            return actualizado;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void actualizarDarBajaUsuario(int idUsuario) {
        String sql = "UPDATE Usuarios SET baja_usuario = 1 WHERE id_usuario = ?";
        try (Connection conn = iniciarConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("----error al dar de baja al usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    // Funciones con delete:
    public static boolean eliminarFavorito(int idUsuario, int idObjeto, Favoritos.TipoObjeto tipoObjeto) {
        String sql = "DELETE FROM Favoritos WHERE id_usuario = ? AND id_objeto = ? AND tipo_objeto = ?";

        try (Connection conn = iniciarConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idObjeto);
            stmt.setString(3, tipoObjeto.getValor());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar favorito: " + e.getMessage());
            return false;
        }
    }
    
    public static void borrarFavoritos(int idUsuario) {
        String sql = "DELETE FROM Favoritos WHERE id_usuario = ?";
        try (Connection conn = iniciarConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("------------error al borrar favoritos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void borrarUsuarioAlergeno(int idUsuario) {
        String sql = "DELETE FROM Usuario_Alergeno WHERE id_usuario = ?";
        try (Connection conn = iniciarConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("-------------error al borrar alergenos del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void borrarUsuarioCodigo(int idUsuario) {
        String sql = "DELETE FROM Usuario_Codigo WHERE id_usuario = ?";
        try (Connection conn = iniciarConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("--------error al borrar códigos del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
 
    public static void guardarAlergenosUsuario(int idUsuario, List<Alergeno> alergenos) {
        try (Connection conn = iniciarConexion()) {
            
            String deleteSql = "DELETE FROM UsuarioAlergeno WHERE id_usuario = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, idUsuario);
                deleteStmt.executeUpdate();
            }

            String insertSql = "INSERT INTO UsuarioAlergeno (id_usuario, id_alergeno) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Alergeno alergeno : alergenos) {
                    insertStmt.setInt(1, idUsuario);
                    insertStmt.setInt(2, alergeno.getId_alergeno());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar alergenos del usuario: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    

    
    
    
    
    // Crear Card:
    public static VBox crearCardReceta(Usuario usuario, Receta receta) {
        try {
            List<Alergeno> alergenos = obtenerRecetaAlergenos(receta.getId_receta());
            
            FXMLLoader loader = new FXMLLoader(FuncionesRepetidas.class.getResource("/vistas/Card.fxml"));
            VBox card = loader.load();

            // card.lookup("#...) sirve para busrcar por el id en el fxml enlazadp
            ImageView img = (ImageView) card.lookup("#imgCard");
            
            Button btnAñadirCardFav = (Button) card.lookup("#btnAñadirCardFav");
            if (btnAñadirCardFav != null) {
                Node graphic = btnAñadirCardFav.getGraphic();
                
                if (graphic instanceof SVGPath) {
                    SVGPath svgAñadirCardFav = (SVGPath) graphic;
                    svgAñadirCardFav.getStyleClass().add("svgSoloBordes");
                    ponerHoverFavorito(btnAñadirCardFav, svgAñadirCardFav);
                    
                    boolean esFavorito = esFavorito(usuario.getId_usuario(), receta.getId_receta(), Favoritos.TipoObjeto.RECETA);
                    if (esFavorito) {
                        svgAñadirCardFav.getStyleClass().remove("svgSinBordes");
                        svgAñadirCardFav.getStyleClass().add("fillFav");
                    } else {
                        svgAñadirCardFav.getStyleClass().remove("fillFav");
                        svgAñadirCardFav.getStyleClass().add("svgSinBordes");
                    }

                    btnAñadirCardFav.setOnMouseClicked(event -> {
                        boolean esFavoritoActualizado = esFavorito(usuario.getId_usuario(), receta.getId_receta(), Favoritos.TipoObjeto.RECETA);

                        if (esFavoritoActualizado) {
                            if (FuncionesRepetidas.eliminarFavorito(usuario.getId_usuario(),receta.getId_receta(), Favoritos.TipoObjeto.RECETA)) {
                                svgAñadirCardFav.getStyleClass().remove("fillFav");
                                svgAñadirCardFav.getStyleClass().add("svgSinBordes");
                            }
                        } else {
                            if (FuncionesRepetidas.insertarFavorito(new Favoritos(0, Favoritos.TipoObjeto.RECETA, receta.getId_receta(), usuario.getId_usuario(), new java.util.Date()))) {
                                svgAñadirCardFav.getStyleClass().remove("svgSinBordes");
                                svgAñadirCardFav.getStyleClass().add("fillFav");
                            }
                        }
                    });
              
                } else {
                    System.err.println("---- no es un SVGPath: " + graphic);
                }
            } else {
                System.err.println("---- no svgAñadirCardFav");
            }

            
            Label nombre = (Label) card.lookup("#lblNombreCard");
            Label tipo = (Label) card.lookup("#lblTipoCard");
            HBox tipoIconos = (HBox) card.lookup("#tipoIconosCard");
            Label alergenosLabel = (Label) card.lookup("#lblAlergenosCard");
            HBox alergenosIconosCard = (HBox) card.lookup("#alergenosIconosCard");
            Label nombreUsuarioLabel = (Label) card.lookup("#lblNombreUsuarioCard");
            
            nombre.setText(receta.getNombre_receta());
            
            String rutaImagen = receta.getImagen_receta();
            img.setImage(FuncionesRepetidas.cargarImagenReceta(rutaImagen));
        
            tipo.setText("Tipo: " + receta.getTipo_receta());

            if (receta.getId_autor()!= null) {
                Usuario autor = null;
                for (Usuario u : obtenerListaUsuarios()) {
                    if (u.getId_usuario() == receta.getId_autor()) {
                        autor = u;
                        break;
                    }
                }

                if (autor != null) {
                    nombreUsuarioLabel.setText(autor.getNombre_usuario());
                    nombreUsuarioLabel.setVisible(true);
                } else {
                    nombreUsuarioLabel.setVisible(false);
                }
            } else {
                nombreUsuarioLabel.setVisible(false);
            }
            
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
    
    public static HBox crearCardValoraciones(Valoracion val) {
        try {            
            FXMLLoader loader = new FXMLLoader(FuncionesRepetidas.class.getResource("/vistas/CardValoracion.fxml"));
            HBox card = loader.load(); 

            ImageView imgCardValora = (ImageView) card.lookup("#imgCardValora");
            Label lblNombreCardValora = (Label) card.lookup("#lblNombreCardValora");
            Label lblPuntuacionCardValora = (Label) card.lookup("#lblPuntuacionCardValora");
            Label lblFechaCardValora = (Label) card.lookup("#lblFechaCardValora");
            Label lblComentarioCardValora = (Label) card.lookup("#lblComentarioCardValora");

            Usuario usuarioVal = null;
            ObservableList<Usuario> listaUsuarios = FuncionesRepetidas.obtenerListaUsuarios();

            for (Usuario usuario : listaUsuarios) {
                if (usuario.getId_usuario() == val.getId_usuario()) {
                    usuarioVal = usuario;
                    break;
                }
            }

            if (usuarioVal != null) {
                String rutaImagenUsuario = obtenerRutaIconoUsuario(usuarioVal.getIcono_perfil_id());
                URL urlImg = FuncionesRepetidas.class.getResource(rutaImagenUsuario);
                if (urlImg != null && imgCardValora != null) {
                    imgCardValora.setImage(new Image(urlImg.toExternalForm()));
                }

                if (lblNombreCardValora != null) {
                    lblNombreCardValora.setText(usuarioVal.getNombre_usuario());
                }
            }
            
            String estrellas = "★".repeat(val.getPuntuacion_valoracion());
            lblPuntuacionCardValora.setText(val.getPuntuacion_valoracion() + " " + estrellas);
            lblFechaCardValora.setText(val.getFecha_valoracion().toString());

            String comentario = val.getComentario_valoracion();
            if (comentario != null && !comentario.isEmpty()) {
                lblComentarioCardValora.setText(comentario);
            } else {
                lblComentarioCardValora.setVisible(false);
            }

            return card;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static VBox crearCardIngrediente(Usuario usuario, Ingrediente ingre) {
        try {            
            FXMLLoader loader = new FXMLLoader(FuncionesRepetidas.class.getResource("/vistas/Card.fxml"));
            VBox card = loader.load();

            ImageView img = (ImageView) card.lookup("#imgCard");
            
            Button btnAñadirCardFav = (Button) card.lookup("#btnAñadirCardFav");
            if (btnAñadirCardFav != null) {
                Node graphic = btnAñadirCardFav.getGraphic();
                
                if (graphic instanceof SVGPath) {
                    SVGPath svgAñadirCardFav = (SVGPath) graphic;
                    svgAñadirCardFav.getStyleClass().add("svgSoloBordes");
                    ponerHoverFavorito(btnAñadirCardFav, svgAñadirCardFav);
                    
                    boolean esFavorito = esFavorito(usuario.getId_usuario(), ingre.getId_ingrediente(), Favoritos.TipoObjeto.INGREDIENTE);
                    if (esFavorito) {
                        svgAñadirCardFav.getStyleClass().remove("svgSinBordes");
                        svgAñadirCardFav.getStyleClass().add("fillFav");
                    } else {
                        svgAñadirCardFav.getStyleClass().remove("fillFav");
                        svgAñadirCardFav.getStyleClass().add("svgSinBordes");
                    }

                    btnAñadirCardFav.setOnMouseClicked(event -> {
                        boolean esFavoritoActualizado = esFavorito(usuario.getId_usuario(), ingre.getId_ingrediente(), Favoritos.TipoObjeto.INGREDIENTE);

                        if (esFavoritoActualizado) {
                            if (FuncionesRepetidas.eliminarFavorito(usuario.getId_usuario(), ingre.getId_ingrediente(), Favoritos.TipoObjeto.INGREDIENTE)) {
                                svgAñadirCardFav.getStyleClass().remove("fillFav");
                                svgAñadirCardFav.getStyleClass().add("svgSinBordes");
                            }
                        } else {
                            if (FuncionesRepetidas.insertarFavorito(new Favoritos(0, Favoritos.TipoObjeto.INGREDIENTE, ingre.getId_ingrediente(), usuario.getId_usuario(), new java.util.Date()))) {
                                svgAñadirCardFav.getStyleClass().remove("svgSinBordes");
                                svgAñadirCardFav.getStyleClass().add("fillFav");
                            }
                        }
                    });
              
                } else {
                    System.err.println("---- no es un SVGPath: " + graphic);
                }
            } else {
                System.err.println("---- no svgAñadirCardFav");
            }
            
            
            Label nombre = (Label) card.lookup("#lblNombreCard");
            Label tipo = (Label) card.lookup("#lblTipoCard");
            HBox tipoIconos = (HBox) card.lookup("#tipoIconosCard");
            Label alergenosLabel = (Label) card.lookup("#lblAlergenosCard");
            HBox alergenosIconosCard = (HBox) card.lookup("#alergenosIconosCard");
            Label nombreUsuarioLabel = (Label) card.lookup("#lblNombreUsuarioCard");

            nombreUsuarioLabel.setVisible(false);
            nombre.setText(ingre.getNombre_ingrediente());
            String rutaImagen = ingre.getImagen_ingrediente();
            img.setImage(FuncionesRepetidas.cargarImagenReceta(rutaImagen));
          
            Label tipoLbl = new Label(ingre.getTipo_ingrediente());
            tipoIconos.getChildren().add(tipoLbl);

            ObservableList<Alergeno> alergenos = obtenerListaAlergenos();
            boolean encontrado = false;

            for (Alergeno alergeno : alergenos) {
                if (alergeno.getNombre_alergeno().equalsIgnoreCase(ingre.getTipo_alergeno_ingrediente())) {
                    String rutaAlergenoIcono = alergeno.getImagen_alergeno();
                    URL urlAlergeno = FuncionesRepetidas.class.getResource(rutaAlergenoIcono);
                    if (urlAlergeno != null) {
                        ImageView alergenoIcono = new ImageView(new Image(urlAlergeno.toExternalForm()));
                        alergenoIcono.setFitWidth(20);
                        alergenoIcono.setFitHeight(20);
                        alergenosIconosCard.getChildren().add(alergenoIcono);
                        encontrado = true;
                        break;
                    }
                }
            }

            if (!encontrado) {
                Label noTiene = new Label("-");
                alergenosIconosCard.getChildren().add(noTiene);
            }

            return card;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static VBox crearCardRestaurante(Usuario usuario, Restaurante restau) {
        try {            
            FXMLLoader loader = new FXMLLoader(FuncionesRepetidas.class.getResource("/vistas/Card.fxml"));
            VBox card = loader.load();

            ImageView img = (ImageView) card.lookup("#imgCard");
            
            Button btnAñadirCardFav = (Button) card.lookup("#btnAñadirCardFav");
            if (btnAñadirCardFav != null) {
                Node graphic = btnAñadirCardFav.getGraphic();
                
                if (graphic instanceof SVGPath) {
                    SVGPath svgAñadirCardFav = (SVGPath) graphic;
                    svgAñadirCardFav.getStyleClass().add("svgSoloBordes");
                    ponerHoverFavorito(btnAñadirCardFav, svgAñadirCardFav);
                    
                    boolean esFavorito = esFavorito(usuario.getId_usuario(), restau.getId_restaurante(), Favoritos.TipoObjeto.RESTAURANTE);
                    if (esFavorito) {
                        svgAñadirCardFav.getStyleClass().remove("svgSinBordes");
                        svgAñadirCardFav.getStyleClass().add("fillFav");
                    } else {
                        svgAñadirCardFav.getStyleClass().remove("fillFav");
                        svgAñadirCardFav.getStyleClass().add("svgSinBordes");
                    }

                    btnAñadirCardFav.setOnMouseClicked(event -> {
                        boolean esFavoritoActualizado = esFavorito(usuario.getId_usuario(), restau.getId_restaurante(), Favoritos.TipoObjeto.RESTAURANTE);

                        if (esFavoritoActualizado) {
                            if (FuncionesRepetidas.eliminarFavorito(usuario.getId_usuario(), restau.getId_restaurante(), Favoritos.TipoObjeto.RESTAURANTE)) {
                                svgAñadirCardFav.getStyleClass().remove("fillFav");
                                svgAñadirCardFav.getStyleClass().add("svgSinBordes");
                            }
                        } else {
                            if (FuncionesRepetidas.insertarFavorito(new Favoritos(0, Favoritos.TipoObjeto.RESTAURANTE, restau.getId_restaurante(), usuario.getId_usuario(), new java.util.Date()))) {
                                svgAñadirCardFav.getStyleClass().remove("svgSinBordes");
                                svgAñadirCardFav.getStyleClass().add("fillFav");
                            }
                        }
                    });
              
                } else {
                    System.err.println("---- no es un SVGPath: " + graphic);
                }
            } else {
                System.err.println("---- no svgAñadirCardFav");
            }

            
            Label nombre = (Label) card.lookup("#lblNombreCard");
            Label tipo = (Label) card.lookup("#lblTipoCard");
            HBox tipoIconos = (HBox) card.lookup("#tipoIconosCard");
            Label alergenosLabel = (Label) card.lookup("#lblAlergenosCard");
            HBox alergenosIconosCard = (HBox) card.lookup("#alergenosIconosCard");
            Label nombreUsuarioLabel = (Label) card.lookup("#lblNombreUsuarioCard");

            nombreUsuarioLabel.setVisible(false);
            nombre.setText(restau.getNombre_restaurante());
            
            String rutaImagen = restau.getImagen_restaurante();
            img.setImage(FuncionesRepetidas.cargarImagenReceta(rutaImagen));
        
            tipo.setText("Tipo: " + restau.getTipo_restaurante());
            alergenosLabel.setVisible(false);
            alergenosIconosCard.setVisible(false);
            return card;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    
    
    public static int puntuacionSeleccionada = 0;
    public static void crearBotonesValoracion(HBox contenedor) {
        contenedor.getChildren().clear();
        String svgPath = "m354-287 126-76 126 77-33-144 111-96-146-13-58-136-58 135-146 13 111 97-33 143Z";

        for (int i = 1; i <= 5; i++) {
            int valor = i;
            Button btn = new Button();
            btn.setMinSize(40, 40);
            btn.setMaxSize(40, 40);
            btn.getStyleClass().add("btnSinNada");

            SVGPath estrella = new SVGPath();
            estrella.setContent(svgPath);
            estrella.setScaleX(0.06);
            estrella.setScaleY(0.06);
            estrella.getStyleClass().add("iconoSvg");
            //estrella.setPickOnBounds(false);

            btn.setGraphic(estrella);
            btn.setOnAction(e -> {
                puntuacionSeleccionada = valor;
                actualizarEstrellas(contenedor, valor);
                System.out.println("Puntuación seleccionada: " + valor);
            });

            contenedor.getChildren().add(btn);
        }
    }
    
    public static void actualizarEstrellas(HBox contenedor, int hasta) {
        for (int i = 0; i < contenedor.getChildren().size(); i++) {
            Button btn = (Button) contenedor.getChildren().get(i);
            SVGPath svg = (SVGPath) btn.getGraphic();
            if (i < hasta) {
                svg.getStyleClass().add("fillEstrella");
            } else {
                svg.getStyleClass().remove("fillEstrella");
            }
        }
    }

    public static void ponerHoverFavorito(Button boton, SVGPath iconoSVG) {
        boton.setOnMouseEntered(event -> {
            iconoSVG.getStyleClass().remove("svgSoloBordes");
            iconoSVG.getStyleClass().add("fillFav");
        });

        boton.setOnMouseExited(event -> {
            iconoSVG.getStyleClass().remove("fillFav");
            iconoSVG.getStyleClass().add("svgSoloBordes");
        });
    }
   
    public static boolean esFavorito(int idUsuario, int idObjeto, Favoritos.TipoObjeto tipoObjeto) {
        ObservableList<Favoritos> lista = obtenerFavoritosUsuario(idUsuario);
        for (Favoritos f : lista) {
            if (f.getTipo_objeto() == tipoObjeto && f.getId_objeto() == idObjeto) {
                return true;
            }
        }
        return false;
    }
    
    public static void actualizarEstadoFavoritos(Usuario usu, Object objeto, Favoritos.TipoObjeto tipoObjeto, SVGPath svg) {
        int idObjeto = 0;
        if (tipoObjeto == Favoritos.TipoObjeto.RECETA && objeto instanceof Receta) {
            idObjeto = ((Receta) objeto).getId_receta();
        } else if (tipoObjeto == Favoritos.TipoObjeto.INGREDIENTE && objeto instanceof Ingrediente) {
            idObjeto = ((Ingrediente) objeto).getId_ingrediente();
        } else if (tipoObjeto == Favoritos.TipoObjeto.RESTAURANTE && objeto instanceof Restaurante) {
            idObjeto = ((Restaurante) objeto).getId_restaurante();
        }

        boolean esFavorita = FuncionesRepetidas.esFavorito(usu.getId_usuario(), idObjeto, tipoObjeto);

        if (esFavorita) {
            if (!svg.getStyleClass().contains("fillFav")) {
                svg.getStyleClass().add("fillFav");
            }
        } else {
            svg.getStyleClass().remove("fillFav");
        }
    }


    
    // correo:
    public static void enviarCorreoSoporte(String mensaje, String asunto, String tipoMensaje, Usuario usu) {
        try {
            if (mensaje == null || mensaje.trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, escribe un mensaje");
                return;
            }

            if (asunto == null || asunto.trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, escribe un asunto");
                return;
            }

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usu.getEmail_usuario(), usu.getContraseña_usuario());
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usu.getEmail_usuario()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usu.getEmail_usuario()));

            String subjectPrefix = tipoMensaje.equals("contactar") ? "Contacto soporte" : "Reporte incidencia";
            message.setSubject(subjectPrefix + ", Id: " + usu.getId_usuario() + ", nombre de usuario: " + usu.getNombre_usuario() + ". Asunto: " + asunto);

            message.setText("Mensaje de: " + usu.getEmail_usuario() + "\n\n" + mensaje);

            try {
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com", usu.getEmail_usuario(), usu.getContraseña_usuario());
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Correo enviado correctamente");
            } catch (Exception e) {
                System.err.println("Error al enviar por SMTP, intentando con correo de soporte: " + e.getMessage());

                try {
                    Session sessionSoporte = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("soporteboiledegg@gmail.com", "rsol wphx urmr vgye");
                        }
                    });

                    Message messageSoporte = new MimeMessage(sessionSoporte);
                    messageSoporte.setFrom(new InternetAddress("soporteboiledegg@gmail.com"));
                    messageSoporte.setRecipients(Message.RecipientType.TO, InternetAddress.parse("soporteboiledegg@gmail.com"));
                    messageSoporte.setSubject(subjectPrefix + ", Id: " + usu.getId_usuario() + ", nombre de usuario: " + usu.getNombre_usuario() + ". Asunto: " + asunto);
                    messageSoporte.setText("Mensaje de: " + usu.getEmail_usuario() + "\n\n" + mensaje);

                    Transport transportSoporte = sessionSoporte.getTransport("smtp");
                    transportSoporte.connect("smtp.gmail.com", "soporteboiledegg@gmail.com", "rsol wphx urmr vgye");
                    transportSoporte.sendMessage(messageSoporte, messageSoporte.getAllRecipients());
                    transportSoporte.close();

                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Correo enviado correctamente desde soporte");
                } catch (Exception ex) {
                    System.err.println("Error al enviar con correo de soporte: " + ex.getMessage());
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo enviar el correo. Por favor, inténtalo de nuevo.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo enviar el correo. Por favor, inténtalo de nuevo.");
        }
    }

    
    
    
    // PAra que lea tanto base 64 como ruta
    public static Image cargarImagenReceta(String rutaImagen) {
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            return new Image(FuncionesRepetidas.class.getResourceAsStream("/assets/img_otros/noImagen.png"));
        }

        try {
            if (rutaImagen.startsWith("data:image") || rutaImagen.matches("^[A-Za-z0-9+/=]+$")) {
                byte[] imageBytes = Base64.getDecoder().decode(rutaImagen);
                return new Image(new ByteArrayInputStream(imageBytes));
            } else {

                URL urlImagenReceta = FuncionesRepetidas.class.getResource(rutaImagen);
                if (urlImagenReceta != null) {
                    return new Image(urlImagenReceta.toExternalForm());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Image(FuncionesRepetidas.class.getResourceAsStream("/assets/img_otros/noImagen.png"));
    }
    
    
    
}
