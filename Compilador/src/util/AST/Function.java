package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Type;
import checker.Visitor;

public class Function extends CmdPrimario {

	public ID I1;
	public Parametro P;
	public ArrayList<Type> tipoParams;
	public Tipo T;
	public ArrayList<DecVar> D;
	public ArrayList<Comando> C;
	public ID I2;
	
	public Function (ID I1, Parametro P, Tipo T, ArrayList<DecVar> D, ArrayList<Comando> C, ID I2) {
		this.I1 = I1;
		this.I2 = I2;
		this.P 	= P;
		this.T 	= T;
		this.D 	= D;
		this.C	= C;
		this.I2 = I2;
	}
	
	@Override
	public String toString(int level) {
		String str = this.getSpaces(level);
		
		str += "Cdm-primario\n";
		str += I1.toString(level+1);
		str += P != null ? P.toString(level+1) : "";
		str += T.toString(level+1);
		
		for (DecVar decVar : D) {
			str += decVar.toString(level+1);
		}
		
		for (Comando comando : C) {
			str += comando.toString(level+1);
		}
		
		return str;
	}

	@Override
	public Object visit (Visitor v, Object arg) throws SemanticException {
		return v.visitFunction(this, arg);
	}
	
}
