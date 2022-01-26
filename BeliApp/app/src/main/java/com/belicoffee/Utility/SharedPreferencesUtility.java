package com.belicoffee.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtility {
    public static void savePreferences(Context context, boolean login, String id, String email) {
        SharedPreferences preferences = context.getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("login", login);
        editor.putString("myId", id);
        editor.putString("myEmail", email);
        editor.commit();
    }

    public static boolean loadLoginPreferences(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("caches", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static String loadIdPreferences(Context context, String key){
        SharedPreferences preferences=context.getSharedPreferences("caches",Context.MODE_PRIVATE);
        return preferences.getString(key,null);
    }

    public static String loadEmailPreferences(Context context, String key){
        SharedPreferences preferences=context.getSharedPreferences("caches",Context.MODE_PRIVATE);
        return preferences.getString(key,null);
    }

    public static void removePreferences(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clearPreferences(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
