package util.symbolsTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import util.AST.AST;
import checker.SemanticException;

/**
 * Identification table class
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class IdentificationTable {

	// The table maps a key to an attribute
	private HashMap<Key, Attribute> table;
	// Current table scope
	private int currentScope;
	
	/**
	 * Default constructor
	 */
	public IdentificationTable() {
		// Creates the mapping table
		this.table = new HashMap<Key, Attribute>();

		// Puts in the table each language reserved word
		this.table.put(new Key(0, "show"), null);
		this.table.put(new Key(0, "begin"), null);
		this.table.put(new Key(0, "end"), null);
		this.table.put(new Key(0, "if"), null);
		this.table.put(new Key(0, "then"), null);
		this.table.put(new Key(0, "else"), null);
		this.table.put(new Key(0, "while"), null);
		this.table.put(new Key(0, "loop"), null);
		this.table.put(new Key(0, "procedure"), null);
		this.table.put(new Key(0, "function"), null);
		this.table.put(new Key(0, "return"), null);
		this.table.put(new Key(0, "break"), null);
		this.table.put(new Key(0, "continue"), null);
		this.table.put(new Key(0, "is"), null);
		this.table.put(new Key(0, "global"), null);
		this.table.put(new Key(0, "Integer"), null);
		this.table.put(new Key(0, "Boolean"), null);
		this.table.put(new Key(0, "True"), null);
		this.table.put(new Key(0, "False"), null);

		// Initializes currentScope to 0 (global)
		this.currentScope = 0;
	}
	
	/**
	 * Opens a scope (increments currentScope)
	 */
	public void openScope() {
		this.currentScope++;
	}
	
	/**
	 * Closes a scope (decreases currentScope and updates identification table)
	 */
	public void closeScope() {
		ArrayList<Key> currentScopeKeys = new ArrayList<Key>();
		// Gets the keys of entries for current scope
		Set<Key> keys = this.table.keySet();
		for (Key key : keys) {
			if ( key.getScope() == this.currentScope ) {
				currentScopeKeys.add(key);
			}
		}
		// Removes each entry for current scope
		for (Key key2 : currentScopeKeys) {
			this.table.remove(key2);
		}
		// Decreases current scope
		this.currentScope--;
	}
	
	/**
	 * Adds an entry to the identification table
	 * @param id
	 * @param node
	 * @throws SemanticException
	 */
	public void enter(String id, AST node) throws SemanticException {
		boolean hasFound = false;
		// Verifies if in the current scope already exists an identifier with the same spelling
		Key key = new Key(this.currentScope, id);
		if ( this.table.containsKey(key) ) {
			hasFound = true;
		}
		
		// If does not exist
		if ( hasFound == false ) {
			// Adds the new entry
			this.table.put(key, new Attribute(node));
		// If exists
		} else {
			// Raises a semantic exception
			throw new SemanticException("Identificador [ " + id + " ] já declarado no escopo atual.");
		}
	}
	
	/**
	 * Verifies if exists an identifier (in some scope level) and returns its attribute 
	 * @param id
	 * @return
	 */
	public AST retrieve(String id) {
		// For each scope level
		for (int i=this.currentScope; i>=0; i--) {
			Key key = new Key(i, id);
			// Verifies if the identifier exists
			if ( this.table.containsKey(key) ) {
				// Returns the attribute of the first entry found (for the highest scope)
				return this.table.get(key).getAst();
			}
		}
		return null;
	}
	
	/**
	 * Verifies if exists an identifier (in some scope level) 
	 * @param id
	 * @return
	 */
	public boolean containsKey(String id) {
		// For each scope level
		for (int i=this.currentScope; i>=0; i--) {
			Key key = new Key(i, id);
			// Verifies if the identifier exists
			if ( this.table.containsKey(key) ) {
				// Returns true if it exists
				return true;
			}
		}
		// Returns false if it does not exist
		return false;
	}
	
	public int getScope() {
		return this.currentScope;
	}
}
