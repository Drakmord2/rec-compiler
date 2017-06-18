package util.AST;

import checker.SemanticException;
import checker.Visitor;

public class Parametro extends AST {

	public ID I;
	public Tipo T;
	public Parametro P;
	
	public Parametro (ID I, Tipo T, Parametro P) {
		this.I = I;
		this.T = T;
		this.P = P;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Parametro\n";
		str += I.toString(level+1);
		str += T.toString(level+1);
		str += P != null ? P.toString(level+1) : "";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitParametro(this, arg);
	}
	
}
