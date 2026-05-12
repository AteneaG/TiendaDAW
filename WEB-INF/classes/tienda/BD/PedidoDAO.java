package tienda.BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import tienda.CD;

public class PedidoDAO {

        
    public static boolean registrarPedido(int usuarioId, ArrayList<CD> carrito) {
        Connection conn = null;
        try {
            conn = BaseDeDatos.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el pedido (el total se actualizará automáticamente mediante el trigger)
            String sqlPedido = "INSERT INTO pedidos (usuario_id, fecha_pedido, total, estado)" + 
                                "VALUES (?, CURRENT_TIMESTAMP, 0, 'confirmado') RETURNING id";
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
                for (CD cd : carrito) {
                    int cantidad = cd.getCantidad();
                    double precioUnitario = cd.getPrecio();
                    double subtotal = precioUnitario * cantidad;

                    stmtDetalles.setInt(1, pedidoId);
                    stmtDetalles.setInt(2, cd.getId());
                    stmtDetalles.setInt(3, cantidad);
                    stmtDetalles.setDouble(4, precioUnitario);
                    stmtDetalles.setDouble(5, subtotal);
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



}