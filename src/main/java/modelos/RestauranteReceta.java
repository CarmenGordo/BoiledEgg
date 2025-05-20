package modelos;

/**
 *
 * @author carmen_gordo
 */
public class RestauranteReceta {
    private int id_restaurante;
    private int id_receta;

    public RestauranteReceta(int id_restaurante, int id_receta) {
        this.id_restaurante = id_restaurante;
        this.id_receta = id_receta;
    }

    public int getId_restaurante() {
        return id_restaurante;
    }

    public int getId_receta() {
        return id_receta;
    }
}
