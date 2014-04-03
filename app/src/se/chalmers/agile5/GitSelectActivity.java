package se.chalmers.agile5;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.WatcherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GitSelectActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_select);

        final GitHubClient client = MyActivity.gitHubClient;

        //TODO refactoring

        final ListView repoListView = (ListView) findViewById(R.id.gitRepoListView);
        try {
            //get repositories the user watches (actually starred repositories, not watched)
            FetchStarredReposTask fetchRepoTask = new FetchStarredReposTask();
            fetchRepoTask.execute(client);
            final List<Repository> repositoryList = fetchRepoTask.get();

            //get and add repositories owned by user
            FetchOwnedReposTask fetchOwnedReposTask = new FetchOwnedReposTask();
            fetchOwnedReposTask.execute(client);
            repositoryList.addAll(fetchOwnedReposTask.get());

            //TODO handle properly
            if(repositoryList == null){
                finish();
            }

            final ArrayList<String> repoStrings = new ArrayList<String>();

            //save as strings to make it simple to show in ListView
            for(Repository repo : repositoryList){
                repoStrings.add(repo.getName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    repoStrings );
            repoListView.setAdapter(arrayAdapter);

            //on click listener for the ListView
            repoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //System.out.println("Selected: " + repositoryList.get(position).getName());
                    Repository selectedRepo = repositoryList.get(position);

//                    SharedPreferences settings = getSharedPreferences(MyActivity.GIT_PREFS, 0);
//                    settings.edit().putString("gitRepo", selectedRepo.getGitUrl());

                    // set selected repo as the one currently used in the whole app
                    MyActivity.currentRepository = selectedRepo;

                    finish();
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class FetchStarredReposTask extends AsyncTask<GitHubClient, Void, List<Repository>> {

        @Override
        protected List<Repository> doInBackground(GitHubClient... params) {
            final GitHubClient client = params[0];
            final WatcherService watchService = new WatcherService(client);
            try {
                return watchService.getWatched();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class FetchOwnedReposTask extends AsyncTask<GitHubClient, Void, List<Repository>> {
        @Override
        protected List<Repository> doInBackground(GitHubClient... params) {
            final GitHubClient client = params[0];
            final RepositoryService service = new RepositoryService(client);
            try {
                return service.getRepositories(client.getUser());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}