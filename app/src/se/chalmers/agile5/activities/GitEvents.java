package se.chalmers.agile5.activities;

import java.util.ArrayList;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;

import se.chalmers.agile5.R;
import se.chalmers.agile5.adapter.FileStorageAdapter;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.logic.NotificationHandler;
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
import android.widget.TextView;
import android.widget.Toast;

public class GitEvents extends BaseActivity {
	private final String TAG = "GIT EVENTS";
	private ListView branchesListView;
    private TextView text;
    private Button loginButton;
    private NotificationHandler notify;
    private String newCommitNames;
    private ArrayList<RepositoryCommit> diffCommits;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_git_events);

		branchesListView = (ListView) findViewById(R.id.gitFollowListView);
		text = (TextView) findViewById(R.id.text);
		loginButton = (Button) findViewById(R.id.goToLogin);
		notify = new NotificationHandler();
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent login = new Intent(GitEvents.this, GitLoginActivity.class);
				startActivity(login);
			}
		});
        retrieveBranches();
	}

	
	private void retrieveBranches(){
		RetriveGitEvents git = new RetriveGitEvents();
		ArrayList<String> reposName = new ArrayList<String>();
		ArrayList<Repository> repos = new ArrayList<Repository>();
		ArrayList<String> branchNames = new ArrayList<String>();
		
		//In order to retrieve all branches, you must be connected to github and
		//a repo must have been choosed
		if (!checkLoggedIn()) {
			text.setVisibility(View.VISIBLE);
			loginButton.setVisibility(View.VISIBLE);
		} else {
			final ArrayList<RepositoryBranch> branches = git.getBranches();
			//Load stored trackingBranches into the DataHandler.
			FileStorageAdapter fileAdapter = new FileStorageAdapter(this);
			ArrayList<RepositoryBranch> branchesFromSharedPref = fileAdapter.getTrackingsBranches();
			for(RepositoryBranch branch : branchesFromSharedPref) {
				GitDataHandler.addTrackingBranch(branch);
			}
			repos = git.getRepos();
			for(Repository repo : repos) {
				reposName.add(repo.getName());
			}

			
			for(RepositoryBranch branch : branches) {
				branchNames.add(branch.getName());
			}
			
			branchesListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, branchNames));
				branchesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				
			
				
			branchesListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					String name = (String)parent.getItemAtPosition(position);
					saveCheckedBranch(name, branches);
					}
			});	
			
			//Checks the right branches that are already tracked
			for(int i = 0; i < branchesListView.getCount() ; i++) {
				for(RepositoryBranch branch : branchesFromSharedPref) {
					if(branch.getName().equals(branchesListView.getItemAtPosition(i).toString())) {
						branchesListView.setItemChecked(i, true);
					}
				}
			}
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		text.setVisibility(View.GONE);
		loginButton.setVisibility(View.GONE);
		retrieveBranches();
	}
	
	public void updateCommits(View view) {
		updateCommits();
	}
	
	/*
	 * Checks if there are any new commits in git.
	 */
	public void updateCommits() {
		//Hämta oldCommits från Shared Preferences!
		//Kör detta för varje branch som trackas.
		FileStorageAdapter fileStorage = new FileStorageAdapter(this);
		ArrayList<RepositoryBranch> trackedBranches = new ArrayList<RepositoryBranch>();
		ArrayList<RepositoryCommit> oldCommits = new ArrayList<RepositoryCommit>();
		ArrayList<String> oldCommitShas = new ArrayList<String>();
		ArrayList<RepositoryCommit> newCommits = new ArrayList<RepositoryCommit>();
		
		trackedBranches = fileStorage.getTrackingsBranches();
		
		for(RepositoryBranch branch : trackedBranches) {
			diffCommits = new ArrayList<RepositoryCommit>();
			newCommitNames = "";
			
			
			oldCommits = fileStorage.getCommitsForTrackingBranch(branch);
			Log.i("test", "Size of oldCommits i GitEvents:" + oldCommits.size());
			for (RepositoryCommit r : oldCommits) {
				oldCommitShas.add(r.getSha());
				Log.i("test", "We found a Sha in oldCommit: " + r.getSha());
			}
			//Diffcommits skall bara innehålla de commits som kommer efter den commiten som ligger i OldCommits.
			RetriveGitEvents git = new RetriveGitEvents();
			newCommits = git.getCommitsByBranch(branch.getName());
			if(oldCommits.size() != newCommits.size() && !oldCommits.isEmpty()) {
				for(RepositoryCommit commit : newCommits) {
					if(oldCommitShas.contains(commit.getSha())) {
						Log.i("test", "Vi har satt diff till true!");
						break;
					}
					diffCommits.add(commit);
					Log.i("test", "Vi har lagt in commiten till diffcommits!");
				}
				
			}
			fileStorage.storeCommitsForATrackingBranch(newCommits, branch);
			Log.i(TAG, GitDataHandler.getCommits().size() + " " + newCommits.size());
			
			
			if(!diffCommits.isEmpty()) {
				Log.i("test", "DiffCommits size: " + diffCommits.size());
				ArrayList<RepositoryCommit> extendedDiffCommits = new ArrayList<RepositoryCommit>();
				RepositoryCommit extendedCommit;
				for (RepositoryCommit rc : diffCommits) {
					extendedCommit = git.getExtendedCommit(rc.getSha());
					extendedDiffCommits.add(extendedCommit);
					newCommitNames += rc.getCommit().getMessage() + "\n";
				}
				
				
				//fileStorage.storeCommitsForATrackingBranch(diffCommits, branch);
				
				notify.DisplayNotification(this, GitEvents.class, "New commit!", newCommitNames, "");
	
			} else {
				Toast.makeText(getApplicationContext(),"No new commits", Toast.LENGTH_LONG).show();
			}
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
	
	
	private void saveCheckedBranch(String branchName, ArrayList<RepositoryBranch> branchesInRepo) {
		FileStorageAdapter fileStorageAdapter = new FileStorageAdapter(this);
		ArrayList<RepositoryBranch> following = GitDataHandler.getTrackingBranches();
		RepositoryBranch branchToFollow = new RepositoryBranch();
		Boolean toBeAdded = true;
		
		for(RepositoryBranch branch : branchesInRepo) {
			if(branch.getName().equals(branchName)) {
				branchToFollow = branch; 
				break; 
			}
		}
		
		for(RepositoryBranch branch : following) {
			//If the branch is already in the list, it should be deleted from the list.
			if(branchToFollow.getName().equals(branch.getName())) {
				GitDataHandler.removeTrackingBranch(branchToFollow);
				//Store the new List of trackingBranches in Shared Preferences.
				fileStorageAdapter.storeTrackingBranches(GitDataHandler.getTrackingBranches());
				toBeAdded = false;
				break;
			}
		}
		
		//If the branch is not removed from the list, it has to be added.
		if(toBeAdded) {
			GitDataHandler.addTrackingBranch(branchToFollow);
			fileStorageAdapter.storeTrackingBranches(GitDataHandler.getTrackingBranches());
		}
		
	}
	
//	private ArrayList<String> getFileNames (RepositoryCommit commit) {
//		
//		ArrayList<String> fileNames = new ArrayList<String>();
//		
//		for(CommitFile file : commit.getFiles()) {
//			fileNames.add(file.getFilename());
//		  
//		}
//		return fileNames;
//	}
	
	private boolean checkLoggedIn() {
		return GitDataHandler.isUserLoggedIn() && !(GitDataHandler.getCurrentGitRepo() == null);
	}
	

	}
