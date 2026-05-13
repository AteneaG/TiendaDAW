<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mi Perfil - Música para DAA</title>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: #FDF5E6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 20px;
        }
        h1 {
            margin: 0;
            line-height: 50px;
            padding: 0 15px;
            font-size: 2em;
        }
        .header {
            text-align: center;
            margin-bottom: 20px;
            position: relative;
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
        hr {
            border: 0;
            height: 1px;
            background-color: #ccc;
            margin: 20px 0;
        }
        .perfil-container {
            max-width: 600px;
            margin: 30px auto;
            background-color: white;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .perfil-foto {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-bottom: 30px;
        }
        .perfil-foto img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #4CAF50;
            margin-bottom: 10px;
        }
        .perfil-foto span {
            font-size: 1.3em;
            font-weight: bold;
            color: #333;
        }
        .perfil-datos {
            border-top: 1px solid #eee;
            padding-top: 20px;
        }
        .dato-fila {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #f5f5f5;
        }
        .dato-label {
            font-weight: bold;
            color: #555;
            width: 40%;
        }
        .dato-valor {
            color: #333;
            width: 60%;
            text-align: right;
        }
        .buttons-container {
            margin-top: 25px;
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
        .btn-danger {
            background-color: #f44336;
            color: white;
        }
        th {
            vertical-align: middle;
            text-align: center;
        }
    </style>
</head>
<body>

    <div class="header">
        <table align="center" border="0">
            <tr>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
                <th><h1>Mi Perfil</h1></th>
                <th><img src="${pageContext.request.contextPath}/img/musica.png" width="50" height="50"></th>
            </tr>
        </table>

        <a href="${pageContext.request.contextPath}/servlet/tienda?accion=verPerfil" class="btn-perfil">
            <img src="${pageContext.request.contextPath}/img/fotoDePerfil.png" alt="Perfil">
            Mi perfil
        </a>
    </div>

    <hr>

    <div class="perfil-container">
        <div class="perfil-foto">
            <img src="${pageContext.request.contextPath}/img/fotoDePerfil.png" alt="Foto de perfil">
            <span>${sessionScope.nombreUsuario}</span>
        </div>

        <div class="perfil-datos">
            <div class="dato-fila">
                <span class="dato-label">Nombre</span>
                <span class="dato-valor">${sessionScope.nombreUsuario}</span>
            </div>
            <div class="dato-fila">
                <span class="dato-label">Correo electrónico</span>
                <span class="dato-valor">${sessionScope.emailUsuario}</span>
            </div>
            <div class="dato-fila">
                <span class="dato-label">Tipo de tarjeta</span>
                <span class="dato-valor">${sessionScope.tipoTarjeta}</span>
            </div>
            <div class="dato-fila">
                <span class="dato-label">Número de tarjeta</span>
                <span class="dato-valor">**** **** **** ${sessionScope.ultimosTarjeta}</span>
            </div>
        </div>

        <div class="buttons-container">
            <a href="${pageContext.request.contextPath}/index.html" class="btn btn-primary">Volver a la tienda</a>
            <a href="${pageContext.request.contextPath}/servlet/tienda?accion=cerrarSesion" class="btn btn-danger">Cerrar sesión</a>
        </div>
    </div>

    <hr>
</body>
</html>
