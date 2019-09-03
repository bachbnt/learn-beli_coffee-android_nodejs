package com.belicoffee.Utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.belicoffee.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;

public class NotificationsUtility {
    static final String CHANNEL_ID = "default_id";
    static final String CHANNEL_NAME = "BeliCoffee";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription("Messaging");
            channel.setShowBadge(true);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    public static void loadNotificationAvatar(final Context context, final int notificationId, final String title, final String body, Uri avatarUri, final PendingIntent pendingIntent) {
        Glide.with(context)
                .asBitmap().load(avatarUri)
                .listener(new RequestListener<Bitmap>() {

                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                  createNotification(context, notificationId, title, body, BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_belicoffee), pendingIntent);
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                  createNotification(context, notificationId, title, body, resource, pendingIntent);
                                  return true;
                              }
                          }
                ).submit();
    }

    public static void createNotification(Context context, int notificationId, String title, String body, Bitmap avatar, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_belicoffee_small)
                    .setColor(context.getResources().getColor(R.color.colorDarkCyan))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setLargeIcon(avatar)
                    .setPriority(PRIORITY_HIGH)
                    .setVibrate(new long[]{500, 500})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        } else {
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_belicoffee_small)
                    .setColor(context.getResources().getColor(R.color.colorDarkCyan))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setLargeIcon(avatar)
                    .setPriority(PRIORITY_HIGH)
                    .setVibrate(new long[]{500, 500})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
    }
}
