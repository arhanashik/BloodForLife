package com.blackspider.bloodforlife.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.blackspider.bloodforlife.receiver.ConnectivityReceiver;

/**
 * Created by Mr blackSpider on 14-feb-2018.
 */

public class ConnectivityController extends Application {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static ConnectivityController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ConnectivityController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}