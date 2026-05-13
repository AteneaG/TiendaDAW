package tienda.BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tienda.Modelo.CD;

public class CDDAO
{
    public static List<CD> obtenerTodosLosCDs() {
        List<CD> cds = new ArrayList<>();
        String sql = "SELECT id, titulo, artista, pais, precio FROM CD";
        
        try (Connection conn = BaseDeDatos.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                CD cd = new CD(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("artista"),
                    rs.getString("pais"),
                    rs.getDouble("precio")
                );
                cds.add(cd);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener CDs: " + e.getMessage());
        }
        
        return cds;
    }
    
    public static CD obtenerCDPorId(int id) {
        String sql = "SELECT id, titulo, artista, pais, precio FROM CD WHERE id = ?";
        
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
                        rs.getDouble("precio")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al intentar obtener CD por el ID: " + e.getMessage());
        }
        
        return null;
    }

    public static CD obtenerCDPorArtistaYTitulo(String artista, String titulo) {
        String sql = "SELECT id, titulo, artista, pais, precio FROM CD WHERE artista = ? AND titulo = ?";
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artista);
            stmt.setString(2, titulo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CD cdReturn = new CD(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("artista"),
                        rs.getString("pais"),
                        rs.getDouble("precio")
                    );

                    System.out.println("Datos CD obtenido: " + cdReturn);

                    return cdReturn;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al intentar obtener CD por Artista ("+artista+") y Titulo ("+titulo+"): " + e.getMessage());
        }
        
        return null;
    }
}