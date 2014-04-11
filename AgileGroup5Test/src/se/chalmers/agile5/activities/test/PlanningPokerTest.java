package se.chalmers.agile5.activities.test;

import junit.framework.Assert;
import se.chalmers.agile5.activities.PlanningPoker;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class PlanningPokerTest extends ActivityUnitTestCase<PlanningPoker> {
	
	PlanningPoker planningPoker;
	
	public PlanningPokerTest(){
		super(PlanningPoker.class);
	}
	
	
	@Override
	public void setUp() throws Exception{
		Intent startUpIntent = new Intent(getInstrumentation().getContext(),PlanningPoker.class);
		startActivity(startUpIntent,null,null);
		planningPoker = (PlanningPoker) getActivity();
		super.setUp();
	}
	
	
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		planningPoker.finish();
	}
/*	
	public void testPrecondition(){
		
	}
*/
	/*
	@MediumTest
	public void testBackButton(){
		planningPoker.onBackPressed();
		Assert.assertTrue(isFinishCalled());
	}
	*/
}
