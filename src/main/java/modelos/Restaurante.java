package modelos;

/**
 *
 * @author carmen_gordo
 */
public class Restaurante {
    public int id_restaurante;
    public String nombre_restaurante;
    public String email_restaurante;
    public String contraseña_restaurante;
    public String imagen_restaurante;
    public String ciudad_restaurante;
    public String direccion_restaurante;
    public String tipo_restaurante;
    public String url_restaurante;
    public int id_usuario;

    public Restaurante() {
    }

    public Restaurante(int id_restaurante, String nombre_restaurante, String email_restaurante, String contraseña_restaurante, String imagen_restaurante, String ciudad_restaurante, String direccion_restaurante, String tipo_restaurante, String url_restaurante, int id_usuario) {
        this.id_restaurante = id_restaurante;
        this.nombre_restaurante = nombre_restaurante;
        this.email_restaurante = email_restaurante;
        this.contraseña_restaurante = contraseña_restaurante;
        this.imagen_restaurante = imagen_restaurante;
        this.ciudad_restaurante = ciudad_restaurante;
        this.direccion_restaurante = direccion_restaurante;
        this.tipo_restaurante = tipo_restaurante;
        this.url_restaurante = url_restaurante;
        this.id_usuario = id_usuario;
    }

    public int getId_restaurante() {
        return id_restaurante;
    }

    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }

    public String getNombre_restaurante() {
        return nombre_restaurante;
    }

    public void setNombre_restaurante(String nombre_restaurante) {
        this.nombre_restaurante = nombre_restaurante;
    }

    public String getEmail_restaurante() {
        return email_restaurante;
    }

    public void setEmail_restaurante(String email_restaurante) {
        this.email_restaurante = email_restaurante;
    }

    public String getContraseña_restaurante() {
        return contraseña_restaurante;
    }

    public void setContraseña_restaurante(String contraseña_restaurante) {
        this.contraseña_restaurante = contraseña_restaurante;
    }

    public String getImagen_restaurante() {
        return imagen_restaurante;
    }

    public void setImagen_restaurante(String imagen_restaurante) {
        this.imagen_restaurante = imagen_restaurante;
    }

    public String getCiudad_restaurante() {
        return ciudad_restaurante;
    }

    public void setCiudad_restaurante(String ciudad_restaurante) {
        this.ciudad_restaurante = ciudad_restaurante;
    }

    public String getDireccion_restaurante() {
        return direccion_restaurante;
    }

    public void setDireccion_restaurante(String direccion_restaurante) {
        this.direccion_restaurante = direccion_restaurante;
    }

    public String getTipo_restaurante() {
        return tipo_restaurante;
    }

    public void setTipo_restaurante(String tipo_restaurante) {
        this.tipo_restaurante = tipo_restaurante;
    }

    public String getUrl_restaurante() {
        return url_restaurante;
    }

    public void setUrl_restaurante(String url_restaurante) {
        this.url_restaurante = url_restaurante;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }


}
