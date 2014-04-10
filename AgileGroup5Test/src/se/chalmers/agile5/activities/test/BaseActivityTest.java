package se.chalmers.agile5.activities.test;

import junit.framework.Assert;
import se.chalmers.agile5.activities.BaseActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

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
	public void setUp() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
	}
	/**
	 * Called last.
	 * Use it to clean up.
	 */
	@Override
	public void tearDown(){
		
	}
	
	/**
	 * Another useful convention is to add the method testPreconditions() to your test class. Use this method to test that the application under test is initialized correctly. If this test fails, you know that that the initial conditions were in error. When this happens, further test results are suspect, regardless of whether or not the tests succeeded.
	 */
	public void testPreconditions(){
	}
	
	@MediumTest
	public void test1(){
		Assert.assertTrue(true);
	}
}
