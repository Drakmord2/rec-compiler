package util.symbolsTable;

/**
 * Identification key class (scope and id pair)
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Key {

	// The identifier scope
	private int scope;
	// The identifier spelling
	private String id;
	
	/**
	 * Default constructor
	 * @param scope
	 * @param id
	 */
	public Key(int scope, String id) {
		this.scope = scope;
		this.id = id;
	}

	/**
	 * Gets the scope
	 * @return
	 */
	public int getScope() {
		return scope;
	}

	/**
	 * Gets the spelling
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Overrides the equals method  
	 */
	public boolean equals(Object obj) {
		// Two keys are the same if both have the same scope and spelling
		if (obj instanceof Key) {
			Key objKey = (Key) obj;
			if ( this.scope == objKey.getScope() ) {
				if ( this.id.equals(objKey.getId()) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns a hash code to the key
	 */
	public int hashCode() {
		return this.id.hashCode() ^ this.scope;
	}
	
}
