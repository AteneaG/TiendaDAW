import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public static List<CD> obtenerTodosLosProductos() {
        List<CD> productos = new ArrayList<>();
        String sql = "SELECT id, titulo, artista, pais, precio, stock FROM productos";
        
        try (Connection conn = BaseDeDatos.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                CD cd = new CD(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("artista"),
                    rs.getString("pais"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
                productos.add(cd);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        
        return productos;
    }
    
    public static CD obtenerProductoPorId(int id) {
        String sql = "SELECT id, titulo, artista, pais, precio, stock FROM productos WHERE id = ?";
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CD(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("artista"),
                        rs.getString("pais"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
        }
        
        return null;
    }
}