package se.chalmers.agile5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;

public class MyActivity extends Activity {

    public static final String GIT_PREFS = "GIT_PREFS";
    public static GitHubClient gitHubClient = null;
    public static Repository currentRepository = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void gitLogin(View view){
        Intent loginIntent = new Intent(this, GitLoginActivity.class);
        startActivity(loginIntent);
    }
}
