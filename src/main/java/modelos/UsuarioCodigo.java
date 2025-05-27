package modelos;

import java.sql.Timestamp;

/**
 *
 * @author carmen_gordo
 */
public class UsuarioCodigo {
    private int id_escaneo;
    private int id_usuario;
    private int id_codigo;
    private Timestamp fecha_escaneo;
    private int cantidad;

    public UsuarioCodigo() {
    }

    public UsuarioCodigo(int id_escaneo, int id_usuario, int id_codigo, Timestamp fecha_escaneo, int cantidad) {
        this.id_escaneo = id_escaneo;
        this.id_usuario = id_usuario;
        this.id_codigo = id_codigo;
        this.fecha_escaneo = fecha_escaneo;
        this.cantidad = cantidad;
    }

    public int getId_escaneo() {
        return id_escaneo;
    }

    public void setId_escaneo(int id_escaneo) {
        this.id_escaneo = id_escaneo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_codigo() {
        return id_codigo;
    }

    public void setId_codigo(int id_codigo) {
        this.id_codigo = id_codigo;
    }

    public Timestamp getFecha_escaneo() {
        return fecha_escaneo;
    }

    public void setFecha_escaneo(Timestamp fecha_escaneo) {
        this.fecha_escaneo = fecha_escaneo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
}
