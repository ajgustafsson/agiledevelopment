package se.chalmers.agile5.logic;

import se.chalmers.agile5.R;
import se.chalmers.agile5.activities.MyActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NotificationHandler implements INotificationHandler {

	public void DisplayNotification(Context context, Class targetActivity, 
			String title, String text, String info) {
		Bitmap largeIcon = 
				BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		
		Notification.Builder mBuilder =
		        new Notification.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title)
		        .setContentText(text)
		        .setContentInfo(info)
		        .setLargeIcon(largeIcon);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, targetActivity);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MyActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(1, mBuilder.build());
		
	}
}
