package se.chalmers.agile5.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.EntryType;
import se.chalmers.agile5.entities.RoadMapEntry;

import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;


public class RoadMapActivity extends BaseActivity {
	private static final String TAG = RoadMapActivity.class.getSimpleName();

    private List<RoadMapEntry> roadMapList = new LinkedList<RoadMapEntry>();
    private ListView roadMapListView;
    private ListView macroList;
    
    private SharedPreferences jsonData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonData = getSharedPreferences("jsonData", MODE_PRIVATE);
        
        setContentView(R.layout.roadmap_activity);
        roadMapListView = (ListView)findViewById(R.id.roadMapTasksListView);
        macroList = (ListView)findViewById(R.id.roadMapMacroListView);
        initMacroList();
        /*
        try {
			loadRoadmap();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        roadMapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(RoadMapActivity.this, RoadMapDetailActivity.class);
                RoadMapEntry entryClicked = roadMapList.get(position);
                detailIntent.putExtra("title", entryClicked.getTitle());
                detailIntent.putExtra("description", entryClicked.getDescription());
                startActivity(detailIntent);
            }
        });

        macroList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == macroList.getCount() - 1){
                    DialogFragment dialog = new CreateCustomTaskDialogFragment();
                    dialog.show(getFragmentManager(), "Create custom task");
                }
            }
        });
    }

    private void initMacroList(){
        String[] macroStrings = {"Class", "Algo", "Custom"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                macroStrings);
        macroList.setAdapter(arrayAdapter);
    }

    private void updateTaskList(){
        ArrayAdapter<RoadMapEntry> arrayAdapter = new ArrayAdapter<RoadMapEntry>(
                this,
                android.R.layout.simple_list_item_1,
                roadMapList);
        roadMapListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onPause() {
    	super.onPause();
    	Log.i(TAG, "onPause");
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
    	Log.i(TAG, "onResume");
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
		public class CreateCustomTaskDialogFragment extends DialogFragment {
	        @Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
	            // Use the Builder class for convenient dialog construction
	            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	            LayoutInflater inflater = getActivity().getLayoutInflater();

	            // Inflate and set the layout for the dialog
	            // Pass null as the parent view because its going in the dialog layout
	            builder.setView(inflater.inflate(R.layout.custom_task_dialog, null));

	            builder.setMessage(R.string.dialog_new_custom_task)
	                    .setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
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

	        @Override
	        public void onStart(){
	            super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
	            AlertDialog d = (AlertDialog)getDialog();
	            if(d != null)
	            {
	                Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
	                positiveButton.setOnClickListener(new View.OnClickListener()
	                {
	                    @Override
	                    public void onClick(View v)
	                    {
	                        EditText titleEditText = (EditText) getDialog().findViewById(R.id.taskTitleDialogTextView);
	                        EditText descEditText = (EditText) getDialog().findViewById(R.id.taskDescDialogTextView);
	                        String title = titleEditText.getText().toString();
	                        if (title != null && !title.isEmpty()) {
	                            String desc = descEditText.getText().toString();
	                            roadMapList.add(new RoadMapEntry(title, desc, EntryType.CUSTOM));
	                            updateTaskList();
	                            dismiss();
	                        } else {
	                            titleEditText.setError("Title is not allowed to be empty");
	                        }
	                    }
	                });
	            }
	        }
	    }
}