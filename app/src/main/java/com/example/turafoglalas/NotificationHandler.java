package com.example.turafoglalas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private NotificationManager mManager;
    private Context mContext;
    private static final String CHANNEL_ID = "tura_notification_channel";
    private final int NOTIFICATION_ID = 0;
    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Tura Notification", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Notifications from Turafoglalas app.");
        mManager.createNotificationChannel(channel);
    }

    public void send(String message){
        Intent intent = new Intent(mContext, TuraListActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        if (mContext != null && mManager != null){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID).setContentTitle("Turafoglalas app").setContentText(message).setSmallIcon(R.drawable.ic_hiker).setContentIntent(pendingIntent).setAutoCancel(true);;

            mManager.notify(NOTIFICATION_ID, builder.build());
        }

    }
}
