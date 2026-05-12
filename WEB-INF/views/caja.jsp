<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Caja - Música para DAA</title>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: #FDF5E6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 20px;
        }
        .page-container {
            max-width: 90%;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 5px;
        }
        .header {
            text-align: center;
            margin-bottom: 20px;
        }
        h1 {
            margin: 0;
            line-height: 50px;
        }
        .tabla {
            width: 100%;
            border-collapse: collapse;
        }
        .tabla th,
        .tabla td {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }
        .tabla th {
            background-color: #f2f2f2;
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
            font-family: inherit;
        }
        .btn-primary {
            background-color: #4CAF50;
            color: white;
        }
        .btn-secondary {
            background-color: #2196F3;
            color: white;
        }
        hr { 
            border: 0; 
            height: 1px; 
            background-color: #ccc; 
            margin: 20px 0;
        }
    </style>
</head>

<body>

    <div class="header">
        <table align="center" border="0">
            <tr>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
                <th><h1>Caja</h1></th>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
            </tr>
        </table>
    </div>

    <hr>
    <div class="page-container">

        <table class="tabla">
            <tr><th>TOTAL A PAGAR</th></tr>
            <tr><td><strong>${sessionScope.carrito.calcularTotal()}€</strong></td></tr>
        </table>

        <div class="buttons-container">
            <form method="post" action="../servlet/tienda">
                <input type="hidden" name="accion" value="irAPago">
                <input type="submit" value="Proceder al Pago" class="btn btn-primary">
            </form>

            <a href="${pageContext.request.contextPath}/servlet/tienda?accion=verCarrito"
            class="btn btn-secondary">
                Volver al Carrito
            </a>
        </div>

    </div>

</body>
</html>