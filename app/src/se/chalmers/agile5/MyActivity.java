package se.chalmers.agile5;

import logic.RetrivePivotalStories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*
        TextView textview = (TextView) findViewById(R.id.textView1);
        
        try
        {
        	textview.setText(new RetrivePivotalStories().execute().get());         
         }
       catch(Exception ex)
       {
                 textview.setText(ex.toString());
       }*/
        
    }
    
    public void getUserStories(View view){
    	Intent intent = new Intent(this, UserStoriesActivity.class);
    	startActivity(intent);
    }
    
}
