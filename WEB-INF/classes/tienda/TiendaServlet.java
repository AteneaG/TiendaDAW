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
        


        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("pagar")) {
            //Paso a caja (F3)
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);
            return;
        
        } else if (accion != null && accion.equals("confirmarPago")) {
            //Confirmar pago, vaciar carrito y volver al inicio (F5)
            String email = request.getParameter("email");
            String nombre = request.getParameter("nombre");

            session.setAttribute("totalFinal", carrito.calcularTotal());
            session.setAttribute("nombreUsuario", nombre);

            //Obtener el id del usuario a partir del email
            int usuarioId = UsuarioDAO.obtenerIdUsuario(email);

            //Registrar el pedido en la BD
            ArrayList<CD> cds = new ArrayList<>(carrito.getItems().values());
            PedidoDAO.registrarPedido(usuarioId, cds);

            carrito.vaciar();
            request.getRequestDispatcher("/WEB-INF/views/confirmacion.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("irAPago")) {
            //Ir al pago desde la confirmacion (F3)
            request.getRequestDispatcher("/WEB-INF/views/pago.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("eliminar")) {
            // Eliminar CD del carrito (F4)
            String[] cdsEliminar = request.getParameterValues("cdEliminar");
            if (cdsEliminar != null) {
                for (String id : cdsEliminar) {
                    carrito.eliminar(Integer.parseInt(id));
                }
            }
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
            return;
        } else {    //Añadir CD al carrito (F2)
            //Leer datos del formulario
            String cdStr = request.getParameter("cd");
            String cantidadStr = request.getParameter("cantidad");
            int cantidad = Integer.parseInt(cantidadStr.trim());

            StringTokenizer t = new StringTokenizer(cdStr, "|");
            String titulo = t.nextToken().trim();
            String artista = t.nextToken().trim();

            CD cd = CDDAO.obtenerCDPorArtistaYTitulo(artista, titulo);
            detallePedido dp = new detallePedido(cd, cantidad);

            if (cd != null) {
                //Cuando añade un carrito si este no existe, lo crea con el CD que va a crear
                if (session.getAttribute("carrito") == null) {
                    session.setAttribute("carrito", new Carrito(dp));
                }
                else session.getAttribute("carrito").agregar(cd);
            } else {
                System.err.println("CD no encontrado por  Artista ("+artista+") y Titulo ("+titulo+")");
            }

            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
        }
    }   

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        if (session.getAttribute("carrito") == null) {
            session.setAttribute("carrito", new Carrito());
        }

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