<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Registrar Usuario - Música para DAA</title>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: #FDF5E6;
            font-family: 'Times New Roman', Times, serif;
            margin: 0;
            padding: 20px;
        }
        .header {
            text-align: center;
            margin-bottom: 20px;
        }
        .formulario-container {
            max-width: 500px;
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
            font-family: 'Times New Roman', Times, serif;
            font-size: 16px;
            box-sizing: border-box;
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
        .error-box {
            background-color: #fff5f5;
            border: 1px solid #f44336;
            color: #c0392b;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
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
            var password = document.getElementById('password').value;
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

            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                alert('Por favor, ingrese un correo electrónico válido.');
                return false;
            }

            if (password.trim() === '') {
                alert('Por favor, ingrese una contraseña.');
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
    <div class='header'>
        <table align="center" border="0">
            <tr>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
                <th><font face="Times New Roman,Times" size="+3">Música para DAA - Registro</font></th>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
            </tr>
        </table>
    </div>

    <hr>

    <div class="formulario-container">

        <%-- Mostrar error si lo hay (viene del servlet cuando falla el registro) --%>
        <c:if test="${not empty requestScope.error}">
            <div class="error-box">
                ${requestScope.error}
            </div>
        </c:if>

        <div class="mensaje-resumen">
            <p>Cree su cuenta para continuar con la compra.</p>
        </div>

        <%-- action apunta a FormularioRegistro, que es formRegistro.java --%>
        <form method="post" action="../servlet/tienda" onsubmit="return validarFormulario();">
            <input type="hidden" name="accion" value="registrarUsuario">

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
                <div class="form-group">
                    <label for="password" class="required">Contraseña</label>
                    <input type="password" id="password" name="password" placeholder="Ingrese una contraseña segura">
                </div>
            </div>

            <div class="form-section">
                <h3>Datos de Pago</h3>
                <div class="form-group">
                    <label for="tipoTarjeta" class="required">Tipo de Tarjeta</label>
                    <select id="tipoTarjeta" name="tipoTarjeta">
                        <option value="">Seleccione un tipo de tarjeta</option>
                        <option value="visa">Visa</option>
                        <option value="mastercard">MasterCard</option>
                        <option value="amex">American Express</option>
                        <option value="discover">Discover</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="numeroTarjeta" class="required">Número de Tarjeta</label>
                    <input type="text" id="numeroTarjeta" name="numeroTarjeta" placeholder="Sin espacios, entre 13 y 19 dígitos">
                </div>
                <div class="form-group">
                    <label for="fechaExpiracion">Fecha de Expiración</label>
                    <input type="month" id="fechaExpiracion" name="fechaExpiracion"
                           min="<%= java.time.LocalDate.now().getYear() %>-<%= String.format("%02d", java.time.LocalDate.now().getMonthValue()) %>">
                </div>
                <div class="form-group">
                    <label for="codigoSeguridad">Código de Seguridad (CVV)</label>
                    <input type="password" id="codigoSeguridad" name="codigoSeguridad" maxlength="4" placeholder="CVV">
                </div>
            </div>

            <div class="buttons-container">
                <button type="submit" class="btn btn-primary">Registrar Usuario</button>
                <a href="${pageContext.request.contextPath}/servlet/tienda?accion=verLogin" class="btn btn-secondary">Volver al login</a>
            </div>

        </form>
    </div>

    <hr>

    <div style="text-align: center; margin-top: 10px; font-size: 12px; color: #666;">
        <p>Todos los datos son procesados de forma segura.</p>
    </div>
</body>
</html>
