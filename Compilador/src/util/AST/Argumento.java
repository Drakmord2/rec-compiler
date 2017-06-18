package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Argumento extends AST{

	public Expressao E;
	public ArrayList<Expressao> E1;
	
	public Argumento (Expressao E, ArrayList<Expressao> E1) {
		this.E 	= E;
		this.E1 = E1;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Argumento\n";
		str += E != null ? E.toString(level+1) : "";
		
		if (E1 != null) {
			for (Expressao expressao : E1) {
				str += expressao.toString(level+1);
			}
		}
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitArgumento(this, arg);
	}
	
}
