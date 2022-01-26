package com.belicoffee.Service;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.belicoffee.Activity.MainActivity;
import com.belicoffee.Utility.NotificationsUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sender = remoteMessage.getData().get("sender");
        String receiver = remoteMessage.getData().get("receiver");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && receiver.equals(firebaseUser.getUid())) {
            String title = remoteMessage.getData().get("title");
            String boby = remoteMessage.getData().get("body");
            Uri avatarUri = Uri.parse(remoteMessage.getData().get("avatar"));
            RemoteMessage.Notification notification = remoteMessage.getNotification();

            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sender", sender);
            bundle.putInt("screenId", 6);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationsUtility.loadNotificationAvatar(getApplicationContext(), 0, title, boby, avatarUri, pendingIntent);
        }
    }
}
