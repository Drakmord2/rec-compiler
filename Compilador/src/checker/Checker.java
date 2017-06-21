package checker;

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
import util.symbolsTable.IdentificationTable;

import java.util.ArrayList;

import checker.SemanticException;

public final class Checker implements Visitor {
	
	public IdentificationTable idTable = null;
	
	public Checker() {
		this.idTable = new IdentificationTable();
	}
	
	public Programa check(Programa prog) throws SemanticException {
		prog.visit(this, null);
		
		return prog;
	}

	@Override
	public Object visitPrograma(Programa prog, Object arg) throws SemanticException {
		for (DecVar dec : prog.D) {
			dec.visit(this, null);
		}
		
		for (CmdPrimario cmd : prog.C) {
			cmd.visit(this, null);
		}

		return null;
	}

	@Override
	public Object visitProcedure(Procedure proc, Object arg) throws SemanticException {
		String id1 = proc.I1.spelling;
		String id2 = proc.I2.spelling;
		
		if (! id1.equals(id2)) {
			throw new SemanticException("Fechamento incorreto do Procedimento [ "+id1+" ]");
		}

		this.idTable.enter(proc.I1.spelling, proc.I1);
		proc.I1.visit(this, null);

		this.idTable.openScope();

		if (proc.P != null) {
			proc.P.visit(this, null);
		}
		
		for (DecVar dec : proc.D) {
			dec.visit(this, null);
		}
		
		for (Comando cmd: proc.C) {
			cmd.visit(this, null);
		}
		
		proc.I2.visit(this, null);
		
		this.idTable.closeScope();
		
		proc.I1.tipo = Type.empty;
		
		return null;
	}

	@Override
	public Object visitFunction(Function func, Object arg) throws SemanticException {
		String id1 = func.I1.spelling;
		String id2 = func.I2.spelling;
		
		if (! id1.equals(id2)) {
			throw new SemanticException("Fechamento incorreto da Função [ "+id1+" ]");
		}

		this.idTable.enter(func.I1.spelling, func.I1);
		func.I1.visit(this, null);

		this.idTable.openScope();

		if (func.P != null) {
			func.P.visit(this, null);
		}
		
		Type tipo = (Type) func.T.visit(this, null);
		
		for (DecVar dec : func.D) {
			dec.visit(this, null);
		}
		
		for (Comando cmd: func.C) {
			cmd.visit(this, null);
		}
		
		func.I2.visit(this, null);
		
		this.idTable.closeScope();

		func.I1.tipo = tipo;
		
		return null;
	}

	@Override
	public Object visitComandoIf(ComandoIf cmdIf, Object arg) throws SemanticException {
		
		Type tipoExp = (Type) cmdIf.E.visit(this, null);
		
		if (! tipoExp.equals(Type.bool)) {
			throw new SemanticException("Expressão não booleana em comando IF.");
		}
		
		for (Comando cmd1 : cmdIf.C1) {
			cmd1.visit(this, null);
		}
		
		for (Comando cmd2 : cmdIf.C2) {
			cmd2.visit(this, null);
		}
		
		return null;
	}

	@Override
	public Object visitComandoWhile(ComandoWhile cmdWhile, Object arg) throws SemanticException {
		Type tipoExp = (Type) cmdWhile.E.visit(this, null);
		
		if (! tipoExp.equals(Type.bool)) {
			throw new SemanticException("Expressão não booleana em comando WHILE.");
		}
		
		for (Comando cmd : cmdWhile.C) {
			cmd.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitComandoBreak(ComandoBreak cmdBreak, Object arg) {
		cmdBreak.B.visit(this, null);
		return null;
	}

	@Override
	public Object visitComandoContinue(ComandoContinue cmdContinue, Object arg) {
		cmdContinue.C.visit(this, null);
		return null;
	}

	@Override
	public Object visitComandoReturn(ComandoReturn cmdReturn, Object arg) throws SemanticException {
		if (cmdReturn.E != null) {
			cmdReturn.E.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitComandoShow(ComandoShow cmdShow, Object arg) throws SemanticException {
		cmdShow.E.visit(this, null);
		return null;
	}

	@Override
	public Object visitComandoChamada(ComandoChamada cmdChamada, Object arg) throws SemanticException {
		cmdChamada.C.visit(this, null);
		return null;
	}

	@Override
	public Object visitParametro(Parametro param, Object arg) throws SemanticException {
		Type tipo = (Type) param.T.visit(this, null);
		
		param.I.tipo = tipo;
		this.idTable.enter(param.I.spelling, param.I);
		
		if (param.P != null) {
			param.P.visit(this, null);
		}
		
		return null;
	}

	@Override
	public Object visitDecVar(DecVar decVar, Object arg) throws SemanticException {
		Type tipo 	= (Type) decVar.T.visit(this, null);
		
		if (decVar.A != null) {
			Type tipoAt = (Type) decVar.A.visit(this, null);
			
			if (! tipo.equals(tipoAt)) {
				String var 		= decVar.I1 != null ? decVar.I1.spelling : "";
				String tipoStr 	= tipo != null ? tipo.toString() : "";
				throw new SemanticException("Atribuição com tipo incompatíveis. Var: "+var+" | Tipo: "+tipoStr);
			}
		}
		
		decVar.I1.tipo = tipo;
		this.idTable.enter(decVar.I1.spelling, decVar.I1);
		
		for (ID id : decVar.I) {
			id.tipo = tipo;
			this.idTable.enter(id.spelling, id);
		}
		
		return null;
	}

	@Override
	public Object visitAtribuicao(Atribuicao atrib, Object arg) throws SemanticException {
		Type tipo = (Type) atrib.E.visit(this, null);
		
		return tipo;
	}

	@Override
	public Object visitChamada(Chamada chamada, Object arg) throws SemanticException {
		ID id 			= (ID) chamada.I.visit(this, null);
		chamada.I.tipo 	= id == null ? Type.error : id.tipo;
		Type tipo		= (Type) chamada.I.tipo;
		
		if (chamada.A != null) {
			chamada.A.visit(this, null);
		}
		
		if (chamada.At != null) {
			Type tipo2 = (Type) chamada.At.visit(this, null);
			 
			if (! tipo.equals(tipo2)) {
				String var 		= id != null ? id.spelling : "";
				String tipoStr 	= tipo != null ? tipo.toString() : "";
				throw new SemanticException("Atribuição com tipo incompatíveis. Var: "+var+" | Tipo: "+tipoStr);
			}
		}
		
		return tipo;
	}

	@Override
	public Object visitArgumento(Argumento argumento, Object arg) throws SemanticException {
		if (argumento.E != null) {
			ArrayList<Type> tipos 	= new ArrayList<>();
			Type tipo 				= (Type) argumento.E.visit(this, null);
			
			tipos.add(tipo);
			
			for (Expressao exp : argumento.E1) {
				Type tipo2 = (Type) exp.visit(this, null);
				tipos.add(tipo2);
			}
			
			return tipos;
		}
		
		return Type.empty;
	}

	@Override
	public Object visitExpressao(Expressao exp, Object arg) throws SemanticException {
		Type tipoExp = (Type) exp.E1.visit(this, null);
		
		if (exp.E2 != null) {
			exp.O.visit(this, null);
			Type tipoExp2 = (Type) exp.E2.visit(this, null);
			
			String tipo1 = tipoExp.toString();
			String tipo2 = tipoExp2.toString();
			
			if (! tipo1.equals(tipo2)) {
				throw new SemanticException("Tipos de operandos incompatíveis. [ "+tipoExp.toString()+" ] - [ "+tipoExp2.toString()+" ]");
			}
			
			tipoExp = Type.bool;
		}
		
        exp.tipo = tipoExp;
		return tipoExp;
	}

	@Override
	public Object visitExpressaoArit(ExpressaoArit expArit, Object arg) throws SemanticException {
		Type tipoExp = (Type) expArit.T1.visit(this, null);
		
		if (expArit.T2 != null) {
			
			int size = expArit.T2.size();
			for (int i = 0; i < size; i++) {
				expArit.O.get(i).visit(this, null);
				Type tipoExp2 = (Type) expArit.T2.get(i).visit(this, null);
				
				String tipo1 = tipoExp.toString();
				String tipo2 = tipoExp2.toString();
				
				if (! tipo1.equals(tipo2)) {
					throw new SemanticException("Tipos de operandos incompatíveis. [ "+tipoExp.toString()+" ] - [ "+tipoExp2.toString()+" ]");
				}
			}
		}
		
		expArit.tipo = tipoExp;
		return tipoExp;
	}

	@Override
	public Object visitTermo(Termo termo, Object arg) throws SemanticException {
		Type tipoTermo = (Type) termo.F1.visit(this, null);
		
		if (termo.F2 != null) {
			int size = termo.F2.size();
			for (int i = 0; i < size; i++) {
				termo.O.get(i).visit(this, null);
				Type tipoTermo2 = (Type) termo.F2.get(i).visit(this, null);
				
				if (! tipoTermo.equals(Type.integer) || ! tipoTermo2.equals(Type.integer)) {
					throw new SemanticException("Tipos de operandos incompatíveis.");
				}
			}
		}
		
		termo.tipo = tipoTermo;
		return tipoTermo;
	}

	@Override
	public Object visitFatorExpressao(FatorExpressao fatorExp, Object arg) throws SemanticException {
		Type tipo = (Type) fatorExp.E.visit(this, null);
		
		fatorExp.tipo = tipo;
		return tipo;
	}

	@Override
	public Object visitFatorLiteral(FatorLiteral fatorLit, Object arg) throws SemanticException {
		Type tipo = (Type) fatorLit.L.visit(this, null);
		
		fatorLit.tipo = tipo;
		return tipo;
	}

	@Override
	public Object visitFatorChamada(FatorChamada fatorChm, Object arg) throws SemanticException {
		ID id 			= (ID) fatorChm.I.visit(this, null);
		fatorChm.I.tipo = id == null ? Type.error : id.tipo;
		Type tipo		= (Type) fatorChm.I.tipo;
		
		if (fatorChm.A != null) {
			fatorChm.A.visit(this, null);
		}
		
		if (fatorChm.At != null) {
			Type tipo2 = (Type) fatorChm.At.visit(this, null);
			
			if (tipo != tipo2) {
				throw new SemanticException("Atribuição com tipo incompatíveis. Var: "+id.spelling+" | Tipo: "+tipo.toString());
			}
		}
		
		return tipo;
	}

	@Override
	public Object visitLiteralBool(LiteralBool litBool, Object arg) {
		litBool.B.visit(this, null);
		
		return Type.bool;
	}

	@Override
	public Object visitLiteralInt(LiteralInt litInt, Object arg) {
		litInt.N.visit(this, null);
		
		return Type.integer;
	}

	@Override
	public Object visitTipo(Tipo tipo, Object arg) {
		String tipo2 = (String) tipo.T.visit(this, null);

		if (tipo2.equals("Integer")) {
			return Type.integer;
		}
		
		return Type.bool;
	}

	@Override
	public Object visitTipoLiteral(TipoLiteral tipoLit, Object arg) {
		this.idTable.containsKey(tipoLit.spelling);
		return tipoLit.spelling;
	}

	@Override
	public Object visitID(ID id, Object arg) throws SemanticException {
		id.decl = this.idTable.retrieve(id.spelling);
		
		if (id.decl == null) {
			throw new SemanticException("Identificador não declarado. [ "+id.spelling+" ]");
		}
		
		return id.decl;
	}

	@Override
	public Object visitBool(Bool bool, Object arg) {
		this.idTable.containsKey(bool.spelling);
		return null;
	}

	@Override
	public Object visitNumero(Numero numero, Object arg) {
		return null;
	}

	@Override
	public Object visitBreak(Break brk, Object arg) {
		this.idTable.containsKey(brk.spelling);
		return null;
	}

	@Override
	public Object visitContinue(Continue cont, Object arg) {
		this.idTable.containsKey(cont.spelling);
		return null;
	}

	@Override
	public Object visitOpArit1(OpArit1 opArit, Object arg) {
		opArit.decl = (ExpressaoArit) this.idTable.retrieve(opArit.spelling);
		
		return opArit.decl;
	}

	@Override
	public Object visitOpArit2(OpArit2 opArit, Object arg) {
		opArit.decl = (Termo) this.idTable.retrieve(opArit.spelling);
		
		return opArit.decl;
	}

	@Override
	public Object visitOpLog(OpLog opLog, Object arg) {
		opLog.decl = (Expressao) this.idTable.retrieve(opLog.spelling);
		
		return opLog.decl;
	}
	
}
