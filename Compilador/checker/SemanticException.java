package checker;

/**
 * Semantic Exception
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class SemanticException extends Exception {

	private static final long serialVersionUID = 3457448332803077642L;
	
	/**
	 * Default constructor
	 * @param message
	 */
	public SemanticException(String message) {
		super(message);
	}
	
	/**
	 * Creates the error report
	 */
	public String toString() {
		String errorMessage =
			"----------------------------- SEMANTIC ERROR REPORT - BEGIN -----------------------------\n\n" +
			">> Message: " + super.getMessage() + "\n\n" +
			"------------------------------ SEMANTIC ERROR REPORT - END ------------------------------\n";
			
		return errorMessage;
	}
	
}
