package se.chalmers.agile5.activities;

import java.util.ArrayList;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;

import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.logic.RetriveGitEvents;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
		
			repoListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, reposName));
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
	
}
