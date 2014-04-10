package se.chalmers.agile5.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import se.chalmers.agile5.R;
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

        if(GitDataHandler.isUserLoggedIn()){
            final SharedPreferences.Editor editor = getSharedPreferences(GIT_PREFS, 0).edit();
            //save user name
            final String userName = GitDataHandler.getGitUserName();
            if(userName != null){
                editor.putString("latestUserName", GitDataHandler.getGitUserName());
            }
            //save user token
            final String userToken = null; //TODO fetch not hard code
            editor.putString("latestUserToken", "56d2852a19493cea928a21323e5cdbce5274cb62");
            //save latest repository
            final Repository currentRepo = GitDataHandler.getCurrentGitRepo();
            if(currentRepo != null){
                editor.putString("latestRepositoryId", currentRepo.generateId());
            }

            // Commit the edits made to the file
            editor.commit();
        }
    }

    public void recoverLatestGitSettings(){
        SharedPreferences settings = getSharedPreferences(GIT_PREFS, 0);
        String userName = settings.getString("latestUser", null);
        System.err.println("[DBUG] latest username: "+userName);

        String userToken = settings.getString("latestUserToken", null);
        if(userToken != null){
            System.err.println("[DBUG] found token: "+userToken);
            final GitHubClient gitHubClient = new GitHubClient();
            gitHubClient.setOAuth2Token(userToken);
            //TODO check token
            GitDataHandler.setGitHubClient(gitHubClient);
            if(!GitDataHandler.isUserLoggedIn()){
                return;
            }
            System.err.println("[DBUG] user is: "+GitDataHandler.getGitUserName());

            if(!GitDataHandler.isUserLoggedIn()){
                System.err.println("[DBUG] not logged in??");
            }
        }

        String repoId = settings.getString("latestRepositoryId", null);

        if(repoId != null){
            System.err.println("[DBUG] repoId: "+repoId);
            System.err.println("[DBUG] latest Repo: "+GitDataHandler.getRepositoryById(repoId).getName());
            Repository latestRepo = GitDataHandler.getRepositoryById(repoId);
            if(latestRepo != null){
                GitDataHandler.setCurrentGitRepo(latestRepo);
            }
        }

    }
}
