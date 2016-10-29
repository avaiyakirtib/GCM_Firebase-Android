package com.aexyn.gcm_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aexyn.gcm_demo.gcm_work.QuickstartPreferences;
import com.aexyn.gcm_demo.gcm_work.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Demonstrate the use of Google Location Api to know the last known location of device
 * <p>
 * First of all we need to create a project at google console using this projects name and package name
 * Reference :- http://stackoverflow.com/questions/34367870/where-do-i-get-a-google-services-json
 * After successfully registered google will provide the link for GoogleServices.json file
 * Download it and copy/paste it to the app folder of the project
 * <p>
 * Then Go to the manifest file and write required code there - See AndroidManifest file
 * <p>
 * onLocationChanged(Location location)  // This method will return location of the device
 * <p>
 * put all the code written here as it is to the desired class when needed
 * GoogleApiClient need Minimum api is 15 (Just for info)
 */

/**
 * Just copy and paste GoogleServices.json file And Copy/paste whole code as it is
 * <p>
 * just make change at @MyGcmListenerService.class inside onMessageReceived method
 * Put required code as per key-value data from Bundle.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9580;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    String gcmDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.e(TAG, "Registered Successfully");
                } else {
                    Log.e(TAG, "Registered Error");
                }
            }
        };
        gcmDeviceId = Utils.getGCMDeviceID(MainActivity.this);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
