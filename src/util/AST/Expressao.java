package util.AST;

import checker.SemanticException;
import checker.Type;
import checker.Visitor;

public class Expressao extends AST {
	public Type tipo;
	public ExpressaoArit E1, E2;
	public OpLog O;
	
	public Expressao (ExpressaoArit E1, ExpressaoArit E2, OpLog O) {
		this.E1	= E1;
		this.O 	= O;
		this.E2 = E2;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Expressao";
		str += this.tipo != null ? " ( "+this.tipo.toString()+" )\n" : "\n";
		str += E1.toString(level+1);
		str += O != null ? O.toString(level+1) : "";
		str += E2 != null ? E2.toString(level+1) : "";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitExpressao(this, arg);
	}
	
}
