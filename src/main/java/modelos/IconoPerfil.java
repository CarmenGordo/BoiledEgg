package modelos;

/**
 *
 * @author carmen_gordo
 */
public class IconoPerfil {
    public int id_icono;
    public String nombre_icono;
    public String imagen_icono;
    public int desbloqueable_por_nivel_icono;
    public int nivel_requerido_icono;

    public IconoPerfil() {
    }

    public IconoPerfil(int id_icono, String nombre_icono, String imagen_icono, int desbloqueable_por_nivel_icono, int nivel_requerido_icono) {
        this.id_icono = id_icono;
        this.nombre_icono = nombre_icono;
        this.imagen_icono = imagen_icono;
        this.desbloqueable_por_nivel_icono = desbloqueable_por_nivel_icono;
        this.nivel_requerido_icono = nivel_requerido_icono;
    }

    public int getId_icono() {
        return id_icono;
    }

    public void setId_icono(int id_icono) {
        this.id_icono = id_icono;
    }

    public String getNombre_icono() {
        return nombre_icono;
    }

    public void setNombre_icono(String nombre_icono) {
        this.nombre_icono = nombre_icono;
    }

    public String getImagen_icono() {
        return imagen_icono;
    }

    public void setImagen_icono(String imagen_icono) {
        this.imagen_icono = imagen_icono;
    }

    public int getDesbloqueable_por_nivel_icono() {
        return desbloqueable_por_nivel_icono;
    }

    public void setDesbloqueable_por_nivel_icono(int desbloqueable_por_nivel_icono) {
        this.desbloqueable_por_nivel_icono = desbloqueable_por_nivel_icono;
    }

    public int getNivel_requerido_icono() {
        return nivel_requerido_icono;
    }

    public void setNivel_requerido_icono(int nivel_requerido_icono) {
        this.nivel_requerido_icono = nivel_requerido_icono;
    }

    

    
}
