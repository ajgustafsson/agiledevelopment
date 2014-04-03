package se.chalmers.agile5;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

public class ActionBarOptionSelected {
	
	public static boolean mainActionBar(MenuItem item, Context c) {
		switch (item.getItemId()) {
        case R.id.edit_activity_link:
        	//do something
            return true;
        case R.id.pivotal_activity_link:
        	//do something
            return true;
        case R.id.git_activity_link:
            //do something
            return true;
        case R.id.poker_activity_link:
        	Intent poker = new Intent(c, PlanningPoker.class);
        	c.startActivity(poker);
            return true;
        default:
        	return false;
            //return super.onOptionsItemSelected(item);
		}
	}
	
}
