package se.chalmers.agile5.activities;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.UserStory;

import se.chalmers.agile5.logic.INotificationHandler;
import se.chalmers.agile5.logic.NotificationHandler;
import se.chalmers.agile5.logic.RetrivePivotalStories;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserStoriesActivity extends BaseActivity{
	ListView listView;
	ArrayList<UserStory> userStories;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.userstories);
        
        userStories = new ArrayList<UserStory>();
        listView = (ListView) findViewById(R.id.list);
        
        try
        {
        	userStories = new RetrivePivotalStories(getIntent().getStringExtra("Label")).execute().get();  
        	
            List<String> titles = new ArrayList<String>();
            for (UserStory userStory : userStories) {
                titles.add(userStory.getTitle());
            }
    
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1, android.R.id.text1, titles);
            
            listView.setAdapter(adapter); 
            
            listView.setOnItemClickListener(new OnItemClickListener() {
 
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                     int position, long id) {
                   String  itemValue = (String) listView.getItemAtPosition(position);
                   
                   String desc = userStories.get(position).getDescription(); 
                    Toast.makeText(getApplicationContext(),
                      "ListItem : " +itemValue + " Description: " + desc, Toast.LENGTH_LONG)
                      .show();   
                  }  
             }); 

         } catch(Exception ex){
    	   ex.toString();
         }
        
        INotificationHandler notificationHandler = new NotificationHandler();
        notificationHandler.DisplayNotification(this, PivotalActivity.class, 
        		userStories.get(0).getTitle(), userStories.get(0).getDescription(),
        		Integer.toString(userStories.size()));
	}

}
