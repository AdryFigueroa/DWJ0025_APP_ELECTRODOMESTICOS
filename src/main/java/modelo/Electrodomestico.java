package modelo;

public class Electrodomestico {

	private int id;
	private String nombre;
	private String problema;
	private String cliente;
	private int idCliente;
	
	public Electrodomestico() {
	}
	
	public Electrodomestico(String nombre, String problema, int idCliente) {
		this.nombre = nombre;
		this.problema = problema;
		this.idCliente = idCliente;
	}
	
	public Electrodomestico(int id, String nombre, String problema, String cliente, int idCliente) {
		this.id = id;
		this.nombre = nombre;
		this.problema = problema;
		this.cliente = cliente;
		this.idCliente = idCliente;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProblema() {
		return problema;
	}

	public void setProblema(String problema) {
		this.problema = problema;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	
}
