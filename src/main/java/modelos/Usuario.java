package modelos;

/**
 *
 * @author carmen_gordo
 */
public class Usuario {
    private int id_usuario;
    private String nombre_usuario;
    private String email_usuario;
    private String contraseña_usuario;
    private int nivel_usuario;
    private int puntos_usuario;
    private int icono_perfil_id;
    private int juego_completado_usuario;

    public Usuario() {
    }

    public Usuario(int id_usuario, String nombre_usuario, String email_usuario, String contraseña_usuario, int nivel_usuario, int puntos_usuario, int icono_perfil_id, int juego_completado_usuario) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.email_usuario = email_usuario;
        this.contraseña_usuario = contraseña_usuario;
        this.nivel_usuario = nivel_usuario;
        this.puntos_usuario = puntos_usuario;
        this.icono_perfil_id = icono_perfil_id;
        this.juego_completado_usuario = juego_completado_usuario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public String getContraseña_usuario() {
        return contraseña_usuario;
    }

    public void setContraseña_usuario(String contraseña_usuario) {
        this.contraseña_usuario = contraseña_usuario;
    }

    public int getNivel_usuario() {
        return nivel_usuario;
    }

    public void setNivel_usuario(int nivel_usuario) {
        this.nivel_usuario = nivel_usuario;
    }

    public int getPuntos_usuario() {
        return puntos_usuario;
    }

    public void setPuntos_usuario(int puntos_usuario) {
        this.puntos_usuario = puntos_usuario;
    }

    public int getIcono_perfil_id() {
        return icono_perfil_id;
    }

    public void setIcono_perfil_id(int icono_perfil_id) {
        this.icono_perfil_id = icono_perfil_id;
    }

    public int getJuego_completado_usuario() {
        return juego_completado_usuario;
    }

    public void setJuego_completado_usuario(int juego_completado_usuario) {
        this.juego_completado_usuario = juego_completado_usuario;
    }
     
}
