package parser;

import java.util.ArrayList;
import scanner.LexicalException;
import scanner.Scanner;
import scanner.Token;
import util.AST.Argumento;
import util.AST.Atribuicao;
import util.AST.Bool;
import util.AST.Break;
import util.AST.Chamada;
import util.AST.CmdPrimario;
import util.AST.Comando;
import util.AST.ComandoBreak;
import util.AST.ComandoChamada;
import util.AST.ComandoContinue;
import util.AST.ComandoIf;
import util.AST.ComandoReturn;
import util.AST.ComandoShow;
import util.AST.ComandoWhile;
import util.AST.Continue;
import util.AST.DecVar;
import util.AST.Expressao;
import util.AST.ExpressaoArit;
import util.AST.Fator;
import util.AST.FatorChamada;
import util.AST.FatorExpressao;
import util.AST.FatorLiteral;
import util.AST.Function;
import util.AST.ID;
import util.AST.Literal;
import util.AST.LiteralBool;
import util.AST.LiteralInt;
import util.AST.Numero;
import util.AST.OpArit1;
import util.AST.OpArit2;
import util.AST.OpLog;
import util.AST.Parametro;
import util.AST.Procedure;
import util.AST.Programa;
import util.AST.Termo;
import util.AST.Tipo;
import util.AST.TipoLiteral;

/**
 * Parser class
 * 
 * @version 2010-august-29
 * @discipline Projeto de Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Parser {

	private Token currentToken = null;
	private Scanner scanner = null;

	/**
	 * Parser constructor
	 * @throws Exception 
	 */
	public Parser(String location) throws Exception {
		this.scanner = new Scanner(location);
		this.currentToken = this.scanner.getNextToken();
	}

	/**
	 * Veririfes if the current token kind is the expected one
	 * 
	 * @param kind
	 * @throws SyntacticException
	 * @throws LexicalException
	 */
	private void accept(GrammarSymbols kind) throws SyntacticException, LexicalException {
		if (this.currentToken.getKind() == kind) {
			this.acceptIt();
		} else {
			throw new SyntacticException("Esperado: " + kind + " | verificado: " + this.currentToken.getKind(),
					this.currentToken);
		}
	}

	/**
	 * Gets next token
	 * 
	 * @throws LexicalException
	 */
	private void acceptIt() throws LexicalException {
		this.currentToken = this.scanner.getNextToken();
	}

	/**
	 * Verifies if the source program is syntactically correct
	 * 
	 * @throws SyntacticException
	 * @throws LexicalException
	 */
	public Programa parse() throws SyntacticException, LexicalException {
		Programa P;
		
		P = this.parsePrograma();
		accept(GrammarSymbols.EOT);

		return P;
	}

	private Programa parsePrograma() throws SyntacticException, LexicalException {
		ArrayList<DecVar> D = new ArrayList<>();
		ArrayList<CmdPrimario> C = new ArrayList<>();
		
		while (this.currentToken.getKind() == GrammarSymbols.GLOBAL) {
			accept(GrammarSymbols.GLOBAL);
			D.add(parseDecVar());
			accept(GrammarSymbols.SEMICOLON);
		}

		do {
			C.add(parseCmdPrimario());
			accept(GrammarSymbols.SEMICOLON);
		} while (this.currentToken.getKind() != GrammarSymbols.EOT);

		return new Programa(D, C);
	}

	private CmdPrimario parseCmdPrimario() throws LexicalException, SyntacticException {
		if (this.currentToken.getKind() == GrammarSymbols.PROCEDURE)  {
			ID I1;
			Parametro P = null;
			ArrayList<DecVar> D = new ArrayList<>();
			ArrayList<Comando> C = new ArrayList<>();
			ID I2;
			
			accept(GrammarSymbols.PROCEDURE);
			I1 = new ID(this.currentToken.getSpelling());
			accept(GrammarSymbols.ID);

			accept(GrammarSymbols.LP);
			if (this.currentToken.getKind() == GrammarSymbols.ID)  {
				P = parseParametro();
			}
			accept(GrammarSymbols.RP);

			accept(GrammarSymbols.IS);

			while (this.currentToken.getKind() != GrammarSymbols.BEGIN) {
				D.add(parseDecVar());
				accept(GrammarSymbols.SEMICOLON);
			}

			accept(GrammarSymbols.BEGIN);
			do{
				C.add(parseComando());
				accept(GrammarSymbols.SEMICOLON);
			}while (this.currentToken.getKind() != GrammarSymbols.END);

			accept(GrammarSymbols.END);
			I2 = new ID(this.currentToken.getSpelling());
			accept(GrammarSymbols.ID);
			
			return new Procedure(I1, P, D, C, I2);

		}else if (this.currentToken.getKind() == GrammarSymbols.FUNCTION) {
			ID I1;
			Parametro P = null;
			Tipo T;
		    ArrayList<DecVar> D = new ArrayList<>();
			ArrayList<Comando> C = new ArrayList<>();
			ID I2;
			
			accept(GrammarSymbols.FUNCTION);
			I1 = new ID(this.currentToken.getSpelling());
			accept(GrammarSymbols.ID);

			accept(GrammarSymbols.LP);
			if (this.currentToken.getKind() == GrammarSymbols.ID)  {
				P = parseParametro();
			}
			accept(GrammarSymbols.RP);

			accept(GrammarSymbols.RETURN);
			T = parseTipo();
			accept(GrammarSymbols.IS);

			while (this.currentToken.getKind() != GrammarSymbols.BEGIN) {
				D.add(parseDecVar());
				accept(GrammarSymbols.SEMICOLON);
			}

			accept(GrammarSymbols.BEGIN);
			do{
				C.add(parseComando());
				accept(GrammarSymbols.SEMICOLON);
			}while (this.currentToken.getKind() != GrammarSymbols.END);

			accept(GrammarSymbols.END);
			I2 = new ID(this.currentToken.getSpelling());
			accept(GrammarSymbols.ID);
			
			return new Function(I1, P, T, D, C, I2);
		}else{
			throw new SyntacticException("Token inexperado.", this.currentToken);
		}

	}

	private Comando parseComando() throws LexicalException, SyntacticException {
		if (this.currentToken.getKind() == GrammarSymbols.ID) {
			Chamada C = parseChamada();
			
			return new ComandoChamada(C);
		}else if (this.currentToken.getKind() == GrammarSymbols.IF) {
			Expressao E = null;
			ArrayList<Comando> C1 = new ArrayList<>();
			ArrayList<Comando> C2 = new ArrayList<>();
			
			accept(GrammarSymbols.IF);
			E = parseExpressao();
			accept(GrammarSymbols.THEN);

			do{
				C1.add(parseComando());
				accept(GrammarSymbols.SEMICOLON);
			}while (this.currentToken.getKind() != GrammarSymbols.ELSE && this.currentToken.getKind() != GrammarSymbols.END);

			if (this.currentToken.getKind() == GrammarSymbols.ELSE) {
				accept(GrammarSymbols.ELSE);
				do{
					C2.add(parseComando());
					accept(GrammarSymbols.SEMICOLON);
				}while (this.currentToken.getKind() != GrammarSymbols.END);
			}

			accept(GrammarSymbols.END);
			accept(GrammarSymbols.IF);
			
			return new ComandoIf(E, C1, C2);
		}else if (this.currentToken.getKind() == GrammarSymbols.WHILE) {
			Expressao E;
			ArrayList<Comando> C = new ArrayList<>();
			
			accept(GrammarSymbols.WHILE);
			E = parseExpressao();
			accept(GrammarSymbols.LOOP);
			do{
				C.add(parseComando());
				accept(GrammarSymbols.SEMICOLON);
			}while (this.currentToken.getKind() != GrammarSymbols.END);
			accept(GrammarSymbols.END);
			accept(GrammarSymbols.LOOP);

			return new ComandoWhile(E, C);
		}else if (this.currentToken.getKind() == GrammarSymbols.BREAK) {
			Break B = new Break(this.currentToken.getSpelling());
			accept(GrammarSymbols.BREAK);
			
			return new ComandoBreak(B);
		}else if (this.currentToken.getKind() == GrammarSymbols.CONTINUE) {
			Continue C = new Continue(this.currentToken.getSpelling());
			accept(GrammarSymbols.CONTINUE);
			
			return new ComandoContinue(C);
		}else if (this.currentToken.getKind() == GrammarSymbols.RETURN) {
			Expressao E = null;
			
			accept(GrammarSymbols.RETURN);

			if (this.currentToken.getKind() != GrammarSymbols.SEMICOLON) {
				E = parseExpressao();
			}
			
			return new ComandoReturn(E);
		}else if (this.currentToken.getKind() == GrammarSymbols.SHOW) {
			accept(GrammarSymbols.SHOW);
			Expressao E = parseExpressao();
			
			return new ComandoShow(E);
		}else{
			throw new SyntacticException("Token inexperado.", this.currentToken);
		}
	}

	private Parametro parseParametro() throws SyntacticException, LexicalException {
		ID I;
		Tipo T;
		Parametro P = null;
		
		I 			= new ID(this.currentToken.getSpelling());
		I.variavel 	= true;
		
		accept(GrammarSymbols.ID);
		accept(GrammarSymbols.COLON);
		
		T = parseTipo();

		if (this.currentToken.getKind() == GrammarSymbols.COMMA) {
			accept(GrammarSymbols.COMMA);
			P = parseParametro();
		}
		
		return new Parametro(I, T, P); 
	}

	private DecVar parseDecVar() throws LexicalException, SyntacticException {
		ID I1;
		ArrayList<ID> I = new ArrayList<>();
		Tipo T;
		Atribuicao A = null;
		
		I1 			= new ID(this.currentToken.getSpelling());
		I1.variavel = true;
		
		accept(GrammarSymbols.ID);

		while (this.currentToken.getKind() == GrammarSymbols.COMMA) {
			accept(GrammarSymbols.COMMA);
			
			ID id 		= new ID( this.currentToken.getSpelling());
			id.variavel = true;
			
			I.add(id);
			accept(GrammarSymbols.ID);
		}

		accept(GrammarSymbols.COLON);
		T = parseTipo();

		if (this.currentToken.getKind() == GrammarSymbols.ASSIGN) {
			A = parseAtribuicao();
		}

		return new DecVar(I1, I, T, A);
	}

	private Atribuicao parseAtribuicao() throws SyntacticException, LexicalException {
		accept(GrammarSymbols.ASSIGN);
		Expressao E = parseExpressao();
		
		return new Atribuicao(E);
	}

	private Chamada parseChamada() throws SyntacticException, LexicalException {
		ID I = new ID(this.currentToken.getSpelling());
		accept(GrammarSymbols.ID);

		if (this.currentToken.getKind() == GrammarSymbols.LP) {
			Argumento A = parseArgumento();
			return new Chamada(I, A, null);
			
		}else if (this.currentToken.getKind() == GrammarSymbols.ASSIGN) {
			Atribuicao At = parseAtribuicao();
			return new Chamada(I, null, At);
			
		}else{
			throw new SyntacticException("Unexpected token.", this.currentToken);
		}

	}

	private Argumento parseArgumento() throws LexicalException, SyntacticException {
		Expressao E = null;
		ArrayList<Expressao> E1 = new ArrayList<>();
		
		accept(GrammarSymbols.LP);

		if (this.currentToken.getKind() != GrammarSymbols.RP) {
			E = parseExpressao();

			while (this.currentToken.getKind() == GrammarSymbols.COMMA) {
				accept(GrammarSymbols.COMMA);
				E1.add(parseExpressao());
			}
		}

		accept(GrammarSymbols.RP);
		
		return new Argumento(E, E1);
	}

	private Expressao parseExpressao() throws LexicalException, SyntacticException {
		ExpressaoArit E1	= null;
		OpLog O 			= null;
		ExpressaoArit E2 	= null;
		
		E1 = parseExpressaoArit();

		switch (this.currentToken.getKind()) {
		case EQUALS:
		case NEQUALS:
		case GT:
		case GTE:
		case LT:
		case LTE:
			O = new OpLog(this.currentToken.getSpelling());
			acceptIt();
			E2 = parseExpressaoArit();
			break;
		default:
			break;
		}

		return new Expressao(E1, E2, O);
	}

	private ExpressaoArit parseExpressaoArit() throws LexicalException, SyntacticException {
		Termo T1 			 = parseTermo();
		ArrayList<Termo> T2  = new ArrayList<>();
		ArrayList<OpArit1> O = new ArrayList<>();

		while (this.currentToken.getKind() == GrammarSymbols.ADD || this.currentToken.getKind() == GrammarSymbols.SUB) {
			O.add(new OpArit1(this.currentToken.getSpelling()));
			acceptIt();
			T2.add(parseTermo());
		}
		
		return new ExpressaoArit(T1, T2, O);
	}

	private Termo parseTermo() throws LexicalException, SyntacticException {
		Fator F1 			 = parseFator();
		ArrayList<Fator> F2  = new ArrayList<>();
		ArrayList<OpArit2> O = new ArrayList<>();

		while (this.currentToken.getKind() == GrammarSymbols.MULT || this.currentToken.getKind() == GrammarSymbols.DIV) {
			O.add(new OpArit2(this.currentToken.getSpelling()));
			acceptIt();
			F2.add(parseFator());
		}
		
		return new Termo(F1, F2, O);
	}

	private Fator parseFator() throws LexicalException, SyntacticException {
		if (this.currentToken.getKind() == GrammarSymbols.LP) {
			accept(GrammarSymbols.LP);
			Expressao E = parseExpressao();
			accept(GrammarSymbols.RP);
			
			return new FatorExpressao(E);
		} else if (this.currentToken.getKind() == GrammarSymbols.ID) {
			Argumento A		= null;
			Atribuicao At 	= null;
			ID I 			= new ID(this.currentToken.getSpelling());
			
			accept(GrammarSymbols.ID);

			if (this.currentToken.getKind() == GrammarSymbols.LP) {
				A = parseArgumento();
			}else if (this.currentToken.getKind() == GrammarSymbols.ASSIGN) {
				At = parseAtribuicao();
			}else{
				I.variavel = true;
			}
			
			return new FatorChamada(I,At,A);
		} else if (this.currentToken.getKind() == GrammarSymbols.TRUE || this.currentToken.getKind() == GrammarSymbols.FALSE || this.currentToken.getKind() == GrammarSymbols.NUMERO) {
			Literal L = parseLiteral();
			
			return new FatorLiteral(L);
		}else{
			throw new SyntacticException("Token inexperado.", this.currentToken);
		}

	}

	private Literal parseLiteral() throws LexicalException, SyntacticException {
		if (this.currentToken.getKind() == GrammarSymbols.NUMERO) {
			LiteralInt L = parseLiteralInt();
			
			return L;
		}else if (this.currentToken.getKind() == GrammarSymbols.TRUE || this.currentToken.getKind() == GrammarSymbols.FALSE) {
			LiteralBool L = parseLiteralBool();
			
			return L;
		}else{
			throw new SyntacticException("Token inexperado.", this.currentToken);
		}

	}

	private LiteralInt parseLiteralInt() throws SyntacticException, LexicalException {
		Numero N = new Numero(this.currentToken.getSpelling());
		accept(GrammarSymbols.NUMERO);	
		
		return new LiteralInt(N);
	}

	private LiteralBool parseLiteralBool() throws LexicalException, SyntacticException {
		if (this.currentToken.getKind() == GrammarSymbols.TRUE) {
			Bool B = new Bool(this.currentToken.getSpelling());
			accept(GrammarSymbols.TRUE);
			
			return new LiteralBool(B);
		}else if (this.currentToken.getKind() == GrammarSymbols.FALSE) {
			Bool B = new Bool(this.currentToken.getSpelling());
			accept(GrammarSymbols.FALSE);
			
			return new LiteralBool(B);
		}else{
			throw new SyntacticException("Token inexperado.", this.currentToken);
		}

	}

	private Tipo parseTipo() throws LexicalException, SyntacticException {
		if (this.currentToken.getKind() == GrammarSymbols.INTEGER) {
			TipoLiteral T = new TipoLiteral(this.currentToken.getSpelling());
			accept(GrammarSymbols.INTEGER);
			
			return new Tipo(T);
			
		}else if (this.currentToken.getKind() == GrammarSymbols.BOOLEAN) {
			TipoLiteral T = new TipoLiteral(this.currentToken.getSpelling());
			accept(GrammarSymbols.BOOLEAN);
			
			return new Tipo(T); 
		}else{
			throw new SyntacticException("Token inexperado.", this.currentToken);
		}

	}

}
