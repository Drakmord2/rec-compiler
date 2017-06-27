package util;

public class FileException extends Exception {

	private static final long serialVersionUID = 3457448332803077642L;

	public FileException(String message) {
		super(message);
	}
	
	public String toString() {
		String errorMessage =
			"\n\n----------------------------- I/O ERROR REPORT - BEGIN -----------------------------\n\n" +
			">> Message: " + super.getMessage() + "\n\n" +
			"------------------------------ I/O ERROR REPORT - END ------------------------------\n";
			
		return errorMessage;
	}
	
}	
