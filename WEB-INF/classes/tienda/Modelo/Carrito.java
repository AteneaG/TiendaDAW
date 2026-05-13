package tienda.Modelo;


import java.util.*;

import tienda.BD.PedidoDAO;

public class Carrito {
    private final int pedidoID; 
    private int usuarioID;
    private HashMap<Integer, detallePedido> detallesPedido; 

    public Carrito(detallePedido dp) {
        this.usuarioID = -1;                                            // Usuario no registrado
        this.pedidoID = PedidoDAO.registrarPedidoUnCD(dp);              // ID Pedido BD
        this.detallesPedido = new HashMap<Integer, detallePedido>();    //cdID, detallePedido
        this.detallesPedido.put(dp.getCD().getId(), dp);                //Añadir el CD al carrito
    }


    

    //GETTERS
    public HashMap<Integer, detallePedido> getDetallesPedido() {
        return detallesPedido;
    }
    public int getPedidoID() {
        return pedidoID;
    }
    public int getUsuarioID() {
        return usuarioID;
    }


    //SETTERS
    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
        
    }




    //Añadir detalle pedido al carrito, si ya existe, sumar la cantidad
    public void agregar(CD cd, int cantidad) {
        if (detallesPedido.containsKey(cd.getId())) {
            detallePedido dp = detallesPedido.get(cd.getId());
            dp.setCantidad(dp.getCantidad() + cantidad);
        } else {
            detallesPedido.put(cd.getId(), new detallePedido(cd, cantidad));
        }
    }

    //Eliminar cd del carrito
    public void eliminar(CD cd) {
        detallesPedido.remove(cd.getId());
    }
    
    //Vaciar el carrito
    public void vaciar() {
        detallesPedido.clear();
    }

    //Calcular el total del carrito dado un mapa de precios
    public double calcularTotal() {
        double total = 0.0;
        for (detallePedido detalle : detallesPedido.values()) {
            total += detalle.getCD().getPrecio() * detalle.getCantidad();
        }
        return Math.round(total * 100.0) / 100.0;
    }
}
