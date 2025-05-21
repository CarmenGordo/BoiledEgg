package modelos;

import java.util.Date;

/**
 *
 * @author carmen_gordo
 */
public class Favoritos {
    private int id_favorito;
    private TipoObjeto tipo_objeto;
    private int id_objeto;
    private int id_usuario;
    private Date fecha_favorito;

    public Favoritos() {
    }

    public Favoritos(int id_favorito, TipoObjeto tipo_objeto, int id_objeto, int id_usuario, Date fecha_favorito) {
        this.id_favorito = id_favorito;
        this.tipo_objeto = tipo_objeto;
        this.id_objeto = id_objeto;
        this.id_usuario = id_usuario;
        this.fecha_favorito = fecha_favorito;
    }

    public int getId_favorito() {
        return id_favorito;
    }

    public void setId_favorito(int id_favorito) {
        this.id_favorito = id_favorito;
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

    public Date getFecha_favorito() {
        return fecha_favorito;
    }

    public void setFecha_favorito(Date fecha_favorito) {
        this.fecha_favorito = fecha_favorito;
    }

    
    public enum TipoObjeto {
        RECETA("Receta"),
        RESTAURANTE("Restaurante"),
        INGREDIENTE("Ingrediente");

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
