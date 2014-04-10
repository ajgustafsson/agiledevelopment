package se.chalmers.agile5.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.activities.pivotal.PivotalProjectActivity;

public class BaseActivity extends Activity {

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.edit_activity_link:
            	//do something
                return true;
            case R.id.pivotal_activity_link:
            	Intent pivotalIntent = new Intent(this, PivotalProjectActivity.class);
                startActivity(pivotalIntent);
                return true;
            case R.id.git_activity_link:
                final Intent gitIntent;
                if(GitDataHandler.isUserLoggedIn()){
                    gitIntent = new Intent(this, GitSettingsActivity.class);
                } else {
                    gitIntent = new Intent(this, GitLoginActivity.class);
                }
                startActivity(gitIntent);
                return true;
            case R.id.poker_activity_link:
            	Intent poker = new Intent(this, PlanningPoker.class);
            	startActivity(poker);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
