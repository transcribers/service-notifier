package com.example.iotsolutions.servicenotifier.utility;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.iotsolutions.servicenotifier.R;
import com.example.iotsolutions.servicenotifier.message_display;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
	private static final int BROADCAST_NOTIFICATION_ID = 1;

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

		String notificationBody = "";
		String notificationTitle = "";
		String notificationData = "";
		try{
		   notificationData = remoteMessage.getData().toString();
		   notificationTitle = remoteMessage.getNotification().getTitle();
		   notificationBody = remoteMessage.getNotification().getBody();
		}catch (NullPointerException e){
		   Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage() );
		}
		Log.d(TAG, "onMessageReceived: data: " + notificationData);
		Log.d(TAG, "onMessageReceived: notification body: " + notificationBody);
		Log.d(TAG, "onMessageReceived: notification title: " + notificationTitle);


		String dataType = remoteMessage.getData().get(getString(R.string.data_type));
		if(dataType.equals(getString(R.string.direct_message))){
			Log.d(TAG, "onMessageReceived: new incoming message.");
			String title = remoteMessage.getData().get(getString(R.string.data_title));
			String message = remoteMessage.getData().get(getString(R.string.data_message));
			sendMessageNotification(title, message);
		}
    }

	/**
	 * Build a push notification for a chat message
	 * @param title
	 * @param message
	 */
	private void sendMessageNotification(String title, String message){
		Log.d(TAG, "sendChatmessageNotification: building a chatmessage notification");

		//get the notification id
		int notificationId = new Random().nextInt(800000);

		// Instantiate a Builder object.
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
				getString(R.string.default_notification_channel_id));
		// Creates an Intent for the Activity




		Intent pendingIntent = new Intent(this,message_display.class);
		pendingIntent.putExtra("title",title);
		pendingIntent.putExtra("message",message);
        Random generator = new Random();
	//	startActivity(pendingIntent);

		// Sets the Activity to start in a new, empty task
		pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Creates the PendingIntent
		PendingIntent notifyPendingIntent =
				PendingIntent.getActivity(
						this,
						generator.nextInt(),
						pendingIntent,
						PendingIntent.FLAG_UPDATE_CURRENT
				);

		//add properties to the builder
		builder.setSmallIcon(R.mipmap.ic_launcher_round)
				.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
						R.mipmap.ic_launcher_round))
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentTitle("Reminder By Service Notifier")
				.setColor(getColor(R.color.colorAccent))
				.setAutoCancel(true)
				.setSubText(message)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setOnlyAlertOnce(true)
				// Set the intent that will fire when the user taps the notification
				.setContentIntent(notifyPendingIntent)
				.build();

		builder.setContentIntent(notifyPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(notificationId, builder.build());
//		PendingIntent.getBroadcast(this, 0, pendingIntent,
//				PendingIntent.FLAG_UPDATE_CURRENT).cancel();

	}




}













