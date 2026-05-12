<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Caja - Música para DAA</title>
    <meta charset="UTF-8">
    <style><!--TODO REVISAR EL H1 en todos los jsp-->
        body { 
            background-color: #FDF5E6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;            
            margin: 0;
            padding: 20px;
        }
        h1 { 
            text-align: center; 
            margin-bottom: 20px;
            size: +3;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .header { 
            text-align: center; 
            margin-bottom: 20px;
        }
        table { 
            width: 50%; 
            margin: 0 auto;
            border-collapse: collapse;
        }
        th, td { 
            padding: 10px; 
            text-align: center; 
            border-bottom: 1px solid #ddd;
        }
        th { 
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
                <th><img src="/img/musica.png" width="50" height="50"></th>
                <th><h1>Caja</h1></th>
                <th><img src="/img/musica.png" width="50" height="50"></th>
            </tr>
        </table>
    </div>
    <hr>

    <table>
        <tr><th>TOTAL A PAGAR</th></tr>
        <tr><td><strong>{sessionScope.carrito.calcularTotal()}€</strong></td></tr>
    </table>

    <!--TODO: CAMBIAR A GET -->
    <div class="buttons-container">
        <form method="post" action="../servlet/tienda">
            <input type="hidden" name="accion" value="irAPago">
            <input type="submit" value="Proceder al Pago" class="btn btn-primary">
        </form>
        <a href="servlet/tienda?accion=verCarrito" class="btn btn-secondary">Volver al Carrito</a>
    </div>
    <hr>
</body>
</html>