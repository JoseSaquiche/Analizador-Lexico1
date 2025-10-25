// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package Analizador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame extends JFrame {
   private JTextPane codeTextArea;
   private JTextArea tokensTextArea;
   private JTextArea errorsTextArea;
   private JButton analyzeButton;
   private JButton loadFileButton;
   private JButton clearButton;
   private JLabel statusLabel;
   private static final Color BACKGROUND = new Color(245, 245, 245);
   private static final Color SURFACE;
   private static final Color PRIMARY;
   private static final Color SECONDARY;
   private static final Color ACCENT;
   private static final Color TEXT_PRIMARY;
   private static final Color TEXT_SECONDARY;

   public MainFrame() {
      this.initializeUI();
   }

   private void initializeUI() {
      this.setTitle("SwiftLex - Analizador Léxico");
      this.setDefaultCloseOperation(3);
      this.setSize(1300, 850);
      this.setLocationRelativeTo((Component)null);
      this.getContentPane().setBackground(BACKGROUND);
      this.createMenuBar();
      this.createMainPanel();
      this.setVisible(true);
   }

   private void createMenuBar() {
      JMenuBar menuBar = new JMenuBar();
      menuBar.setBackground(SURFACE);
      menuBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
      JMenu fileMenu = this.createStyledMenu("Archivo");
      JMenuItem openItem = this.createStyledMenuItem("Abrir archivo");
      JMenuItem exitItem = this.createStyledMenuItem("Salir");
      openItem.addActionListener((e) -> {
         this.loadFile();
      });
      exitItem.addActionListener((e) -> {
         System.exit(0);
      });
      fileMenu.add(openItem);
      fileMenu.addSeparator();
      fileMenu.add(exitItem);
      JMenu helpMenu = this.createStyledMenu("Ayuda");
      JMenuItem aboutItem = this.createStyledMenuItem("Acerca de");
      aboutItem.addActionListener((e) -> {
         this.showAboutDialog();
      });
      helpMenu.add(aboutItem);
      menuBar.add(fileMenu);
      menuBar.add(helpMenu);
      this.setJMenuBar(menuBar);
   }

   private JMenu createStyledMenu(String text) {
      JMenu menu = new JMenu(text);
      menu.setForeground(TEXT_PRIMARY);
      menu.setFont(new Font("Segoe UI", 0, 12));
      return menu;
   }

   private JMenuItem createStyledMenuItem(String text) {
      JMenuItem item = new JMenuItem(text);
      item.setForeground(TEXT_PRIMARY);
      item.setFont(new Font("Segoe UI", 0, 12));
      return item;
   }

   private void createMainPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
      mainPanel.setBackground(BACKGROUND);
      mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
      mainPanel.add(this.createHeaderPanel(), "North");
      mainPanel.add(this.createCodePanel(), "Center");
      mainPanel.add(this.createResultsPanel(), "South");
      this.add(mainPanel);
   }

   private JPanel createHeaderPanel() {
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(BACKGROUND);
      JLabel titleLabel = new JLabel("SwiftLex - Analizador Léxico para Swift");
      titleLabel.setFont(new Font("Segoe UI", 1, 24));
      titleLabel.setForeground(PRIMARY);
      JPanel buttonPanel = new JPanel(new FlowLayout(2, 10, 0));
      buttonPanel.setBackground(BACKGROUND);
      this.loadFileButton = this.createPrimaryButton("\ud83d\udcc1 Cargar Archivo");
      this.analyzeButton = this.createAccentButton("\ud83d\udd0d Analizar Código");
      this.clearButton = this.createSecondaryButton("\ud83d\uddd1️ Limpiar");
      this.loadFileButton.addActionListener((e) -> {
         this.loadFile();
      });
      this.analyzeButton.addActionListener((e) -> {
         this.analyzeCode();
      });
      this.clearButton.addActionListener((e) -> {
         this.clearAll();
      });
      buttonPanel.add(this.loadFileButton);
      buttonPanel.add(this.analyzeButton);
      buttonPanel.add(this.clearButton);
      headerPanel.add(titleLabel, "West");
      headerPanel.add(buttonPanel, "East");
      return headerPanel;
   }

   private JButton createPrimaryButton(String text) {
      JButton button = new JButton(text);
      button.setBackground(PRIMARY);
      button.setForeground(Color.WHITE);
      button.setFont(new Font("Segoe UI", 1, 12));
      button.setFocusPainted(false);
      button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
      button.setCursor(new Cursor(12));
      return button;
   }

   private JButton createAccentButton(String text) {
      JButton button = new JButton(text);
      button.setBackground(ACCENT);
      button.setForeground(Color.WHITE);
      button.setFont(new Font("Segoe UI", 1, 12));
      button.setFocusPainted(false);
      button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
      button.setCursor(new Cursor(12));
      return button;
   }

   private JButton createSecondaryButton(String text) {
      JButton button = new JButton(text);
      button.setBackground(SECONDARY);
      button.setForeground(Color.WHITE);
      button.setFont(new Font("Segoe UI", 0, 12));
      button.setFocusPainted(false);
      button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
      button.setCursor(new Cursor(12));
      return button;
   }

   private JPanel createCodePanel() {
      JPanel codePanel = new JPanel(new BorderLayout(10, 10));
      codePanel.setBackground(BACKGROUND);
      
      JLabel codeLabel = new JLabel("EDITOR DE CÓDIGO SWIFT");
      codeLabel.setFont(new Font("Segoe UI", 1, 14));
      codeLabel.setForeground(TEXT_SECONDARY);
      codeLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
      
      this.codeTextArea = new JTextPane();
      this.codeTextArea.setFont(new Font("Consolas", 0, 13));
      this.codeTextArea.setBackground(Color.WHITE);
      this.codeTextArea.setCaretColor(PRIMARY);
      this.codeTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      
      // ✅ SCROLL MEJORADO PARA EDITOR DE CÓDIGO
      JScrollPane scrollPane = new JScrollPane(this.codeTextArea);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(180, 180, 180)),
          BorderFactory.createEmptyBorder(2, 2, 2, 2)
      ));
      scrollPane.getViewport().setBackground(Color.WHITE);
      
      this.statusLabel = new JLabel(" Listo para analizar");
      this.statusLabel.setFont(new Font("Segoe UI", 0, 11));
      this.statusLabel.setForeground(TEXT_SECONDARY);
      this.statusLabel.setBackground(new Color(240, 240, 240));
      this.statusLabel.setOpaque(true);
      this.statusLabel.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
          BorderFactory.createEmptyBorder(8, 15, 8, 15)
      ));
      
      codePanel.add(codeLabel, "North");
      codePanel.add(scrollPane, "Center");
      codePanel.add(this.statusLabel, "South");
      
      return codePanel;
   }

   private JPanel createResultsPanel() {
      JPanel resultsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
      resultsPanel.setBackground(BACKGROUND);
      resultsPanel.add(this.createTokensPanel());
      resultsPanel.add(this.createErrorsPanel());
      return resultsPanel;
   }

   private JPanel createTokensPanel() {
      JPanel tokensPanel = new JPanel(new BorderLayout());
      tokensPanel.setBackground(BACKGROUND);
      
      JLabel tokensLabel = new JLabel("TOKENS IDENTIFICADOS");
      tokensLabel.setFont(new Font("Segoe UI", 1, 14));
      tokensLabel.setForeground(TEXT_SECONDARY);
      tokensLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
      
      this.tokensTextArea = new JTextArea();
      this.tokensTextArea.setFont(new Font("Consolas", 0, 11));
      this.tokensTextArea.setBackground(SURFACE);
      this.tokensTextArea.setEditable(false);
      this.tokensTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      
      // ✅ SCROLL MEJORADO PARA TOKENS
      JScrollPane tokensScroll = new JScrollPane(this.tokensTextArea);
      tokensScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      tokensScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      tokensScroll.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(200, 200, 200)),
          BorderFactory.createEmptyBorder(2, 2, 2, 2)
      ));
      
      tokensPanel.add(tokensLabel, "North");
      tokensPanel.add(tokensScroll, "Center");
      
      return tokensPanel;
   }

   private JPanel createErrorsPanel() {
      JPanel errorsPanel = new JPanel(new BorderLayout());
      errorsPanel.setBackground(BACKGROUND);
      
      JLabel errorsLabel = new JLabel("ERRORES LÉXICOS");
      errorsLabel.setFont(new Font("Segoe UI", 1, 14));
      errorsLabel.setForeground(TEXT_SECONDARY);
      errorsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
      
      this.errorsTextArea = new JTextArea();
      this.errorsTextArea.setFont(new Font("Consolas", 0, 11));
      this.errorsTextArea.setBackground(SURFACE);
      this.errorsTextArea.setEditable(false);
      this.errorsTextArea.setForeground(new Color(200, 0, 0));
      this.errorsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      
      // ✅ SCROLL MEJORADO PARA ERRORES
      JScrollPane errorsScroll = new JScrollPane(this.errorsTextArea);
      errorsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      errorsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      errorsScroll.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(200, 200, 200)),
          BorderFactory.createEmptyBorder(2, 2, 2, 2)
      ));
      
      errorsPanel.add(errorsLabel, "North");
      errorsPanel.add(errorsScroll, "Center");
      
      return errorsPanel;
   }

   private void loadFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Swift (*.swift)", new String[]{"swift"}));
      fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (*.txt)", new String[]{"txt"}));
      int result = fileChooser.showOpenDialog(this);
      if (result == 0) {
         File file = fileChooser.getSelectedFile();

         try {
            String content = Files.readString(file.toPath());
            this.codeTextArea.setText(content);
            this.statusLabel.setText(" Archivo cargado: " + file.getName());
         } catch (IOException var5) {
            this.showError("Error al leer el archivo: " + var5.getMessage());
         }
      }

   }

   private void analyzeCode() {
      String code = this.codeTextArea.getText();
      if (code.trim().isEmpty()) {
         this.showWarning("No hay código para analizar");
      } else {
         try {
            this.statusLabel.setText(" Analizando código...");
            SyntaxHighlighter.applySwiftHighlighting(this.codeTextArea);
            SwiftLexer lexer = new SwiftLexer(code);
            List<Token> tokens = lexer.Analizar();
            this.displayTokens(tokens);
            this.displayErrors(lexer, tokens);
            this.statusLabel.setText(" Análisis completado" + (lexer.tieneError() ? " - Se encontraron errores" : " - Sin errores"));
         } catch (Exception var4) {
            this.statusLabel.setText(" Error durante el análisis");
            this.showError("Error durante el análisis: " + var4.getMessage());
         }

      }
   }

   private void displayTokens(List<Token> tokens) {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("%-18s %-22s %-8s %-8s\n", "TIPO", "VALOR", "LÍNEA", "COL"));
      sb.append(String.format("%-18s %-22s %-8s %-8s\n", "────", "─────", "─────", "───"));
      Iterator var3 = tokens.iterator();

      while(var3.hasNext()) {
         Token token = (Token)var3.next();
         if (token.getTipo() != TokenType.Espacio && token.getTipo() != TokenType.Nueva_linea) {
            String tipo = token.getTipo().toString();
            String valor = token.getValor().replace("\n", "\\n").replace("\t", "\\t");
            if (valor.length() > 20) {
               valor = valor.substring(0, 17) + "...";
            }

            sb.append(String.format("%-18s %-22s %-8d %-8d\n", tipo, valor, token.getLinea(), token.getColumna()));
         }
      }

      this.tokensTextArea.setText(sb.toString());
   }

   private void displayErrors(SwiftLexer lexer, List<Token> tokens) {
      StringBuilder sb = new StringBuilder();
      if (lexer.tieneError()) {
         int errorCount = 0;
         Iterator var5 = tokens.iterator();

         while(var5.hasNext()) {
            Token token = (Token)var5.next();
            if (token.getTipo() == TokenType.Error) {
               ++errorCount;
               sb.append(String.format("• Error %d: Línea %d, Columna %d\n", errorCount, token.getLinea(), token.getColumna()));
               sb.append("  Token: '" + token.getValor() + "'\n\n");
            }
         }

         sb.insert(0, "Se encontraron " + errorCount + " errores:\n\n");
      } else {
         sb.append("✓ Análisis completado sin errores léxicos");
      }

      this.errorsTextArea.setText(sb.toString());
   }

   private void clearAll() {
      int result = JOptionPane.showConfirmDialog(this, "¿Limpiar todo el contenido?", "Confirmar", 0);
      if (result == 0) {
         this.codeTextArea.setText("");
         this.tokensTextArea.setText("");
         this.errorsTextArea.setText("");
         this.statusLabel.setText(" Listo para analizar");
      }

   }

   private void showError(String message) {
      JOptionPane.showMessageDialog(this, message, "Error", 0);
   }

   private void showWarning(String message) {
      JOptionPane.showMessageDialog(this, message, "Advertencia", 2);
   }

   private void showAboutDialog() {
      String aboutText = "SwiftLex - Analizador Léxico\n\nVersión 1.0\nAnalizador léxico para código Swift\nInterfaz minimalista moderna";
      JOptionPane.showMessageDialog(this, aboutText, "Acerca de", 1);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new MainFrame();
         }
      });
   }

   static {
      SURFACE = Color.WHITE;
      PRIMARY = new Color(70, 130, 180);
      SECONDARY = new Color(100, 150, 200);
      ACCENT = new Color(33, 150, 243);
      TEXT_PRIMARY = new Color(33, 33, 33);
      TEXT_SECONDARY = new Color(97, 97, 97);
   }
}
