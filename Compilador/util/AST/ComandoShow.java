package util.AST;

import checker.SemanticException;
import checker.Visitor;

public class ComandoShow extends Comando{

	public Expressao E;
	
	public ComandoShow (Expressao E) {
		this.E = E;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Comando\n";
		str += E.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitComandoShow(this, arg);
	}
	
}
