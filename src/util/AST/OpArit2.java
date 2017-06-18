package util.AST;

import checker.Visitor;

public class OpArit2 extends Terminal {
	public Termo decl;

	public OpArit2 (String spelling) {
		this.spelling = spelling;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "OpArit2  [ " + this.spelling + " ]\n";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitOpArit2(this, arg);
	}
	
}
