package logic;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Label;
import entities.UserStory;

import android.os.AsyncTask;

public class RetrivePivotalStories extends AsyncTask<String, Void, ArrayList<UserStory>>{
	
	private String Label;
	
	public RetrivePivotalStories(String label){
		this.Label = label;
	}
	
	protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }

	@Override
	protected ArrayList<UserStory> doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
        String url = "https://www.pivotaltracker.com/services/v5/projects/1043910/stories?date_format=millis&filter=label%3A" + Label;
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");
        httpget.setHeader("Content-type", "application/json");
        httpget.addHeader("X-TrackerToken", "eb1fc5f215b658333a34a995da84d097");
        ArrayList<UserStory> userStories = new ArrayList<UserStory>();
        try
        {
        	String SetServerString;
            
            // Create Request to server and get response
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = client.execute(httpget, responseHandler);
                     
            // Show response on activity 
            return ParseUserStories(SetServerString, userStories);
                    
         } catch(Exception ex) {
        	 UserStory uS = new UserStory();
        	 uS.setTitle(ex.toString());
        	 userStories.add(uS);
        	 return userStories;
         }
	}

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
		
	}

}
