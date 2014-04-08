package se.chalmers.agile5.entities.pivotal;

import java.util.ArrayList;
import java.util.List;

public final class PivotalUserStory extends PivotalData{
	
	private String description;
	private ArrayList<Label> labels;
	
	public PivotalUserStory(int id, String title, String description, List<Label> labels){
		super(id,title);
		this.description = description;
		labels = new ArrayList<Label>();
		for(Label l : labels){
			labels.add(l);
		}
	}
	
	public String getDescription(){
		return description;
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
