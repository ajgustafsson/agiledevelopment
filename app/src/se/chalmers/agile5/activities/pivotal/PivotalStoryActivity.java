package se.chalmers.agile5.activities.pivotal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.chalmers.agile5.R;
import se.chalmers.agile5.activities.BaseActivity;
import se.chalmers.agile5.adapter.ExpandableListAdapter;
import se.chalmers.agile5.entities.pivotal.PivotalResponse;
import se.chalmers.agile5.entities.pivotal.PivotalUserStory;
import se.chalmers.agile5.logic.RetriveUserStories;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PivotalStoryActivity extends BaseActivity{
	private final static String TAG = PivotalStoryActivity.class.getSimpleName();
	private ExpandableListView expListView;
	private ArrayList<String> pivotalStates;
	private ArrayList<PivotalUserStory> userStories;
	private Map<String, List<String>> storyMap;
	//TODO get from sharedpreferences
	private String token = "";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelist);
        
        userStories = new ArrayList<PivotalUserStory>();
        pivotalStates = new ArrayList<String>();
        storyMap = new LinkedHashMap<String, List<String>>();
        expListView = (ExpandableListView) findViewById(R.id.expandlist);
        
        try
        {
        	List<PivotalResponse> response = 
        			new RetriveUserStories(token, getIntent().getStringExtra("ProjectId")).execute().get();
        	Set<String> states = new LinkedHashSet<String>();
        	for(PivotalResponse r : response){
        		PivotalUserStory story = (PivotalUserStory) r;
        		userStories.add(story);
        		states.add(story.getState());
        	}
        	for(String state : states){
        		pivotalStates.add(state);
        		ArrayList<String> stateStories = new ArrayList<String>();
        		for(PivotalUserStory story : userStories){
        			if(state.equals(story.getState())){
        				stateStories.add(story.getTitle());
        			}
        		}
        		storyMap.put(state, stateStories);
        	}
        	
        	ExpandableListAdapter adapter = new ExpandableListAdapter(this, pivotalStates, storyMap);
        	expListView.setAdapter(adapter);
        	
        	expListView.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					RelativeLayout view = (RelativeLayout) v;
					TextView textView = (TextView) view.findViewById(R.id.child);
					String selectedItem = (String) textView.getText();
					PivotalUserStory selectedStory = null;
					for(PivotalUserStory s : userStories){
						if(s.getTitle().equals(selectedItem)){
							selectedStory = s;
							break;
						}
					}
					String desc = selectedStory.getDescription();
                    // Show Alert 
					//TODO: Insert logic for opening detail-view here
                    Toast.makeText(getApplicationContext(),
                      "Title : " + selectedStory + "\n" + " Description: " + desc, Toast.LENGTH_LONG)
                      .show();
					
					return false;
				}
			});
         }
       catch(Exception ex)
       {
    	   ex.toString();
       }
	}
}
