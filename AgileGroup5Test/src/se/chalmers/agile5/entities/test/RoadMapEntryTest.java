package se.chalmers.agile5.entities.test;

import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.agile5.entities.RoadMapEntry;
import android.test.AndroidTestCase;

public class RoadMapEntryTest extends AndroidTestCase {
	
	public void testEquals(){
		RoadMapEntry r1 = new RoadMapEntry("Test");
		RoadMapEntry r2 = (RoadMapEntry) r1.copy();
		assertTrue("The objects are not equal", r1.equals(r2));
	}
	
	public void testNotEquals(){
		RoadMapEntry r1 = new RoadMapEntry("Test1");
		RoadMapEntry r2 = new RoadMapEntry("Test2");
		assertTrue("The objects are incorrectly considered equal", !r1.equals(r2));
	}
	
	public void testSymmetryJSON() throws JSONException{
		RoadMapEntry r = new RoadMapEntry("Test");
		JSONObject JSONentry = r.toJSON();
		
		RoadMapEntry recovered = RoadMapEntry.createFromJSON(JSONentry);
		
		assertTrue("The object was not preserved correctly through JSON-conversion", 
				r.equals(recovered));
	}
}
