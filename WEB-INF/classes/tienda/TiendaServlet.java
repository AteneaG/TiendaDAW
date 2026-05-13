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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Crear la sesión
        HttpSession session = request.getSession(true); //Si no existe, la crea
        Carrito carrito = (Carrito) session.getAttribute("carrito");

        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("pagar")) {
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);        
        } else if (accion != null && accion.equals("confirmarPago")) {        // (F5) //Confirmar pago, vaciar carrito y volver al inicio (F5)
            String email = request.getParameter("email");
            String nombre = request.getParameter("nombre");

            session.setAttribute("totalFinal", carrito.calcularTotal());
            session.setAttribute("nombreUsuario", nombre);

            //Obtener el id del usuario a partir del email
            int usuarioId = UsuarioDAO.obtenerIdUsuario(email);

            //Registrar el pedido en la BD
            carrito.terminarPedido();

            request.getRequestDispatcher("/WEB-INF/views/confirmacion.jsp").forward(request, response);
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
            request.getRequestDispatcher("/WEB-INF/views/pago.jsp").forward(request, response);
            return;
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
                return;
            }

            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
        }
    }   

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);

        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("verCaja")) {           //Volver a caja
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);
        } 
        else if (accion != null && accion.equals("verCarrito")) {   //Volver al carrito
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
        } 
        else {                                                      //Por defecto volver al index
            //LOGICA SI CARRITO = VACIO -> BORRAR CARRITO
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }
}