package se.chalmers.agile5.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;

import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.logic.RetriveGitEvents;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GitEvents extends BaseActivity {
	private final String TAG = "GIT EVENTS";
	ListView repoListView;
    TextView text;
    Button loginButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_git_events);

		repoListView = (ListView) findViewById(R.id.gitFollowListView);
		text = (TextView) findViewById(R.id.text);
		loginButton = (Button) findViewById(R.id.goToLogin);
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent login = new Intent(GitEvents.this, GitLoginActivity.class);
				startActivity(login);
			}
		});
        retrieveRepos();
	}

	
	private void retrieveRepos(){
		RetriveGitEvents git = new RetriveGitEvents();
		ArrayList<String> reposName = new ArrayList<String>();
		ArrayList<Repository> repos = new ArrayList<Repository>();
		
		repos = git.getRepos();
		if (repos == null) {
			text.setVisibility(View.VISIBLE);
			loginButton.setVisibility(View.VISIBLE);
		} else {
			
			for(Repository repo : repos) {
				reposName.add(repo.getName());
			}
			
			
			ArrayList<RepositoryCommit> commits = new ArrayList<RepositoryCommit>();
			commits = git.getCommits();
			

			//Set commits to be able to get update about new commits.
			GitDataHandler.setCommits(commits);

			repoListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, repoCommitsToCommitNames(commits)));
				repoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		text.setVisibility(View.GONE);
		loginButton.setVisibility(View.GONE);
		retrieveRepos();
	}
	
	public void updateCommits(View view) {
		updateCommits();
	}
	
	/*
	 * Checks if there are any new commtis in git.
	 */
	public void updateCommits() {
		ArrayList<RepositoryCommit> oldCommits = new ArrayList<RepositoryCommit>();
		ArrayList<String> oldCommitShas = new ArrayList<String>();
		ArrayList<RepositoryCommit> newCommits = new ArrayList<RepositoryCommit>();
		ArrayList<RepositoryCommit> diffCommits = new ArrayList<RepositoryCommit>();
		oldCommits = GitDataHandler.getCommits();
		for (RepositoryCommit r : oldCommits)
			oldCommitShas.add(r.getSha());
		
		RetriveGitEvents git = new RetriveGitEvents();
		newCommits = git.getCommits();
		if(oldCommits.size() != newCommits.size() && !oldCommits.isEmpty()) {
			for(RepositoryCommit commit : newCommits) {
				
				if(!oldCommitShas.contains(commit.getSha())) {
					diffCommits.add(commit);
				}
			}
			
		}
		GitDataHandler.setCommits(newCommits);	
		
		Log.i(TAG, GitDataHandler.getCommits().size() + " " + newCommits.size());
		
		
		if(diffCommits != null) {
			Log.i(TAG, "DiffCommits size: " + diffCommits.size());
			ArrayList<RepositoryCommit> extendedDiffCommits = new ArrayList<RepositoryCommit>();
			String filesChanged = "";
			RepositoryCommit extendedCommit;
			for (RepositoryCommit rc : diffCommits) {
				extendedCommit = git.getExtendedCommit(rc.getSha());
				extendedDiffCommits.add(extendedCommit);
				filesChanged += extendedCommit.getFiles().get(0).getFilename() + "\n";
			}
			Toast.makeText(getApplicationContext(),"Files changed: \n" + filesChanged, Toast.LENGTH_LONG).show();

		}
	}
	
	
	private ArrayList<String> repoCommitsToCommitNames(ArrayList<RepositoryCommit> commitList) {
		ArrayList<String> commits = new ArrayList<String>();
		for(RepositoryCommit commit : commitList) {
			commits.add(commit.getCommit().getMessage());
			//Log.i("test",commit.getCommit().getMessage());
		}
		return commits;
	}
	
	
	private ArrayList<String> getFileNames (RepositoryCommit commit) {
		
		ArrayList<String> fileNames = new ArrayList<String>();
		
		for(CommitFile file : commit.getFiles()) {
			fileNames.add(file.getFilename());
		  
		}
		return fileNames;
	}
	

	}
