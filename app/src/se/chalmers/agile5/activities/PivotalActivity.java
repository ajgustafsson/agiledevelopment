package se.chalmers.agile5.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import se.chalmers.agile5.R;

public class PivotalActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pivotal_activity);
    }

    public void getUserStories(View view){
        EditText labelInput = (EditText) findViewById(R.id.userStoryText);
        Intent intent = new Intent(this, UserStoriesActivity.class);
        intent.putExtra("Label", labelInput.getText().toString());
        startActivity(intent);
    }
}