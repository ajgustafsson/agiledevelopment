package se.chalmers.agile5.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.service.ContentsService;

import se.chalmers.agile5.R;
import se.chalmers.agile5.adapter.FileStorageAdapter;
import se.chalmers.agile5.entities.GitDataHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FilesActivity extends BaseActivity {
	private final String TAG = "FILES ACTIVITY";
	ListView fileListView;
    Button updateButton;
    Button storeButton;
    Button loadButton;
    private ArrayList<String> filePaths;
    private ArrayList<String> selection;
    private FetchRepoContents frc; 
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
		//TODO Load up fileList
		
		
		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
				selectFile(file);
				}
		});	//TODO Fix prettier clickListener.
			
	}

	

	
	@Override
	protected void onResume() {
		super.onResume();
		//TODO Retrieve files from storage?
	}
	
//	private void updateList() {
//		filePaths.clear();
//		
//		updateFiles("");
//		updateListView();
//		
//	}
		
	/*
	 * updates the list of files on the repo
	 */
	public void updateFiles(String path) {
		frc = new FetchRepoContents();
		String filePath;
		
		try {
			
			List<RepositoryContents> repoContents = frc.execute(path).get();
			
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
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void storeFileList() {
		if (!filePaths.isEmpty()) {
			Log.i(TAG, "Storing files");
			storage.storeFileList(filePaths);
			storage.storeSelection(selection);
			Toast.makeText(this, "File list stored. " + selection.size() + " files tracked", Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(this, "No files to store", Toast.LENGTH_LONG).show();
	}
	
	private void loadFileList() {
		filePaths = storage.retrieveFileList();
		selection = storage.loadSelection();
		if (!filePaths.isEmpty()) {
			updateListView();
			Toast.makeText(this, selection.size() + " files selected", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(this, "No files found", Toast.LENGTH_SHORT).show();
	}
	
	private void updateListView() {
		fileListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, filePaths));
				fileListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	private void selectFile(String file) {
		if (!selection.isEmpty())
			for (String s : selection) {
				if(s.equals(file)) {
					selection.remove(s);
					Toast.makeText(this, "File removed", Toast.LENGTH_LONG).show();
					return;
				}
			}
		selection.add(file);
		Toast.makeText(this, "Added file: " +file, Toast.LENGTH_LONG).show();
		
		
	}
		
	private class FetchRepoContents extends AsyncTask<String, Void, List<RepositoryContents>> {
		@Override
		protected List<RepositoryContents> doInBackground(String... params) {
			ContentsService contentsService = new ContentsService(GitDataHandler.getGitClient());
			Repository repo = GitDataHandler.getCurrentGitRepo();
			List<RepositoryContents> rc = null;
			String path = null;
			if(params.length > 0)
				path = params[0];
			try {
				rc = contentsService.getContents(repo, path);
//				Log.i(TAG, "Repo contents in " + path + ": " + rc.size());
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return rc;
		}
		}
	
	}
