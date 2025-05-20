package modelos;

/**
 *
 * @author carmen_gordo
 */
public class RecetaIngrediente {
    private Ingrediente ingrediente;
    private String cantidad;

    public RecetaIngrediente(Ingrediente ingrediente, String cantidad) {
        this.ingrediente = ingrediente;
        this.cantidad = cantidad;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public String getCantidad() {
        return cantidad;
    }
}
