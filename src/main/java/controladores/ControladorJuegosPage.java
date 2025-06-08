package controladores;

import conexion.ConexionBD;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import javafx.util.Duration;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import modelos.Restaurante;
import modelos.Usuario;
import utils.FuncionesRepetidas;

/**
 *
 * @author carmen_gordo
 */
public class ControladorJuegosPage implements Initializable {
    
    @FXML private VBox juegoMemoria, mensajeNivelInsuficiente;
    @FXML private GridPane gridCartas;
    @FXML private Label lblPuntuacion, lblIntentos, lblNivelRequerido, mostrarNormasMemoria;
    @FXML private Button btnReiniciar,  btnSiguienteJuego, btnQuitarMusicaMemoria;
    @FXML private SVGPath svgQuitarMusicaMemoria;
    
    // juego dados
    @FXML private VBox juegoDados;
    @FXML private TextField txtNumeroDado;
    @FXML private ImageView imgDado;
    @FXML private Label lblRondaDados, lblVidasDados, lblResultadoDado, mostrarNormasDados;
    @FXML private Button btnComprobarDado, btnReiniciarDados, btnQuitarMusicaDados;
    @FXML private SVGPath svgQuitarMusicaDados;
    
    private Stage stage;
    private Usuario usuario;
     
    // Var juego Memoria cartas:
    private boolean puedeJugar = false;
    private boolean juegoMemoriaCompletado = false;
    private int intentos = 0;
    private int paresEncontrados = 0;
    private boolean bloqueado = false;
    private ObservableList<String> cartas = FXCollections.observableArrayList();
    private String primeraCarta = null;
    private String segundaCarta = null;
    private boolean relacionPocimaEncontrada = false;
    private boolean relacionLibroEncontrada = false;
    private int relacionesCompletadas = 0;
    private Set<String> parejasEncontradas = new HashSet<>();
    private static final String[] TIPOS_CARTAS = {
        "pocima_azul", "pocima_verde",
        "calavera_bomba", "calavera_pocima",
        "libro_exclama", "libro_interro",
        "espada_libro", "espada_tabla",
        "estrella_llave", "estrella_medalla",
        "joya_corona", "joya_trofeo"
    };
    
    
    // Var juego de dados:
    private int numeroAleatorio;
    private int vidas = 5;
    private int ronda = 1;
    
    
    private MediaPlayer mediaPlayer;
    private boolean musicaActiva = true;
    private String svgConSonido = "M400-120q-66 0-113-47t-47-113q0-66 47-113t113-47q23 0 42.5 5.5T480-418v-382q0-17 11.5-28.5T520-840h160q17 0 28.5 11.5T720-800v80q0 17-11.5 28.5T680-680H560v400q0 66-47 113t-113 47Z";
    private String svgSinSonido = "M764-84 84-764q-11-11-11-28t11-28q11-11 28-11t28 11l680 680q11 11 11 28t-11 28q-11 11-28 11t-28-11ZM560-680v70q0 20-12.5 29.5T520-571q-15 0-27.5-10T480-611v-189q0-17 11.5-28.5T520-840h160q17 0 28.5 11.5T720-800v80q0 17-11.5 28.5T680-680H560ZM400-120q-66 0-113-47t-47-113q0-66 47-113t113-47q23 0 42.5 5.5T480-418v-62l80 80v120q0 66-47 113t-113 47Z";
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        juegoMemoria.setVisible(false);
        juegoDados.setVisible(false);
        mensajeNivelInsuficiente.setVisible(false);
        
        //iniciarMusica();
        cargarEstadoSonido();
        btnQuitarMusicaMemoria.setOnAction(event -> toggleMusica());
        btnQuitarMusicaDados.setOnAction(event -> toggleMusica());

        
        mostrarNormasMemoria.setOnMouseClicked(event->{
            String normas = "Normas del Juego de Memoria:\n\n" +
            "1. Encuentra las parejas de cartas, estas pueden tener la misma forma, o detalles iguales.\n" +
            "2. Si encuentras las cartas 'Calavera' sin haber hecho ninguna relación, pierdes.\n" +
            "3. Si encuentras las cartas 'Joya' sin haber completado todas las demás relaciones, pierdes. Por tanto estas deben de ser las últimas en encontrar.\n" +
            "4. La relación 'Pocima' te protege de perder con las de 'Calavera'.\n" +
            "5. Al encontrar la relación 'Libro', se te mostrará la ubicación de una carta de 'Pocima' durante 3 segundos.\n" +
            "Recuerda que solo podrás jugar una vez, si llegas a superar este nivel.";
            
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "Normas del juego de las Cartas", normas);
        });
        
        mostrarNormasDados.setOnMouseClicked(event->{
            String normas = "Normas del Juego de Memoria:\n\n" +
            "1. Escribe un número del 1 al 6 (ambos incluidos).\n" +
            "2. Si adivinas que sacará el dado, ganas.\n" +
            "3. Si no lo adivinas, perderas una vida.\n" +
            "4. Si llegas a 0 vidas pierdes.\n" +
            "5. Si se te acaban los intentos, pierdes.\n" +
            "5. Si completas las 6 rondas y te quedan vidas, ganas.\n" +
            "Recuerda que solo podrás jugar una vez, si llegas a superar este nivel.";
            
            FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "Normas del juego del dado", normas);
        });
        
        imgDado.setImage(new Image(getClass().getResourceAsStream("/assets/img_juegos/juego_dados/dadoDefecto.png")));
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        Platform.runLater(() -> verificarNivelUsuario());
    }
    
    private void verificarNivelUsuario() {
        //System.out.println("juego completado: " + usuario.getJuego_completado_usuario());
        
        if (usuario.getJuego_completado_usuario() == 1) {
            juegoMemoria.setVisible(false);
            juegoDados.setVisible(true);
            mensajeNivelInsuficiente.setVisible(false);
            iniciarJuegoDados();
        } else {
            juegoMemoria.setVisible(true);
            juegoDados.setVisible(false);
            mensajeNivelInsuficiente.setVisible(false);
            puedeJugar = true;
            iniciarJuego();
        }
    }
    
    private void iniciarJuego() {
        if (!puedeJugar) return;
        
        juegoMemoriaCompletado = false;
        btnSiguienteJuego.setVisible(false);
        cartas.clear();
        parejasEncontradas.clear();
        gridCartas.getChildren().clear();
        intentos = 0;
        paresEncontrados = 0;
        bloqueado = false;
        primeraCarta = null;
        segundaCarta = null;
        relacionPocimaEncontrada = false;
        relacionLibroEncontrada = false;
        relacionesCompletadas = 0;
        
        btnReiniciar.setDisable(false);
        
        for (String tipo : TIPOS_CARTAS) {
            cartas.add(tipo);
        }
        mezclarCartas();
        
        int columna = 0;
        int fila = 0;
        for (String valor : cartas) {
            StackPane cartaPane = crearCartaPane(valor);
            gridCartas.add(cartaPane, columna, fila);
            columna++;
            if (columna > 3) {
                columna = 0;
                fila++;
            }
        }
        
        actualizarContadores();
    }
    
    private void mezclarCartas() {
        Random random = new Random();
        for (int i = cartas.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String temp = cartas.get(i);
            cartas.set(i, cartas.get(j));
            cartas.set(j, temp);
        }
    }
    
    private StackPane crearCartaPane(String valor) {
        StackPane cartaPane = new StackPane();
        cartaPane.getStyleClass().add("carta");

        StackPane reverso = new StackPane();
        reverso.setStyle("-fx-background-color: #dee0df; -fx-min-width: 80; -fx-min-height: 130; -fx-border-radius: 10px; -fx-border-width: 10px;");

        Image frenteImg = new Image(getClass().getResourceAsStream("/assets/img_juegos/juego_cartas/" + valor + ".png"));
        ImageView frente = new ImageView(frenteImg);
        frente.setFitWidth(80);
        frente.setFitHeight(130);
        frente.setVisible(false);

        cartaPane.getChildren().addAll(reverso, frente);

        cartaPane.setOnMouseClicked(e -> manejarClicCarta(valor, frente, cartaPane));

        return cartaPane;
    }
     
    private void manejarClicCarta(String valor, ImageView frente, StackPane cartaPane) {
        if (bloqueado || frente.isVisible() || valor.equals(primeraCarta)) return;

        frente.setVisible(true);

        if (primeraCarta == null) {
            primeraCarta = valor;
        } else {
            segundaCarta = valor;
            intentos++;
            actualizarContadores();

            String tipoPrimera = primeraCarta.split("_")[0];
            String tipoSegunda = segundaCarta.split("_")[0];

            if (tipoPrimera.equals(tipoSegunda)) {
                if (tipoPrimera.equals("calavera")) {
                    if (!relacionPocimaEncontrada && relacionesCompletadas == 0) {
                        perderJuego();
                        return;
                    }
                    if (relacionLibroEncontrada) {
                        perderJuego();
                        return;
                    }
                }

                if (tipoPrimera.equals("joya") && relacionesCompletadas < 5) {
                    perderJuego();
                    return;
                }

                if (tipoPrimera.equals("pocima")) {
                    relacionPocimaEncontrada = true;
                }

                if (tipoPrimera.equals("libro")) {
                    relacionLibroEncontrada = true;
                    mostrarPocima();
                }

                parejasEncontradas.add(primeraCarta);
                parejasEncontradas.add(segundaCarta);

                relacionesCompletadas++;
                paresEncontrados++;

                StackPane reversoPrimera = (StackPane) cartaPane.getChildren().get(0);
                reversoPrimera.setStyle("");
                StackPane primeraCartaPane = encontrarCartaPane(primeraCarta);
                if (primeraCartaPane != null) {
                    StackPane reversoSegunda = (StackPane) primeraCartaPane.getChildren().get(0);
                    reversoSegunda.setStyle("");
                }

                if (paresEncontrados == 6) {
                    ganarJuego();
                }
            } else {
                bloqueado = true;
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        Platform.runLater(() -> {

                            if (!parejasEncontradas.contains(valor)) {
                                frente.setVisible(false);
                            }
                            StackPane primeraCartaPane = encontrarCartaPane(primeraCarta);
                            if (primeraCartaPane != null && !parejasEncontradas.contains(primeraCarta)) {
                                ImageView primeraFrente = (ImageView) primeraCartaPane.getChildren().get(1);
                                primeraFrente.setVisible(false);
                            }
                            primeraCarta = null;
                            segundaCarta = null;
                            bloqueado = false;
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
    
    private StackPane encontrarCartaPane(String valor) {
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 4; columna++) {
                int index = fila * 4 + columna;
                if (cartas.get(index).equals(valor)) {
                    return (StackPane) gridCartas.getChildren().get(index);
                }
            }
        }
        return null;
    }
    
    private void mostrarPocima() {
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 4; columna++) {
                int index = fila * 4 + columna;
                StackPane cartaPane = (StackPane) gridCartas.getChildren().get(index);
                String valor = cartas.get(index);

                if (valor.startsWith("pocima_")) {
                    cartaPane.setStyle("-fx-border-color: #bed600; -fx-border-width: 3;");
                    new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                            Platform.runLater(() -> cartaPane.setStyle(""));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    return;
                }
            }
        }
    }
    
    private void perderJuego() {
        bloqueado = true;
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 4; columna++) {
                int index = fila * 4 + columna;
                StackPane cartaPane = (StackPane) gridCartas.getChildren().get(index);
                cartaPane.setDisable(true);
            }
        }
        
        FuncionesRepetidas.mostrarAlerta(Alert.AlertType.ERROR, "¡Has perdido!", "No has seguido las reglas del juego. Inténtalo de nuevo.");
        btnReiniciar.setDisable(false);
    }
     
    private void actualizarCartas() {
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 4; columna++) {
                int index = fila * 4 + columna;
                StackPane cartaPane = (StackPane) gridCartas.getChildren().get(index);
                ImageView frente = (ImageView) cartaPane.getChildren().get(1);
                String valor = cartas.get(index);
                
                frente.setVisible(parejasEncontradas.contains(valor) || 
                                valor.equals(primeraCarta) || 
                                valor.equals(segundaCarta));
            }
        }
    }
    
    private void actualizarContadores() {
        lblIntentos.setText("Intentos: " + intentos);
        lblPuntuacion.setText("Pares encontrados: " + paresEncontrados + "/6");
    }
    
    private void ganarJuego() {
        bloqueado = true;
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 4; columna++) {
                int index = fila * 4 + columna;
                StackPane cartaPane = (StackPane) gridCartas.getChildren().get(index);
                cartaPane.setDisable(true);
            }
        }

        usuario.setPuntos_usuario(usuario.getPuntos_usuario() + 10);

        if (usuario.getPuntos_usuario() >= 25) {
            usuario.setNivel_usuario(2);
        } else if (usuario.getPuntos_usuario() >= 10) {
            usuario.setNivel_usuario(1);
        }

        usuario.setJuego_completado_usuario(1);
    
        FuncionesRepetidas.actualizarUsuarioPuntos(usuario);
        
        FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "¡Felicidades!", "Has ganado 10 puntos. Puntos totales: " + usuario.getPuntos_usuario());
    
        juegoMemoria.setVisible(false);
        juegoDados.setVisible(true);
        iniciarJuegoDados();
    }
    
    @FXML private void reiniciarJuego() {
        iniciarJuego();
    }
    
    @FXML private void siguienteJuego() {
        juegoMemoria.setVisible(false);
        juegoDados.setVisible(true);
        iniciarJuegoDados();
    }
    
    // Juego de dados:
    private void iniciarJuegoDados() {
        vidas = 5;
        ronda = 1;
        actualizarContadoresDados();
        generarNumeroAleatorio();
        txtNumeroDado.clear();
        imgDado.setVisible(true);
        lblResultadoDado.setText("");
        btnReiniciarDados.setVisible(false);
        btnComprobarDado.setVisible(true);
        txtNumeroDado.setDisable(false);
    }
    
    private void generarNumeroAleatorio() {
        numeroAleatorio = (int) (Math.random() * 6) + 1;
        //System.err.println(numeroAleatorio);
    }
    
    private void actualizarContadoresDados() {
        lblRondaDados.setText("Ronda: " + ronda + "/6");
        lblVidasDados.setText("Vidas: " + vidas);
    }
    
    @FXML private void comprobarNumeroDado() {
        try {
            int numeroUsuario = Integer.parseInt(txtNumeroDado.getText());
            
            if (numeroUsuario < 1 || numeroUsuario > 6) {
                lblResultadoDado.setText("Por favor, introduce un número entre 1 y 6");
                return;
            }
            
            imgDado.setImage(new Image(getClass().getResourceAsStream("/assets/img_juegos/juego_dados/dado_" + numeroAleatorio + ".png")));
            
            if (numeroUsuario == numeroAleatorio) {
                lblResultadoDado.setText("¡Correcto!");
                ronda++;
                
                if (ronda > 6) {
                    ganarJuegoDados();
                } else {
                    actualizarContadoresDados();
                    generarNumeroAleatorio();
                    txtNumeroDado.clear();
                }
            } else {
                vidas--;
                actualizarContadoresDados();
                
                if (vidas <= 0) {
                    perderJuegoDados();
                } else {
                    lblResultadoDado.setText("Incorrecto. Inténtalo de nuevo");
                    txtNumeroDado.clear();
                    generarNumeroAleatorio();
                }
            }
            
        } catch (NumberFormatException e) {
            lblResultadoDado.setText("Por favor, introduce un número válido");
        }
    }
    
    private void ganarJuegoDados() {
        usuario.setPuntos_usuario(usuario.getPuntos_usuario() + 15);
        
        if (usuario.getPuntos_usuario() >= 25) {
            usuario.setNivel_usuario(2);
        } else if (usuario.getPuntos_usuario() >= 10) {
            usuario.setNivel_usuario(1);
        }
        
        FuncionesRepetidas.actualizarUsuarioPuntos(usuario);
        
        FuncionesRepetidas.mostrarAlerta(Alert.AlertType.INFORMATION, "¡Felicidades!", "Has ganado 15 puntos. Puntos totales: " + usuario.getPuntos_usuario());
        
        iniciarJuegoDados();
    }
    
    private void perderJuegoDados() {
        lblResultadoDado.setText("¡Has perdido! Te has quedado sin vidas");
        btnComprobarDado.setVisible(false);
        btnReiniciarDados.setVisible(true);
        txtNumeroDado.setDisable(true);
    }
    
    @FXML private void reiniciarJuegoDados() {
        iniciarJuegoDados();
    }
    
    
    
    
    // Musica:
    public void iniciarMusica() {
        try {
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
            mediaPlayer.setMute(!musicaActiva);
            mediaPlayer.play();

        } catch (Exception e) {
            System.out.println("Error al inicializar la música: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void cargarEstadoSonido() {
        Map<String, String> cacheData = FuncionesRepetidas.leerSesionCache();
        
        if (!cacheData.containsKey("sonido_juegos")) {
            musicaActiva = true;
            FuncionesRepetidas.guardarSesionCache("sonido_juegos", "true");
        } else {
            musicaActiva = Boolean.parseBoolean(cacheData.get("sonido_juegos"));
        }
        
        actualizarIconosSonido();
    }
    
    private void actualizarIconosSonido() {
        String icono = musicaActiva ? svgConSonido : svgSinSonido;
        svgQuitarMusicaMemoria.setContent(icono);
        svgQuitarMusicaDados.setContent(icono);
    }
    
    private void toggleMusica() {
        musicaActiva = !musicaActiva;
        
        if (mediaPlayer != null) {
            mediaPlayer.setMute(!musicaActiva);
        }
        
        actualizarIconosSonido();
        FuncionesRepetidas.guardarSesionCache("sonidoJuegos", String.valueOf(musicaActiva));
    }
    
    public void pararMusica() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
    
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            pararMusica();
        });
    }
    
}
