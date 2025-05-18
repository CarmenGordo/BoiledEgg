package modelos;

/**
 *
 * @author carmen_gordo
 */
public class Receta {
    private int id_receta;
    private String nombre_receta;
    private String descripcion_receta;
    private String pasos_receta;
    private String imagen_receta;
    private int tiempo_preparacion_receta;
    private String dificultad_receta;
    private int autor_id;
    private String tipo_receta;
    private String tipo_coccion_receta;
    private int publicada_por_restaurante;

    public Receta() {
    }

    public Receta(int id_receta, String nombre_receta, String descripcion_receta, String pasos_receta, String imagen_receta, int tiempo_preparacion_receta, String dificultad_receta, int autor_id, String tipo_receta, String tipo_coccion_receta, int publicada_por_restaurante) {
        this.id_receta = id_receta;
        this.nombre_receta = nombre_receta;
        this.descripcion_receta = descripcion_receta;
        this.pasos_receta = pasos_receta;
        this.imagen_receta = imagen_receta;
        this.tiempo_preparacion_receta = tiempo_preparacion_receta;
        this.dificultad_receta = dificultad_receta;
        this.autor_id = autor_id;
        this.tipo_receta = tipo_receta;
        this.tipo_coccion_receta = tipo_coccion_receta;
        this.publicada_por_restaurante = publicada_por_restaurante;
    }

    public int getId_receta() {
        return id_receta;
    }

    public void setId_receta(int id_receta) {
        this.id_receta = id_receta;
    }

    public String getNombre_receta() {
        return nombre_receta;
    }

    public void setNombre_receta(String nombre_receta) {
        this.nombre_receta = nombre_receta;
    }

    public String getDescripcion_receta() {
        return descripcion_receta;
    }

    public void setDescripcion_receta(String descripcion_receta) {
        this.descripcion_receta = descripcion_receta;
    }

    public String getPasos_receta() {
        return pasos_receta;
    }

    public void setPasos_receta(String pasos_receta) {
        this.pasos_receta = pasos_receta;
    }

    public String getImagen_receta() {
        return imagen_receta;
    }

    public void setImagen_receta(String imagen_receta) {
        this.imagen_receta = imagen_receta;
    }

    public int getTiempo_preparacion_receta() {
        return tiempo_preparacion_receta;
    }

    public void setTiempo_preparacion_receta(int tiempo_preparacion_receta) {
        this.tiempo_preparacion_receta = tiempo_preparacion_receta;
    }

    public String getDificultad_receta() {
        return dificultad_receta;
    }

    public void setDificultad_receta(String dificultad_receta) {
        this.dificultad_receta = dificultad_receta;
    }

    public int getAutor_id() {
        return autor_id;
    }

    public void setAutor_id(int autor_id) {
        this.autor_id = autor_id;
    }

    public String getTipo_receta() {
        return tipo_receta;
    }

    public void setTipo_receta(String tipo_receta) {
        this.tipo_receta = tipo_receta;
    }

    public String getTipo_coccion_receta() {
        return tipo_coccion_receta;
    }

    public void setTipo_coccion_receta(String tipo_coccion_receta) {
        this.tipo_coccion_receta = tipo_coccion_receta;
    }

    public int getPublicada_por_restaurante() {
        return publicada_por_restaurante;
    }

    public void setPublicada_por_restaurante(int publicada_por_restaurante) {
        this.publicada_por_restaurante = publicada_por_restaurante;
    }
    
    
    
}
