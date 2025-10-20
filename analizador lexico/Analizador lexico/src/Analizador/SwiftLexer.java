package Analizador;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SwiftLexer {
    private String codigo;
    private int posicion;
    private int linea;
    private int columna;
    private List<Token> tokens;
    private boolean tieneError;

    private static final Set<String> Palabra_Reservada = Set.of("var", "let", "func", "class", "struct", "enum", "protocol", "extension",
            "if", "else", "switch", "case", "default", "for", "while", "repeat",
            "in", "return", "import", "init", "self", "Self", "deinit", "guard",
            "defer", "do", "try", "catch", "throw", "as", "is", "nil", "true",
            "false", "super", "any", "some", "where", "break", "continue", "fallthrough"
    );

    private static final Set<String> LITERALES_BOOLEANOS = Set.of("true", "false");


    private static final Set<String> OPERADORES_ARITMETICOS = Set.of("+", "-", "*", "/", "%");
    private static final Set<String> OPERADORES_COMPARACION = Set.of("==", "!=", "<", ">", "<=", ">=");
    private static final Set<String> OPERADORES_LOGICOS = Set.of("&&", "||", "!");
    private static final Set<String> OPERADORES_ASIGNACION = Set.of("=", "+=", "-=", "*=", "/=", "%=");
    private static final Set<String> OPERADORES_RANGO = Set.of("...", "..<");
    private static final Set<String> OPERADORES_NIL_COALESCENTE = Set.of("??");

    public SwiftLexer(String codigo) {
        this.codigo = codigo;
        this.posicion = 0;
        this.linea = 1;
        this.columna = 1;
        this.tokens = new ArrayList<>();
        this.tieneError = false;
    }

    public List<Token> Analizar() {
        tokens.clear();
        tieneError = false;

        while (posicion < codigo.length()) {
            char caracterActual = codigo.charAt(posicion);

            if (Character.isWhitespace(caracterActual)) {
                if(caracterActual == '\n') {
                    tokens.add(new Token(TokenType.Nueva_linea, "\n",linea,columna));
                    linea++;
                    columna++;
                }else {
                    tokens.add(new Token(TokenType.Espacio," ",linea,columna));
                    columna++;
                }
                posicion++;
                continue;
            }

            if(caracterActual == '/' && posicion +1 < codigo.length() && codigo.charAt(posicion) == '/') {
                procesarComentarioLinea();
                continue;
            }

            if(caracterActual == '/' && posicion +1 < codigo.length() && codigo.charAt(posicion +1 ) == '*') {
                procesarComentarioBloque();
                continue;
            }

            if (caracterActual == '"'){
                procesarLiteralCadena();
                continue;
            }

            if (Character.isDigit(caracterActual)||(caracterActual == '.' && posicion +1 < codigo.length() && Character.isDigit(codigo.charAt(posicion +1)))) {
                procesarLiteralNumerico();
                continue;
            }

            if(Character.isLetter(caracterActual) || caracterActual == '_' || caracterActual == '$') {
                procesarIdentificador();
                continue;
            }

            if(procesarOperadoresYSimbolos()){
                continue;
            }

            tokens.add(new Token(TokenType.Error, String.valueOf(caracterActual),linea,columna));
            tieneError = true;
            posicion++;
            columna++;
        }
        return tokens;

    }
private void procesarComentarioLinea() {
        int inicio = posicion;
        int inicioLinea = linea;
        int inicioColumna = columna;

        while (posicion < codigo.length() && codigo.charAt(posicion) != '\n') {
            posicion++;
            columna++;
    }
    String comentario = codigo.substring(inicio, posicion);
        tokens.add(new Token(TokenType.Comentario_Linea, comentario,inicioLinea,inicioColumna));
}
private void procesarComentarioBloque(){
        int inicio = posicion;
        int inicioLinea = linea;
        int inicioColumna = columna;
        int profundidad = 1;

        posicion +=2;
        columna +=2;

        while (posicion < codigo.length() -1 && profundidad > 0){
            if (codigo.charAt(posicion) == '/' && codigo.charAt(posicion +1) == '*') {
                profundidad++;
                posicion +=2;
                columna +=2;
            }else if (codigo.charAt(posicion) == '*' && codigo.charAt(posicion +1)== '/') {
                profundidad--;
                posicion +=2;
                columna +=2;
                } else {
                   if (codigo.charAt(posicion) != '\n') {
                       linea++;
                       columna =1;
                   } else {
                       columna++;
                   }
                   posicion++;
                }
            }
            if (profundidad > 0){
                String comentario = codigo.substring(inicio, posicion);
                tokens.add(new Token(TokenType.Error, comentario,inicioLinea,inicioColumna));
                tieneError = true;
            }else{
                String comentario = codigo.substring(inicio, posicion);
                tokens.add(new Token(TokenType.Comentario_Bloque, comentario,inicioLinea,inicioColumna));
            }

}

private void procesarLiteralCadena() {
    int inicio = posicion;
    int inicioLinea = linea;
    int inicioColumna = columna;

    posicion++; // Saltar la comilla inicial
    columna++;
    boolean escape = false;
    boolean cerrado = false;

    while (posicion < codigo.length()) {
        char c = codigo.charAt(posicion);

        if (escape) {
            escape = false;
        } else if (c == '\\') {
            escape = true;
        } else if (c == '"') {
            cerrado = true;
            posicion++;
            columna++;
            break;
        } else if (c == '\n') {
            // Salto de línea en cadena - ERROR
            break;
        }

        posicion++;
        columna++;
    }

    String literal = codigo.substring(inicio, posicion);
    if (!cerrado) {
        tokens.add(new Token(TokenType.Error, literal, inicioLinea, inicioColumna));
        tieneError = true;
    } else {
        tokens.add(new Token(TokenType.Literal_Cadena, literal, inicioLinea, inicioColumna));
    }
}

    private void procesarLiteralNumerico() {
        int inicio = posicion;
        int inicioLinea = linea;
        int inicioColumna = columna;

        // Procesar números en diferentes bases
        if (posicion + 1 < codigo.length() && codigo.charAt(posicion) == '0') {
            char siguiente = codigo.charAt(posicion + 1);
            if (siguiente == 'b') { // Binario
                posicion += 2;
                columna += 2;
                while (posicion < codigo.length() &&
                        (codigo.charAt(posicion) == '0' || codigo.charAt(posicion) == '1' ||
                                codigo.charAt(posicion) == '_')) {
                    posicion++;
                    columna++;
                }
            } else if (siguiente == 'o') { // Octal
                posicion += 2;
                columna += 2;
                while (posicion < codigo.length() &&
                        (codigo.charAt(posicion) >= '0' && codigo.charAt(posicion) <= '7' ||
                                codigo.charAt(posicion) == '_')) {
                    posicion++;
                    columna++;
                }
            } else if (siguiente == 'x') { // Hexadecimal
                posicion += 2;
                columna += 2;
                while (posicion < codigo.length() &&
                        (Character.isDigit(codigo.charAt(posicion)) ||
                                (codigo.charAt(posicion) >= 'a' && codigo.charAt(posicion) <= 'f') ||
                                (codigo.charAt(posicion) >= 'A' && codigo.charAt(posicion) <= 'F') ||
                                codigo.charAt(posicion) == '_')) {
                    posicion++;
                    columna++;
                }
            } else {
                // Decimal normal
                procesarNumeroDecimal();
            }
        } else {
            // Decimal normal
            procesarNumeroDecimal();
        }

        String numero = codigo.substring(inicio, posicion);
        tokens.add(new Token(TokenType.Literal_Numerico, numero, inicioLinea, inicioColumna));
    }

    private void procesarNumeroDecimal() {
        // Parte entera
        while (posicion < codigo.length() &&
                (Character.isDigit(codigo.charAt(posicion)) || codigo.charAt(posicion) == '_')) {
            posicion++;
            columna++;
        }

        // Parte decimal
        if (posicion < codigo.length() && codigo.charAt(posicion) == '.') {
            posicion++;
            columna++;
            while (posicion < codigo.length() &&
                    (Character.isDigit(codigo.charAt(posicion)) || codigo.charAt(posicion) == '_')) {
                posicion++;
                columna++;
            }
        }

      
        if (posicion < codigo.length() && (codigo.charAt(posicion) == 'e' || codigo.charAt(posicion) == 'E')) {
            posicion++;
            columna++;
            if (posicion < codigo.length() && (codigo.charAt(posicion) == '+' || codigo.charAt(posicion) == '-')) {
                posicion++;
                columna++;
            }
            while (posicion < codigo.length() &&
                    (Character.isDigit(codigo.charAt(posicion)) || codigo.charAt(posicion) == '_')) {
                posicion++;
                columna++;
            }
        }
    }

    private void procesarIdentificador() {
        int inicio = posicion;
        int inicioLinea = linea;
        int inicioColumna = columna;

        while (posicion < codigo.length() &&
                (Character.isLetterOrDigit(codigo.charAt(posicion)) ||
                        codigo.charAt(posicion) == '_' || codigo.charAt(posicion) == '$')) {
            posicion++;
            columna++;
        }

        String identificador = codigo.substring(inicio, posicion);
        TokenType tipo;

        if (Palabra_Reservada.contains(identificador)) {
            tipo = TokenType.Palabra_Reservada;
        } else if (LITERALES_BOOLEANOS.contains(identificador)) {
            tipo = TokenType.Literal_Booleano;
        } else if ("nil".equals(identificador)) {
            tipo = TokenType.Literal_Nil;
        } else {
            tipo = TokenType.Indentificador;
        }

        tokens.add(new Token(tipo, identificador, inicioLinea, inicioColumna));
    }

    private boolean procesarOperadoresYSimbolos() {
        char caracter = codigo.charAt(posicion);
        TokenType tipo = null;
        String valor = null;

        // Verificar operadores de múltiples caracteres primero
        if (posicion + 2 < codigo.length()) {
            String tresCaracteres = codigo.substring(posicion, posicion + 3);
            if (OPERADORES_RANGO.contains(tresCaracteres)) {
                tipo = TokenType.Operador_Rango;
                valor = tresCaracteres;
            }
        }

        if (valor == null && posicion + 1 < codigo.length()) {
            String dosCaracteres = codigo.substring(posicion, posicion + 2);
            if (OPERADORES_COMPARACION.contains(dosCaracteres) ||
                    OPERADORES_LOGICOS.contains(dosCaracteres) ||
                    OPERADORES_ASIGNACION.contains(dosCaracteres) ||
                    OPERADORES_NIL_COALESCENTE.contains(dosCaracteres) ||
                    "->".equals(dosCaracteres)) {
                tipo = obtenerTipoOperador(dosCaracteres);
                valor = dosCaracteres;
            }
        }

        if (valor == null) {

            valor = String.valueOf(caracter);
            tipo = obtenerTipoOperador(valor);


            if (tipo == null) {
                switch (caracter) {
                    case '(': tipo = TokenType.Parentesis_Izquierda; break;
                    case ')': tipo = TokenType.Parentesis_Derecha; break;
                    case '{': tipo = TokenType.Llave_Izquierda; break;
                    case '}': tipo = TokenType.Llave_Derecha; break;
                    case '[': tipo = TokenType.Corchete_Izquierda; break;
                    case ']': tipo = TokenType.Corchete_Derecha; break;
                    case ',': tipo = TokenType.Coma; break;
                    case '.': tipo = TokenType.Punto; break;
                    case ';': tipo = TokenType.Punto_Coma; break;
                    case ':': tipo = TokenType.Dos_Punto; break;
                    case '@': tipo = TokenType.Arroba; break;
                    case '#': tipo = TokenType.Numeral; break;
                    default: return false; // No es un operador ni símbolo reconocido
                }
            }
        }

        tokens.add(new Token(tipo, valor, linea, columna));
        posicion += valor.length();
        columna += valor.length();
        return true;
    }

    private TokenType obtenerTipoOperador(String operador) {
        if (OPERADORES_ARITMETICOS.contains(operador)) {
            return TokenType.Operador_Aritmetico;
        } else if (OPERADORES_COMPARACION.contains(operador)) {
            return TokenType.Operador_Comparacion;
        } else if (OPERADORES_LOGICOS.contains(operador)) {
            return TokenType.Operador_Logico;
        } else if (OPERADORES_ASIGNACION.contains(operador)) {
            return TokenType.Operador_Asignacion;
        } else if (OPERADORES_RANGO.contains(operador)) {
            return TokenType.Operador_Rango;
        } else if (OPERADORES_NIL_COALESCENTE.contains(operador)) {
            return TokenType.Operador_Nil_Coalescente;
        }
        return null;
    }

    public boolean tieneError() {
        return tieneError;
    }

    public void mostrarErrores() {
        for (Token token : tokens) {
            if (token.getTipo() == TokenType.Error) {
                System.out.printf("Error léxico en línea %d, columna %d: Token no reconocido '%s'%n",
                        token.getLinea(), token.getColumna(), token.getValor());
            }
        }
    }

}
