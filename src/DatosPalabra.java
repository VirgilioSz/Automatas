public class DatosPalabra {
	
	private String palabra;
    private int valorToken;
    private int esIdentificador;
    private int posicion;

    public DatosPalabra(String palabra, int valorToken, int esIdentificador, int posicion) {
        this.palabra = palabra;
        this.valorToken = valorToken;
        this.esIdentificador = esIdentificador;
        this.posicion = posicion;
    }
    public DatosPalabra(String palabra, int posicion) {
        this.palabra = palabra;
        this.posicion = posicion;
    }
    public String getPalabra() {
        return palabra;
    }
    public int getValorToken() { return valorToken; }
    public int getEsIdentificador() { return esIdentificador; }
    public int getPosicion() {
        return posicion;
    }


    public void setValorToken(int valorToken) { this.valorToken = valorToken; }
    public void setEsIdentificador(int esIdentificador) { this.esIdentificador = esIdentificador; }
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return(palabra != null && !palabra.isEmpty() ? palabra : "") + ","+
                (valorToken != 0 ? + valorToken : "") +","+
                (esIdentificador != 0 ? + esIdentificador : "")+","+
                (posicion != 0 ? + posicion : "");
    }

    
    
    
}
