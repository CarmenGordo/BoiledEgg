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
    private int id_ingrediente;
    private Timestamp fecha_escaneo;

    public UsuarioCodigo() {
    }

    public UsuarioCodigo(int id_escaneo, int id_usuario, int id_codigo, int id_ingrediente, Timestamp fecha_escaneo) {
        this.id_escaneo = id_escaneo;
        this.id_usuario = id_usuario;
        this.id_codigo = id_codigo;
        this.id_ingrediente = id_ingrediente;
        this.fecha_escaneo = fecha_escaneo;
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

    public int getId_ingrediente() {
        return id_ingrediente;
    }

    public void setId_ingrediente(int id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }
    
    public Timestamp getFecha_escaneo() {
        return fecha_escaneo;
    }

    public void setFecha_escaneo(Timestamp fecha_escaneo) {
        this.fecha_escaneo = fecha_escaneo;
    }

}
