package tienda.Modelo;

public class detallePedido {
    private CD cd;
    private int cantidad;

    // Constructor
    public detallePedido(CD cd, int cantidad) {
        this.cd = cd;
        this.cantidad = cantidad;
    }

    // Getters
    public CD getCD() { return cd; }
    public int getCantidad() { return cantidad; }

    // Setters
    public void setCD(CD cd) { this.cd = cd; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

}