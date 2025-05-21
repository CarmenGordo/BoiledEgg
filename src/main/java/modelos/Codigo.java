package modelos;

/**
 *
 * @author carmen_gordo
 */
public class Codigo {
    
    private int id_codigo;
    private String codigo_barras;
    private int id_ingrediente;
    private String nombre_tienda;
    private String nombre_marca;
    private String pais_origen;
    private String descripcion_opcional;

    public Codigo() {
    }

    public Codigo(int id_codigo, String codigo_barras, int id_ingrediente, String nombre_tienda, String nombre_marca, String pais_origen, String descripcion_opcional) {
        this.id_codigo = id_codigo;
        this.codigo_barras = codigo_barras;
        this.id_ingrediente = id_ingrediente;
        this.nombre_tienda = nombre_tienda;
        this.nombre_marca = nombre_marca;
        this.pais_origen = pais_origen;
        this.descripcion_opcional = descripcion_opcional;
    }

    public int getId_codigo() {
        return id_codigo;
    }

    public void setId_codigo(int id_codigo) {
        this.id_codigo = id_codigo;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public int getId_ingrediente() {
        return id_ingrediente;
    }

    public void setId_ingrediente(int id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }

    public String getNombre_tienda() {
        return nombre_tienda;
    }

    public void setNombre_tienda(String nombre_tienda) {
        this.nombre_tienda = nombre_tienda;
    }

    public String getNombre_marca() {
        return nombre_marca;
    }

    public void setNombre_marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }

    public String getPais_origen() {
        return pais_origen;
    }

    public void setPais_origen(String pais_origen) {
        this.pais_origen = pais_origen;
    }

    public String getDescripcion_opcional() {
        return descripcion_opcional;
    }

    public void setDescripcion_opcional(String descripcion_opcional) {
        this.descripcion_opcional = descripcion_opcional;
    }

    
}
