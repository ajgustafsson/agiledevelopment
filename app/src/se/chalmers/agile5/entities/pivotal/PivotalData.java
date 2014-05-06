package se.chalmers.agile5.entities.pivotal;

/**
 * Baseclass for all formatted variants of PivotalResponses.
 * @author Johannes Westlund
 *
 */
public abstract class PivotalData extends PivotalResponse {
	private int id;
	private String title;
	
	public PivotalData(int id, String title){
		this.id = id;
		this.title = title;
	}
	
	public int getId(){
		return id;
	}
	public String getTitle(){
		return title;
	}
	
	/**
	 * In general if the object is instance of this type and 
	 * id and title matches, objects are considered to be equal.
	 */
	public boolean equals(Object object){
		if(object instanceof PivotalData){
			PivotalData r = (PivotalData) object;
			if(id == r.id && title.equals(r.title))
				return true;
		}
		return false;
	}
	
	/**
	 * Human readable string is in the general case the pivotal title/name that
	 * was supplied during instantiation.
	 */
	public String toString(){
		return getTitle();
	}
}
