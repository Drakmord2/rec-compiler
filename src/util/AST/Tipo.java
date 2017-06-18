package util.AST;

import checker.Visitor;

public class Tipo extends AST {

	public TipoLiteral T;
	
	public Tipo (TipoLiteral T) {
		this.T = T;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Tipo\n";
		str += T.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitTipo(this, arg);
	}
	
}
