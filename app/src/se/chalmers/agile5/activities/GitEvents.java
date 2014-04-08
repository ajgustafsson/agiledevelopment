package se.chalmers.agile5.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.egit.github.core.Commit;

import se.chalmers.agile5.R;
import se.chalmers.agile5.R.id;
import se.chalmers.agile5.R.layout;
import se.chalmers.agile5.R.menu;
import se.chalmers.agile5.logic.RetriveGitEvents;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class GitEvents extends BaseActivity {

	private TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_git_events);
		text = (TextView) findViewById(R.id.text);
		changeText();
	
	}
	
	public void changeText() {
		RetriveGitEvents git = new RetriveGitEvents();
		ArrayList<String> commits = new ArrayList<String>();
		try {
			commits = git.execute().get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			text.setText(e.toString());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			text.setText(e.toString());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			text.setText(e.toString());
		}
		if(commits.size() > 0)
		{
			for(String commit : commits)
			{
				text.append(commit);
				text.append("\n");
			}
		}
		else
		{
			text.setText("Null size");
		}
	}

	

}
