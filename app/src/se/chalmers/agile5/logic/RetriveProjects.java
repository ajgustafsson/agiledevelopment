package se.chalmers.agile5.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;
import se.chalmers.agile5.entities.pivotal.PivotalData;
import se.chalmers.agile5.entities.pivotal.PivotalProject;
import se.chalmers.agile5.entities.pivotal.PivotalResponse;
import se.chalmers.agile5.entities.pivotal.PivotalUnformattedResponse;

public class RetriveProjects extends RetrivePivotalData {
	
	public RetriveProjects(String token){
		super(token, BASEURL);
	}
	
	@Override
	protected List<PivotalResponse> doInBackground(String... params) {
		ArrayList<PivotalResponse> projects = new ArrayList<PivotalResponse>();
		List<PivotalResponse> responses = super.doInBackground(params);
		for(PivotalResponse r : responses){
			if(r instanceof PivotalUnformattedResponse){
				PivotalUnformattedResponse ur = (PivotalUnformattedResponse) r;
				JSONObject project = ur.getJsonObject();
				int id = project.optInt("id", 0);
				String title = project.optString("name", "undefined");
				Log.i("TAG", title);
				projects.add(new PivotalProject(id, title));
			}
		}
		return projects;
	}
}
