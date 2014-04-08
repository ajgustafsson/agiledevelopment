package se.chalmers.agile5.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;

import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.logic.RetriveGitEvents;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GitEvents extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_git_events);
		
        final ListView repoListView = (ListView) findViewById(R.id.gitFollowListView);
		RetriveGitEvents git = new RetriveGitEvents();
		ArrayList<String> reposName = new ArrayList<String>();
		ArrayList<Repository> repos = new ArrayList<Repository>();
		
		repos = git.getRepos();
		
		for(Repository repo : repos) {
			reposName.add(repo.getName());
		}
		
		repoListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, reposName));
				repoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

	}
	



	

}
