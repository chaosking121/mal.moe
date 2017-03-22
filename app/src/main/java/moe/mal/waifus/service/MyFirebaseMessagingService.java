package moe.mal.waifus.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import moe.mal.waifus.R;
import moe.mal.waifus.activity.ImageActivity;

/**
 * Extended class to handle incoming foreground notifications
 * Created by Arshad on 04/01/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_favorite_black_24dp))
                .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setAutoCancel(false);


        if (message.contains("new images saved to")) {
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra("waifu", message.split(" ")[5].replace(".", ""));
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder = builder.addAction(R.drawable.ic_open_in_new_black_24dp, "View Waifu", pIntent);
        }

        if (message.contains("Image saved as")) {
            //Construct View Waifu Button
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra("waifu", message.split(" ")[5].replace(".", ""));
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder = builder.addAction(R.drawable.ic_open_in_new_black_24dp, "View Waifu", pIntent);

            //Construct View Image Button
            Intent intent2 = new Intent(this, ImageActivity.class);
            intent2.putExtra("waifu", message.split(" ")[5].replace(".", ""));
            intent2.putExtra("imageNum", message.split(" ")[3].replace("#", ""));
            PendingIntent pIntent2 = PendingIntent.getActivity(this, 10, intent2,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder = builder.addAction(R.drawable.ic_image_black_24dp, "View Image", pIntent2);
        }

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());

    }
}