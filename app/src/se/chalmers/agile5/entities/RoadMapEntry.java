package se.chalmers.agile5.entities;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Logical representation of task entry in Roadmap.
 * 
 * Created by Armin on 4/29/14 and cowritten with Johannes Vestlund.
 */
public class RoadMapEntry implements Cloneable {
	private static final String TAG = RoadMapEntry.class.getSimpleName();

    private String title;
    private String description;
    private EntryType type;
    private Timestamp creationDate;
    private boolean done;
    
    public RoadMapEntry(String title) {
        this(title, "", EntryType.CUSTOM);
    }

    public RoadMapEntry(String title, String description, EntryType type) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.done = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EntryType getType(){
    	return type;
    }
    
    public Timestamp getCreationDate(){
    	return creationDate;
    }
    
    public void setDone(){
    	done = true;
    }
    
    public boolean isDone(){
    	return done;
    }

    @Override
    public String toString() {
        return title;
    }
    
    public RoadMapEntry copy(){
    	try {
			return (RoadMapEntry) this.clone();
		} catch (CloneNotSupportedException e) {
			Log.w(TAG, "Attempted to clone unclonable");
		}
    	return null;
    }
    
    /**
     * Two objects are considered equal when title, description, 
     * creationDate and done are all equal. 
     */
    @Override
    public boolean equals(Object o) {
    	if(o instanceof RoadMapEntry){
    		RoadMapEntry other = (RoadMapEntry) o;
    		if(title.equals(other.title) &&
    				description.equals(other.description) &&
    				type.equals(other.type) &&
    				creationDate.equals(other.creationDate) &&
    				done == other.done){
    			return true;
    		}
    	}
    	return false;
    }
    
    
    /**
     * Reconstructs a RoadMapEntry given a valid JSON-representation.
     * @param JSONentry
     * @return
     * @throws JSONException
     */
    public static RoadMapEntry createFromJSON(JSONObject JSONentry) throws JSONException{
    	String title = JSONentry.getString("title");
    	String description = JSONentry.optString("description");
    	EntryType type = EntryType.valueOf(JSONentry.getString("type"));
    	Timestamp timestamp = Timestamp.valueOf(JSONentry.getString("timestamp"));
    	boolean done = JSONentry.getBoolean("done");
    	
    	RoadMapEntry entry = new RoadMapEntry(title, description, type);
    	entry.creationDate = timestamp;
    	entry.done = done;
    	return entry;
    }
    
    public JSONObject toJSON() throws JSONException{
    	JSONObject entry = new JSONObject();
    	entry.accumulate("title", title);
    	entry.accumulate("description", description);
    	entry.accumulate("type", type.name());
    	entry.accumulate("timestamp", creationDate.toString());
    	entry.accumulate("done", done);
    	return entry;
    }
}
