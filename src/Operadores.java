public class Operadores {
    protected String lexema;
	protected int prioridad;
	
	public Operadores(String lexema, int prioridad) {
		super();
		this.lexema = lexema;
		this.prioridad = prioridad;
	}

	public String getLexema() {
		return lexema;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
	
	@Override
	public String toString() {
		return "Lexema = " + lexema + ", prioridad = " + prioridad;
	}
}
