package scanner;

/**
 * Lexical Exception
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class LexicalException extends Exception {

	private static final long serialVersionUID = 3457448332803077642L;
	
	// Not expected char
	private char c;
	// Line and column of error
	private int line, column;
	
	/**
	 * Default constructor
	 * @param message
	 * @param c
	 * @param line
	 * @param column
	 */
	public LexicalException(String message, char c, int line, int column) {
		super(message);
		this.c = c;
		this.line = line;
		this.column = column;
	}
	
	/**
	 * Creates the error report
	 */	
	public String toString() {
		String errorMessage =
			"----------------------------- LEXICAL ERROR REPORT - BEGIN -----------------------------\n" +
			">> Message: " + super.getMessage() + "\n" +
			">> Character: " + this.c + "\n" +
			"   at line: " + (this.line+1) + "\n" +
			"   at column: " + (this.column+1) + "\n" +
			"------------------------------ LEXICAL ERROR REPORT - END ------------------------------\n";
			
		return errorMessage;
	}
	
}	
