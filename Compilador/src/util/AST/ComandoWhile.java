package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ComandoWhile extends Comando{

	public Expressao E;
	public ArrayList<Comando> C;
	
	public ComandoWhile (Expressao E, ArrayList<Comando> C) {
		this.E = E;
		this.C = C;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Comando\n";
		str += E.toString(level+1);
		
		for (Comando comando : C) {
			str += comando.toString(level+1);
		}
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitComandoWhile(this, arg);
	}
	
}
