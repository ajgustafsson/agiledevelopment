package se.chalmers.agile5.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.logic.INotificationHandler;
import se.chalmers.agile5.logic.NotificationHandler;
import se.chalmers.agile5.activities.pivotal.PivotalProjectActivity;

public class BaseActivity extends Activity {
	private INotificationHandler notificationHandler = new NotificationHandler();
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {	

        @Override
        public void run() {
            
            /* 
             * Add logic to when the notification is to be triggered
             * and which messages to be displayed.
             */
        	notificationHandler.DisplayNotification(getApplicationContext(), 
        			MyActivity.class, 
        			"New Commit", "Message", "1337");

            timerHandler.postDelayed(this, 10000);
        }
    };
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_items, menu);
        
        if(GitDataHandler.getInstance().isUserLoggedIn())
        {
        	timerHandler.postDelayed(timerRunnable, 0);
        }
        else
        {
        	timerHandler.removeCallbacks(timerRunnable);
        }
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public void onPause()
	{
		super.onPause();
		timerHandler.removeCallbacks(timerRunnable);
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
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
            case R.id.roadmap_activity_link:
                Intent roadMap = new Intent(this, RoadMapActivity.class);
                startActivity(roadMap);
                return true;
            case R.id.git_events_activity_link:
            	Intent gitIntent2 = new Intent(this, GitEvents.class);
            	startActivity(gitIntent2);
                return true;
            case R.id.files_activity_link:
                Intent files = new Intent(this, FilesActivity.class);
                startActivity(files);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
