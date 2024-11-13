public class Operadores {
    protected Token token;
	protected int prioridad;
	
	public Operadores(Token token, int prioridad) {
		super();
		this.token = token;
		this.prioridad = prioridad;
	}

	public Token getToken() {
		return token;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
	
	@Override
	public String toString() {
		return "Lexema = " + token.getLexema() + ", prioridad = " + prioridad;
	}
}
