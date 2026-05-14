<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            position: relative;
        }
        h1 {
            margin: 0;
            line-height: 50px;
        }
        .btn-perfil {
            position: absolute;
            top: 50%;
            right: 20px;
            transform: translateY(-50%);
            display: flex;
            align-items: center;
            gap: 8px;
            text-decoration: none;
            color: #2c2c2c;
            border-color: 1px solid #2c2c2c;
            background-color: #FDF5E6;
            border-radius: 999px;
            padding: 8px 16px 8px 10px;
            font-family: inherit;
            font-size: 14px;
            font-weight: 600;
            box-shadow: 0 2px 6px rgba(0,0,0,0.25);
            transition: background-color 0.2s;
        }
        .btn-perfil:hover {
            background-color: #444;
        }
        .btn-perfil img {
            width: 28px;
            height: 28px;
            border-radius: 50%;
            object-fit: cover;
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

        <a href="${pageContext.request.contextPath}/servlet/tienda?accion=verPerfil" class="btn-perfil">
            <img src="${pageContext.request.contextPath}/img/fotoDePerfil.png" alt="Perfil">
            Mi perfil
        </a>
    </div>

    <hr>
    <div class="page-container">

        <table class="tabla">
            <tr><th>TOTAL A PAGAR</th></tr>
            <tr><td><strong><fmt:formatNumber value="${sessionScope.carrito.calcularTotal()}" minFractionDigits="2" maxFractionDigits="2"/>€</strong></td></tr>
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
