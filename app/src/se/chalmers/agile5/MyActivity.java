package se.chalmers.agile5;

import logic.RetrivePivotalStories;

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
        
        TextView textview = (TextView) findViewById(R.id.textView1);
        
        try
        {
        	textview.setText(new RetrivePivotalStories().execute().get());         
         }
       catch(Exception ex)
       {
                 textview.setText(ex.toString());
       }
        
    }
    
}
