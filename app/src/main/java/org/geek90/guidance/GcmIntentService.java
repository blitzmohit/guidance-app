package org.geek90.guidance;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
public class GcmIntentService extends IntentService{
	Context context;
	private static final int NOTIFICATION_ID = 1;
	NotificationCompat.Builder builder;
    private static final String TAG = "GCM Demo";

	public GcmIntentService() {
		super("GcmIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
//		String msg = intent.getStringExtra("message");
		String msg = "Today's guidance is now available";
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		 if (!extras.isEmpty()) {

			 if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
	                sendNotification("Send error: " + extras.toString());
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_DELETED.equals(messageType)) {
	                sendNotification("Deleted messages on server: " +
	                        extras.toString());
	            // If it's a regular GCM message, do some work.
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
	                // This loop represents the service doing some work.
	                for (int i=0; i<5; i++) {
	                    Log.i(TAG, "Working... " + (i+1)
	                            + "/5 @ " + SystemClock.elapsedRealtime());
	                    try {
	                        Thread.sleep(500);
	                    } catch (InterruptedException e) {
                            e.printStackTrace();
	                    }
	                }
	                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
	                // Post notification of received message.
	                //sendNotification("Received: " + extras.toString());
	                sendNotification(msg);
	                Log.i(TAG, "Received: " + extras.toString());
	            }
	        }
		 GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	private void sendNotification(String msg) {
		NotificationManager mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(this, Welcome.class);
        myintent.putExtra("message", msg);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        		myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.small_logo)
        .setContentTitle("LMG Guidance")
        .setAutoCancel(true)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}