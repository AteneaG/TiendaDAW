package tienda.BD;

import java.sql.*;

public class UsuarioDAO {

    /**
     * Registra un nuevo usuario en la BD.
     * Esquema: usuarios(id VARCHAR PK, email, password, tarjeta_tipo, tarjeta_numero)
     * 'id' actúa como nombre de usuario (identificador único).
     */
    public static boolean registrarUsuario(String email, String password, String tarjetaTipo, long tarjetaNumero) {
        String sql = "INSERT INTO usuarios (email, password, tarjeta_tipo, tarjeta_numero) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, tarjetaTipo);
            stmt.setLong(4, tarjetaNumero);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario registrado correctamente: " + email);
                return true;
            } else {
                System.err.println("No se insertó ningún usuario.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprueba si el email y password corresponden a un usuario existente.
     */
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

    /**
     * Devuelve el id (nombreUsuario) del usuario dado su email.
     * Retorna -1 si no existe.
     */
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
}