package util.AST;

import checker.SemanticException;
import checker.Type;
import checker.Visitor;

public class FatorExpressao extends Fator {
	public Type tipo;
	public Expressao E;
	
	public FatorExpressao(Expressao E) {
		this.E = E;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Fator\n";
		str += E.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitFatorExpressao(this, arg);
	}
	
}
