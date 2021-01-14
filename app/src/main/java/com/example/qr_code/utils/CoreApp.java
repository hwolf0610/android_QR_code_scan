package com.example.qr_code.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

public class CoreApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean isNetworkConnection(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}