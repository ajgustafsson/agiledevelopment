package se.chalmers.agile5.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.eclipse.egit.github.core.Repository;

import se.chalmers.agile5.R;
import se.chalmers.agile5.activities.pivotal.PivotalProjectActivity;
import se.chalmers.agile5.entities.AgileGitHubClient;
import se.chalmers.agile5.entities.GitDataHandler;

public class MyActivity extends BaseActivity {
    
    public static final String GIT_PREFS = "GIT_PREFS";
	private static final String TAG = MyActivity.class.getSimpleName();

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
    protected void onResume() {
        super.onResume();
        TextView tv = (TextView)findViewById(R.id.mainGitLoginTextView);
        if(GitDataHandler.isUserLoggedIn()){
            String loggedInName = GitDataHandler.getCurrentGitUser().getName();
            if (loggedInName == null || loggedInName == ""){
                loggedInName = GitDataHandler.getCurrentGitUser().getLogin();
            }
            tv.setText("You are logged in on GitHub as "+ loggedInName);
            tv.setClickable(false);
            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        } else {
            tv.setText("You are not logged in on GitHub. Click here to login!");
            tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv.setClickable(true);
        }
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

    public void startPivotal(View view){
        final Intent intent = new Intent(this, PivotalProjectActivity.class);
        startActivity(intent);
    }

    public void startRoadMap(View view){
        final Intent intent = new Intent(this, RoadMapActivity.class);
        startActivity(intent);
    }

    public void startPlanningPoker(View view){
        final Intent intent = new Intent(this, PlanningPoker.class);
        startActivity(intent);
    }

    public void startGitSettings(View view){
        if(GitDataHandler.isUserLoggedIn()){
            final Intent gitIntent = new Intent(this, GitSettingsActivity.class);
            startActivity(gitIntent);
        } else {
            startGitLogin(view);
        }

    }

    public void startGitLogin(View view){
        final Intent intent = new Intent(this, GitLoginActivity.class);
        startActivity(intent);
    }

    public void startTrackFiles(View view){
        final Intent intent = new Intent(this, FilesActivity.class);
        startActivity(intent);
    }

    public void startAbout(View view){
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
        Log.d(TAG, "User is: " + GitDataHandler.getGitUserName());
        
        //only continue if the old credentials are valid, that is the user is now logged in
        if(!GitDataHandler.isUserLoggedIn()){
            GitDataHandler.setSaveLoginInfoEnabled(true);
            Log.e(TAG, "User not logged in to Git");
            return;
        }

        //retrieve the latest used repo (if any)
        final String repoId = settings.getString("latestRepositoryId", null);
        if(repoId != null){
        	Log.d(TAG, "Latest Repo: " + GitDataHandler.getRepositoryById(repoId).getName());
            
            Repository latestRepo = GitDataHandler.getRepositoryById(repoId);
            if(latestRepo != null){
                GitDataHandler.setCurrentGitRepo(latestRepo);
            }
        }

    }
}
