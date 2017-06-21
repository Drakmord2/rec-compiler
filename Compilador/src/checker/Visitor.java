package checker;

import util.AST.Argumento;
import util.AST.Atribuicao;
import util.AST.Bool;
import util.AST.Break;
import util.AST.Chamada;
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
import util.AST.FatorChamada;
import util.AST.FatorExpressao;
import util.AST.FatorLiteral;
import util.AST.Function;
import util.AST.ID;
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

public interface Visitor {

	//Programa
	public Object visitPrograma (Programa prog, Object arg) throws SemanticException;
	
	//Cmd-primario
	public Object visitProcedure	(Procedure proc, Object arg) throws SemanticException;
	public Object visitFunction 	(Function func, Object arg) throws SemanticException;
	
	//Comando
	public Object visitComandoIf 		(ComandoIf cmdIf, Object arg) throws SemanticException;
	public Object visitComandoWhile 	(ComandoWhile cmdWhile, Object arg) throws SemanticException;
	public Object visitComandoBreak 	(ComandoBreak cmdBreak, Object arg);
	public Object visitComandoContinue 	(ComandoContinue cmdContinue, Object arg);
	public Object visitComandoReturn 	(ComandoReturn cmdReturn, Object arg) throws SemanticException;
	public Object visitComandoShow 		(ComandoShow cmdShow, Object arg) throws SemanticException;
	public Object visitComandoChamada	(ComandoChamada cmdChamada, Object arg) throws SemanticException;
	
	//Parametro
	public Object visitParametro (Parametro param, Object arg) throws SemanticException;
	
	//Dec-var
	public Object visitDecVar (DecVar decVar, Object arg) throws SemanticException;
	
	//Atribuicao
	public Object visitAtribuicao (Atribuicao atrib, Object arg) throws SemanticException;
	
	//Chamada
	public Object visitChamada (Chamada chamada, Object arg) throws SemanticException;
	
	//Argumento
	public Object visitArgumento (Argumento argumento, Object arg) throws SemanticException;
	
	//Expressao
	public Object visitExpressao (Expressao exp, Object arg) throws SemanticException;
	
	//Expressao-arit
	public Object visitExpressaoArit (ExpressaoArit expArit, Object arg) throws SemanticException;
	
	//Termo
	public Object visitTermo (Termo termo, Object arg) throws SemanticException;
	
	//Fator
	public Object visitFatorExpressao	(FatorExpressao fatorExp, Object arg) throws SemanticException;
	public Object visitFatorLiteral 	(FatorLiteral fatorLit, Object arg) throws SemanticException;
	public Object visitFatorChamada 	(FatorChamada fatorChm, Object arg) throws SemanticException;
	
	//Literal
	public Object visitLiteralBool	(LiteralBool litBool, Object arg);
	public Object visitLiteralInt 	(LiteralInt litInt, Object arg);
	
	//Tipo
	public Object visitTipo 		(Tipo tipo, Object arg);
	public Object visitTipoLiteral	(TipoLiteral tipoLit, Object arg);
	
	//Terminais
	public Object visitID 		(ID id, Object arg) throws SemanticException;
	public Object visitBool 	(Bool bool, Object arg);
	public Object visitNumero 	(Numero numero, Object arg);
	public Object visitBreak 	(Break brk, Object arg);
	public Object visitContinue	(Continue cont, Object arg);
	public Object visitOpArit1 	(OpArit1 opArit, Object arg);
	public Object visitOpArit2 	(OpArit2 opArit, Object arg);
	public Object visitOpLog 	(OpLog opLog, Object arg);
	
}
