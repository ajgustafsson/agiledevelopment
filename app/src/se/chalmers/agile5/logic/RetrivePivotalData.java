package se.chalmers.agile5.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.agile5.entities.pivotal.Label;
import se.chalmers.agile5.entities.pivotal.PivotalData;
import se.chalmers.agile5.entities.pivotal.PivotalResponse;
import se.chalmers.agile5.entities.pivotal.PivotalUnformattedResponse;
import se.chalmers.agile5.entities.pivotal.PivotalUserStory;
import android.os.AsyncTask;
import android.util.Log;

public abstract class RetrivePivotalData extends AsyncTask<String, Void, List<PivotalResponse>>{
	private final static String TAG = RetrivePivotalData.class.getSimpleName();
	private static final String JWToken = "cc123e5396cc4f521d8754187049cebb";
	private static final String HLToken = "eb1fc5f215b658333a34a995da84d097";
	protected static final String BASEURL = "https://www.pivotaltracker.com/services/v5/projects";
	private String token;
	private String url;
	
	private String project;
	//TODO: Retrieve using project not label
	
	public RetrivePivotalData(String token, String url){
		this.token = token;
		this.url = url;
	}
	
	protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
	
	
	protected List<PivotalResponse> doInBackground(String... params){
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Accept", "application/json");
		httpget.setHeader("Content-type", "application/json");
		httpget.addHeader("X-TrackerToken", JWToken);
		
		List<PivotalResponse> pivotalResponse = new ArrayList<PivotalResponse>();
		try {
			String setServerString;
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			setServerString = client.execute(httpget,responseHandler);
			Log.d(TAG, setServerString);
			pivotalResponse = parse(setServerString);
		} catch (Exception e) {
		}
		return pivotalResponse;
	}
	
	private ArrayList<PivotalResponse> parse(String jsonStr){
		ArrayList<PivotalResponse> responses = new ArrayList<PivotalResponse>(); 
		try{
			JSONArray objectsInJson = new JSONArray(jsonStr);
			for(int i = 0; i<objectsInJson.length(); i++){
				responses.add(new PivotalUnformattedResponse(objectsInJson.optJSONObject(i)));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return responses;
	}
	/*
	@Override
	protected ArrayList<UserStory> doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		//TODO: parameterize project
        //String url = "https://www.pivotaltracker.com/services/v5/projects/1043910/stories?date_format=millis&filter=label%3A" + Label;
        String url = "https://www.pivotaltracker.com/services/v5/projects";
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");
        httpget.setHeader("Content-type", "application/json");
        httpget.addHeader("X-TrackerToken", JWToken);
        
        ArrayList<UserStory> userStories = new ArrayList<UserStory>();
        try
        {
        	String SetServerString;
            
            // Create Request to server and get response
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = client.execute(httpget, responseHandler);
            Log.i("RPS", SetServerString);
            // Show response on activity 
            //return ParseUserStories(SetServerString, userStories);
            
            return userStories;
                    
         } catch(Exception ex) {
        	 UserStory uS = new UserStory();
        	 uS.setTitle(ex.toString());
        	 userStories.add(uS);
        	 return userStories;
         }
	}*/



}
