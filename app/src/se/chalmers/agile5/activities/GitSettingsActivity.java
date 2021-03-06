package se.chalmers.agile5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import org.eclipse.egit.github.core.Repository;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;


public class GitSettingsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_settings);
        loadUserSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCurrentRepo();
    }

    private void loadUserSettings(){
        TextView gitLoginInfoView = (TextView) findViewById(R.id.loginInfoView);
        String loggedInName = GitDataHandler.getCurrentGitUser().getName();
        if (loggedInName == null || loggedInName == ""){
        	loggedInName = GitDataHandler.getCurrentGitUser().getLogin();
        }
        gitLoginInfoView.setText("Logged in as "+ loggedInName);
        CheckBox saveLoginBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        saveLoginBox.setChecked(GitDataHandler.isSaveLoginInfoEnabled());
        loadCurrentRepo();
    }

    private void loadCurrentRepo() {
        TextView gitRepoView = (TextView) findViewById(R.id.currentRepoViewText);
        final Repository currentGitRepo = GitDataHandler.getCurrentGitRepo();
        if (currentGitRepo != null) {
            gitRepoView.setText(currentGitRepo.getName());
        } else {
            gitRepoView.setText("<not-selected>");
        }
    }

    public void saveSettings(View view){
        GitDataHandler.setSaveLoginInfoEnabled(((CheckBox)(findViewById(R.id.saveLoginCheckBox))).isChecked());
        finish();
    }

    public void cancel(View view){
        finish();
    }

    public void logoutUser(View view){
        if(GitDataHandler.isUserLoggedIn()){
            GitDataHandler.logout();
            finish();
        }
    }

    public void changeRepository(View view){
        Intent intent = new Intent(this, GitSelectActivity.class);
        startActivity(intent);
    }
}