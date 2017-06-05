package com.example.cheng.enet;

import android.content.SharedPreferences;

/**
 * Created by padeoe on 2016/4/21.
 */
public class PrefFileManager {
    public static SharedPreferences getAccountPref() {
        return App.context.getSharedPreferences("DataFile", 0);
    }

    public static SharedPreferences getWiFiPref() {
        return App.context.getSharedPreferences("WiFiSSID", 0);
    }
}
