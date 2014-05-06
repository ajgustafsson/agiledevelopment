package se.chalmers.agile5.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.service.ContentsService;

import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FilesActivity extends BaseActivity {
	private final String TAG = "FILES ACTIVITY";
	ListView fileListView;
    Button updateButton;
    private ArrayList<String> fileNames;
    private FetchRepoContents frc; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_files);

		fileListView = (ListView) findViewById(R.id.filesListView);
		updateButton = (Button) findViewById(R.id.buttonUpdate);
		//TODO Load up fileList
		
		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateList();
			}
		});
		
		fileNames = new ArrayList<String>();

		fileListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, fileNames));
				fileListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	

	
	@Override
	protected void onResume() {
		super.onResume();
		//TODO Retrieve files from storage?
	}
	
	private void updateList() {
		fileNames.clear();
		
		updateFiles("");
		fileListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, fileNames));
				fileListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

	}
		
	/*
	 * updates the list of files on the repo
	 */
	public void updateFiles(String path) {
		frc = new FetchRepoContents();
		String fileName;
		
		try {
			
			List<RepositoryContents> repoContents = frc.execute(path).get();
			
			for (RepositoryContents rc : repoContents) {
				//Log.i("DEBUG", "RC name: " + rc.getName() + " type: " +rc.getType());
				if (rc.getType().equals("file")) {
					fileName = rc.getName();
					if (fileName.endsWith("java") || fileName.endsWith("xml"))
						fileNames.add(fileName); 
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
				Log.i(TAG, "Repo contents in " + path + ": " + rc.size());
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return rc;
		}
		}
	
	}
