package se.chalmers.agile5.activities;

import android.os.Bundle;
import se.chalmers.agile5.R;

public class MyActivity extends BaseActivity {
    
    public static final String GIT_PREFS = "GIT_PREFS";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
