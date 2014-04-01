package se.chalmers.agile5;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.WatcherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitLoginActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_login);


        final Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userNameText = (EditText) findViewById(R.id.userName);
                final EditText pwText = (EditText) findViewById(R.id.userPassword);

                final String userName = userNameText.getText().toString();
                final String password = pwText.getText().toString();

                if (!userName.isEmpty() && !password.isEmpty()) {
                    doLogin(userName, password);
                }

            }
        });


    }

    public void doLogin(String userName, String password){
        final GitHubClient client = new GitHubClient();
        client.setCredentials(userName, password);

        final UserService userService = new UserService(client);
        final WatcherService watchService = new WatcherService(client);

        final TextView infoView = (TextView) findViewById(R.id.gitInfoView);
        final ListView repoList = (ListView) findViewById(R.id.repoListView);
        try {
            infoView.setText("Hello " + userService.getUser().getName());

            //get repositories the user watches (actually starred repos, not watched)
            final List<Repository> repositoryList = watchService.getWatched();
            final ArrayList<String> repoStrings = new ArrayList<String>();

            //save as strings to make it simple to show in ListView
            for(Repository repo : repositoryList){
                repoStrings.add(repo.getName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    repoStrings );
            repoList.setAdapter(arrayAdapter);

            //on click listener for the ListView
            repoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Selected: " + repositoryList.get(position).getName());
                    //TODO: set selected repo as the one currently used in the whole app
                    //TODO: redirect somewhere in the app?
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}