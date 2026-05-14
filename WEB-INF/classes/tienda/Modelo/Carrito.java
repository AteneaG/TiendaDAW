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

    public Carrito(int pedidoID, int usuarioID) {
        this.usuarioID = usuarioID;
        this.pedidoID = pedidoID;
        this.detallesPedido = new HashMap<>();
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
        PedidoDAO.actualizarUsuarioPedido(this.pedidoID, usuarioID);
    }




    //Añadir detalle pedido al carrito, si ya existe, sumar la cantidad
    public void agregarDetalle(detallePedido dp) {
        CD cd = dp.getCD();
        int cantidad = dp.getCantidad();

        if (this.detallesPedido.containsKey(cd.getId())) {
            System.out.println("\nCD encontrado: Modificando cantidad");
            detallePedido existingDp = detallesPedido.get(cd.getId());
            existingDp.setCantidad(existingDp.getCantidad() + cantidad);
            PedidoDAO.actualizarCantidad(pedidoID, cd.getId(), existingDp.getCantidad());
        } else {
            System.out.println("\nCD no encontrado: Añadiendo nuevo detalle");
            detallesPedido.put(cd.getId(), new detallePedido(cd, cantidad));
            PedidoDAO.anhadirDetalleAPedido(this.pedidoID, detallesPedido.get(cd.getId()));
        }
    }

    //Eliminar cd del carrito
    public void eliminar(CD cd) {
        detallesPedido.remove(cd.getId());
        PedidoDAO.eliminarDetallePedido(this.pedidoID, cd.getId());
    }
    
    //Vaciar el carrito
    public void vaciar() {
        detallesPedido.clear();
        PedidoDAO.eliminarPedido(this.pedidoID);
    }

    public void terminarPedido() {
        PedidoDAO.actualizarFechaPedido(this.pedidoID);
    }

    //Calcular el total del carrito dado un mapa de precios
    public double calcularTotal() {
        double total = 0.0;
        for (detallePedido detalle : detallesPedido.values()) {
            total += detalle.getCD().getPrecio() * detalle.getCantidad();
        }
        PedidoDAO.actualizarTotalPedido(this.pedidoID, total);
        return total;
    }
}
