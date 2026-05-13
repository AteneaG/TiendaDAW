package tienda.BD;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import tienda.Modelo.CD;

/**
 * Clase que gestiona la conexión con la base de datos PostgreSQL
 */
public class BaseDeDatos {
    private static final String URL = "jdbc:postgresql://aws-0-eu-west-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.odgpdypondrfuuiwhbqi";
    private static final String PASSWORD = "tez21lCwlIA6sTeM";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("\nConexión establecida con éxito.");
            } catch (ClassNotFoundException e) {
                System.err.println("\nNo se pudo cargar el driver: " + e.getMessage());
                throw new SQLException(e);
            }
        }
        return connection;
    }

    
    /**
     * Cierra la conexión a la base de datos
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada con éxito.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}



