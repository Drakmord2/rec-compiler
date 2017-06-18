package util.AST;

import checker.Visitor;

public class LiteralInt extends Literal {
	public Numero N;
	
	public LiteralInt (Numero N) {
		this.N = N;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Literal\n";
		str += N.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitLiteralInt(this, arg);
	}
	
}
