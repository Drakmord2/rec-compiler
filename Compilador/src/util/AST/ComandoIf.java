package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ComandoIf extends Comando {

	public Expressao E;
	public ArrayList<Comando> C1;
	public ArrayList<Comando> C2;
	
	public ComandoIf (Expressao E, ArrayList<Comando> C1, ArrayList<Comando> C2) {
		this.E = E;
		this.C1 = C1;
		this.C2 = C2;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Comando\n";
		str += E.toString(level+1);
		
		for (Comando comando : C1) {
			str += comando.toString(level+1);
		}
		
		if (E != null) {
			for (Comando comando2 : C2) {
				str += comando2.toString(level+1);
			}
		}
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitComandoIf(this, arg);
	}
	
}
