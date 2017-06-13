package com.pooyesh.appmanager.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import com.pooyesh.appmanager.R;


public final class AnalyticsTrackers {
    private final Context mContext;
    private final Map<Target, Tracker> mTrackers = new HashMap<Target, Tracker>();
    private static AnalyticsTrackers sInstance;

    private AnalyticsTrackers(Context context) {
        mContext = context.getApplicationContext();
    }

    public static synchronized void initialize(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Extra call to initialize analytics trackers");
        }

        sInstance = new AnalyticsTrackers(context);
    }

    public static synchronized AnalyticsTrackers getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Call initialize() before getInstance()");
        }
        return sInstance;
    }

    public synchronized Tracker get(Target target) {
        if (mTrackers != null) {
            Tracker tracker;
            switch (target) {
                case App:
                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
                    tracker.enableAutoActivityTracking(true);
                    tracker.enableExceptionReporting(true);
                    tracker.enableAdvertisingIdCollection(true);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
            mTrackers.put(target, tracker);
        }
        return mTrackers.get(target);
    }

    public enum Target {
        App
    }
}
