package se.chalmers.agile5;

import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Johannes Westlund. Created 2014-04-01. Copyright 2014.
 *
 */
public class PlanningPoker extends BaseActivity {
	public final static String TAG = PlanningPoker.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planning_poker);
		
	}
	
	public void selectCard(View v){
		RelativeLayout r = new RelativeLayout(this);
		r.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		r.setBackgroundColor(Color.rgb(44, 125, 246));
		
		TextView cardText = new TextView(this);
		cardText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		cardText.setText(((Button) v).getText());
		cardText.setTextSize(100);
		cardText.setGravity(Gravity.CENTER);
		r.addView(cardText);
		
		Button back = new Button(this);
		back.setText("Go back");
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reloadPlanningPoker();
			}
		});
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		back.setLayoutParams(params);
		r.addView(back);
		setContentView(r);
	}
	
	private void reloadPlanningPoker(){
		onCreate(null);
	}
}
