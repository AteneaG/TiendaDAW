package tienda.Modelo;

public class detallePedido {
    private int pedidoId;
    private CD cd;
    private int cantidad;

    // Constructor
    public detallePedido(int pedidoId, CD cd, int cantidad) {
        if(pedidoId <= 0){
            this.pedidoId = -1;
        }
        else this.pedidoId = pedidoId;
        this.cd = cd;
        this.cantidad = cantidad;
    }

    // Getters
    public int getPedidoId() { return pedidoId; }
    public CD getCD() { return cd; }
    public int getCantidad() { return cantidad; }

    // Setters
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
    public void setCD(CD cd) { this.cd = cd; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    //TODO REVISAR SI MANTENER
    @Override
    public String toString() {
        return "Pedido ID: " + pedidoId + ", Producto ID: " + cd.getId() + ", Cantidad: " + cantidad;
    }
}