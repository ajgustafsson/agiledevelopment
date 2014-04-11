package se.chalmers.agile5.activities.test;

import junit.framework.Assert;
import se.chalmers.agile5.activities.GitSelectActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class GitSelectActivityTest extends ActivityUnitTestCase<GitSelectActivity> {
	
	private GitSelectActivity gitSelectActivity;
	
	public GitSelectActivityTest(){
		super(GitSelectActivity.class);
	}
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		Intent startupIntent = new Intent(getInstrumentation().getContext(),GitSelectActivity.class);
		startActivity(startupIntent,null,null);
		gitSelectActivity = (GitSelectActivity) getActivity();
	}
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		gitSelectActivity.finish();
	}
/*	
	@MediumTest
	public void testBackButton(){
		gitSelectActivity.onBackPressed();
		Assert.assertTrue(isFinishCalled());
	}
	*/
}
