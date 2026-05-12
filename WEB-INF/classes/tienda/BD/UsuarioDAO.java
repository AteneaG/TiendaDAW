package tienda.BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tienda.CD;

public class UsuarioDAO {
     public static boolean registrarUsuario(String nombre, String nombreUsuario, String email, String password, String tarjetaTipo, String tarjetaNumero) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        try {
            System.out.println("Intentando conectar a la base de datos...");
            conn = BaseDeDatos.getConnection();
            System.out.println("Conexión establecida exitosamente!");
    
            String insertSql = "INSERT INTO usuarios (nombre_usuario, nombre, email, password, tarjeta_tipo, tarjeta_numero) " +
                               "VALUES (?, ?, ?, ?, ?, ?)";
    
            stmt = conn.prepareStatement(insertSql);
    
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, nombre);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, tarjetaTipo);
            stmt.setString(6, tarjetaNumero);
    
            int filasInsertadas = stmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Usuario insertado correctamente.");
            } else {
                System.out.println("No se insertó ningún usuario.");
                return false;
            }
    
            // Aquí usamos un Statement simple para consultar metadatos
            Statement metaStmt = conn.createStatement();
            rs = metaStmt.executeQuery("SELECT current_database(), current_user, version();");
    
            if (rs.next()) {
                System.out.println("\nInformación de la base de datos:");
                System.out.println("Base de datos actual: " + rs.getString(1));
                System.out.println("Usuario actual: " + rs.getString(2));
                System.out.println("Versión de PostgreSQL: " + rs.getString(3));
            }
    
            rs = metaStmt.executeQuery(
                "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema = 'public' ORDER BY table_name;");
    
            int tableCount = 0;
            while (rs.next()) {
                System.out.println(" - " + rs.getString(1));
                tableCount++;
            }
    
            if (tableCount == 0) {
                System.out.println("No se encontraron tablas en el esquema 'public'");
            } else {
                System.out.println("Total de tablas encontradas: " + tableCount);
            }
    
            return true;
    
        } catch (SQLException e) {
            System.err.println("Error durante la prueba de conexión: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                BaseDeDatos.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    public static boolean autenticarUsuario(String email, String password) {
        String sql = "SELECT 1 FROM usuarios WHERE email = ? AND password = ?";
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
            return false;
        }
    }

    public static int obtenerIdUsuario(String email) {
        String sql = "SELECT id FROM usuarios WHERE email = ?";
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return -1;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de usuario: " + e.getMessage());
            return -1;
        }
    }
    
    public static UsuarioDAO obtenerUsuarioPorID(int ID) {
      return null;
   }

}