import java.util.*;

public class Carrito {
    private HashMap<String, Integer> items = new HashMap<String, Integer>();

    //Añadir cd al carrito, si ya existe, sumar la cantidad
    public void agregar(String cd, int cantidad) {
        if (items.containsKey(cd)) {
            int cantidadActual = items.get(cd);
            items.put(cd, cantidadActual + cantidad);
        } else {
            items.put(cd, cantidad);
        }
    }

    //Eliminar cd del carrito
    public void eliminar(String cd) {
        items.remove(cd);
    }

    //Obtener los items del carrito
    public HashMap<String, Integer> getItems() {
        return items;
    }
    
    //Vaciar el carrito
    public void vaciar() {
        items.clear();
    }

    //Calcular el total del carrito dado un mapa de precios
    public double calcularTotal() {
        double total = 0.0;
        for (String cd : items.keySet()) {
            StringTokenizer t = new StringTokenizer(cd, "|");
            t.nextToken(); t.nextToken(); t.nextToken();
            String precioStr = t.nextToken().replace('$', ' ').trim();
            total += Double.parseDouble(precioStr) * items.get(cd);
        }
        return total;
    }
}
