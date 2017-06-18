package util.AST;

import checker.Visitor;

public class LiteralBool extends Literal {

	public Bool B;
	
	public LiteralBool (Bool B) {
		this.B = B;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Literal\n";
		str += B.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitLiteralBool(this, arg);
	}
	
}
