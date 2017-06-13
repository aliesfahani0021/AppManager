package com.pooyesh.appmanager;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.pooyesh.appmanager.analytics.AnalyticsTrackers;
import com.pooyesh.appmanager.utils.AppPreferences;


public class StarterApplication extends Application {
    private static StarterApplication mInstance;

    private static AppPreferences sAppPreferences;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.App);
        context = getApplicationContext();
        sAppPreferences = new AppPreferences(this);

    }

    public static synchronized StarterApplication getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.App);
    }

    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public static AppPreferences getAppPreferences() {

        return sAppPreferences;
    }
}
