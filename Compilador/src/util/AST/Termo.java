package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Type;
import checker.Visitor;

public class Termo extends AST {
	
	public Type tipo;
	public Fator F1;
	public ArrayList<Fator> F2;
	public ArrayList<OpArit2> O;
	
	public Termo (Fator F1, ArrayList<Fator> F2, ArrayList<OpArit2> O) {
		this.F1 = F1;
		this.O 	= O;
		this.F2	= F2;
	}

	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Termo\n";
		str += F1.toString(level+1);
		
		for (int i = 0; i < F2.size(); i++) {
			str += O.get(i).toString(level+1);
			str += F2.get(i).toString(level+1);
		}
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitTermo(this, arg);
	}
	
}
