package util.AST;

import checker.SemanticException;
import checker.Type;
import checker.Visitor;

public class ID extends Terminal {
	public AST decl;
	public boolean variavel;
	public Type tipo = null;

	public ID(String spelling) {
		this.spelling = spelling;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "ID  [ " + this.spelling + " ] ";
		str += this.variavel ? "( variavel )" : "";
		str += this.tipo != null ? "( "+this.tipo.toString()+" )\n" : "\n";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitID(this, arg);
	}
	
}
