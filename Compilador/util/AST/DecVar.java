package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class DecVar extends AST {
	public ID I1;
	public ArrayList<ID> I;
	public Tipo T;
	public Atribuicao A;
	
	public DecVar (ID I1, ArrayList<ID> I, Tipo T, Atribuicao A) {
		this.I1 = I1;
		this.I	= I;
		this.T 	= T;
		this.A 	= A;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Dec-var\n";
		str += I1.toString(level+1);
		
		for (ID id : I) {
			str += id.toString(level+1);
		}
		
		str += T.toString(level+1);
		str += A != null ? A.toString(level+1) : "";
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitDecVar(this, arg);
	}
	
}
