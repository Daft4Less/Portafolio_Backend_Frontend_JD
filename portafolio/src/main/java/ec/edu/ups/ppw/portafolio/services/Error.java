package ec.edu.ups.ppw.portafolio.services;

public class Error {
	private int codigo;
	private String name;
	private String description;
	
	// Add no-argument constructor for JSON-B/JAX-RS serialization
	public Error() {
		
	}
	
	public Error (int codigo, String name, String description) {
		this.codigo = codigo;
		this.name = name;
		this.description = description;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
