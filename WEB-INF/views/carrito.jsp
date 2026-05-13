<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Carrito de Compra - Música para DAA</title>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: #FDF5E6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 20px;
        }
        h1 { 
            text-align: center;
            margin: 0;
            line-height: 50px;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 0 10px;
        }
        .header { 
            text-align: center; 
            margin-bottom: 20px;
        }
        .tabla{ 
            width: 90%; 
            margin: 0 auto; 
            border-collapse: collapse; 
        }
        .tabla th,
        .tabla td{ 
            padding: 10px; 
            text-align: left; 
            border-bottom: 1px solid #ddd;
        }
        .total-row { 
            font-weight: bold;
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
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .btn-primary { 
            background-color: #4CAF50; 
            color: white;
        }
        .btn-secondary { 
            background-color: #2196F3;
            color: white;
        }
        .btn-danger {
            background-color: #f44336; 
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
                <th><h1>Carrito de Compra</h1></th>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
            </tr>
        </table>
    </div>
    <hr>

    <form method="post" action="../servlet/tienda">
        <input type="hidden" name="accion" value="eliminar">
        <table class="tabla">
            <tr>
                <th>Título</th>
                <th>Artista</th>
                <th>País</th>
                <th>Precio Unitario</th>
                <th>Cantidad</th>
                <th>Subtotal</th>
                <th>Eliminar</th>
            </tr>
            <c:forEach var="entry" items="${sessionScope.carrito.detallesPedido}">
                <tr>
                    <td>${entry.value.CD.titulo}</td>
                    <td>${entry.value.CD.artista}</td>
                    <td>${entry.value.CD.pais}</td>
                    <td>${entry.value.CD.precio}€</td>
                    <td>${entry.value.cantidad}</td>
                    <td>${entry.value.CD.precio * entry.value.cantidad}€</td>
                    <td><input type="checkbox" name="cdEliminar" value="${entry.key}"></td>
                </tr>
            </c:forEach>
            <tr class="total-row">
                <td colspan="5" style="text-align: right;">IMPORTE TOTAL:</td>
                <td>${sessionScope.carrito.calcularTotal()}€</td>
                <td><input type="submit" value="Eliminar" class="btn btn-danger"></td>
            </tr>
        </table>

        <div class="buttons-container">
            <a href="../index.html" class="btn btn-secondary">Continuar Comprando</a>
        </div>
    </form>
    
    <!--TODO: CAMBIAR A GET -->
    <div class="buttons-container">
        <form method="post" action="../servlet/tienda">
            <input type="hidden" name="accion" value="pagar">
            <input type="submit" value="Confirmar Compra" class="btn btn-primary">
        </form>
    </div>

    <hr>
</body>
</html>