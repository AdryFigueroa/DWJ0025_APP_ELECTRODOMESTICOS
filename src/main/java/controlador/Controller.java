package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Controller() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accion = request.getParameter("accion");
		
		
		switch(accion) {
		case "listar":
			try {
				List<Cliente> clientes = getClientes();
				List<Electrodomestico> electrodomesticos = getElectrodomesticos();
				List<OrdenDeTrabajo> odt = getODT();
				request.setAttribute("clientes", clientes);
				request.setAttribute("electrodomesticos", electrodomesticos);				
				request.setAttribute("odt", odt);				
				request.getRequestDispatcher("/WEB-INF/jsp/vista/listado.jsp").forward(request, response);

			} catch (SQLException | NamingException e) {
				response.sendError(500);
				e.printStackTrace();
			}
			break;
		
		case "formulario":
			request.getRequestDispatcher("/WEB-INF/jsp/vista/formulario-paso1.jsp").forward(request, response);
			break;		
		default:
			response.sendError(500);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accion = request.getParameter("accion");
		
		switch(accion) {
		case "addCliente":
			// AGREGAR
			String nombre 		= request.getParameter("nombre");
			String telefono 	= request.getParameter("telefono");
			String direccion	= request.getParameter("direccion");
			
			Cliente nuevoCliente = new Cliente(nombre,telefono,direccion);
			
			try {
				// CREAR
				nuevoCliente = crearCliente(nuevoCliente);
				
				request.setAttribute("cliente", nuevoCliente);
				request.getRequestDispatcher("/WEB-INF/jsp/vista/formulario-paso2.jsp").forward(request, response);				
				
			} catch (SQLException | NamingException e) {
				e.printStackTrace();
				response.sendError(500);
			}
			break;
		
		case "addElectrodomestico":
			// GET CLIENTE
			int idCliente			= Integer.parseInt(request.getParameter("idCliente"));
			String nombreCliente	= request.getParameter("nombreCliente");
			String telefonoCliente	= request.getParameter("telefonoCliente");
			String direccionCliente	= request.getParameter("direccionCliente");
			
			// CREAR CLIENTE
			Cliente cliente = new Cliente(idCliente,nombreCliente,telefonoCliente,direccionCliente);
			
			// GET PRODUCTO
			String nombreProducto 	= request.getParameter("nombreProducto");
			String problemaProducto 	= request.getParameter("problemaProducto");

			// CREA PRODUCTO
			Electrodomestico electrodomestico = new Electrodomestico(nombreProducto,problemaProducto,idCliente);
			
			try {
				// BD
				electrodomestico = crearElectrodomestico(electrodomestico);
				
				request.setAttribute("cliente", cliente);
				request.getRequestDispatcher("/WEB-INF/jsp/vista/formulario-paso2.jsp").forward(request, response);		
				
			} catch (SQLException | NamingException e) {
				e.printStackTrace();
				response.sendError(500);
			}
			break;
			
		case "finalizar":
			request.getRequestDispatcher("index.jsp").forward(request, response);				
			break;
			
		default:
			response.sendError(500);
		}
		
	}

	public Connection getConexion() throws NamingException, SQLException {
		InitialContext initialContext = new InitialContext();
		DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/postgres");
		return dataSource.getConnection();
	}
	
	private List<Cliente> getClientes() throws SQLException, NamingException {
		try(
				Connection conn = getConexion();
				Statement st = conn.createStatement();
			) {
			ResultSet rs = st.executeQuery("SELECT * FROM cliente");
			List<Cliente> clientes = new ArrayList<Cliente>();
			while(rs.next()) {
				int id = rs.getInt("id_cliente");
				String nombre = rs.getString("Nombre");
				String telefono = rs.getString("Telefono");
				String direccion = rs.getString("Direccion");
				Cliente cliente = new Cliente(id,nombre,telefono,direccion);
				clientes.add(cliente);
			}
			return clientes;
		}
	}
	
	private List<Electrodomestico> getElectrodomesticos() throws SQLException, NamingException {
		try(
				Connection conn = getConexion();
				Statement st = conn.createStatement();
			) {
			ResultSet rs = st.executeQuery("SELECT electrodomestico.id_electrodomestico, electrodomestico.nombre, electrodomestico.problema, electrodomestico.id_cliente, cliente.nombre AS asignado FROM electrodomestico, cliente WHERE electrodomestico.id_cliente = cliente.id_cliente");
			List<Electrodomestico> electrodomesticos = new ArrayList<>();
			while(rs.next()) {
				int id = rs.getInt("id_electrodomestico");
				String nombre = rs.getString("Nombre");
				String problema = rs.getString("Problema");
				String cliente = rs.getString("asignado");
				int idCliente = rs.getInt("id_cliente");
				Electrodomestico electrodomestico= new Electrodomestico(id,nombre,problema,cliente,idCliente);
				electrodomesticos.add(electrodomestico);
			}
			return electrodomesticos;
		}
	}
	
	private List<OrdenDeTrabajo> getODT() throws SQLException, NamingException {
		try(
				Connection conn = getConexion();
				Statement st = conn.createStatement();
			) {
			ResultSet rs = st.executeQuery("SELECT ordendetrabajo.id_odt, ordendetrabajo.estado, ordendetrabajo.fechasolicitud, ordendetrabajo.fechaactualizacionorden, electrodomestico.nombre AS objeto, cliente.nombre AS asignado FROM ordendetrabajo, electrodomestico, cliente WHERE ordendetrabajo.id_electrodomestico = electrodomestico.id_electrodomestico AND electrodomestico.id_cliente = cliente.id_cliente");
			List<OrdenDeTrabajo> ordenesDeTrabajo = new ArrayList<>();
			while(rs.next()) {
				int id = rs.getInt("id_odt");
				String objeto = rs.getString("objeto");
				String estado = rs.getString("estado");
				String cliente = rs.getString("asignado");
				String fechaSolicitud = rs.getString("fechasolicitud");
				OrdenDeTrabajo ordenDeTrabajo= new OrdenDeTrabajo(id,objeto,estado,cliente,fechaSolicitud);
				ordenesDeTrabajo.add(ordenDeTrabajo);
			}
			return ordenesDeTrabajo;
		}
	}
	
	private Cliente crearCliente(Cliente nuevoCliente) throws SQLException, NamingException {
		try(
				Connection conn = getConexion();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO cliente(nombre, telefono, direccion) VALUES (?,?,?)");

			) {
			ps.setString(1, nuevoCliente.getNombre());
			ps.setString(2, nuevoCliente.getTelefono());
			ps.setString(3, nuevoCliente.getDireccion());
			ps.executeUpdate();
		}
		
		try(
				Connection conn = getConexion();
				Statement st = conn.createStatement();
			) {
			
			ResultSet rs = st.executeQuery("SELECT * FROM cliente ORDER BY id_cliente DESC LIMIT 1");
			
			
			if(rs.next()) {				
				int id = rs.getInt("id_cliente");
				nuevoCliente.setId(id);
				return nuevoCliente;
			}else {
				return null;
			}
		}
		
	}
	
	private Electrodomestico crearElectrodomestico(Electrodomestico electrodomestico) throws SQLException, NamingException {
		try(
				Connection conn = getConexion();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO electrodomestico(nombre, problema, id_cliente) VALUES (?,?,?)");

			) {
			ps.setString(1, electrodomestico.getNombre());
			ps.setString(2, electrodomestico.getProblema());
			ps.setInt(3, electrodomestico.getIdCliente());
			ps.executeUpdate();
			
		}
		
		try(
				Connection conn = getConexion();
				Statement st = conn.createStatement();
			) {
			ResultSet rs = st.executeQuery("SELECT * FROM electrodomestico ORDER BY id_electrodomestico DESC LIMIT 1");
			if(rs.next()) {				
				int id = rs.getInt("id_electrodomestico");
				electrodomestico.setId(id);
				return electrodomestico;
			}else {
				return null;
			}
		}
		
		
	}
	
	
}
