package tienda;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import tienda.BD.*;
import tienda.Modelo.CD;
import tienda.Modelo.Carrito;
import tienda.Modelo.detallePedido;

public class TiendaServlet extends HttpServlet {
    /* ===================================================================================================== */
    /*                                                  DO POST                                              */
    /* ===================================================================================================== */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Crear la sesión
        HttpSession session = request.getSession(true); //Si no existe, la crea
        boolean logged = session.getAttribute("logged") != null && (Boolean) session.getAttribute("logged");
        Carrito carrito = (Carrito) session.getAttribute("carrito");

        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("pagar")) {
            if(!logged) {
                request.setAttribute("error", "Debe iniciar sesión para proceder al pago.");
                session.setAttribute("redirectAfterLogin", "caja");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);        
        } else if (accion != null && accion.equals("confirmarPago")) {        // (F5) //Confirmar pago, vaciar carrito y volver al inicio (F5)
            if(!logged) {
                request.setAttribute("error", "Debe iniciar sesión para proceder al pago.");
                session.setAttribute("redirectAfterLogin", "confirmacion");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            String email = request.getParameter("email");
            String nombre = request.getParameter("nombre");
            String tipoTarjeta = request.getParameter("tipoTarjeta");
            String numeroTarjeta = request.getParameter("numeroTarjeta");

            double total = carrito.calcularTotal();
            double iva = total * 0.21;
            double subtotal = total - iva;
            session.setAttribute("totalFinal", total);
            session.setAttribute("subtotalFinal", String.format("%.2f", subtotal));
            session.setAttribute("ivaFinal", String.format("%.2f", iva));
            session.setAttribute("nombreUsuario", nombre);

            // Guardar datos para el recibo
            session.setAttribute("emailRecibo", email);
            session.setAttribute("tipoTarjetaRecibo", tipoTarjeta);
            session.setAttribute("numeroTarjetaRecibo", numeroTarjeta);
            session.setAttribute("detallesRecibo", new java.util.ArrayList<>(carrito.getDetallesPedido().values()));
            session.setAttribute("numeroFactura", (int)(Math.random() * 900000) + 100000); // 6 dígitos aleatorios

            //Obtener el id del usuario a partir del email
            int usuarioId = UsuarioDAO.obtenerIdUsuario(email);

            //Registrar el pedido en la BD
            carrito.terminarPedido();

            request.getRequestDispatcher("/WEB-INF/views/recibo.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("eliminar")) {             // (F4) Eliminar CD del carrito (F4)
            String[] cdsEliminar = request.getParameterValues("cdEliminar");
            if (cdsEliminar != null) {
                for (String id : cdsEliminar) {
                    CD cd = CDDAO.obtenerCDPorId(Integer.parseInt(id));
                    carrito.eliminar(cd);
                }
            }
            if (carrito.getDetallesPedido().isEmpty()) {
                carrito.vaciar();
                session.setAttribute("carrito", null);
            }
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("irAPago")) {              // (F3) Ir al pago desde la confirmacion (F3)
            if(!logged) {
                request.setAttribute("error", "Debe iniciar sesión para proceder al pago.");
                session.setAttribute("redirectAfterLogin", "pago");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/recibo.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("login")) {                // (F6) Ir a registro desde el login (F6)
            
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (!UsuarioDAO.autenticarUsuario(email, password)) {
                request.setAttribute("error", "Email o contraseña incorrectos.");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }

            session.setAttribute("logged", true);
            session.setAttribute("email", email);           

            String redirect = (String) session.getAttribute("redirectAfterLogin");
            session.removeAttribute("redirectAfterLogin");           

            if (redirect != null) {
                request.getRequestDispatcher("/WEB-INF/views/" + redirect + ".jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/index.html");
            }
        } else if (accion != null && accion.equals("irARegistro")) {           // Ir a la página de registro
            request.getRequestDispatcher("/WEB-INF/views/registrarUsuario.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("registrarUsuario")) {      // Registrar nuevo usuario
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String tipoTarjeta = request.getParameter("tipoTarjeta");
            String numeroTarjeta = request.getParameter("numeroTarjeta");

            if (UsuarioDAO.obtenerIdUsuario(email) != -1) {
                request.setAttribute("error", "El email ya está registrado.");
                request.getRequestDispatcher("/WEB-INF/views/registrarUsuario.jsp").forward(request, response);
                return;
            }

            UsuarioDAO.registrarUsuario(email, password, tipoTarjeta, Long.parseLong(numeroTarjeta));
            session.setAttribute("logged", true);
            session.setAttribute("email", email);
            response.sendRedirect(request.getContextPath() + "/index.html");

        } else {                                                                       // (F2) Añadir CD al carrito (F2)
            //Leer datos del formulario
            String cdStr = request.getParameter("cd");
            String cantidadStr = request.getParameter("cantidad");
            int cantidad = Integer.parseInt(cantidadStr.trim());

            StringTokenizer t = new StringTokenizer(cdStr, "|");
            String titulo = t.nextToken().trim();
            String artista = t.nextToken().trim();

            CD cd = CDDAO.obtenerCDPorArtistaYTitulo(artista, titulo);
            detallePedido dp = new detallePedido(cd, cantidad);

            if (cd != null && dp != null) {
                //Cuando añade un carrito si este no existe, lo crea con el CD que va a crear
                if (session.getAttribute("carrito") == null) {
                    session.setAttribute("carrito", new Carrito(dp));
                }
                else carrito.agregarDetalle(dp);
            } else {
                System.err.println("\nCD no encontrado por  Artista ("+artista+") y Titulo ("+titulo+")");
                
            }
            response.sendRedirect(request.getContextPath() + "/index.html");
            
        }
    }   


    /* ===================================================================================================== */
    /*                                                  DO GET                                               */
    /* ===================================================================================================== */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        boolean logged = session.getAttribute("logged") != null && (Boolean) session.getAttribute("logged");
        
        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("cerrarSesion")) {             //Cerrar sesión
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        } else if (accion != null && accion.equals("verCaja")) {                   //Volver a caja
            if(!logged) {
                request.setAttribute("error", "Debe iniciar sesión para proceder al pago.");
                session.setAttribute("redirectAfterLogin", "caja");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);
        } else if (accion != null && accion.equals("verCarrito")) {         //Volver al carrito
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
        } else if (accion != null && accion.equals("verLogin")) {           //Ir al login
            request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
        } else if ("verPerfil".equals(accion)) {                                      //Ir al perfil
             if(!logged) {
                request.setAttribute("error", "Debe iniciar sesión para entrar a su perfil.");
                session.setAttribute("redirectAfterLogin", "perfil");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/perfil.jsp").forward(request, response);
        }
        else {                                                                        //Por defecto volver al index
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }
}