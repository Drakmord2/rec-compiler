package util.AST;

import checker.SemanticException;
import checker.Type;
import checker.Visitor;

public class FatorLiteral extends Fator {
	public Type tipo;
	public Literal L;
	
	public FatorLiteral (Literal L) {
		this.L = L;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Fator\n";
		str += L.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitFatorLiteral(this, arg);
	}
	
}
