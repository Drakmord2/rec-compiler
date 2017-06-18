package util.AST;

import checker.SemanticException;
import checker.Visitor;

public class Atribuicao extends AST {
	public Expressao E;

	public Atribuicao (Expressao E) {
		this.E = E;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Atribuicao\n";
		str += E.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitAtribuicao(this, arg);
	}
	
}
