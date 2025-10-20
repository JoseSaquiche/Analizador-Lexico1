package Analizador;

public enum TokenType {
    Palabra_Reservada,

    Indentificador,

    Literal_Numerico,
    Literal_Cadena,
    Literal_Booleano,
    Literal_Nil,

    Operador_Aritmetico,
    Operador_Comparacion,
    Operador_Logico,
    Operador_Asignacion,
    Operador_Rango,
    Operador_Nil_Coalescente,

    Parentesis_Izquierda,
    Parentesis_Derecha,
    Llave_Izquierda,
    Llave_Derecha,
    Corchete_Izquierda,
    Corchete_Derecha,
    Coma,
    Punto,
    Punto_Coma,
    Dos_Punto,
    Arroba,
    Numeral,

    Comentario_Linea,
    Comentario_Bloque,

    Nueva_linea,
    Espacio,
    Error


}
