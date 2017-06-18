package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Programa extends AST {
	public ArrayList<DecVar> D;
	public ArrayList<CmdPrimario> C;
	
	public Programa(ArrayList<DecVar> D, ArrayList<CmdPrimario> C) {
		this.D = D;
		this.C = C;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		str += "Programa\n";
		
		for (DecVar decVar : D) {
			str += decVar.toString(level+1);
		}
		
		for (CmdPrimario cmdPrimario : C) {
			str += cmdPrimario.toString(level+1);
		}
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitPrograma(this, arg);
	}
	
}
