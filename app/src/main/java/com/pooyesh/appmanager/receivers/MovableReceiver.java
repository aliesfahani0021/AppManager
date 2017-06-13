package com.pooyesh.appmanager.receivers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.pooyesh.appmanager.R;
import com.pooyesh.appmanager.activity.MainActivity;


public class MovableReceiver extends BroadcastReceiver {
    private Context context;
    String applicationName;
    ApplicationInfo ai;
    PackageInfo packageInfo;

    @Override
    public void onReceive(Context context, Intent intent) {


        this.context = context;
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        final PackageManager pm = context.getPackageManager();
        try {
            ai = pm.getApplicationInfo(packageName, 0);
            packageInfo = pm.getPackageInfo(ai.packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        new getInstalledApps().execute();

    }

    class getInstalledApps extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            final PackageManager packageManager = context.getPackageManager();
            // Get Sort Mode

            if (((ai.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != ApplicationInfo.FLAG_EXTERNAL_STORAGE)) {
                if (packageInfo.installLocation == PackageInfo.INSTALL_LOCATION_AUTO
                        || packageInfo.installLocation == PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL) {
                    if (!(packageManager.getApplicationLabel(packageInfo.applicationInfo).equals("") || packageInfo.packageName.equals(""))) {

                        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                            try {
                                PendingIntent contentIntent =
                                        PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
                                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(context);
                                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.bell);
                                Notification notification = builder
                                        .setContentTitle(applicationName)
                                        .setContentText("This app can be move to sd card")
                                        .setSmallIcon(getNotificationIcon())
                                        .setLargeIcon(bm)
                                        .setSound(uri)
                                        .setContentIntent(contentIntent)
                                        .setColor(Color.parseColor("#4B8A08"))
                                        .setPriority(Notification.PRIORITY_HIGH)
                                        .build();

                                NotificationManagerCompat notificationManager =
                                        NotificationManagerCompat.from(context);

                                notificationManager.notify(0x1234, notification);
                            } catch (OutOfMemoryError e) {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_star_white : R.drawable.bell;
    }
}

