package com.aexyn.gcm_demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aexyn.gcm_demo.gcm_work.QuickstartPreferences;

public class Utils {

    public static final String PREF_GCM_DEVICE_ID = "gcm_device_id";

    public static String getGCMDeviceID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_GCM_DEVICE_ID, "");
    }

}
