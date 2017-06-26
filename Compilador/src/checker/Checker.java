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
	
	public IdentificationTable idTable 	= null;
	public boolean hasReturn 			= false;
	public Type returnType 				= null;
	public int whileScope 				= 0;
	
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
		
		boolean entry = false;
		for (CmdPrimario cmd : prog.C) {
			cmd.visit(this, null);
			
			if (cmd instanceof Procedure) {
				String id = ((Procedure) cmd).I1.spelling;
				
				if (id.equals("Main")) {
					entry = true;
				}
			}
		}
		
		if (! entry) {
			throw new SemanticException("Programa não possui procedimento [ Main ].");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitProcedure(Procedure proc, Object arg) throws SemanticException {
		String id1 = proc.I1.spelling;
		String id2 = proc.I2.spelling;
		
		if (! id1.equals(id2)) {
			throw new SemanticException("Fechamento incorreto do Procedimento [ "+id1+" ]");
		}
		
		this.idTable.enter(proc.I1.spelling, proc);
		
		proc.I1.tipo = Type.empty;
		proc.I1.visit(this, null);

		this.idTable.openScope();

		if (proc.P != null) {
			ArrayList<Type> tipoParams;
			tipoParams = (ArrayList<Type>) proc.P.visit(this, null);
			
			proc.tipoParams = tipoParams;
		}
		
		for (DecVar dec : proc.D) {
			dec.visit(this, null);
		}
		
		this.returnType = Type.empty;
		for (Comando cmd: proc.C) {
			cmd.visit(this, null);
			
			this.unconditional(cmd);
			
			this.checkReturn(cmd);
		}
		
		if (! this.hasReturn) {
			throw new SemanticException("Procedimento [ "+id1+" ] não possui comando [ return ]");
		}
		
		proc.I2.visit(this, null);

		this.returnType = null;
		this.hasReturn 	= false;
		this.idTable.closeScope();
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitFunction(Function func, Object arg) throws SemanticException {
		String id1 = func.I1.spelling;
		String id2 = func.I2.spelling;
		
		if (! id1.equals(id2)) {
			throw new SemanticException("Fechamento incorreto da Função [ "+id1+" ]");
		}

		this.idTable.enter(func.I1.spelling, func);
		func.I1.visit(this, null);

		this.idTable.openScope();

		if (func.P != null) {
			ArrayList<Type> tipoParams;
			tipoParams = (ArrayList<Type>) func.P.visit(this, null);
			
			func.tipoParams = tipoParams;
		}
		
		Type tipo = (Type) func.T.visit(this, null);
		func.I1.tipo = tipo;
		
		for (DecVar dec : func.D) {
			dec.visit(this, null);
		}
		
		this.returnType = tipo;
		for (Comando cmd: func.C) {
			cmd.visit(this, null);
			
			this.unconditional(cmd);
			
			this.checkReturn(cmd);
		}
		
		if (! this.hasReturn) {
			throw new SemanticException("Função [ "+id1+" ] não possui comando [ return ]");
		}
		
		func.I2.visit(this, null);
		
		this.returnType = null;
		this.hasReturn 	= false;
		this.idTable.closeScope();
		
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
			
			this.unconditional(cmd1);
			this.checkReturn(cmd1);
		}
		
		for (Comando cmd2 : cmdIf.C2) {
			cmd2.visit(this, null);
			
			this.unconditional(cmd2);
			this.checkReturn(cmd2);
		}
		
		return null;
	}

	@Override
	public Object visitComandoWhile(ComandoWhile cmdWhile, Object arg) throws SemanticException {
		Type tipoExp = (Type) cmdWhile.E.visit(this, null);
		
		if (! tipoExp.equals(Type.bool)) {
			throw new SemanticException("Expressão não booleana em comando WHILE.");
		}
		
		this.whileScope++;
		
		for (Comando cmd : cmdWhile.C) {
			cmd.visit(this, null);
			
			this.checkReturn(cmd);
		}
		
		this.whileScope--;
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

	@SuppressWarnings("unchecked")
	@Override
	public Object visitParametro(Parametro param, Object arg) throws SemanticException {
		ArrayList<Type> tipos 	= new ArrayList<>();
		Type tipo 				= (Type) param.T.visit(this, null);
		
		param.I.tipo 	 = tipo;
		param.I.variavel = true;
		this.idTable.enter(param.I.spelling, param);
		
		tipos.add(tipo);
		if (param.P != null) {
			ArrayList<Type> t;
			t = (ArrayList<Type>) param.P.visit(this, null);
			
			tipos.addAll(t);
		}
		
		return tipos;
	}

	@Override
	public Object visitDecVar(DecVar decVar, Object arg) throws SemanticException {
		Type tipo = (Type) decVar.T.visit(this, null);
		
		decVar.I1.tipo 		= tipo;
		decVar.I1.variavel  = true;
		this.idTable.enter(decVar.I1.spelling, decVar);
		
		if (decVar.A != null) {
			Type tipo2 = (Type) decVar.A.visit(this, null);
			
			this.checkAtribuicao(decVar.I1, tipo, tipo2);
		}
		
		for (ID id : decVar.I) {
			id.tipo 	= tipo;
			id.variavel = true;
			this.idTable.enter(id.spelling, decVar);
		}
		
		return null;
	}

	@Override
	public Object visitAtribuicao(Atribuicao atrib, Object arg) throws SemanticException {
		Type tipo = (Type) atrib.E.visit(this, null);
		
		return tipo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitChamada(Chamada chamada, Object arg) throws SemanticException {
		ID id 			= (ID) chamada.I.visit(this, null);
		chamada.I.tipo 	= id == null ? Type.error : id.tipo;
		Type tipo		= (Type) chamada.I.tipo;
		
		if (chamada.A != null) {
			ArrayList<Type> tipoArgs;
			
			tipoArgs = (ArrayList<Type>) chamada.A.visit(this, null);
			
			this.checkArgumento(id, tipoArgs);
		}
		
		if (chamada.At != null) {
			Type tipo2 = (Type) chamada.At.visit(this, null);
			 
			this.checkAtribuicao(id, tipo, tipo2);
		}
		
		return tipo;
	}

	@Override
	public Object visitArgumento(Argumento argumento, Object arg) throws SemanticException {
		ArrayList<Type> tipos 	= new ArrayList<>();
		
		if (argumento.E != null) {
			Type tipo = (Type) argumento.E.visit(this, null);
			
			tipos.add(tipo);
			
			for (Expressao exp : argumento.E1) {
				Type tipo2 = (Type) exp.visit(this, null);
				tipos.add(tipo2);
			}
			
			return tipos;
		}
		
		return tipos;
	}

	@Override
	public Object visitExpressao(Expressao exp, Object arg) throws SemanticException {
		Type tipo1 = (Type) exp.E1.visit(this, null);
		
		if (exp.E2 != null) {
			exp.O.visit(this, null);
			Type tipo2 = (Type) exp.E2.visit(this, null);
			
			this.checkExpressao(tipo1, tipo2);
			
			if (! exp.O.spelling.equals("=") && ! exp.O.spelling.equals("/=") && ( ! tipo1.equals(Type.integer) || ! tipo2.equals(Type.integer)) ) {
				throw new SemanticException("Tipos de operandos incompatíveis para operador [ "+exp.O.spelling+" ]");
			}
			
			tipo1 = Type.bool;
		}
		
        exp.tipo = tipo1;
		return tipo1;
	}

	@Override
	public Object visitExpressaoArit(ExpressaoArit expArit, Object arg) throws SemanticException {
		Type tipo1 = (Type) expArit.T1.visit(this, null);
		
		if (expArit.T2 != null) {
			int size = expArit.T2.size();
			
			for (int i = 0; i < size; i++) {
				expArit.O.get(i).visit(this, null);
				Type tipo2 = (Type) expArit.T2.get(i).visit(this, null);
				
				this.checkExpressao(tipo1, tipo2);
			}
		}
		
		expArit.tipo = tipo1;
		return tipo1;
	}

	@Override
	public Object visitTermo(Termo termo, Object arg) throws SemanticException {
		Type tipo1 = (Type) termo.F1.visit(this, null);
		
		if (termo.F2 != null) {
			int size = termo.F2.size();
			
			for (int i = 0; i < size; i++) {
				termo.O.get(i).visit(this, null);
				Type tipo2 = (Type) termo.F2.get(i).visit(this, null);
				
				this.checkExpressao(tipo1, tipo2);
			}
		}
		
		termo.tipo = tipo1;
		return tipo1;
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

	@SuppressWarnings("unchecked")
	@Override
	public Object visitFatorChamada(FatorChamada fatorChm, Object arg) throws SemanticException {
		ID id 			= (ID) fatorChm.I.visit(this, null);
		fatorChm.I.tipo = id == null ? Type.error : id.tipo;
		Type tipo		= (Type) fatorChm.I.tipo;
		
		if (fatorChm.A != null) {
			ArrayList<Type> tipoArgs;
			tipoArgs = (ArrayList<Type>) fatorChm.A.visit(this, null);
			
			this.checkArgumento(id, tipoArgs);
		}
		
		if (fatorChm.At != null) {
			Type tipo2 = (Type) fatorChm.At.visit(this, null);
			
			this.checkAtribuicao(id, tipo, tipo2);
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
		
		if (id.decl instanceof Procedure) {
			return ((Procedure) id.decl).I1;
		}
		
		if (id.decl instanceof Function) {
			return ((Function) id.decl).I1;
		}
		
		if (id.decl instanceof Parametro) {
			return ((Parametro) id.decl).I;
		}
		
		if (id.decl instanceof DecVar) {
			DecVar decl = ((DecVar) id.decl);
			
			if (decl.I1.spelling.equals(id.spelling)) {
				return decl.I1;
			}
			
			for (ID i : decl.I) {
				if (i.spelling.equals(id.spelling)) {
					return i;
				}
			}
		}
		
		return null;
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
	
	private void checkExpressao(Type tipo1, Type tipo2) throws SemanticException {
		if (! tipo1.equals(tipo2) && (tipo1.equals(Type.bool) || tipo2.equals(Type.bool)) ) {
			throw new SemanticException("Tipos de operandos incompatíveis. [ "+tipo1.toString()+" ] - [ "+tipo2.toString()+" ]");
		}
	}
	
	private void unconditional(Comando cmd) throws SemanticException {
		if ( (cmd instanceof ComandoBreak || cmd instanceof ComandoContinue) && this.whileScope == 0) {
			throw new SemanticException("Uso inválido de [ break ] ou [ continue ].");
		}
	}
	
	private void checkReturn(Comando cmd) throws SemanticException {
		Type tipo = this.returnType;
		
		if (cmd instanceof ComandoReturn) {
			Expressao exp = ((ComandoReturn) cmd).E;
			
			if (exp == null && ! tipo.equals(Type.empty)) {
				throw new SemanticException("Tipo de retorno incompatível. "
						+ "Esperado: [ "+tipo.toString()+" ] | Verificado: [ "+Type.empty.toString()+" ]");
			}
			
			if (exp != null && ! exp.tipo.equals(tipo) ) {
				throw new SemanticException("Tipo de retorno incompatível. "
						+ "Esperado: [ "+tipo.toString()+" ] | Verificado: [ "+exp.tipo.toString()+" ]");
			}
			
			this.hasReturn = true;
		}
	}
	
	private void checkAtribuicao(ID id, Type tipo, Type tipo2) throws SemanticException {
		if (! tipo.equals(tipo2)) {
			String var 		= id != null ? id.spelling : "";
			String tipoStr 	= tipo != null ? tipo.toString() : "";
			
			throw new SemanticException("Atribuição com tipo incompatíveis. Var: "+var+" | Tipo: "+tipoStr);
		}
		
		if (! id.variavel) {
			throw new SemanticException("Atribuição inválida. [ "+id.spelling+" ] não é variável.");
		}	
	}
	
	private void checkArgumento(ID id, ArrayList<Type> tipoArgs) throws SemanticException {
		ArrayList<Type> tipoParams = new ArrayList<>();
		String nome = "";
		
		if (id.variavel) {
			throw new SemanticException("Variável [ "+id.spelling+" ] não pode ser "
					+ "chamada como [ procedimento ] ou [ função ].");
		}
		
		if (id.decl instanceof Procedure) {
			Procedure proc 	= ((Procedure) id.decl);
			nome 			= proc.I1.spelling;
			
			if (proc.tipoParams != null) {
				tipoParams.addAll(proc.tipoParams);
			}
		}
		
		if (id.decl instanceof Function) {
			Function func 	= ((Function) id.decl);
			nome 			= func.I1.spelling;

			if (func.tipoParams != null) {
				tipoParams.addAll(func.tipoParams);
			}
		}

		if (tipoArgs.size() != tipoParams.size()) {
			throw new SemanticException("Número incorreto de argumentos em chamada de [ "+nome+" ].");
		}
		
		for (int i = 0; i < tipoParams.size(); i++) {
			Type t1 = tipoParams.get(i);
			Type t2 = tipoArgs.get(i);
			
			if (! t1.equals(t2)) {
				throw new SemanticException("Argumento de tipo inválido em chamada de [ "+nome+" ]. "
						+ "\n            Esperado: [ "+t1.toString()+" ] | Verificado: [ "+t2.toString()+" ] ");
			}
		}
		
	}
	
}
