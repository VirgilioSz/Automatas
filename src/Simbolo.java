public class Simbolo {
	
	protected String identificador;
	protected int token;
	protected int valorInt;
	protected float valorReal;
	protected String valorcadena;
	protected boolean valorLogico;
	protected int d1;
	protected int d2;
	protected String ptr;
	protected String ambito;
	
	public Simbolo(String identificador, int token, int valorInt, int d1, int d2, String ptr, String ambito) {
		super();
		this.identificador = identificador;
		this.token = token;
		this.valorInt = valorInt;
		this.d1 = d1;
		this.d2 = d2;
		this.ptr = ptr;
		this.ambito = ambito;
	}
	
	public Simbolo(String identificador, int token, float valorReal, int d1, int d2, String ptr, String ambito) {
		super();
		this.identificador = identificador;
		this.token = token;
		this.valorReal = valorReal;
		this.d1 = d1;
		this.d2 = d2;
		this.ptr = ptr;
		this.ambito = ambito;
	}


	public Simbolo(String identificador, int token, String valorcadena, int d1, int d2, String ptr, String ambito) {
		super();
		this.identificador = identificador;
		this.token = token;
		this.valorcadena = valorcadena;
		this.d1 = d1;
		this.d2 = d2;
		this.ptr = ptr;
		this.ambito = ambito;
	}
	

	public Simbolo(String identificador, int token, boolean valorLogico, int d1, int d2, String ptr, String ambito) {
		super();
		this.identificador = identificador;
		this.token = token;
		this.valorLogico = valorLogico;
		this.d1 = d1;
		this.d2 = d2;
		this.ptr = ptr;
		this.ambito = ambito;
	}

	public String getIdentificador() {
		return identificador;
	}

	public int getToken() {
		return token;
	}

	public int getValorInt() {
		return valorInt;
	}

	public float getValorReal() {
		return valorReal;
	}

	public String getValorcadena() {
		return valorcadena;
	}

	public boolean isValorLogico() {
		return valorLogico;
	}

	public int getD1() {
		return d1;
	}

	public int getD2() {
		return d2;
	}

	public String getPtr() {
		return ptr;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public void setValorInt(int valorInt) {
		this.valorInt = valorInt;
	}

	public void setValorReal(float valorReal) {
		this.valorReal = valorReal;
	}

	public void setValorcadena(String valorcadena) {
		this.valorcadena = valorcadena;
	}

	public void setValorLogico(boolean valorLogico) {
		this.valorLogico = valorLogico;
	}

	public void setD1(int d1) {
		this.d1 = d1;
	}

	public void setD2(int d2) {
		this.d2 = d2;
	}

	public void setPtr(String ptr) {
		this.ptr = ptr;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	@Override
	public String toString() {
		return "Simbolo [identificador=" + identificador + ", token=" + token + ", valorInt=" + valorInt
				+ ", valorReal=" + valorReal + ", valorcadena=" + valorcadena + ", valorLogico=" + valorLogico + ", d1="
				+ d1 + ", d2=" + d2 + ", ptr=" + ptr + ", ambito=" + ambito + "]";
	}
}
