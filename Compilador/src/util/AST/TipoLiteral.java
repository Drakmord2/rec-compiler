package util.AST;

import checker.Visitor;

public class TipoLiteral extends Terminal {

	public TipoLiteral (String spelling) {
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
		return v.visitTipoLiteral(this, arg);
	}
	
}
