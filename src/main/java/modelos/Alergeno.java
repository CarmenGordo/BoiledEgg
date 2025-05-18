package modelos;

/**
 *
 * @author carmen_gordo
 */
public class Alergeno {
    private int id_alergeno;
    private String nombre_alergeno;
    private String imagen_alergeno;
    private String tipo_alergeno;

    public Alergeno() {
    }

    public Alergeno(int id_alergeno, String nombre_alergeno, String imagen_alergeno, String tipo_alergeno) {
        this.id_alergeno = id_alergeno;
        this.nombre_alergeno = nombre_alergeno;
        this.imagen_alergeno = imagen_alergeno;
        this.tipo_alergeno = tipo_alergeno;
    }

    public int getId_alergeno() {
        return id_alergeno;
    }

    public void setId_alergeno(int id_alergeno) {
        this.id_alergeno = id_alergeno;
    }

    public String getNombre_alergeno() {
        return nombre_alergeno;
    }

    public void setNombre_alergeno(String nombre_alergeno) {
        this.nombre_alergeno = nombre_alergeno;
    }

    public String getImagen_alergeno() {
        return imagen_alergeno;
    }

    public void setImagen_alergeno(String imagen_alergeno) {
        this.imagen_alergeno = imagen_alergeno;
    }

    public String getTipo_alergeno() {
        return tipo_alergeno;
    }

    public void setTipo_alergeno(String tipo_alergeno) {
        this.tipo_alergeno = tipo_alergeno;
    }
    
    
    
}
