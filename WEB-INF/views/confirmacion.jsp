<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmación - Música para DAA</title>
    <meta charset="UTF-8">
    <style>
        body { background-color: #FDF5E6; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; }
        .header { text-align: center; margin-bottom: 20px; }
        .confirmacion-container { max-width: 90%; margin: 0 auto; background-color: white; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1); border-radius: 5px; text-align: center; }
        .btn { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; text-decoration: none; font-size: 14px; display: inline-block; }
        .btn-primary { background-color: #4CAF50; color: white; }
        hr { border: 0; height: 1px; background-color: #ccc; margin: 20px 0; }
    </style>
</head>
<body>
    <div class="header">
        <table align="center" border="0">
            <tr>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
                <th><h1>¡Gracias por tu compra, ${sessionScope.nombreUsuario}!</h1></th>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
            </tr>
        </table>
    </div>
    <hr>
    <div class="confirmacion-container">
        <p>Tu pedido ha sido registrado correctamente.</p>
        <p>Total pagado: <strong>${sessionScope.totalFinal}€</strong></p>
        <br>
        <a href="${pageContext.request.contextPath}/index.html" class="btn btn-primary">Volver a la tienda</a>
    </div>
    <hr>
</body>
</html>
