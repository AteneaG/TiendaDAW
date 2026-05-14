package tienda.BD;

import java.sql.*;

import tienda.Modelo.CD;
import tienda.Modelo.Carrito;
import tienda.Modelo.detallePedido;

public class PedidoDAO {

    //Registrar PEDIDOS
    public static int registrarPedidoUnCD(detallePedido detalle) {
        Connection conn = null;
        System.out.println("\nPedidoDAO: Probando conexion: ");

        try {
            conn = BaseDeDatos.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el pedido (el total se actualizará automáticamente mediante el trigger)
            String sqlPedido = "INSERT INTO pedidos (usuario_id, fecha_pedido, total)" + 
                                "VALUES (NULL, NULL, ?) RETURNING id";
            int pedidoId;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido)) {
                stmtPedido.setDouble(1, detalle.getCD().getPrecio() * detalle.getCantidad());
                try (ResultSet rs = stmtPedido.executeQuery()) {
                    if (rs.next()) {
                        pedidoId = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID del pedido");
                    }
                }
            }

            // 2. Insertar los productos del pedido
            String sqlDetalles = "INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad)" +
                                    " VALUES (?, ?, ?)";
            try (PreparedStatement stmtDetalles = conn.prepareStatement(sqlDetalles)) {
                stmtDetalles.setInt(1, pedidoId);
                stmtDetalles.setInt(2, detalle.getCD().getId());
                stmtDetalles.setInt(3, detalle.getCantidad());
                int filasInsertadas = stmtDetalles.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Detalle del pedido registrado con éxito para pedido ID: " + pedidoId);
                } else {
                    throw new SQLException("No se pudo registrar el detalle del pedido");
                }
            }

            // Opcionalmente, podemos obtener el total final para confirmación
            double totalFinal = 0;
            String sqlTotal = "SELECT total FROM pedidos WHERE id = ?";
            try (PreparedStatement stmtTotal = conn.prepareStatement(sqlTotal)) {
                stmtTotal.setInt(1, pedidoId);
                try (ResultSet rs = stmtTotal.executeQuery()) {
                    if (rs.next()) {
                        totalFinal = rs.getDouble("total");
                    }
                }
            }

            System.out.println("Pedido registrado con éxito. ID: " + pedidoId + ", Total: " + totalFinal);
            conn.commit(); // Confirmar transacción
            return pedidoId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al registrar pedido: " + e.getMessage());
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }


    //Actualizar datos PEDIDOS
    public static boolean anhadirDetalleAPedido(int pedidoId, detallePedido dp) {
        Connection conn = null;
        System.out.println("\nPedidoDAO: Probando conexion: ");

        try {
            conn = BaseDeDatos.getConnection();
            conn.setAutoCommit(false);
            try{
                // 1. Insertar los productos del pedido
                String sqlDetalles = "INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad)" +
                                        " VALUES (?, ?, ?)";
                try (PreparedStatement stmtDetalles = conn.prepareStatement(sqlDetalles)) {
                    stmtDetalles.setInt(1, pedidoId);
                    stmtDetalles.setInt(2, dp.getCD().getId());
                    stmtDetalles.setInt(3, dp.getCantidad());
                    int filasInsertadas = stmtDetalles.executeUpdate();
                    if (filasInsertadas > 0) {
                        System.out.println("Detalle del pedido registrado con éxito para pedido ID: " + pedidoId);
                    } else {
                        throw new SQLException("No se pudo registrar el detalle del pedido");
                    }

                }
            } catch (SQLException e) {
                System.err.println("Error al registrar detalle del pedido: " + e.getMessage());
                conn.rollback();
                return false;
            }
            

            // Opcionalmente, podemos obtener el total final para confirmación
            double totalFinal = 0;
            String sqlTotal = "SELECT total FROM pedidos WHERE id = ?";
            try (PreparedStatement stmtTotal = conn.prepareStatement(sqlTotal)) {
                stmtTotal.setInt(1, pedidoId);
                try (ResultSet rs = stmtTotal.executeQuery()) {
                    if (rs.next()) {
                        totalFinal = rs.getDouble("total");
                    }
                }
            }

            System.out.println("Pedido registrado con éxito. ID: " + pedidoId + ", Total: " + totalFinal);
            conn.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al registrar pedido: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    public static boolean actualizarUsuarioPedido(int pedidoId, int usuarioId) {
        Connection conn = null;
        System.out.println("\nPedidoDAO: Probando conexion: ");

        try {
            conn = BaseDeDatos.getConnection();
            String sql = "UPDATE pedidos SET usuario_id = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);
                stmt.setInt(2, pedidoId);
                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Usuario actualizado con éxito para pedido ID: " + pedidoId);
                    return true;
                } else {
                    System.err.println("No se pudo actualizar el usuario para pedido ID: " + pedidoId);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario del pedido: " + e.getMessage());
            return false;
        } 
    }

    public static boolean actualizarCantidad(int pedidoId, int productoId, int nuevaCantidad) {
        System.out.println("\nPedidoDAO: Probando conexion: ");

        String sql = "UPDATE detalle_pedidos SET cantidad = ? WHERE pedido_id = ? AND producto_id = ?";
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nuevaCantidad);
            stmt.setInt(2, pedidoId);
            stmt.setInt(3, productoId);

            System.out.println("Actualizando: pedidoId=" + pedidoId + ", productoId=" + productoId + ", nuevaCantidad=" + nuevaCantidad);
            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Filas afectadas: " + filasAfectadas);

            if (filasAfectadas > 0) System.out.println("Cantidad actualizada para pedido ID " + pedidoId + ", CD ID " + productoId + ": " + nuevaCantidad);
            else System.err.println("No se pudo actualizar la cantidad para pedido ID " + pedidoId + ", CD ID " + productoId);

            return filasAfectadas > 0; // Retorna true si se actualizó al menos un registro
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad del pedido: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarFechaPedido(int pedidoId) {
        System.out.println("\nPedidoDAO: Actualizando fecha pedido ID: " + pedidoId);

        String sql = "UPDATE pedidos SET fecha_pedido = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) System.out.println("Fecha actualizada para pedido ID: " + pedidoId);
            else System.err.println("No se encontró pedido con ID: " + pedidoId);

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar fecha del pedido: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarDetallePedido(int pedidoId, int detalleId) {

        System.out.println("\nPedidoDAO: Probando conexion: ");
        
        String sql = "DELETE FROM detalle_pedidos WHERE pedido_id = ? AND producto_id = ?";
        
        //Eliminar el total del pedido.

        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pedidoId);
            stmt.setInt(2, detalleId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se eliminó al menos un registro
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle de pedido: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarTotalPedido(int pedidoID, double nuevoTotal) {
        String sql = "UPDATE pedidos SET total = ? WHERE id = ?";
        System.out.println("\nPedidoDAO: Probando conexion: ");
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, nuevoTotal);
            stmt.setInt(2, pedidoID);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se actualizó al menos un registro
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar total del pedido: " + e.getMessage());
            return false;
        }
    }

    //Eliminar PEDIDOS
    public static boolean eliminarPedido(int pedidoId) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        
        System.out.println("\nPedidoDAO: Probando conexion: ");
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pedidoId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se eliminó al menos un registro
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
            return false;
        }
    }


    // OBTENER DATOS PEDIDOS
    public static Carrito obtenerDatosPedido(int pedidoId) {
        
        System.out.println("\nPedidoDAO: Probando conexion: ");

        String sql =
            "SELECT p.usuario_id, dp.cantidad, " +
            "       c.id AS cd_id, c.titulo, c.artista, c.pais, c.precio " +
            "FROM pedidos p " +
            "JOIN detalle_pedidos dp ON dp.pedido_id = p.id " +
            "JOIN \"CD\" c ON c.id = dp.producto_id " +
            "WHERE p.id = ?";

        System.out.println("[DEBUG] SQL preparada:");
        System.out.println(sql);

        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);


            try (ResultSet rs = stmt.executeQuery()) {

                Carrito carrito = null;

                int contadorProductos = 0;

                while (rs.next()) {

                    int usuarioId = rs.getInt("usuario_id");

                    if (carrito == null) {
                        carrito = new Carrito(pedidoId, usuarioId);
                    }

                    int cdId = rs.getInt("cd_id");
                    String titulo = rs.getString("titulo");
                    String artista = rs.getString("artista");
                    String pais = rs.getString("pais");
                    double precio = rs.getDouble("precio");
                    int cantidad = rs.getInt("cantidad");

                    CD cd = new CD(cdId, titulo, artista, pais, precio);

                    detallePedido detalle = new detallePedido(cd, cantidad);

                    carrito.getDetallesPedido().put(cd.getId(), detalle);

                    contadorProductos++;

                }

                return carrito;
            }

        } catch (SQLException e) {

            System.err.println("\n[ERROR] Error SQL al obtener datos del pedido.");
            e.printStackTrace();

            return null;
        }
    }

}