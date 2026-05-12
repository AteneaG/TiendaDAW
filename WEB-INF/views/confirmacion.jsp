<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Confirmacion</title></head>
<body bgcolor="#FDF5E6">
    <center>
        <h2>¡Gracias por tu compra, ${sessionScope.nombreUsuario}!</h2>
        <p>Total pagado: ${sessionScope.totalFinal}</p>
        <a href="../index.html">Volver a la tienda</a>
    </center>
</body>
</html>