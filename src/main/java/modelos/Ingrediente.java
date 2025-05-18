package modelos;

/**
 *
 * @author carmen_gordo
 */
public class Ingrediente {
    private int id_ingrediente;
    private String nombre_ingrediente;
    private String imagen_ingrediente;
    private String tipo_ingrediente;
    private int alergeno_ingrediente;
    private String tipo_alergeno_ingrediente;

    public Ingrediente() {
    }

    public Ingrediente(int id_ingrediente, String nombre_ingrediente, String imagen_ingrediente, String tipo_ingrediente, int alergeno_ingrediente, String tipo_alergeno_ingrediente) {
        this.id_ingrediente = id_ingrediente;
        this.nombre_ingrediente = nombre_ingrediente;
        this.imagen_ingrediente = imagen_ingrediente;
        this.tipo_ingrediente = tipo_ingrediente;
        this.alergeno_ingrediente = alergeno_ingrediente;
        this.tipo_alergeno_ingrediente = tipo_alergeno_ingrediente;
    }

    public int getId_ingrediente() {
        return id_ingrediente;
    }

    public void setId_ingrediente(int id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }

    public String getNombre_ingrediente() {
        return nombre_ingrediente;
    }

    public void setNombre_ingrediente(String nombre_ingrediente) {
        this.nombre_ingrediente = nombre_ingrediente;
    }

    public String getImagen_ingrediente() {
        return imagen_ingrediente;
    }

    public void setImagen_ingrediente(String imagen_ingrediente) {
        this.imagen_ingrediente = imagen_ingrediente;
    }

    public String getTipo_ingrediente() {
        return tipo_ingrediente;
    }

    public void setTipo_ingrediente(String tipo_ingrediente) {
        this.tipo_ingrediente = tipo_ingrediente;
    }

    public int getAlergeno_ingrediente() {
        return alergeno_ingrediente;
    }

    public void setAlergeno_ingrediente(int alergeno_ingrediente) {
        this.alergeno_ingrediente = alergeno_ingrediente;
    }

    public String getTipo_alergeno_ingrediente() {
        return tipo_alergeno_ingrediente;
    }

    public void setTipo_alergeno_ingrediente(String tipo_alergeno_ingrediente) {
        this.tipo_alergeno_ingrediente = tipo_alergeno_ingrediente;
    }

    
   
    
}
