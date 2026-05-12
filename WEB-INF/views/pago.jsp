<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pago - Música para DAA</title>
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
        .formulario-container {
            max-width: 90%;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 5px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            font-size: 16px;
        }
        .form-section {
            margin-bottom: 30px;
            border-bottom: 1px solid #eee;
            padding-bottom: 20px;
        }
        .form-section h3 {
            border-bottom: 2px solid #4CAF50;
            padding-bottom: 10px;
            color: #333;
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
        .mensaje-resumen {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #4CAF50;
        }
        hr {
            border: 0;
            height: 1px;
            background-color: #ccc;
            margin: 20px 0;
        }
        .required:after {
            content: " *";
            color: red;
        }
    </style>
    <script>
        function validarFormulario() {
            var nombre = document.getElementById('nombre').value;
            var email = document.getElementById('email').value;
            var tipoTarjeta = document.getElementById('tipoTarjeta').value;
            var numeroTarjeta = document.getElementById('numeroTarjeta').value;
            
            if (nombre.trim() === '') {
                alert('Por favor, ingrese su nombre.');
                return false;
            }
            
            if (email.trim() === '') {
                alert('Por favor, ingrese su correo electrónico.');
                return false;
            }
            
            // Validación básica de email
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                alert('Por favor, ingrese un correo electrónico válido.');
                return false;
            }
            
            if (tipoTarjeta === '') {
                alert('Por favor, seleccione un tipo de tarjeta.');
                return false;
            }
            
            if (numeroTarjeta.trim() === '') {
                alert('Por favor, ingrese el número de tarjeta.');
                return false;
            }
            
            // Validación básica de número de tarjeta (solo números y longitud entre 13-19 dígitos)
            var tarjetaRegex = /^[0-9]{13,19}$/;
            if (!tarjetaRegex.test(numeroTarjeta)) {
                alert('Por favor, ingrese un número de tarjeta válido (solo números, entre 13 y 19 dígitos).');
                return false;
            }
            
            return true;
        }
    </script>
</head>
<body>
    <div class="header">
        <table align="center" border="0">
            <tr> 
                <th><img src="/img/musica.png" width="50" height="50"></th>
                <th><h1>Pago</h1></th>
                <th><img src="/img/musica.png" width="50" height="50"></th>
            </tr>  
        </table>
    </div>

    <hr>
    
    <div class="formulario-container">
        <div class="mensaje-resumen">
            <p>Está a punto de finalizar su compra. El total a pagar es: <strong>${sessionScope.totalFinal}€</strong> (IVA incluido).</p>
            <p>Por favor, complete sus datos de contacto y pago para procesar la operación.</p>
        </div>
        
        <form method="post" action="../servlet/tienda" onsubmit="return validarFormulario();">
            <input type="hidden" name="accion" value="confirmarPago">
            <div class="form-section">
                <h3>Datos de Contacto</h3>
                <div class="form-group">
                    <label for="nombre" class="required">Nombre Completo</label>
                    <input type="text" id="nombre" name="nombre" placeholder="Ingrese su nombre completo">
                </div>
                <div class="form-group">
                    <label for="email" class="required">Correo Electrónico</label>
                    <input type="email" id="email" name="email" placeholder="ejemplo@email.com">
                </div>
            </div>
            
            <div class="form-section">
                <h3>Datos de Pago</h3>
                <div class="form-group">
                    <label for="tipoTarjeta" class="required">Tipo de Tarjeta</label>
                    <select id="tipoTarjeta" name="tipoTarjeta">
                        <option value="">Seleccione un tipo de tarjeta</option>
                        <option value="Visa">Visa</option>
                        <option value="MasterCard">MasterCard</option>
                        <option value="American Express">American Express</option>
                        <option value="Discover">Discover</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="numeroTarjeta" class="required">Número de Tarjeta</label>
                    <input type="text" id="numeroTarjeta" name="numeroTarjeta" placeholder="Ingrese el número de tarjeta (sin espacios)">
                </div>
                <div class="form-group">
                    <label for="fechaExpiracion" class="required">Fecha de Expiración</label>
                    <input type="month" id="fechaExpiracion" name="fechaExpiracion" min="${requestScope.fechaMin}">                </div>
                <div class="form-group">
                    <label for="codigoSeguridad" class="required">Código de Seguridad (CVV)</label>
                    <input type="password" id="codigoSeguridad" name="codigoSeguridad" maxlength="4" placeholder="CVV">
                </div>
            </div>
            
            <div class="buttons-container">
                <button type="submit" class="btn btn-primary">Finalizar Compra</button>
                <a href="../servlet/tienda" class="btn btn-secondary">Volver al Carrito</a>
            </div>
        </form>
    </div>
    
    <hr>
    
    <div style="text-align: center; margin-top: 10px; font-size: 12px; color: #666;">
        <p>Todos los datos son procesados de forma segura. No almacenamos los datos completos de su tarjeta.</p>
    </div>
</body>
</html>