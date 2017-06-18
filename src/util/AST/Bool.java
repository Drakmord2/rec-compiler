package util.AST;

import checker.Visitor;

public class Bool extends Terminal {

	public Bool (String spelling) {
		this.spelling = spelling;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "[ " + this.spelling + " ]\n";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitBool(this, arg);
	}
	
}
