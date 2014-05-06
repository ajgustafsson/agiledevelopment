package se.chalmers.agile5.activities;

import java.util.ArrayList;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;

import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.logic.RetriveGitEvents;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GitEvents extends BaseActivity {
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
		ArrayList<RepositoryCommit> newCommits = new ArrayList<RepositoryCommit>();
		ArrayList<RepositoryCommit> diffCommits = new ArrayList<RepositoryCommit>();
		oldCommits = GitDataHandler.getCommits();
		RetriveGitEvents git = new RetriveGitEvents();
		newCommits = git.getCommits();
		if(oldCommits.size() != newCommits.size()) {
			for(RepositoryCommit commit : newCommits) {
				if(!oldCommits.contains(commit)) {
					diffCommits.add(commit);
				}
			}
			
		}
		GitDataHandler.setCommits(newCommits);
		if(oldCommits.get(0).getFiles() == null) {
			Toast.makeText(getApplicationContext(),"getFiles() == null", Toast.LENGTH_LONG).show();

		}
		//Toast.makeText(getApplicationContext(),oldCommits.get(0).getFiles().get(0).getFilename(), Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(),diffCommits.toString(), Toast.LENGTH_LONG).show();
		
	//Måste köra commit.getCommit().getMessage(), men då borde ju commit.getCommit().getFiles(), men 
		//det är det inte utan, commit.getFiles()
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
