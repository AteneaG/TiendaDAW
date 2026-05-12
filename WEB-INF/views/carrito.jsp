<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><title>Carrito</title></head>
<body bgcolor="#FDF5E6">
    <center><h1>Carrito de la compra</h1></center>
    <hr>
    <form method="post" action="../servlet/tienda">
        <input type="hidden" name="accion" value="eliminar">
        <table border="1" align="center">
            <tr>
                <th>TITULO DEL CD</th>
                <th>Cantidad</th>
                <th>Importe</th>
                <th>Eliminar</th>
            </tr>
            <c:forEach var="entry" items="${sessionScope.carrito.items}">
                <tr>
                    <td>${entry.key}</td>
                    <td>${entry.value}</td>
                    <td></td>
                    <td><input type="radio" name="cdEliminar" value="${entry.key}"></td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="2"><b>IMPORTE TOTAL</b></td>
                <td><b>${sessionScope.carrito.calcularTotal()}</b></td>
                <td><input type="submit" value="Eliminar"></td>
            </tr>
        </table>
    </form>
    <br>
    <center>
        <form method="get" action="../index.html">
            <input type="submit" value="Sigo comprando">
        </form>
        <form method="post" action="../servlet/tienda">
            <input type="hidden" name="accion" value="pagar">
            <input type="submit" value="Me largo a pagar">
        </form>
    </center>
</body>
</html>