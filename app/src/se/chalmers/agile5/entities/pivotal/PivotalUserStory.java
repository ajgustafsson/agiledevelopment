package se.chalmers.agile5.entities.pivotal;

import java.util.ArrayList;
import java.util.List;

//TODO Write unit-test!
public final class PivotalUserStory extends PivotalData{
	
	private String description;
	private String state;
	private ArrayList<Label> labels;
	
	public PivotalUserStory(int id, String title, String description, 
			String state, List<Label> labels){
		super(id,title);
		this.description = description;
		this.state = state;
		labels = new ArrayList<Label>();
		for(Label l : labels){
			labels.add(l);
		}
	}
	
	public String getDescription(){
		return description;
	}
	public String getState(){
		return state;
	}
	
	/*
	public void addLabel(Label label){
		Labels.add(label);
	}*/
	
	public List<Label> getLabels(){
		return labels;
	}
	public boolean containsLabel(Label label){
		return labels.contains(label);
	}
	

}
