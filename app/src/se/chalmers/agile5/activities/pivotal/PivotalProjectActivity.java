package se.chalmers.agile5.activities.pivotal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.chalmers.agile5.R;
import se.chalmers.agile5.activities.BaseActivity;
import se.chalmers.agile5.entities.pivotal.PivotalProject;
import se.chalmers.agile5.entities.pivotal.PivotalResponse;
import se.chalmers.agile5.logic.RetriveProjects;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PivotalProjectActivity extends BaseActivity {
	private String pivotalToken;
	private ListView listview;
	private ArrayList<PivotalProject> projects;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("TAG", "test");
		setContentView(R.layout.pivotal_project);
		//TODO: Get from sharedpreferences
		pivotalToken ="";
		listview = (ListView) findViewById(R.id.project_list_view);
		
		projects = new ArrayList<PivotalProject>();
		try {
			List<PivotalResponse> responses = new RetriveProjects("").execute().get();
			for(PivotalResponse r : responses){
				PivotalProject p = (PivotalProject) r;
				projects.add(p);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		ArrayAdapter<PivotalProject> listAdapter = new ArrayAdapter<PivotalProject>(this, 
				android.R.layout.simple_list_item_1);
		for(PivotalProject p : projects){
			listAdapter.add(p);
		}
		
		
		listview.setAdapter(listAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				openProject(projects.get(position).getId());
			}
			
		});
	}
	
	private void openProject(int projectId){
		Intent intent = new Intent(this , PivotalStoryActivity.class);	
		intent.putExtra("ProjectId",Integer.toString(projectId));
		startActivity(intent);
	}
}
