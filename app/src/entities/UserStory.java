package entities;

import java.util.ArrayList;

public class UserStory {
	
	private int ID;
	private String Title;
	private String Description;
	private ArrayList<Label> Labels = new ArrayList<Label>();
	
	public void setTitle(String title) {
		this.Title = title;
	}
	
	public String getTitle(){
		return Title;
	}

	public void setDescription(String description) {
		this.Description = description;
		
	}
	
	public String getDescription(){
		return Description;
	}

	public void setID(int id) {
		this.ID = id;
	}
	
	public int getID(){
		return ID;
	}
	
	public void addLabel(Label label){
		Labels.add(label);
	}
	
	public ArrayList<Label> getLabels(){
		return Labels;
	}
	

}
