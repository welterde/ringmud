package ring.nrapi.data;

/**
 * Interface representing something persistable to an XMLDataStore.
 * @author projectmoon
 *
 */
public interface Persistable {
	/**
	 * Returns true if this Persistable object is to be stored as an update to
	 * to an existing document, rather than as an insert.
	 * @return true or false
	 */
	public boolean storeAsUpdate();
	
	/**
	 * Sets whether or not to store this Persistable as an update or insert.
	 * @param val
	 */
	public void setStoreAsUpdate(boolean val);
	
	/**
	 * Makes this Persistable's ID unique so it will not clash with other documents
	 * already in the datastore. This is accomplished by appending a UUID to the ID,
	 * generated by Java's UUID facility.
	 */
	public void makeUnique();
	
	/**
	 * Transforms this Persistable into an XML fragment. It does not
	 * contain the header information or the &lt;ring&gt; root element.
	 * @return an XML String representation of this Persistable.
	 */
	public String toXML();
	
	/**
	 * Transforms this Persistable into a full XML documemnt with a &lt;ring&gt;
	 * root element.
	 * @return The XML string.
	 */
	public String toXMLDocument();
	
	public Persistable getParent();
	public Persistable getRoot();
	
	/**
	 * Gets the ID of this Persistable.
	 * @return the id
	 */
	public String getID();
	
	/**
	 * Sets the id of this Persistable.
	 * @param id
	 */
	public void setID(String id);
	
	/**
	 * Gets the root document ID associated with this object.
	 * @return the ID
	 */
	public String getDocumentID();
	
	public void setDocumentID(String docID);
	
	/**
	 * Tells whether or not this Persistable is referential. A referential
	 * Persistable does not store any data but its ID. Other objects can
	 * reference this ID and load the actual object from the datastore.
	 * A Persistable should be referential when being stored as part of
	 * a larger object. Persistables nested in larger Persistables cannot
	 * be stored referentially during server use. They can only be imported.
	 * That is, an Item in an Inventory will be stored as its own entry when
	 * the server is running, instead of an Item reference. When using the
	 * import module, the Item can be a reference, though.
	 * @return true or false
	 */
	public boolean isReferential();
	
	/**
	 * Sets the referentiality of this Persistable. A referential
	 * Persistable does not store any data but its ID. Other objects
	 * can reference the ID and load the actual object from the datastore. 
	 * A Persistable should be referential when being stored as part of a
	 * larger object. Persistables nested in larger Persistables cannot
	 * be stored referentially during server use. They can only be imported.
	 * That is, an Item in an Inventory will be stored as its own entry when
	 * the server is running, instead of an Item reference. When using the
	 * import module, the Item can be a reference, though.
	 * @param val
	 */
	public void setReferential(boolean val);
}