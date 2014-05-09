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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
	private Context thisContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandablelist);
		thisContext = this;
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
					final CheckBox checkBox = (CheckBox) view.findViewById(R.id.follow);
					
					String selectedItem = (String) textView.getText();
					//logic to follow
					PivotalUserStory selectedStory = null;
					for(PivotalUserStory s : userStories){
						if(s.getTitle().equals(selectedItem)){
							selectedStory = s;
							break;
						}
					}
					String desc = selectedStory.getDescription();
					if (checkBox.isChecked()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
						builder.setMessage("Title: " + selectedStory + "\n\nUnfollow this story?");
						builder.setCancelable(false);
						builder.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								checkBox.setChecked(false);
								//TODO UNFOLLOW
							}
						});
						builder.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = builder.create();
						alertDialog.show();
					}
					else {
					AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
					builder.setMessage("Title: " + selectedStory + "\n\n" + "Desc: " + desc + "\n\nFollow this story?");
					builder.setCancelable(false);
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							checkBox.setChecked(true);
							//TODO SAVE STUFF
						}
					});
					builder.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog alertDialog = builder.create();
					alertDialog.show();
					}
					return false;
				}
			});
			Log.i(TAG, "last" + expListView.getChildCount());
		}

		catch(Exception ex)
		{
			ex.toString();
		}
	}
}
