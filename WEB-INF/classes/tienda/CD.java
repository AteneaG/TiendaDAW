package tienda;

public class CD {
    private int id;
    private String titulo;
    private String artista;
    private String pais;
    private double precio;
    private int cantidad;

    // Constructor
    public CD(int id, String titulo, String artista, String pais, double precio, int cantidad) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.pais = pais;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public String getPais() { return pais; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setArtista(String artista) { this.artista = artista; }
    public void setPais(String pais) { this.pais = pais; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    //TODO REVISAR SI MANTENER
    @Override
    public String toString() {
        return titulo + " | " + artista + " | " + pais + " | $" + String.format("%.2f", precio);
    }
}