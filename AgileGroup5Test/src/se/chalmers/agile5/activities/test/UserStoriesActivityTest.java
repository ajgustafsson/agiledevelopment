package se.chalmers.agile5.activities.test;

import se.chalmers.agile5.activities.pivotal.PivotalStoryActivity;
import junit.framework.Assert;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class UserStoriesActivityTest extends ActivityUnitTestCase<PivotalStoryActivity> {
	
	private PivotalStoryActivity userStoriesActivity;
	
	public UserStoriesActivityTest(){
		super(PivotalStoryActivity.class);
	}
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		Intent startupIntent = new Intent(getInstrumentation().getContext(),PivotalStoryActivity.class);
		startActivity(startupIntent,null,null);
		userStoriesActivity = (PivotalStoryActivity) getActivity();
	}
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		userStoriesActivity.finish();
	}
	
	@MediumTest
	public void testBackButton(){
		userStoriesActivity.onBackPressed();
		Assert.assertTrue(isFinishCalled());
	}
}
