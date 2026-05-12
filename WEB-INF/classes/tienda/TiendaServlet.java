package tienda;


import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import tienda.BD.*;

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
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);
            return;
        
        } else if (accion != null && accion.equals("confirmarPago")) {
            //Confirmar pago, vaciar carrito y volver al inicio (F5)
            String email = request.getParameter("email");

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
            String cdEliminar = request.getParameter("cdEliminar");
            carrito.eliminar(Integer.parseInt(cdEliminar));
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

<<<<<<< HEAD:WEB-INF/classes/TiendaServlet.java
            //TODO: REVISAR CONSULTA SQL PARA OBTENER ID DEL CD
            int id = 0;
            try {
                System.out.println("Intentando conectar a la base de datos...");
                Connection conn = BaseDeDatos.getConnection();
                System.out.println("Conexión establecida exitosamente!");
        
                PreparedStatement ps = conn.prepareStatement("SELECT id FROM productos WHERE titulo = ?");
                ps.setString(1, titulo);
                ResultSet rs = ps.executeQuery();
=======
>>>>>>> 0adbbffe92ee4a6ed01f5ec39a07d82415ad6956:WEB-INF/classes/tienda/TiendaServlet.java


            CD cd = ProductoDAO.obtenerProductoPorArtistaYTitulo(artista, titulo);
            if (cd != null) {
                carrito.agregar(cd);
            } else {
                System.err.println("CD no encontrado por  Artista ("+artista+") y Titulo ("+titulo+")");
            }

<<<<<<< HEAD:WEB-INF/classes/TiendaServlet.java
            CD cd = new CD(id, titulo, artista, pais, precio, cantidad);
            carrito.agregar(cd);

=======
>>>>>>> 0adbbffe92ee4a6ed01f5ec39a07d82415ad6956:WEB-INF/classes/tienda/TiendaServlet.java
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
        }
    }   

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute("carrito", carrito);
        }

        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("verCaja")) {
            //Volver a caja
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);

        } else if (accion != null && accion.equals("verCarrito")) {
            //Volver al carrito
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);

        } else {
            //Por defecto volver al index
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }
}