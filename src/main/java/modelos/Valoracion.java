package modelos;

import java.util.Date;

/**
 *
 * @author carmen_gordo
 */
public class Valoracion {
    private int id_valoracion;
    private TipoObjeto tipo_objeto;
    private int id_objeto;
    private int id_usuario;
    private int puntuacion_valoracion;
    private String comentario_valoracion;
    private Date fecha_valoracion;

    public Valoracion() {
    }

    public Valoracion(int id_valoracion, TipoObjeto tipo_objeto, int id_objeto, int id_usuario, int puntuacion_valoracion, String comentario_valoracion, Date fecha_valoracion) {
        this.id_valoracion = id_valoracion;
        this.tipo_objeto = tipo_objeto;
        this.id_objeto = id_objeto;
        this.id_usuario = id_usuario;
        this.puntuacion_valoracion = puntuacion_valoracion;
        this.comentario_valoracion = comentario_valoracion;
        this.fecha_valoracion = fecha_valoracion;
    }

    public int getId_valoracion() {
        return id_valoracion;
    }

    public void setId_valoracion(int id_valoracion) {
        this.id_valoracion = id_valoracion;
    }

    public TipoObjeto getTipo_objeto() {
        return tipo_objeto;
    }

    public void setTipo_objeto(TipoObjeto tipo_objeto) {
        this.tipo_objeto = tipo_objeto;
    }

    public int getId_objeto() {
        return id_objeto;
    }

    public void setId_objeto(int id_objeto) {
        this.id_objeto = id_objeto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getPuntuacion_valoracion() {
        return puntuacion_valoracion;
    }

    public void setPuntuacion_valoracion(int puntuacion_valoracion) {
        this.puntuacion_valoracion = puntuacion_valoracion;
    }

    public String getComentario_valoracion() {
        return comentario_valoracion;
    }

    public void setComentario_valoracion(String comentario_valoracion) {
        this.comentario_valoracion = comentario_valoracion;
    }

    public Date getFecha_valoracion() {
        return fecha_valoracion;
    }

    public void setFecha_valoracion(Date fecha_valoracion) {
        this.fecha_valoracion = fecha_valoracion;
    }

    
    
    
    public enum TipoObjeto {
        RECETA("Receta"),
        RESTAURANTE("Restaurante");

        private final String valor;

        TipoObjeto(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static TipoObjeto fromString(String texto) {
            for (TipoObjeto tipo : TipoObjeto.values()) {
                if (tipo.valor.equalsIgnoreCase(texto)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("TipoObjeto inválido: " + texto);
        }
    }

    
}
