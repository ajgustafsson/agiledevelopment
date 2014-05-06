package se.chalmers.agile5.entities.pivotal;

/**
 * Internal representation of a Project on Pivotal Tracker containing
 * the information about a project that the app needs to
 * 1) Display human readable label for the project in UI
 * 2) Retrieve user stories using id
 * @author Johannes Westlund
 *
 */
public final class PivotalProject extends PivotalData{	
	/**
	 * 
	 * @param id The Pivotal Tracker-id of the project
	 * @param name The Pivotal Tracker-name of the project
	 */
	public PivotalProject(int id, String title){
		super(id, title);
		
	}
}
