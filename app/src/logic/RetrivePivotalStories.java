package logic;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class RetrivePivotalStories extends AsyncTask<String, Void, String>{
	
	protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }

	@Override
	protected String doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
        String url = "https://www.pivotaltracker.com/services/v5/projects/1043910/stories?date_format=millis&filter=label%3Aroadmap";
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("X-TrackerToken", "eb1fc5f215b658333a34a995da84d097");
        try
        {
                      String SetServerString = "";
            
                    // Create Request to server and get response
            
                    
                     ResponseHandler<String> responseHandler = new BasicResponseHandler();
                     SetServerString = client.execute(httpget, responseHandler);
           
                      // Show response on activity 
                     return SetServerString;
                    
         }
       catch(Exception ex)
       {
                 return ex.toString();
       }
	}

}
