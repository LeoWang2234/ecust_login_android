package com.example.cheng.enet;

/**
 * Created by padeoe on 2016/6/1.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PrefFileManager.getAccountPref();
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("boot_launch", true) &&
                    PreferenceManager.getDefaultSharedPreferences(context).getBoolean("auto_connect",true)&&
                    sharedPreferences.getString("username", null) != null &&
                    sharedPreferences.getString("password", null) != null) {
            }
        }
    }
}
