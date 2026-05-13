<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList, java.util.Date, java.text.SimpleDateFormat" %>
<%@ page import="CD.CD" %> 

<%
    // Recuperamos el carrito desde la sesión
    ArrayList<CD> carrito = (ArrayList<CD>) session.getAttribute("carrito");
    if (carrito == null) {
        carrito = new ArrayList<CD>();
    }
    
    // Calculamos totales
    double subtotal = 0.0;
    double iva = 0.0;
    double total = 0.0;
    
    for (CD elemento : carrito) {
        subtotal += elemento.getPrecio() * elemento.getCantidad();
    }
    iva = subtotal * 0.21; // 21% de IVA
    total = subtotal + iva;
    
    // Generamos fecha y número de factura
    Date fechaActual = new Date();
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String numeroFactura = "F-" + System.currentTimeMillis();

    String nombre = (String) session.getAttribute("nombreCliente");
    String email = (String) session.getAttribute("emailCliente");
    String tipoTarjeta = (String) session.getAttribute("tipoTarjeta");
    String numeroTarjeta = (String) session.getAttribute("numeroTarjeta");

    String tarjetaFormateada = "****";
    if (numeroTarjeta != null && numeroTarjeta.length() >= 4) {
        int longitud = numeroTarjeta.length();
        String ultimos4 = numeroTarjeta.substring(longitud - 4);
        String asteriscos = "*".repeat(longitud - 4);
        tarjetaFormateada = asteriscos + ultimos4;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Recibo de Compra - Música para DAA</title>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: #FDF5E6;
            font-family: 'Times New Roman', Times, serif;
            margin: 0;
            padding: 20px;
        }
        .header {
            text-align: center;
            margin-bottom: 20px;
        }
        .recibo {
            max-width: 90%;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 5px;
        }
        .info-factura {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }
        .info-cliente, .info-tienda {
            width: 48%;
        }
        table {
            width: 100%;
            margin: 0 auto;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .total-section {
            margin-top: 20px;
            text-align: right;
        }
        .total-line {
            display: flex;
            justify-content: flex-end;
            margin: 5px 0;
        }
        .total-label {
            margin-right: 20px;
            width: 150px;
            text-align: right;
        }
        .total-value {
            width: 100px;
            text-align: right;
        }
        .buttons-container {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            gap: 10px;
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
            text-decoration: none;
            font-size: 14px;
            display: inline-block;
        }
        .btn-primary {
            background-color: #4CAF50;
            color: white;
        }
        .btn-secondary {
            background-color: #2196F3;
            color: white;
        }
        .thank-you {
            text-align: center;
            margin-top: 30px;
            font-style: italic;
            color: #555;
        }
        hr {
            border: 0;
            height: 1px;
            background-color: #ccc;
            margin: 20px 0;
        }
        @media print {
            .buttons-container {
                display: none;
            }
        }
    </style>
</head>
<body>
    <div class='header'>
        <table align="center" border="0">
            <tr> 
                <th><IMG SRC="./Imagenes/musica.png" ALIGN="CENTER" width="50" height="50"></th>
                <th><font face="Times New Roman,Times" size="+3">Música para DAA - Recibo de Compra</font></th>
                <th><IMG SRC="./Imagenes/musica.png" ALIGN="CENTER" width="50" height="50"></th>
            </tr>  
        </table>
    </div>

    <hr>
    
    <div class="recibo">
        
        <!-- Información de factura -->
        <div class="info-factura">
            <div class="info-tienda">
                <h3>Información de la Tienda</h3>
                <p>Música para DAA, S.L.</p>
                <p>Calle de la Música, 123</p>
                <p>28001 Madrid, España</p>
                <p>CIF: B-12345678</p>
                <p>Tel: +34 91 123 45 67</p>
            </div>
            <div class="info-cliente">
                <h3>Detalles del Recibo</h3>
                <p><strong>Nombre:</strong> <%= nombre %></p>
                <p><strong>Email:</strong> <%= email %></p>
                <p><strong>Número de Factura:</strong> <%= numeroFactura %></p>
                <p><strong>Fecha y Hora:</strong> <%= formatoFecha.format(fechaActual) %></p>
                <p><strong>Método de Pago:</strong> <%= tipoTarjeta %> acabada en <%= tarjetaFormateada %></p>
            </div>
        </div>
        
        <!-- Tabla de productos -->
        <h3>Detalle de Productos</h3>
        <table>
            <tr>
                <th>Título</th>
                <th>Artista</th>
                <th>País</th>
                <th>Precio Unitario</th>
                <th>Cantidad</th>
                <th>Subtotal</th>
            </tr>
            
            <% 
            for (CD elemento : carrito) {
                double subtotalProducto = elemento.getPrecio() * elemento.getCantidad();
            %>
                <tr>
                    <td><%= elemento.getTitulo() %></td>
                    <td><%= elemento.getArtista() %></td>
                    <td><%= elemento.getPais() %></td>
                    <td>$<%= String.format("%.2f", elemento.getPrecio()) %></td>
                    <td><%= elemento.getCantidad() %></td>
                    <td>$<%= String.format("%.2f", subtotalProducto) %></td>
                </tr>
            <% } %>
        </table>
        
        <!-- Sección de totales -->
        <div class="total-section">
            <div class="total-line">
                <div class="total-label">Subtotal:</div>
                <div class="total-value">$<%= String.format("%.2f", subtotal) %></div>
            </div>
            <div class="total-line">
                <div class="total-label">IVA (21%):</div>
                <div class="total-value">$<%= String.format("%.2f", iva) %></div>
            </div>
            <div class="total-line" style="font-weight: bold; font-size: 1.2em;">
                <div class="total-label">TOTAL:</div>
                <div class="total-value">$<%= String.format("%.2f", total) %></div>
            </div>
        </div>
        
        <!-- Mensaje de agradecimiento -->
        <div class="thank-you">
            <p>¡Gracias por su compra! Esperamos verle de nuevo pronto.</p>
        </div>
        
        <!-- Botones de acción -->
        <div class="buttons-container">
            <form method="post" action="<%= request.getContextPath() %>/FormularioCompra">
                <button type="submit" name="accion" value="confirmarCompra" class="btn btn-primary">Volver al inicio</button>
                <button type="button" onclick="window.print()" class="btn btn-secondary">Imprimir Recibo</button>
            </form>
        </div>
        
    </div>
    
    <hr>
</body>
</html>
