package se.chalmers.agile5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MyActivity extends Activity {
	EditText labelInput;
	/**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        labelInput = (EditText) findViewById(R.id.editText1);
    }
    
    public void getUserStories(View view){
    	
    	Intent intent = new Intent(this, UserStoriesActivity.class);
    	intent.putExtra("Label", labelInput.getText().toString());
    	startActivity(intent);
    }
    
}
