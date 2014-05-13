package se.chalmers.agile5.activities;

import java.util.ArrayList;

import se.chalmers.agile5.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewCommitsActivity extends BaseActivity {
	private String branch;
	private ArrayList<String> x;
	private ListView commitsListView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_commits);

		x = new ArrayList<String>();
		branch = getIntent().getStringExtra("branch_name");
		x.add(branch);
		
		commitsListView = (ListView) findViewById(R.id.commitsListView); 
		commitsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
//		commitsList.setAdapter(adapter);
		
		commitsListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_selectable_list_item, x));
				commitsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

}
