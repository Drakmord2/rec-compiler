package util.AST;

import checker.Visitor;

public class ComandoContinue extends Comando {
	public Continue C;
	
	public ComandoContinue (Continue C) {
		this.C = C;
	}

	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Comando\n";
		str += C.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitComandoContinue(this, arg);
	}
	
}
