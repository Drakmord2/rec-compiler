package scanner;

import java.util.HashMap;
import java.util.Map;
import parser.GrammarSymbols;
import util.Arquivo;

/**
 * Scanner class
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Scanner {

	private Arquivo file;
	private char currentChar;
	private GrammarSymbols currentKind;
	private StringBuffer currentSpelling;
	private int line, column;
	private Map<String, GrammarSymbols> keywords;
	
	/**
	 * Default constructor
	 * @throws Exception 
	 */
	public Scanner(String location) throws Exception {
		this.file 				= new Arquivo(location);		
		this.line 				= 0;
		this.column 			= 0;
		this.currentChar 		= this.file.readChar();
		this.currentSpelling 	= new StringBuffer();
		this.keywords 			= new HashMap<String, GrammarSymbols>();

		this.keywords.put("show", GrammarSymbols.SHOW);
		this.keywords.put("begin", GrammarSymbols.BEGIN);
		this.keywords.put("end", GrammarSymbols.END);
		this.keywords.put("if", GrammarSymbols.IF);
		this.keywords.put("then", GrammarSymbols.THEN);
		this.keywords.put("else", GrammarSymbols.ELSE);
		this.keywords.put("while", GrammarSymbols.WHILE);
		this.keywords.put("loop", GrammarSymbols.LOOP);
		this.keywords.put("procedure", GrammarSymbols.PROCEDURE);
		this.keywords.put("function", GrammarSymbols.FUNCTION);
		this.keywords.put("return", GrammarSymbols.RETURN);
		this.keywords.put("break", GrammarSymbols.BREAK);
		this.keywords.put("continue", GrammarSymbols.CONTINUE);
		this.keywords.put("is", GrammarSymbols.IS);
		this.keywords.put("global", GrammarSymbols.GLOBAL);
		this.keywords.put("Integer", GrammarSymbols.INTEGER);
		this.keywords.put("Boolean", GrammarSymbols.BOOLEAN);
		this.keywords.put("True", GrammarSymbols.TRUE);
		this.keywords.put("False", GrammarSymbols.FALSE);
	}
	
	/**
	 * Returns the next token
	 * @return
	 * @throws LexicalException 
	 */
	public Token getNextToken() throws LexicalException {
		while (this.isSeparator(this.currentChar)) {
			this.scanSeparator();
		}
		
		this.currentSpelling.delete(0, this.currentSpelling.length());
		
		this.currentKind = this.scanToken();
		
		String spelling = this.currentSpelling.toString();
		Token token 	= new Token(this.currentKind, spelling, this.line, this.column);
		
		return token;
	}
	
	/**
	 * Returns if a character is a separator
	 * @param c
	 * @return
	 */
	private boolean isSeparator(char c) {
		if ( c == ' ' || c == '\n' || c == '\t' || c == '#' ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Reads (and ignores) a separator
	 * @throws LexicalException
	 */
	private void scanSeparator() throws LexicalException {
		if (this.currentChar == '#') {
				while (this.isGraphic(this.currentChar) || this.currentChar == '\t') {
					this.getNextChar();
				}
		}else {
			this.getNextChar();
		}
	}
	
	/**
	 * Gets the next char
	 */
	private void getNextChar() {
		this.currentSpelling.append(this.currentChar);
		this.currentChar = this.file.readChar();
		this.incrementLineColumn();
	}
	
	/**
	 * Increments line and column
	 */
	private void incrementLineColumn() {
		if ( this.currentChar == '\n' ) {
			this.line++;
			this.column = 0;
		} else {
			if ( this.currentChar == '\t' ) {
				this.column = this.column + 4;
			} else {
				this.column++;
			}
		}
	}
	
	/**
	 * Returns if a char is a digit (between 0 and 9)
	 * @param c
	 * @return
	 */
	private boolean isDigit(char c) {
		if ( c >= '0' && c <= '9' ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns if a char is a letter (between a and z or between A and Z)
	 * @param c
	 * @return
	 */
	private boolean isLetter(char c) {
		if ( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns if a char is a graphic (any ASCII visible character)
	 * @param c
	 * @return
	 */
	private boolean isGraphic(char c) {
		if ( c >= ' ' && c <= '~' ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Scans the next token
	 * Simulates the DFA that recognizes the language described by the lexical grammar
	 * @return
	 * @throws LexicalException
	 */
	private GrammarSymbols scanToken() throws LexicalException {
		int state = 0;
		
		while (true) {
			switch (state) {
			case 0:
				if (this.isLetter(this.currentChar)) {
					state = 1;
				}else if (this.isDigit(this.currentChar)) {
					state = 2;
				}else if (this.currentChar == '>') {
					state = 3;
				}else if (this.currentChar == '<') {
					state = 4;
				}else if (this.currentChar == '=') {
					state = 5;
				}else if (this.currentChar == '/') {
					state = 6;
				}else if (this.currentChar == '+') {
					state = 7;
				}else if (this.currentChar == '-') {
					state = 8;
				}else if (this.currentChar == '*') {
					state = 9;
				}else if (this.currentChar == ':') {
					state = 10;
				}else if (this.currentChar == ',') {
					state = 11;
				}else if (this.currentChar == ';') {
					state = 12;
				}else if (this.currentChar == ')') {
					state = 13;
				}else if (this.currentChar == '(') {
					state = 14;
				}else if (this.currentChar == '#') {
					state = 18;
				}else if (this.currentChar == '\000') {
					state = 19;
				}else {
					state = 15;
					break;
				}
				
				this.getNextChar();
				break;
				
			case 1:
				while (this.isLetter(this.currentChar) || this.isDigit(this.currentChar)) {
					this.getNextChar();
				}
				
				String spellingStr = this.currentSpelling.toString();
				if (this.keywords.containsKey(spellingStr)) {
					return this.keywords.get(spellingStr);
				}else {
					return GrammarSymbols.ID;
				}
				
			case 2:
				while (this.isDigit(this.currentChar)) {
					this.getNextChar();
				}
					return GrammarSymbols.NUMERO;
				
			case 3:
				if (this.currentChar == '=') {
					this.getNextChar();
					return GrammarSymbols.GTE;
				}else {
					return GrammarSymbols.GT;
				}
			case 4:
				if (this.currentChar == '=') {
					this.getNextChar();
					return GrammarSymbols.LTE;
				}else {
					return GrammarSymbols.LT;
				}
				
			case 5:
					return GrammarSymbols.EQUALS;
				
			case 6:
				if (this.currentChar == '=') {
					this.getNextChar();
					return GrammarSymbols.NEQUALS;
				}else {
					return GrammarSymbols.DIV;
				}
				
			case 7:
				return GrammarSymbols.ADD;
				
			case 8:
				return GrammarSymbols.SUB;
				
			case 9:
				return GrammarSymbols.MULT;
				
			case 10:
				if (this.currentChar == '=') {
					this.getNextChar();
					return GrammarSymbols.ASSIGN;
				}else {
					return GrammarSymbols.COLON;
				}
				
			case 11:
				return GrammarSymbols.COMMA;
				
			case 12:
				return GrammarSymbols.SEMICOLON;
				
			case 13:
				return GrammarSymbols.RP;
				
			case 14:
				return GrammarSymbols.LP;
				
			case 15:
				throw new LexicalException("Caractere inexperado.", this.currentChar, this.line, this.column);
			
			case 16:
				if (! this.isDigit(this.currentChar)) {
					state = 16;
					break;
				}else {
					state = 18;
					this.getNextChar();
					break;
				}
				
			case 17:
				while (this.isDigit(this.currentChar)) {
					this.getNextChar();
				}
				
				return GrammarSymbols.NUMERO;
				
			case 18:
				return GrammarSymbols.COMMENT;
				
			case 19:
				return GrammarSymbols.EOT;
			}
		}
	}
	
}
