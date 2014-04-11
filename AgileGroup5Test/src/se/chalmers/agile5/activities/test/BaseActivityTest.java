package se.chalmers.agile5.activities.test;

import junit.framework.Assert;
import se.chalmers.agile5.R;
import se.chalmers.agile5.activities.BaseActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.Menu;

public class BaseActivityTest extends ActivityUnitTestCase<BaseActivity> {

	// Activity to test
	BaseActivity baseActivity;
	
	public BaseActivityTest(){
		super(BaseActivity.class);
	}
	
	@Override
	/**
	 * This method is called before everything else
	 */
	public void setUp() throws Exception {
		super.setUp();
		final Intent intent = new Intent(getInstrumentation().getTargetContext(), BaseActivity.class);
		startActivity(intent,null,null);
		baseActivity = (BaseActivity) getActivity();
		
	}
	/**
	 * Called last.
	 * Use it to clean up.
	 */
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		baseActivity.finish();
	}
	
	/**
	 * Another useful convention is to add the method testPreconditions() to your test class. Use this method to test that the application under test is initialized correctly. If this test fails, you know that that the initial conditions were in error. When this happens, further test results are suspect, regardless of whether or not the tests succeeded.
	 */
	public void testPreconditions(){
	}
	
	@MediumTest
	public void testBackButton(){
		//Testing if the activity is properly closed
		baseActivity.onBackPressed();
		Assert.assertTrue(isFinishCalled());
	}
	
	@MediumTest
	public void testMenuCalls(){
		Menu menu = (Menu) baseActivity.findViewById(R.menu.actionbar_items);
	}
}
