package tienda.Modelo;

public class CD {
    private int id;
    private String titulo;
    private String artista;
    private String pais;
    private double precio;

    // Constructor
    public CD(int id, String titulo, String artista, String pais, double precio) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.pais = pais;
        this.precio = precio;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public String getPais() { return pais; }
    public double getPrecio() { return precio; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setArtista(String artista) { this.artista = artista; }
    public void setPais(String pais) { this.pais = pais; }
    public void setPrecio(double precio) { this.precio = precio; }

    //TODO REVISAR SI MANTENER
    @Override
    public String toString() {
        return titulo + " | " + artista + " | " + pais + " | $" + String.format("%.2f", precio);
    }
}