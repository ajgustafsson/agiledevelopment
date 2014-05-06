package se.chalmers.agile5.activities.test;

import se.chalmers.agile5.activities.pivotal.PivotalProjectActivity;
import junit.framework.Assert;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class PivotalActivityTest extends ActivityUnitTestCase<PivotalProjectActivity> {
	
	private PivotalProjectActivity pivotalActivity;
	
	public PivotalActivityTest() {
		super(PivotalProjectActivity.class);
	}
	
	@Override
	public void setUp() throws Exception{
		Intent intent = new Intent(getInstrumentation().getContext(),PivotalProjectActivity.class);
		startActivity(intent,null,null);
		pivotalActivity = (PivotalProjectActivity) getActivity();
		super.setUp();
	}
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		pivotalActivity.finish();
	}
	
/*	@MediumTest
	public void testBackButton(){
		pivotalActivity.onBackPressed();
		Assert.assertTrue(isFinishCalled());
	}
*/
}
