<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Iniciar Sesión - Música para DAA</title>
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
            max-width: 400px;
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
        .form-group input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-family: 'Times New Roman', Times, serif;
            font-size: 16px;
            box-sizing: border-box;
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
            text-decoration: none;
            font-size: 14px;
            background-color: #4CAF50;
            color: white;
            width: 100%;
        }
        .mensaje-resumen {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #4CAF50;
        }
        .required:after {
            content: " *";
            color: red;
        }
    </style>
    <script>
        function validarLogin() {
            var email = document.getElementById('email').value;
            var password = document.getElementById('password').value;

            if (email.trim() === '') {
                alert('Por favor, ingrese su correo electrónico.');
                return false;
            }

            if (password.trim() === '') {
                alert('Por favor, ingrese su contraseña.');
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
                <th><font face="Times New Roman,Times" size="+3">Música para DAA - Iniciar Sesión</font></th>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
            </tr>  
        </table>
    </div>

    <hr>

    <div class="formulario-container">
        <div class="mensaje-resumen">
            <p>Por favor, inicie sesión para continuar con su compra.</p>
        </div>

        <form method="post" action="../servlet/tienda" onsubmit="return validarLogin();">
            <input type="hidden" name="accion" value="login">
            <div class="form-group">
                <label for="email" class="required">Correo Electrónico</label>
                <input type="email" id="email" name="email" placeholder="Ingrese su correo electrónico">
            </div>
            <div class="form-group">
                <label for="password" class="required">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Ingrese su contraseña">
            </div>
            <button type="submit" class="btn">Iniciar Sesión</button>
        </form>

        <br>

        <form method="post" action="../servlet/tienda">
            <input type="hidden" name="accion" value="irARegistro">
            <button type="submit" class="btn" style="background-color:#2196F3;">Regístrese aquí</button>
        </form>

        <br>

        <a href="${pageContext.request.contextPath}/index.html" class="btn" style="display:block;text-align:center;background-color:#888;color:white;text-decoration:none;box-sizing:border-box;">Volver a la tienda</a>
    </div>

    <hr>
</body>
</html>
