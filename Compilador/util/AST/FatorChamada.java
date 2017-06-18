package util.AST;

import checker.SemanticException;
import checker.Visitor;

public class FatorChamada extends Fator {

	public ID I;
	public Atribuicao At;
	public Argumento A;
	
	public FatorChamada(ID I, Atribuicao At, Argumento A) {
		this.I 	= I;
		this.At = At;
		this.A 	= A;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Fator\n";
		str += I.toString(level+1);
		str += At != null ? At.toString(level+1) : "";
		str += A != null ? A.toString(level+1) : "";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitFatorChamada(this, arg);
	}
	
}
