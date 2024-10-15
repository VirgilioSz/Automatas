public class Direccion {
	
	protected String identificador;
	protected int token;
	protected int noLinea;
	protected int vci;
	
	public Direccion(String identificador, int token, int noLinea, int vci) {
		super();
		this.identificador = identificador;
		this.token = token;
		this.noLinea = noLinea;
		this.vci = vci;
	}
	public String getIdentificador() {
		return identificador;
	}
	public int getToken() {
		return token;
	}
	public int getNoLinea() {
		return noLinea;
	}
	public int getVci() {
		return vci;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public void setToken(int token) {
		this.token = token;
	}
	public void setNoLinea(int noLinea) {
		this.noLinea = noLinea;
	}
	public void setVci(int vci) {
		this.vci = vci;
	}
	@Override
	public String toString() {
		return "Direccion [identificador=" + identificador + ", token=" + token + ", noLinea=" + noLinea + ", vci="
				+ vci + "]";
	}
	
	
	
}
