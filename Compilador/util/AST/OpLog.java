package util.AST;

import checker.Visitor;

public class OpLog extends Terminal {
	public Expressao decl;

	public OpLog(String spelling) {
		this.spelling = spelling;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "OpLog  [ " + this.spelling + " ]\n";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitOpLog(this, arg);
	}
	
}
