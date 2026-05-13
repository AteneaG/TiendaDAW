package tienda.BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import tienda.Modelo.CD;
import tienda.Modelo.detallePedido;

public class PedidoDAO {

    public static boolean registrarPedidoMultiplesCDs(int usuarioId, ArrayList<detallePedido> carrito) {
        Connection conn = null;
        try {
            conn = BaseDeDatos.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el pedido (el total se actualizará automáticamente mediante el trigger)
            String sqlPedido = "INSERT INTO pedidos (usuario_id, fecha_pedido, total)" + 
                                "VALUES (?, CURRENT_TIMESTAMP, 0) RETURNING id";
            int pedidoId;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido)) {
                stmtPedido.setInt(1, usuarioId);
                try (ResultSet rs = stmtPedido.executeQuery()) {
                    if (rs.next()) {
                        pedidoId = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID del pedido");
                    }
                }
            }

            // 2. Insertar los productos del pedido
            String sqlDetalles = "INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad, precio_unitario, subtotal)" +
                                    " VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtDetalles = conn.prepareStatement(sqlDetalles)) {
                for (detallePedido dp : carrito) {

                    dp.setPedidoId(pedidoId); 

                    stmtDetalles.setInt(1, dp.getPedidoId());
                    stmtDetalles.setInt(2, dp.getCD().getId());
                    stmtDetalles.setInt(3, dp.getCantidad());
                    stmtDetalles.addBatch();
                }
                stmtDetalles.executeBatch();
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

    public static int registrarPedidoUnCD(int usuarioId, detallePedido detalle) {
        Connection conn = null;
        try {
            conn = BaseDeDatos.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el pedido (el total se actualizará automáticamente mediante el trigger)
            String sqlPedido = "INSERT INTO pedidos (usuario_id, fecha_pedido, total)" + 
                                "VALUES (?, CURRENT_TIMESTAMP, ?) RETURNING id";
            int pedidoId;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido)) {
                stmtPedido.setInt(1, usuarioId);
                stmtPedido.setDouble(2, detalle.getCD().getPrecio() * detalle.getCantidad());
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
                stmtDetalles.setInt(1, detalle.getPedidoId());
                stmtDetalles.setInt(2, detalle.getCD().getId());
                stmtDetalles.setInt(3, detalle.getCantidad());
                stmtDetalles.addBatch();
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

    public static boolean anhadirDetalleAPedido(int pedidoId,  ArrayList<detallePedido> carrito) {
        Connection conn = null;
        try {
            conn = BaseDeDatos.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción


            // 1. Insertar los productos del pedido
            String sqlDetalles = "INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad)" +
                                    " VALUES (?, ?, ?)";
            try (PreparedStatement stmtDetalles = conn.prepareStatement(sqlDetalles)) {
                for (detallePedido detalle : carrito) {
                    stmtDetalles.setInt(1, detalle.getPedidoId());
                    stmtDetalles.setInt(2, detalle.getCD().getId());
                    stmtDetalles.setInt(3, detalle.getCantidad());
                    stmtDetalles.addBatch();
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

    public static boolean eliminarPedido(int pedidoId) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        
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

    public static boolean eliminarDetallePedido(int detalleId) {
        String sql = "DELETE FROM detalle_pedidos WHERE id = ?";
        
        //Eliminar el total del pedido.

        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detalleId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se eliminó al menos un registro
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle de pedido: " + e.getMessage());
            return false;
        }
    }

    public static int obtenerIdPedidoDelDetalle(int detalleId) {
        String sql = "SELECT pedido_id FROM detalle_pedidos WHERE id = ?";
        
        try (Connection conn = BaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detalleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("pedido_id");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ID del pedido para el detalle: " + e.getMessage());
        }
        
        return -1; // Retorna -1 si no se encontró o hubo un error
    }
}