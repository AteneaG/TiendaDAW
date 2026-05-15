<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Recibo de Compra - Música para DAA</title>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: #FDF5E6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 20px;
        }
        h1 {
            margin: 0;
            line-height: 50px;
            padding: 0 15px;
            font-size: 2em;
        }
        .header {
            text-align: center;
            margin-bottom: 20px;
            position: relative;
        }
        .header-icon-left {
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
        }
        .header-icon-right {
            position: absolute;
            right: 0;
            top: 50%;
            transform: translateY(-50%);
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
        .info-cliente, .info-tienda { width: 48%; }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th { background-color: #f2f2f2; }
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
            font-family: inherit;
        }
        .btn-primary   { background-color: #4CAF50; color: white; }
        .btn-secondary { background-color: #2196F3; color: white; }
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
        @media print { .buttons-container { display: none; } }
    </style>
</head>
<body>

    <div class="header">
        <img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50" class="header-icon-left">
        <h1>Recibo de Compra</h1>
        <img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50" class="header-icon-right">
    </div>

    <hr>

    <div class="recibo">

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
                <p><strong>Email:</strong> ${requestScope.emailRecibo}</p>
                <p><strong>Número de Factura:</strong> FAC-${requestScope.numeroFactura}</p>
                <p><strong>Método de Pago:</strong> ${requestScope.tipoTarjeta} acabada en ****${requestScope.numTarjeta}</p>
            </div>
        </div>

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
            <c:forEach var="dp" items="${requestScope.carritoRecibo.detallesPedido.values()}">
                <tr>
                    <td>${dp.CD.titulo}</td>
                    <td>${dp.CD.artista}</td>
                    <td>${dp.CD.pais}</td>
                    <td>${dp.CD.precio} €</td>
                    <td>${dp.cantidad}</td>
                    <td><fmt:formatNumber value="${dp.CD.precio * dp.cantidad}" pattern="#,##0.00"/> €</td>
                </tr>
            </c:forEach>
        </table>

        <div class="total-section">
            <div class="total-line">
                <div class="total-label">Subtotal:</div>
                <div class="total-value">${requestScope.subtotalFinal} €</div>
            </div>
            <div class="total-line">
                <div class="total-label">IVA (21%):</div>
                <div class="total-value">${requestScope.ivaFinal} €</div>
            </div>
            <div class="total-line" style="font-weight: bold; font-size: 1.2em;">
                <div class="total-label">TOTAL:</div>
                <div class="total-value">${requestScope.totalFinal} €</div>
            </div>
        </div>

        <div class="thank-you">
            <p>¡Gracias por su compra, ${requestScope.emailRecibo}! Esperamos verle de nuevo pronto.</p>
        </div>

        <div class="buttons-container">
            <a href="${pageContext.request.contextPath}/index.html" class="btn btn-primary">Volver al inicio</a>
            <button type="button" onclick="window.print()" class="btn btn-secondary">Imprimir Recibo</button>
        </div>

    </div>

    <hr>
</body>
</html>