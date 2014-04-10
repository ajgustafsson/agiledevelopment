package se.chalmers.agile5.entities.pivotal;

import org.json.JSONObject;

public final class PivotalUnformattedResponse extends PivotalResponse {
	private JSONObject jsonObject;
	
	public PivotalUnformattedResponse(JSONObject jsonObject){
		this.jsonObject = jsonObject;
	}
	
	public JSONObject getJsonObject(){
		return jsonObject;
	}
}
