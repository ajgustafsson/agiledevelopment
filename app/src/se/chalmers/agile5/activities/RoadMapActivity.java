package se.chalmers.agile5.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.EntryType;
import se.chalmers.agile5.entities.RoadMapEntry;

import java.util.LinkedList;


public class RoadMapActivity extends BaseActivity {

    private LinkedList<RoadMapEntry> roadMapList = new LinkedList<RoadMapEntry>();

    private ListView roadMapListView;

    private ListView macroList;

    private String[] macroStrings = {"Class","Function", "Algo", "Exception", "Custom"};
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadmap_activity);
        roadMapListView = (ListView)findViewById(R.id.roadMapTasksListView);
        macroList = (ListView)findViewById(R.id.roadMapMacroListView);
        initMacroList();
        fillTaskListWithDummies();
        updateTaskList();

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
                    DialogFragment dialog = new CreateTaskDialogFragment(EntryType.CUSTOM);
                    dialog.show(getFragmentManager(), "Create custom task");
                }else if (position == 0){
                	EntryType type = EntryType.CLASS;
                	DialogFragment dialog = new CreateTaskDialogFragment(type);
                	dialog.show(getFragmentManager(), "Create a class macro");
                }
            }
        });
    }

    private void initMacroList(){
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                macroStrings);
        macroList.setAdapter(arrayAdapter);
    }

    private void fillTaskListWithDummies() {
        roadMapList.add(new RoadMapEntry("Task1", "Description blablab", null));
        roadMapList.add(new RoadMapEntry("Task2", "Description blaBlub", null));
        roadMapList.add(new RoadMapEntry("Task3", "Description........", null));
    }

    private void updateTaskList(){
        ArrayAdapter<RoadMapEntry> arrayAdapter = new ArrayAdapter<RoadMapEntry>(
                this,
                android.R.layout.simple_list_item_1,
                roadMapList);
        roadMapListView.setAdapter(arrayAdapter);
    }


    @SuppressLint("ValidFragment")
	public class CreateTaskDialogFragment extends DialogFragment {
    	private EntryType macroType;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.custom_task_dialog, null));
            switch (macroType){
	            case CUSTOM:
	                builder.setMessage(R.string.dialog_new_custom_task) ;
	            	break;
				case ALGORITHM:
					builder.setMessage(R.string.implement_algorithm);
					break;
				case CLASS:
					builder.setMessage(R.string.create_a_new_class);
					break;
				case EXCEPTION:
					break;
				case FUNCTION:
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
        public void onStart()
        {
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
                        	
                            switch (macroType){
            	            case CUSTOM:
            	                
            	            	break;
            				case ALGORITHM:
            					
            					break;
            				case CLASS:
            					title = "Create Class: " +title;
            					break;
            				case EXCEPTION:
            					break;
            				case FUNCTION:
            					break;
            				default:
            					break;
                        }

                        	
                            String desc = descEditText.getText().toString();
                            roadMapList.add(new RoadMapEntry(title, desc, macroType));
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