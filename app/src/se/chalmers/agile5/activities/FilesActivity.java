package se.chalmers.agile5.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.egit.github.core.RepositoryContents;

import se.chalmers.agile5.R;
import se.chalmers.agile5.adapter.FileStorageAdapter;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.logic.RetriveGitEvents;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FilesActivity extends BaseActivity {
	private final String TAG = "FILES ACTIVITY";
	ListView fileListView;
    Button updateButton;
    Button storeButton;
    Button loadButton;
    private ArrayList<String> filePaths;
    private ArrayList<String> selection;
    private RetriveGitEvents gitRetriever;
    FileStorageAdapter storage;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_files);

		fileListView = (ListView) findViewById(R.id.filesListView);
		updateButton = (Button) findViewById(R.id.buttonUpdate);
		storeButton = (Button) findViewById(R.id.buttonStore);
		loadButton = (Button) findViewById(R.id.buttonLoad);
		storage = new FileStorageAdapter(this);
		filePaths = new ArrayList<String>();
		selection = new ArrayList<String>();
		gitRetriever = new RetriveGitEvents();
		
		
		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				filePaths.clear();
				updateFiles("");
				updateListView();
								
			}
		});
		storeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				storeFileList();
			}
		}); 
		
		loadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadFileList();
			}
		});
		fileListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				String file = (String)parent.getItemAtPosition(position);
				Log.i(TAG, "Clicked: " + file);
				clickFile(file);
				}
		});	//TODO Fix prettier clickListener.
			
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		checkLoggedIn();
		loadFileList();
		updateListView();
		//TODO Retrieve files from storage?
	}
			
	/*
	 * updates the list of files on the repo
	 */
	public void updateFiles(String path) {
		String filePath;
		
		List<RepositoryContents> repoContents = gitRetriever.getContents(path);
		if (repoContents == null) {
			Log.i(TAG, "repoContents empty");
			return;
		}
			
		for (RepositoryContents rc : repoContents) {
			if (rc.getType().equals("file")) {
				filePath = rc.getPath();
				if (filePath.endsWith("java") || filePath.endsWith("xml") || filePath.endsWith("txt")) {
					filePaths.add(filePath);
				}
			}
			if (rc.getType().equals("dir"))
				updateFiles(rc.getPath());
			}
		
	}

	private void storeFileList() {
		storage.storeSelection(new ArrayList<String>());
		if (!filePaths.isEmpty()) {
			Log.i(TAG, "Storing files");
			storage.storeFileList(filePaths);
			storage.storeSelection(selection);
//			Toast.makeText(this, selection.size() + " files tracked", Toast.LENGTH_SHORT).show();
		}
//		else
//			Toast.makeText(this, "No files to store", Toast.LENGTH_SHORT).show();
	}
	
	private void loadFileList() {
		filePaths.clear();
		selection.clear();
		filePaths = storage.retrieveFileList();
		selection = storage.loadSelection();
		if (!filePaths.isEmpty()) {
			updateListView();
//			Toast.makeText(this, "File list loaded, " + selection.size() + " files tracked", Toast.LENGTH_SHORT).show();
		}
//		else
//			Toast.makeText(this, "No files found", Toast.LENGTH_SHORT).show();
	}
	
	private void updateListView() {
		String fileInList;
		fileListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, filePaths));
				fileListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		for (int i = 0; i < fileListView.getCount(); i++) {
			fileInList = (String) fileListView.getItemAtPosition(i);
			for (String trackedFile : selection) {
				if (fileInList.equals(trackedFile))
					fileListView.setItemChecked(i, true);
			}
		}
	}
	
	private void clickFile(String file) {
		if (!selection.isEmpty())
			for (String s : selection) {
				if(s.equals(file)) {
					selection.remove(s);
					return;
				}
			}
		selection.add(file);
//		Toast.makeText(this, "Added file: " +file, Toast.LENGTH_SHORT).show();
		
		
	}
	
	
	private void checkLoggedIn() {
		if (!GitDataHandler.isUserLoggedIn() || GitDataHandler.getCurrentGitRepo() == null) {
			Toast.makeText(this, "Please make sure that you are logged in, and that you have selected a repo", Toast.LENGTH_LONG).show();
			Intent logIn = new Intent(this, GitLoginActivity.class);
			startActivity(logIn);
			finish();
		}
	}
	
	}
