package se.chalmers.agile5.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import se.chalmers.agile5.R;
import se.chalmers.agile5.adapter.FileStorageAdapter;
import se.chalmers.agile5.entities.EntryType;
import se.chalmers.agile5.entities.RoadMapEntry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *	Allows user to manage a Roadmap. This activity provide the overview 
 *  and functionality to add and select item to view in detail-view. The
 *  roadmap is persisted and kept as a JSON-formatted string in 
 *  SharedPreferences 
 */
public class RoadMapActivity extends BaseActivity {
	private static final String[] macroStrings = {"Class","Func", "Algo", "Exception", "Custom"};
	private static final String TAG = RoadMapActivity.class.getSimpleName();
    static final int DETAIL_ACTIVITY_KEY = 0;
    static final int RESULT_DETAIL_CHANGED = 1;
    private static Context thisContext;
    static final String INTENT_TITLE_RESULT = "title_intent";
    static final String INTENT_DESC_RESULT = "description_intent";
    static final String INTENT_INDEX_RESULT = "index_intent";

    private List<RoadMapEntry> roadMapList = new LinkedList<RoadMapEntry>();
    private ListView roadMapListView;
    private ListView macroList;
    private Button userStoryButton;
    
    private SharedPreferences jsonData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonData = getSharedPreferences("jsonData", MODE_PRIVATE);
        thisContext = this;
        setContentView(R.layout.roadmap_activity);
        roadMapListView = (ListView)findViewById(R.id.roadMapTasksListView);
        macroList = (ListView)findViewById(R.id.roadMapMacroListView);
        initMacroList();
        
        userStoryButton = (Button)findViewById(R.id.userStoryButton);
        userStoryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
        		FileStorageAdapter getPivotalStory = new FileStorageAdapter(getApplicationContext());
        		getPivotalStory.setFileListKey("pivotal");
        		ArrayList<String> pivotalStory = getPivotalStory.retrieveFileList();
        		if (pivotalStory.get(0) != null && !pivotalStory.get(0).equals("")) {
        			String story = "Title: " + pivotalStory.get(0) + "\n\nDesc: " + pivotalStory.get(1);
					AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
					builder.setMessage(story)
					       .setCancelable(false)
					       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                //do things
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
        		} else {
        			String noStory = "No story selected \nSelect a story from pivotal tracker";
					Toast.makeText(getApplicationContext(), noStory, Toast.LENGTH_SHORT).show();
        		}
			}
        	
        });
        
        roadMapListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        roadMapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RoadMapEntry roadMapEntry = roadMapList.get(position);
                if(roadMapEntry.isDone()){
                    roadMapEntry.setDone(false);
                    roadMapList.remove(position);
                    roadMapList.add(0, roadMapEntry);
                } else {
                    roadMapEntry.setDone(true);
                    roadMapList.remove(position);
                    roadMapList.add(roadMapEntry);
                }
                updateTaskList();
            }
        });

        registerForContextMenu(roadMapListView);

        macroList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	if(position == macroList.getCount() - 1){
                    DialogFragment dialog = new CreateTaskDialogFragment(EntryType.CUSTOM);
                    dialog.show(getFragmentManager(), "Create custom task");
            	} else if (position == 0) {
                    DialogFragment dialog = new CreateTaskDialogFragment(EntryType.CLASS);
                	dialog.show(getFragmentManager(), "Create a class macro");
                } else if (position == 1) {
                    DialogFragment dialog = new CreateTaskDialogFragment(EntryType.FUNCTION);
                    dialog.show(getFragmentManager(), "Function macro");
                } else if (position == 2) {
                    DialogFragment dialog = new CreateTaskDialogFragment(EntryType.ALGORITHM);
                    dialog.show(getFragmentManager(), "Algorithm macro");
                } else if (position == 3) {
                    DialogFragment dialog = new CreateTaskDialogFragment(EntryType.EXCEPTION);
                    dialog.show(getFragmentManager(), "Exception macro");
                }
            }
        });
    }

    public void startTaskEditing(int position){
        Intent detailIntent = new Intent(RoadMapActivity.this, RoadMapDetailActivity.class);
        RoadMapEntry entryClicked = roadMapList.get(position);
        detailIntent.putExtra("title", entryClicked.getTitle());
        detailIntent.putExtra("description", entryClicked.getDescription());
        detailIntent.putExtra("index", roadMapList.indexOf(entryClicked));
        startActivityForResult(detailIntent, DETAIL_ACTIVITY_KEY);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.roadMapTasksListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.roadmap_task_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.editTaskMenu:
                startTaskEditing(info.position);
                return true;
            case R.id.deleteTaskMenu:
                roadMapList.remove(info.position);
                updateTaskList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initMacroList(){
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                macroStrings);
        macroList.setAdapter(arrayAdapter);
    }

    private void updateTaskList(){
        ArrayAdapter<RoadMapEntry> arrayAdapter = new ArrayAdapter<RoadMapEntry>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                roadMapList

        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                if (roadMapList.get(position).isDone()) {
                    text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    text.setTextColor(Color.GREEN);
                } else {
                    text.setPaintFlags(text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    text.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        roadMapListView.setAdapter(arrayAdapter);
        for (int i = 0; i < roadMapListView.getCount(); i++) {
            roadMapListView.setItemChecked(i, roadMapList.get(i).isDone());
        }
    }

    @Override
    public void onPause() {
    	super.onPause();
    	try {
			saveRoadmap();
		} catch (JSONException e) {
			Log.w(TAG, "JSONException occured during save of the list of tasks");
			e.printStackTrace();
		}
    }

	@Override
    public void onResume() {
    	super.onResume();
    	try {
			loadRoadmap();
		} catch (JSONException e) {
			Log.w(TAG, "JSONException during load of roadmap");
			e.printStackTrace();
		}
    	updateTaskList();
    }

	private void saveRoadmap() throws JSONException {
    	JSONArray saveData = new JSONArray();
    	for(RoadMapEntry task : roadMapList){
    		saveData.put(task.toJSON());
    	}
    	SharedPreferences.Editor editor = jsonData.edit();
    	editor.putString("data", saveData.toString());
    	editor.commit();
	}
	
	private void loadRoadmap() throws JSONException {
		String data = jsonData.getString("data", null);
		if(data != null){
			JSONArray tasklist = new JSONArray(data);
			List<RoadMapEntry> roadmap = new ArrayList<RoadMapEntry>();
			for(int i=0; i<tasklist.length(); i++){
				JSONObject JSONtask = tasklist.getJSONObject(i);
				roadmap.add(RoadMapEntry.createFromJSON(JSONtask));
			}
			roadMapList = roadmap;
		}
	}
	
    @SuppressLint("ValidFragment")
	public class CreateTaskDialogFragment extends DialogFragment {
    	private EntryType macroType;
        private String titleHint = "Title";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.custom_task_dialog, null));
            switch (macroType) {
                case CUSTOM:
                    builder.setMessage(R.string.dialog_new_custom_task);
                    break;
                case ALGORITHM:
                    builder.setMessage(R.string.implement_algorithm);
                    titleHint = getString(R.string.macro_algo_hint);
                    break;
                case CLASS:
                    titleHint = getString(R.string.macro_class_hint);
                    builder.setMessage(R.string.create_a_new_class);
                    break;
                case EXCEPTION:
                    titleHint = getString(R.string.macro_exception_hint);
                    builder.setMessage(R.string.dialog_handle_excption_task);
                    break;
                case FUNCTION:
                    titleHint = getString(R.string.macro_function_hint);
                    builder.setMessage(R.string.dialog_function_task);
                    break;
                default:
                    break;
            }

            builder.setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            })
            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
        public CreateTaskDialogFragment(EntryType macroType){
        	this.macroType = macroType;
        }
        
        @Override
        public void onStart(){
            super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
            AlertDialog d = (AlertDialog)getDialog();
            if(d != null)
            {
                EditText titleEditText = (EditText) getDialog().findViewById(R.id.taskTitleDialogTextView);
                titleEditText.setHint(titleHint);
                Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText titleEditText = (EditText) getDialog().findViewById(R.id.taskTitleDialogTextView);
                        EditText descEditText = (EditText) getDialog().findViewById(R.id.taskDescDialogTextView);
                        String title = titleEditText.getText().toString();
                        String desc = descEditText.getText().toString();
                        boolean inputIsOkay = true;
                        switch (macroType) {
                            case CUSTOM:
                                if (title.isEmpty()) {
                                    inputIsOkay = false;
                                    titleEditText.setError("Please provide some task title");
                                }
                                break;
                            case ALGORITHM:
                                if(title.isEmpty()){
                                    title = getString(R.string.macro_algo_hint);
                                } else {
                                    title = "Algorithm: " + title;
                                }
                                if(desc.isEmpty()){
                                    desc = "We need to implement the algorithm.";
                                }
                                break;
                            case CLASS:
                                if(title.isEmpty()){
                                    title = getString(R.string.macro_class_hint);
                                } else {
                                    title = "Class: " + title;
                                }
                                if(desc.isEmpty()){
                                    desc = "We need to create that class.";
                                }
                                break;
                            case EXCEPTION:
                                if(title.isEmpty()){
                                    title = getString(R.string.macro_exception_hint);
                                } else {
                                    title = "Handle Exception: " + title;
                                }
                                if(desc.isEmpty()){
                                    desc = "We need to consider an exception.";
                                }
                                break;
                            case FUNCTION:
                                if(title.isEmpty()){
                                    title = getString(R.string.macro_function_hint);
                                } else {
                                    title = "Function: " + title;
                                }
                                if(desc.isEmpty()){
                                    desc = "We need to work on that function/method.";
                                }
                                break;
                            default:
                                break;
                        }
                        if(inputIsOkay) {
                            roadMapList.add(new RoadMapEntry(title, desc, macroType));
                            updateTaskList();
                            dismiss();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (DETAIL_ACTIVITY_KEY) : {
                if (resultCode == RESULT_DETAIL_CHANGED) {
                    if(data != null){
                        final int index = data.getIntExtra(INTENT_INDEX_RESULT, -1);
                        if(index >= 0){
                        	Log.i(TAG, "Some data recieved");
                            RoadMapEntry entryToChange = roadMapList.get(index);
                            entryToChange.setTitle(data.getStringExtra(INTENT_TITLE_RESULT));
                            entryToChange.setDescription(data.getStringExtra(INTENT_DESC_RESULT));
                            try {
								saveRoadmap();
							} catch (JSONException e) {
								Log.w(TAG, "JSONException when trying to save"+
										" changes to a roadmap task");
								e.printStackTrace();
							}
                        }
                    }else{
                    	Log.w(TAG, "No data was recieved as result");
                    }
                }
                break;
            }
        }
    }
}
