package util.AST;

import checker.SemanticException;
import checker.Visitor;

public class Chamada extends AST {
	public ID I;
	public Argumento A;
	public Atribuicao At;
	
	public Chamada (ID I, Argumento A, Atribuicao At) {
		this.I 	= I;
		this.A 	= A;
		this.At = At;
	}

	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Chamada\n";
		str += I.toString(level+1);
		str += A != null ? A.toString(level+1) : "";
		str += At != null ? At.toString(level+1) : "";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitChamada(this, arg);
	}
	
}
