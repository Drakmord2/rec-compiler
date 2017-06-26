package util;

public class InputException extends Exception {

	private static final long serialVersionUID = 3457448332803077642L;

	public InputException(String message) {
		super(message);
	}
	
	public String toString() {
		String errorMessage =
			"\n\n----------------------------- INPUT ERROR REPORT - BEGIN -----------------------------\n\n" +
			">> Message: " + super.getMessage() + "\n\n" +
			"------------------------------ INPUT ERROR REPORT - END ------------------------------\n";
			
		return errorMessage;
	}
	
}	
