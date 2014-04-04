package se.chalmers.agile5;

import java.util.ArrayList;
import java.util.List;

import entities.UserStory;

import logic.RetrivePivotalStories;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserStoriesActivity extends Activity{
	ListView listView;
	ArrayList<UserStory> userStories;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.userstories);
        
        userStories = new ArrayList<UserStory>();
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        
        try
        {
        	userStories = new RetrivePivotalStories(getIntent().getStringExtra("Label")).execute().get();  
        	// Defined Array values to show in ListView
            List<String> titles = new ArrayList<String>();
        	for(int i=0; i<userStories.size(); i++) {
            	titles.add(userStories.get(i).getTitle());
            }
    
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1, android.R.id.text1, titles);
            
            // Assign adapter to ListView
            listView.setAdapter(adapter); 
            
            // ListView Item Click Listener
            listView.setOnItemClickListener(new OnItemClickListener() {
 
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                     int position, long id) {
                    
                   // ListView Clicked item index
                   int itemPosition = position;
                   
                   // ListView Clicked item value
                   String  itemValue = (String) listView.getItemAtPosition(position);
                   
                   String desc = userStories.get(itemPosition).getDescription();
                    // Show Alert 
                    Toast.makeText(getApplicationContext(),
                      "ListItem : " +itemValue + " Description: " + desc, Toast.LENGTH_LONG)
                      .show();
                 
                  }
    
             }); 

         }
       catch(Exception ex)
       {
    	   ex.toString();
       }
	}

}
