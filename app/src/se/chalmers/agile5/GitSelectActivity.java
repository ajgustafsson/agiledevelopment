package se.chalmers.agile5;

import android.app.Activity;
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

public class GitSelectActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_select);

        final GitHubClient client = MyActivity.gitHubClient;

        final UserService userService = new UserService(client);
        final WatcherService watchService = new WatcherService(client);
        final RepositoryService repoService = new RepositoryService(client);

        final ListView repoListView = (ListView) findViewById(R.id.gitRepoListView);
        try {
            //get repositories the user watches (actually starred repositories, not watched)
            final List<Repository> repositoryList = watchService.getWatched();
            //get and add repositories owned by user
            repositoryList.addAll(repoService.getRepositories(client.getUser()));
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}