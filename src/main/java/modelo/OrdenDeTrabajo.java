package modelo;

public class OrdenDeTrabajo {

	private int id;
	private String objeto;
	private String estado;
	private String cliente;
	private String fechaSolicitud;
	
	
	public OrdenDeTrabajo() {
		
	}
	
	public OrdenDeTrabajo(int id, String objeto, String estado, String cliente, String fechaSolicitud) {
		super();
		this.id = id;
		this.objeto = objeto;
		this.estado = estado;
		this.cliente = cliente;
		this.fechaSolicitud = fechaSolicitud;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}


}
