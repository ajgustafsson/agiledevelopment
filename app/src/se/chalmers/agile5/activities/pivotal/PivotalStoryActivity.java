package se.chalmers.agile5.activities.pivotal;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.pivotal.PivotalResponse;
import se.chalmers.agile5.entities.pivotal.PivotalUserStory;
import se.chalmers.agile5.logic.RetrivePivotalData;
import se.chalmers.agile5.logic.RetriveProjects;
import se.chalmers.agile5.logic.RetriveUserStories;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PivotalStoryActivity extends Activity{
	private final static String TAG = PivotalStoryActivity.class.getSimpleName();
	ListView listView;
	ArrayList<PivotalUserStory> userStories;
	//TODO get from sharedpreferences
	String token = "";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.userstories);
        
        userStories = new ArrayList<PivotalUserStory>();
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        
        try
        {
        	List<PivotalResponse> response = 
        			new RetriveUserStories(token, getIntent().getStringExtra("ProjectId")).execute().get();
        	for(PivotalResponse r : response){
        		userStories.add((PivotalUserStory) r);
        		Log.i(TAG, ((PivotalUserStory) r).getTitle());
        	}
    
            ArrayAdapter<PivotalUserStory> adapter = new ArrayAdapter<PivotalUserStory>(this,
              android.R.layout.simple_list_item_1, android.R.id.text1, userStories);
            
            // Assign adapter to ListView
            listView.setAdapter(adapter); 
            
            // ListView Item Click Listener
            listView.setOnItemClickListener(new OnItemClickListener() {
 
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                     int position, long id) {
                    
                   // ListView Clicked item value
                   String  itemValue = (String) listView.getItemAtPosition(position);
                   
                   String desc = userStories.get(position).getDescription();
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
