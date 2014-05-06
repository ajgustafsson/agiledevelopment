package se.chalmers.agile5.activities.test;

import junit.framework.Assert;
import se.chalmers.agile5.activities.MyActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class MyActivityTest extends ActivityUnitTestCase<MyActivity> {
	
	private MyActivity myActivity;
	
	public MyActivityTest() {
		super(MyActivity.class);
	}
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		Intent startupIntent = new Intent(getInstrumentation().getContext(),MyActivity.class);
		startActivity(startupIntent, null, null);
		myActivity = (MyActivity) getActivity();
	}
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		myActivity.finish();
	}
	@MediumTest
	public void testBackButton(){
		myActivity.onBackPressed();
		Assert.assertTrue(isFinishCalled());
	}
}
