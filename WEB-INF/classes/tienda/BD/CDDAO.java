package tienda.BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tienda.Modelo.CD;

public class CDDAO
{
    public static List<CD> obtenerTodosLosCDs() {
        List<CD> cds = new ArrayList<>();
        String sql = "SELECT id, titulo, artista, pais, precio FROM \"CD\"";
        
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
        String sql = "SELECT id, titulo, artista, pais, precio FROM \"CD\" WHERE id = ?";
        
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
        Connection conn = null;
        try {
            conn = BaseDeDatos.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el pedido (el total se actualizará automáticamente mediante el trigger)
            String sql = "SELECT id, titulo, artista, pais, precio FROM \"CD\" WHERE artista = ? AND titulo = ?";
        
        
            try (PreparedStatement stmtPedido = conn.prepareStatement(sql)) {
                stmtPedido.setString(1, artista);
                stmtPedido.setString(2, titulo);
                try (ResultSet rs = stmtPedido.executeQuery()) {
                    if (rs.next()) {
                        CD cdReturn = new CD(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("artista"),
                            rs.getString("pais"),
                            rs.getDouble("precio")
                        );
                    
                        System.out.println("\nDatos CD obtenido: " + cdReturn);
                    
                        return cdReturn;
                    }else {
                        throw new SQLException("\nNo se pudo obtener el CD");
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("\nError al intentar obtener CD por Artista ("+artista+") y Titulo ("+titulo+"): " + e.getMessage());
        }
        
        return null;
    }
}