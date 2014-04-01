package se.chalmers.agile5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {
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
