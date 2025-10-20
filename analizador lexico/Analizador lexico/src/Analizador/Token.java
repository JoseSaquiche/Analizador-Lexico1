package Analizador;

public class Token {
    private TokenType tipo;
    private String valor;
    private int linea;
    private int columna;

    public Token(TokenType tipo, String valor, int linea, int columna) {
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
    }

    public TokenType getTipo() {return tipo;}
    public String getValor() {return valor;}
    public int getLinea() {return linea;}
    public int getColumna() {return columna;}

    @Override
    public String toString() {
        return String.format("Token[%s,'%s',L:%d, C:%d]",
                tipo,valor,linea,columna);
    }
}
