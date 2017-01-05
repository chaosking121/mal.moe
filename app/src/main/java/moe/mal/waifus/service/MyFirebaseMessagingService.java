package moe.mal.waifus.service;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import moe.mal.waifus.R;

/**
 * Extended class to handle incoming foreground notifications
 * Created by Arshad on 04/01/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());
    }

    /**
     * Show a notification with the given title and body
     */
    private void sendNotification(String messageTitle, String messageBody) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_favorite_black_24dp))
                .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}