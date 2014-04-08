package se.chalmers.agile5.logic;

import android.content.Context;

public interface INotificationHandler {
	
	public void DisplayNotification(Context context, Class targetActivity, 
			String title, String text, String info);

}
