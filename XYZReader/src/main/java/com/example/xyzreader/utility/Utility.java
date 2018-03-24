package com.example.xyzreader.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;


@SuppressWarnings("ALL")
public class Utility {

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null) && (networkInfo.getState() == NetworkInfo.State.CONNECTED);
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        return context != null && (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void applyColorBar(Context context, ActionBar bar) {
        if((context != null)&&(bar !=null)) {
            if (isOnline(context)) {
                bar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } else {
                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(Costants.COLOR_BACKGROUND_ACTIONBAR_OFFLINE)));
            }
        }
    }
}
