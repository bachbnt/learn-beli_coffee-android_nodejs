package com.belicoffee.Utility;

import android.app.Application;
import android.util.Log;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationsUtility.createNotificationChannel(this);
        Log.i("tag","notification_channel");
    }
}
