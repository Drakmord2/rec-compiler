package util.AST;

import checker.Visitor;

public class ComandoBreak extends Comando {
	public Break B;

	public ComandoBreak (Break B) {
		this.B = B;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Comando\n";
		str += B.toString(level+1);
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) {
		return v.visitComandoBreak(this, arg);
	}
	
}
