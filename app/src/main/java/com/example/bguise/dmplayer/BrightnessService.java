package com.example.bguise.dmplayer;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;

/**
 * Created by Michael Heirendt on 3/18/2015.
 */
public class BrightnessService extends IntentService {
    public static final String ACTION = "com.example.bguise.dmplayer";
    private final Handler handler = new Handler();
    Intent intent;
    String bm;
    double brightness;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BrightnessService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    //--------------------------------------------------------------------------

    //Initializes the intent to be used for the remainder of this service's lifetime
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(ACTION);
    }

    //uses a handler to start the first update to be sent to the activity
    @Override
    public void onStart(Intent intent, int startId) {
        bm = intent.getStringExtra("Bitmap");
        handler.postDelayed(sendUpdatesToUI, 100);
    }

    //function to be run using the handler
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
        }
    };

    //sets up the information to be displayed by putting in an intent
    private void DisplayLoggingInfo() {
        calculateBrightness();
        intent.putExtra("Brightness", ""+brightness);
        sendBroadcast(intent);
        this.stopSelf();
    }

    private void calculateBrightness() {
        Bitmap bitmap;
        try {
            byte [] encodeByte= Base64.decode(bm, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return;
        }
        long red = 0;
        long green = 0;
        long blue = 0;
        long pixelCount = 0;
        for (int y = bitmap.getHeight()/3; y < 2*bitmap.getHeight()/3; y+=5)
        {
            for (int x = 0; x < bitmap.getWidth(); x+=2)
            {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                red += Color.red(c);
                green += Color.green(c);
                blue += Color.blue(c);
            }
        }
        bitmap.recycle();
        brightness = 0.299*(double) (red/pixelCount / 255.0)+ 0.587*(double) (green / pixelCount / 255.0)+ .114*(double) (blue / pixelCount / 255.0);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }
}