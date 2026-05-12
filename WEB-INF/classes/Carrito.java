import java.util.*;

public class Carrito {
    private HashMap<Integer, CD> items = new HashMap<Integer, CD>();    //Id, CD

    //Añadir cd al carrito, si ya existe, sumar la cantidad
    public void agregar(CD cd) {
        if (items.containsKey(cd.getId())) {
            CD cdExistente = items.get(cd.getId());
            cdExistente.setCantidad(cdExistente.getCantidad() + cd.getCantidad());
        } else {
            items.put(cd.getId(), cd);
        }
    }

    //Eliminar cd del carrito
    public void eliminar(int id) {
        items.remove(id);
    }

    //Obtener los items del carrito
    public HashMap<Integer, CD> getItems() {
        return items;
    }
    
    //Vaciar el carrito
    public void vaciar() {
        items.clear();
    }

    //Calcular el total del carrito dado un mapa de precios
    public double calcularTotal() {
        double total = 0.0;
        for (CD cd : items.values()) {
            total += cd.getPrecio() * cd.getCantidad();
        }
        return total;
    }
}
