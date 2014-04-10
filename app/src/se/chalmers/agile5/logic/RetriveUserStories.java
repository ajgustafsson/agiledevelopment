package se.chalmers.agile5.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import se.chalmers.agile5.entities.pivotal.Label;
import se.chalmers.agile5.entities.pivotal.PivotalProject;
import se.chalmers.agile5.entities.pivotal.PivotalResponse;
import se.chalmers.agile5.entities.pivotal.PivotalUnformattedResponse;
import se.chalmers.agile5.entities.pivotal.PivotalUserStory;

public class RetriveUserStories extends RetrivePivotalData {
	private final static String TAG = RetriveUserStories.class.getSimpleName();
	
	public RetriveUserStories(String token, String project){
		super(token, BASEURL + "/"+project+"/stories");
		Log.d(TAG, BASEURL + "/"+project +"/stories" );
	}
	
	@Override
	protected List<PivotalResponse> doInBackground(String... params) {
		ArrayList<PivotalResponse> userStories = new ArrayList<PivotalResponse>();
		List<PivotalResponse> responses = super.doInBackground(params);
		Log.d(TAG, responses.size()+"");
		for(PivotalResponse r : responses){
			if(r instanceof PivotalUnformattedResponse){
				PivotalUnformattedResponse ur = (PivotalUnformattedResponse) r;
				JSONObject userStory = ur.getJsonObject();
				int id = userStory.optInt("id", 0);
				String name = userStory.optString("name", "undefined");
				String description = userStory.optString("description", "undefined");
				String state = userStory.optString("current_state", "undefined");
				Log.d(TAG,state);
				PivotalUserStory story = new PivotalUserStory(id, name, description, state, null);
				
				userStories.add(story);	
			}
		}
		return userStories;
	}
	/*
	private ArrayList<UserStory> ParseUserStories(String setServerString, ArrayList<UserStory> userStories) {
		
		try {
			JSONArray userStoriesInJSON = new JSONArray(setServerString);
			if(userStoriesInJSON.length() == 0){
				UserStory us = new UserStory();
				us.setTitle("*Nothing found*");
				us.setDescription("");
				us.setID(0);
				userStories.add(us);
				return userStories;
			}
			for(int i = 0; i < userStoriesInJSON.length(); i++ ){
				JSONObject storyInJSON = userStoriesInJSON.optJSONObject(i);
				UserStory userStory = new UserStory();
			
				userStory.setID(storyInJSON.optInt("id", 0));
				userStory.setTitle(storyInJSON.optString("name"));
				userStory.setDescription(storyInJSON.optString("description"));
				
				JSONArray jArray = storyInJSON.getJSONArray("labels");
            	
				for(int j = 0; j < jArray.length(); j++){
            		JSONObject labelInJSON = jArray.getJSONObject(j);
            		Label label = new Label();
            		label.setID(labelInJSON.optInt("id", 0));
            		label.setName(labelInJSON.optString("name", "unknown"));
            		userStory.addLabel(label);
            	}
            userStories.add(userStory);
			}
            
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userStories;
		
	}*/
}
