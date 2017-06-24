package util.AST;

import checker.SemanticException;
import checker.Visitor;

public class ComandoChamada extends Comando {
	public Chamada C;
	
	public ComandoChamada (Chamada C) {
		this.C = C;
	}

	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Comando-chamada\n";
		str += C.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitComandoChamada(this, arg);
	}
	
}
