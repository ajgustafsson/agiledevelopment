package se.chalmers.agile5;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.WatcherService;

import java.io.IOException;

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
                    final GitHubClient client = new GitHubClient();
                    client.setCredentials(userName, password);

                    final UserService userService = new UserService(client);
                    final WatcherService watchService = new WatcherService(client);

                    final TextView infoView = (TextView) findViewById(R.id.gitInfoView);
                    final ListView repoList = (ListView) findViewById(R.id.repoListView);
                    try {
                        infoView.setText("Hello " + userService.getUser().getName());
                        //TODO: show watched/starred repos as List
                        //  List<Repository> repos = watchService.getWatched();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }


}