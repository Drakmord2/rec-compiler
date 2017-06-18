package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Type;
import checker.Visitor;

public class ExpressaoArit extends AST {

	public Type tipo;
	public Termo T1;
	public ArrayList<Termo> T2;
	public ArrayList<OpArit1> O;
	
	public ExpressaoArit (Termo T1, ArrayList<Termo> T2, ArrayList<OpArit1> O) {
		this.T1 = T1;
		this.O	= O;
		this.T2 = T2;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Expressao-arit\n";
		str += T1.toString(level+1);
		
		for (int i = 0; i < T2.size(); i++) {
			str += O.get(i).toString(level+1);
			str += T2.get(i).toString(level+1);
		}
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitExpressaoArit(this, arg);
	}
	
}
