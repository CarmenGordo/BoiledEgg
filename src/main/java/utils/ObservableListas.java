package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import javafx.collections.transformation.FilteredList;
import modelos.Favoritos;
import modelos.Ingrediente;
import modelos.Receta;

/**
 *
 * @author carmen_gordo
 */
public class ObservableListas {
    
    
    public static ObservableList<String> listaTiposRestaurantes = ordenar(
        FXCollections.observableArrayList("Italiano", "Francés", "Mediterráneo", "Japonés", "Chino", "Mexicano", "Indio", "Tailandés", "Vietnamita", "Coreano", "Griego", "Turco", "Libanés", "Peruano", "Brasileño", "Argentino", "Estadounidense (American)", "Vegetariano", "Vegano", "De marisco/pescado", "De fusión", "De autor/alta cocina", "Étnico (otros)"
    ));
    
    public static ObservableList<String> listaCiudades = ordenar(
            FXCollections.observableArrayList("A Coruña", "Albacete", "Alicante", "Almería", "Ávila", "Badajoz", "Barcelona", "Burgos", "Cáceres", "Cádiz", "Castellón", "Ceuta", "Ciudad Real", "Córdoba", "Cuenca", "Girona", "Granada", "Guadalajara", "Huelva", "Huesca", "Jaén", "Las Palmas de Gran Canaria", "León", "Lérida", "Lugo", "Madrid", "Málaga", "Murcia", "Orense", "Palencia", "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria", "Tarragona", "Teruel", "Toledo", "Valencia", "Valladolid", "Zamora", "Zaragoza"
    ));

    public static ObservableList<String> listaTiposRecetas = ordenar(
        FXCollections.observableArrayList("Vegetariana", "Vegana", "Keto", "Sin glúten", "Carnívora", "Mediterránea", "Postre", "Entrante", "Bebida", "Crudívora", "Sin azúcar", "Halal")
    );
    
    public static ObservableList<String> listaDificultadRecetas = FXCollections.observableArrayList("Fácil", "Media", "Dificil", "Experto");

    public static ObservableList<String> listaAlergenos = ordenar(
        FXCollections.observableArrayList("Glúten", "Lácteo", "Soja", "Frutos de cáscara", "Huevo", "Pescado", "Marísco", "Sésamo", "Apio", "Cacahuetes", "Moluscos", "Mostaza")
    );

    public static ObservableList<Integer> listaValoraciones = FXCollections.observableArrayList(1, 2, 3, 4, 5);

    public static ObservableList<String> listaTiposCoccion = ordenar(
        FXCollections.observableArrayList("Airfriyer", "Frito", "Hervido", "Vapor", "Crudo", "Asado")
    );

    // Filtros:
    public static ObservableList<String> listaFiltrar = ordenar(
        FXCollections.observableArrayList("Ingrediente", "Receta", "Restaurante")
    );
    
    public static ObservableList<Receta> listaRecetas = FXCollections.observableArrayList(FuncionesRepetidas.obtenerListaRecetas());
    public static FilteredList<Receta> listaFiltradaRecetas = new FilteredList<>(listaRecetas, p -> true);
    
    public static ObservableList<Favoritos> listaFavoritos = FXCollections.observableArrayList();
    //listaFavoritos.setAll(FuncionesRepetidas.obtenerFavoritosUsuario(idUsuario));

    public static ObservableList<Ingrediente> listaIngredientes = FXCollections.observableArrayList(FuncionesRepetidas.obtenerListaIngredientes());
    public static FilteredList<Ingrediente> listaFiltradaIngredientes = new FilteredList<>(listaIngredientes, p -> true);

    
    
    // PAra ordenar alfabeticamente:
    private static ObservableList<String> ordenar(ObservableList<String> lista) {
        FXCollections.sort(lista);
        return lista;
    }

}
