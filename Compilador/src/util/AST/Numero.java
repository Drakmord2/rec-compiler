package util.AST;

import checker.Visitor;

public class Numero extends Terminal {

	public Numero (String spelling) {
		this.spelling = spelling;
	}

	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Numero  [ " + this.spelling + " ]\n";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitNumero(this, arg);
	}
	
}
