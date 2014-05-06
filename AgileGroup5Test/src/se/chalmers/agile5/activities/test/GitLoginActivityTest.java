package se.chalmers.agile5.activities.test;

import junit.framework.Assert;
import se.chalmers.agile5.activities.GitLoginActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class GitLoginActivityTest extends ActivityUnitTestCase<GitLoginActivity> {

	private GitLoginActivity gitLoginActivity;
	
	public GitLoginActivityTest(){
		super(GitLoginActivity.class);
	}

	@Override
	public void setUp() throws Exception{
		Intent startupIntent = new Intent(getInstrumentation().getContext(),GitLoginActivity.class);
		startActivity(startupIntent,null,null);
		gitLoginActivity = (GitLoginActivity) getActivity();
		super.setUp();
	}
	
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		gitLoginActivity.finish();
	}
/*	
	@MediumTest
	public void testBackButton(){
		gitLoginActivity.onBackPressed();
		Assert.assertTrue(isFinishCalled());
	}
	*/
}
