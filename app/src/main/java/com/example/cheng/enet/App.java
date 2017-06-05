package com.example.cheng.enet;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by padeoe on 2016/4/21.
 */
public class App extends Application {
    public static Context context;
    private static Boolean showNotification = null;
    private static Set<String> portalWiFiSSIDSet = null;
    private static Set<String> suspiciousWiFiSSIDSet = null;
    private static int maxTry = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    public static Context getAppContext() {
        return context;
    }



    public static void setShowNotification(Boolean showNotification) {
    }



    public static void addWiFiSSID(String SSID) {
    }

    public static void removeWiFiSSID(String SSID) {
    }







    public static int getMaxTry() {
        return maxTry == -1 ? Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("times_relogin", "4")) : maxTry;
    }

    public static void setMaxTry(int maxTry) {
        App.maxTry = maxTry;
    }
}
