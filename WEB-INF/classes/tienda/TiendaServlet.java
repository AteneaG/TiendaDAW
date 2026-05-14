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
        HttpSession session = request.getSession(true);
        boolean logged = session.getAttribute("logged") != null && (Boolean) session.getAttribute("logged");
        String mail = (String) session.getAttribute("email");
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        String accion = request.getParameter("accion");


        if (accion != null && accion.equals("eliminar")) {                      // (F4) Eliminar CD del carrito (F4)
            String[] cdsEliminar = request.getParameterValues("cdEliminar");
            if (cdsEliminar != null) {
                for (String id : cdsEliminar) {
                    CD cd = CDDAO.obtenerCDPorId(Integer.parseInt(id));
                    carrito.eliminar(cd);
                    PedidoDAO.eliminarDetallePedido(carrito.getPedidoID(), cd.getId());
                }
            }
            if (carrito.getDetallesPedido().isEmpty()) {
                carrito.vaciar();
                PedidoDAO.eliminarPedido(carrito.getPedidoID());
                session.setAttribute("carrito", null);
            }
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("login")) {                  // (F6) Ir a registro desde el login (F6)
            
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (!UsuarioDAO.autenticarUsuario(email, password)) {
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }

            session.setAttribute("logged", true);
            session.setAttribute("email", email);
            if(carrito != null){
                carrito.setUsuarioID(UsuarioDAO.obtenerIdUsuario(email));      
                PedidoDAO.actualizarUsuarioPedido(carrito.getPedidoID(), carrito.getUsuarioID());   
            }   

            String redirect = (String) session.getAttribute("redirectAfterLogin");
            session.removeAttribute("redirectAfterLogin");           

            if (redirect != null) {
                if (redirect.equals("perfil")) cargarDatosPerfil(session);
                request.getRequestDispatcher("/WEB-INF/views/" + redirect + ".jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/index.html");
            }
        } else if (accion != null && accion.equals("registrarUsuario")) {       // Registrar nuevo usuario
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String tipoTarjeta = request.getParameter("tipoTarjeta");
            String numeroTarjeta = request.getParameter("numeroTarjeta");

            if (UsuarioDAO.obtenerIdUsuario(email) != -1) {
                request.getRequestDispatcher("/WEB-INF/views/registrarUsuario.jsp").forward(request, response);
                return;
            }

            UsuarioDAO.registrarUsuario(email, password, tipoTarjeta, Long.parseLong(numeroTarjeta));
            session.setAttribute("logged", true);
            session.setAttribute("email", email);
            if(carrito != null) {
                carrito.setUsuarioID(UsuarioDAO.obtenerIdUsuario(email));
                PedidoDAO.actualizarUsuarioPedido(carrito.getPedidoID(), carrito.getUsuarioID());
            }

            String redirect = (String) session.getAttribute("redirectAfterLogin");
            if (redirect != null) {
                if (redirect.equals("perfil")) cargarDatosPerfil(session);
                request.getRequestDispatcher("/WEB-INF/views/" + redirect + ".jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/index.html");
            }

        } else {                                                                         // (F2) Añadir CD al carrito (F2)
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
                    int pedidoID = PedidoDAO.registrarPedidoUnCD(dp);              // ID Pedido BD
                    session.setAttribute("carrito", new Carrito(dp));   
                    carrito = (Carrito) session.getAttribute("carrito");
                    carrito.setPedidoID(pedidoID);
                }
                else {
                    carrito.agregarDetalle(dp);
                    if (carrito.getDetallesPedido().containsKey(cd.getId())) {
                        detallePedido existingDp = carrito.getDetallesPedido().get(cd.getId());
                        existingDp.setCantidad(existingDp.getCantidad() + cantidad);
                        PedidoDAO.actualizarCantidad(carrito.getPedidoID(), cd.getId(), existingDp.getCantidad());
                    } else {
                        PedidoDAO.anhadirDetalleAPedido(carrito.getPedidoID(), carrito.getDetallesPedido().get(cd.getId()));
                    }

                }

                System.out.println("\nLogged? "+logged+" - Email en sesión: "+mail);
                carrito = (Carrito) session.getAttribute("carrito");
                if(logged) {
                    carrito.setUsuarioID(UsuarioDAO.obtenerIdUsuario(mail));
                    PedidoDAO.actualizarUsuarioPedido(carrito.getPedidoID(), carrito.getUsuarioID());
                }
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
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        boolean logged = session.getAttribute("logged") != null && (Boolean) session.getAttribute("logged");
        String accion = request.getParameter("accion");

        if (accion != null && accion.equals("irAPago")) {                   // (F5) //Confirmar pago, vaciar carrito y volver al inicio (F5)
            if (!logged) {
                session.setAttribute("redirectAfterLogin", "confirmacion");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
        
            double total = 0.0;
            for (detallePedido detalle : carrito.getDetallesPedido().values()) {
                total += detalle.getCD().getPrecio() * detalle.getCantidad();
            }
            PedidoDAO.actualizarTotalPedido(carrito.getPedidoID(), total);
            PedidoDAO.actualizarFechaPedido(carrito.getPedidoID());
        
            double iva = total * 0.21;
            double subtotal = total - iva;
        
            cargarDatosPerfil(session);

            Carrito carritoRecibo = PedidoDAO.obtenerDatosPedido(carrito.getPedidoID());
            System.out.println("\nCarrito para recibo: "+carritoRecibo);
            System.out.println("Total para recibo: "+total);
            System.out.println("Subtotal para recibo: "+subtotal);
            System.out.println("IVA para recibo: "+iva);
            System.out.println("Email para recibo: "+session.getAttribute("emailUsuario"));
            System.out.println("Tipo tarjeta para recibo: "+session.getAttribute("tipoTarjeta"));
            System.out.println("Num tarjeta para recibo: "+session.getAttribute("ultimosTarjeta")); 
        
            request.setAttribute("carritoRecibo", carritoRecibo);
            request.setAttribute("totalFinal", String.format("%.2f", total));
            request.setAttribute("subtotalFinal", String.format("%.2f", subtotal));
            request.setAttribute("ivaFinal", String.format("%.2f", iva));
            request.setAttribute("emailRecibo", session.getAttribute("emailUsuario"));
            request.setAttribute("tipoTarjeta", session.getAttribute("tipoTarjeta"));
            request.setAttribute("numTarjeta", session.getAttribute("ultimosTarjeta"));
            request.setAttribute("numeroFactura", (int)(Math.random() * 900000) + 100000);
        
            session.setAttribute("carrito", null);
        
            request.getRequestDispatcher("/WEB-INF/views/recibo.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("pagar")) {
            if(!logged) {
                session.setAttribute("redirectAfterLogin", "caja");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);        
        } else if (accion != null && accion.equals("cerrarSesion")) {       //Cerrar sesión
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        } else if (accion != null && accion.equals("verCaja")) {            //Volver a caja
            if(!logged) {
                session.setAttribute("redirectAfterLogin", "caja");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/caja.jsp").forward(request, response);
        } else if (accion != null && accion.equals("verCarrito")) {         //Volver al carrito
            request.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(request, response);
        } else if (accion != null && accion.equals("verLogin")) {           //Ir al login
            request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
        } else if (accion != null && accion.equals("irARegistro")) {        // Ir a la página de registro
            request.getRequestDispatcher("/WEB-INF/views/registrarUsuario.jsp").forward(request, response);
            return;
        } else if (accion != null && accion.equals("verPerfil")) {          //Ir al perfil
             if(!logged) {
                session.setAttribute("redirectAfterLogin", "perfil");
                request.getRequestDispatcher("/WEB-INF/views/iniciarSesion.jsp").forward(request, response);
                return;
            }
            cargarDatosPerfil(session);
            request.getRequestDispatcher("/WEB-INF/views/perfil.jsp").forward(request, response);
        } else {                                                                      //Por defecto volver al index
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }


    //FUNCION PARA CAGAR BIEN LOS DATOS DEL PERFIL, sino se usa la primra vez no cargan los datos
    private void cargarDatosPerfil(HttpSession session) {
        String emailSesion = (String) session.getAttribute("email");
        java.util.Map<String, String> datos = UsuarioDAO.obtenerDatosUsuario(emailSesion);
        if (datos != null) {
            session.setAttribute("nombreUsuario", datos.get("nombreUsuario"));
            session.setAttribute("emailUsuario",  datos.get("emailUsuario"));
            session.setAttribute("tipoTarjeta",   datos.get("tipoTarjeta"));
            String num = datos.get("numTarjeta");
            session.setAttribute("ultimosTarjeta",
                num != null && num.length() >= 4 ? num.substring(num.length() - 4) : "****");
        }
    }
}