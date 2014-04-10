package se.chalmers.agile5.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import org.eclipse.egit.github.core.Repository;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.AgileGitHubClient;
import se.chalmers.agile5.entities.GitDataHandler;

public class MyActivity extends BaseActivity {
    
    public static final String GIT_PREFS = "GIT_PREFS";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recoverLatestGitSettings();
        setContentView(R.layout.main);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(GitDataHandler.isUserLoggedIn() && GitDataHandler.isSaveLoginInfoEnabled()){
            saveGitSettings();
        } else {
            clearLatestSetting();
        }
    }

    private void clearLatestSetting() {
        final SharedPreferences.Editor editor = getSharedPreferences(GIT_PREFS, 0).edit();
        editor.remove("latestUserName");
        editor.remove("latestRepositoryId");
        editor.remove("latestCredentials");
        editor.commit();
    }

    private void saveGitSettings(){
        final SharedPreferences.Editor editor = getSharedPreferences(GIT_PREFS, 0).edit();
        //save user name
        final String userName = GitDataHandler.getGitUserName();
        if(userName != null){
            editor.putString("latestUserName", GitDataHandler.getGitUserName());
        }
        //save user credentials
        AgileGitHubClient gitHubClient = GitDataHandler.getGitClient();
        editor.putString("latestCredentials", gitHubClient.getCredentials());
        //save latest repository
        final Repository currentRepo = GitDataHandler.getCurrentGitRepo();
        if(currentRepo != null){
            editor.putString("latestRepositoryId", currentRepo.generateId());
        }

        // Commit the edits made to the file
        editor.commit();
    }

    private void recoverLatestGitSettings(){
        SharedPreferences settings = getSharedPreferences(GIT_PREFS, 0);

        //retrieve latest user credentials (token or name/pw)
        final String userCredentials = settings.getString("latestCredentials", null);
        if(userCredentials != null){
            final AgileGitHubClient gitHubClient = new AgileGitHubClient();
            gitHubClient.setCredentials(userCredentials);
            GitDataHandler.setGitHubClient(gitHubClient);
        }
        //System.err.println("[DBUG] user is: "+GitDataHandler.getGitUserName());

        //only continue if the old credentials are valid, that is the user is now logged in
        if(!GitDataHandler.isUserLoggedIn()){
            GitDataHandler.setSaveLoginInfoEnabled(true);
            return;
        }

        //retrieve the latest used repo (if any)
        final String repoId = settings.getString("latestRepositoryId", null);
        if(repoId != null){
            //System.err.println("[DBUG] latest Repo: "+GitDataHandler.getRepositoryById(repoId).getName());
            Repository latestRepo = GitDataHandler.getRepositoryById(repoId);
            if(latestRepo != null){
                GitDataHandler.setCurrentGitRepo(latestRepo);
            }
        }

    }
}
