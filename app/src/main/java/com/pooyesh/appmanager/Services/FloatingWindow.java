package com.pooyesh.appmanager.Services;


import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.pooyesh.appmanager.R;

public class FloatingWindow extends Service {

    private WindowManager windowManager;
    private TextView chatHead;
    private ImageView closeServiceImg;
    private boolean flag = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        chatHead = new TextView(this);
        closeServiceImg = new ImageView(this);

        chatHead.setText("Scroll to Storage section, '" + "Move to SD Card " + "' button will move app to SD card, or move app" +
                " to internal storage by tapping '" + "Move to Device Storage " + "' button. ");
        chatHead.setBackgroundColor(Color.parseColor("#512DA8"));

        chatHead.setTextColor(Color.WHITE);
        chatHead.setPadding(15, 15, 15, 15);

        closeServiceImg.setImageResource(R.drawable.warning);


        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        params.x = 0;
        params.y = 100;
        windowManager.addView(chatHead, params);
        final WindowManager.LayoutParams params1 = new WindowManager.LayoutParams(
                48,
                48,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params1.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        chatHead.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!flag) {
                    chatHead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    flag = true;
                    params1.y = params.y + chatHead.getHeight() - dpToPx(10);
                    params1.x = dpToPx(10);
                    windowManager.addView(closeServiceImg, params1);
                }
            }
        });
        closeServiceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

     /*   try {

            chatHead.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            // Get current time in nano seconds.

                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(chatHead, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
        if (closeServiceImg != null) windowManager.removeView(closeServiceImg);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
