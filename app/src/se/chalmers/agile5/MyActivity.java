package se.chalmers.agile5;

import android.os.Bundle;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;

public class MyActivity extends BaseActivity {
    
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
    
}
