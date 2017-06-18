package util.symbolsTable;

import util.AST.AST;

/**
 * Identification table attribute class
 * The attribute is some AST node
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Attribute {

	// AST node
	private AST ast;
	
	/**
	 * Default constructor
	 * @param ast
	 */
	public Attribute(AST ast) {
		this.ast = ast;
	}

	/**
	 * Returns the ast node
	 * @return
	 */
	public AST getAst() {
		return ast;
	}
	
}
