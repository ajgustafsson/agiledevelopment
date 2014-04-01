package se.chalmers.agile5;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        HttpClient client = new DefaultHttpClient();
        String url = "https://www.pivotaltracker.com/services/v5/projects/1043910/stories?date_format=millis&filter=label%3Aroadmap";
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("X-TrackerToken", "eb1fc5f215b658333a34a995da84d097");
        TextView textview = (TextView) findViewById(R.id.textView1);
        try
        {
                      String SetServerString = "";
            
                    // Create Request to server and get response
            
                    
                     ResponseHandler<String> responseHandler = new BasicResponseHandler();
                     SetServerString = client.execute(httpget, responseHandler);
           
                      // Show response on activity 
                     textview.setText(SetServerString);
                    
         }
       catch(Exception ex)
       {
                 textview.setText(ex.toString());
       }
        
    }
    
}
