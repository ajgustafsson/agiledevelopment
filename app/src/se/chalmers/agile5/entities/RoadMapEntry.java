package se.chalmers.agile5.entities;

import java.sql.Timestamp;

/**
 * Logical representation of task entry in Roadmap.
 * 
 * Created by Armin on 4/29/14 and cowritten with Johannes Vestlund.
 */
public class RoadMapEntry {

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
}
