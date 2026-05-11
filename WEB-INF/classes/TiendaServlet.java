import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;


public class TiendaServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Crear la sesión
        HttpSession session = request.getSession(true); //Si no existe, la crea

        //Obtener el carrito de la sesión o crear uno nuevo
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute("carrito", carrito);
        }

        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("pagar")) {
            //Paso a caja (F3)
            request.getRequestDispatcher("/views/caja.jsp").forward(request, response);
            return;
        
        } else if (accion != null && accion.equals("confirmarPago")) {
            // F5: vaciar carrito y volver al inicio
            carrito.vaciar();
            request.getRequestDispatcher("/views/confirmacion.jsp").forward(request, response);
            return;
        
        } else if (accion != null && accion.equals("eliminar")) {
            // F4: eliminar CD del carrito
            String cdEliminar = request.getParameter("cdEliminar");
            carrito.eliminar(cdEliminar);
            request.getRequestDispatcher("/views/carrito.jsp").forward(request, response);
            return;
        } else {    //Añadir CD al carrito (F2)
            //Leer datos del formulario
            String cd = request.getParameter("cd");
            String cantidadStr = request.getParameter("cantidad");
            int cantidad = Integer.parseInt(cantidadStr.trim());

            carrito.agregar(cd, cantidad);

            request.getRequestDispatcher("/views/carrito.jsp").forward(request, response);
        }
    }   
}