package Analizador;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class SyntaxHighlighter {

    private static final Color KEYWORD_COLOR = new Color(156, 39, 176);
    private static final Color NUMBER_COLOR = new Color(255, 87, 34);
    private static final Color STRING_COLOR = new Color(76, 175, 80);
    private static final Color COMMENT_COLOR = new Color(158, 158, 158);
    private static final Color OPERATOR_COLOR = new Color(33, 150, 243);
    private static final Color ERROR_COLOR = new Color(244, 67, 54);

    public static void applySwiftHighlighting(JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        StyleContext context = new StyleContext();

        Style defaultStyle = context.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, new Color(33, 33, 33));
        StyleConstants.setFontFamily(defaultStyle, "Consolas");
        StyleConstants.setFontSize(defaultStyle, 13);

        Style keywordStyle = context.addStyle("keyword", defaultStyle);
        StyleConstants.setForeground(keywordStyle, KEYWORD_COLOR);
        StyleConstants.setBold(keywordStyle, true);

        Style numberStyle = context.addStyle("number", defaultStyle);
        StyleConstants.setForeground(numberStyle, NUMBER_COLOR);

        Style stringStyle = context.addStyle("string", defaultStyle);
        StyleConstants.setForeground(stringStyle, STRING_COLOR);

        Style commentStyle = context.addStyle("comment", defaultStyle);
        StyleConstants.setForeground(commentStyle, COMMENT_COLOR);
        StyleConstants.setItalic(commentStyle, true);

        Style operatorStyle = context.addStyle("operator", defaultStyle);
        StyleConstants.setForeground(operatorStyle, OPERATOR_COLOR);

        String text = textPane.getText();
        SwiftLexer lexer = new SwiftLexer(text);
        java.util.List<Token> tokens = lexer.Analizar();

        try {
            doc.remove(0, doc.getLength());
            doc.insertString(0, text, defaultStyle);

            for (Token token : tokens) {
                int start = getPosition(text, token.getLinea(), token.getColumna());
                int length = token.getValor().length();

                switch (token.getTipo()) {
                    case Palabra_Reservada:
                        doc.setCharacterAttributes(start, length, keywordStyle, false);
                        break;
                    case Literal_Numerico:
                        doc.setCharacterAttributes(start, length, numberStyle, false);
                        break;
                    case Literal_Cadena:
                        doc.setCharacterAttributes(start, length, stringStyle, false);
                        break;
                    case Comentario_Linea:
                    case Comentario_Bloque:
                        doc.setCharacterAttributes(start, length, commentStyle, false);
                        break;
                    case Operador_Aritmetico:
                    case Operador_Comparacion:
                    case Operador_Logico:
                    case Operador_Asignacion:
                    case Operador_Rango:
                    case Operador_Nil_Coalescente:
                        doc.setCharacterAttributes(start, length, operatorStyle, false);
                        break;
                    case Error:
                        Style errorStyle = context.addStyle("error", defaultStyle);
                        StyleConstants.setBackground(errorStyle, new Color(255, 235, 238));
                        StyleConstants.setForeground(errorStyle, ERROR_COLOR);
                        StyleConstants.setBold(errorStyle, true);
                        doc.setCharacterAttributes(start, length, errorStyle, false);
                        break;
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private static int getPosition(String text, int linea, int columna) {
        String[] lines = text.split("\n", -1);
        int position = 0;

        for (int i = 0; i < linea - 1 && i < lines.length; i++) {
            position += lines[i].length() + 1;
        }

        position += columna - 1;
        return Math.min(position, text.length());
    }
}