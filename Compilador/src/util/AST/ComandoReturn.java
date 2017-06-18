package util.AST;

import checker.SemanticException;
import checker.Visitor;

public class ComandoReturn extends Comando {
	public Expressao E;
	
	public ComandoReturn (Expressao E) {
		this.E =E;
	}

	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Comando\n";
		str += E != null ? E.toString(level+1) : "";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitComandoReturn(this, arg);
	}
	
}
