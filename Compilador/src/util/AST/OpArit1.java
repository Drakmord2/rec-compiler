package util.AST;

import checker.Visitor;

public class OpArit1 extends Terminal {
	public ExpressaoArit decl;

	public OpArit1 (String spelling) {
		this.spelling = spelling;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "OpArit1 [ " + this.spelling + " ]\n";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitOpArit1(this, arg);
	}
	
}
