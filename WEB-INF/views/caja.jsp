<!DOCTYPE html>
<html>
<head><title>Caja</title></head>
<body bgcolor="#FDF5E6">
    <center>
        <h1>Caja</h1>
        <table border="1">
            <tr><th>TOTAL A PAGAR</th></tr>
            <tr><td>${sessionScope.carrito.calcularTotal()}</td></tr>
        </table>
        <br>
        <form method="post" action="../servlet/tienda">
            <input type="hidden" name="accion" value="confirmarPago">
            <input type="submit" value="Pagar y volver a la pagina principal">
        </form>
    </center>
</body>
</html>