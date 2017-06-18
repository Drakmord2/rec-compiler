package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * AST class
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public abstract class AST {

	public abstract Object visit (Visitor v, Object arg) throws SemanticException;
	
	public String getSpaces( int level) {
		StringBuffer str = new StringBuffer();
		while( level > -1 ) {
			str.append("-");
			level--;
		}

		str.append(" ");
		return str.toString();
	}
	
	public abstract String toString( int level);
	
}
