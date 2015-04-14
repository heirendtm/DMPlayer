package com.example.bguise.dmplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class PlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private Intent intent;
    private YouTubePlayerView youTubeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Check for


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        youTubeView = (YouTubePlayerView)findViewById(R.id.youtube_player);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

/*
        View v = findViewById(R.id.view);
        View view = v.getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        startIntent(bitmap);

        registerReceiver(broadcastReceiver, new IntentFilter(BrightnessService.ACTION));*/
    }

    private void startIntent(Bitmap bitmap) {

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
        double brightness = 0.299*(double) (red/pixelCount / 255.0)+ 0.587*(double) (green / pixelCount / 255.0)+ .114*(double) (blue / pixelCount / 255.0);

        changeBrightness(brightness);
    }

    private void changeBrightness(double brightness) {
        TextView text = (TextView) findViewById(R.id.textView);
        text.setText("Brightness: "+(100*brightness));
       // WindowManager.LayoutParams lp = getWindow().getAttributes();
       // lp.screenBrightness =  (int) (brightness*100) / 100.0f;
        //getWindow().setAttributes(lp);
       // MyAsyncTask task = new MyAsyncTask();
       // task.setContext(this);
       // task.execute( (Integer) ((int) (brightness*100)));

        //android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, (int) (brightness*100));
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo("dQw4w9WgXcQ");
        }
        intent = new Intent(this, BrightnessService.class);

        startService(intent);//starts  service
        player.getCurrentTimeMillis();

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("Brightness")) {
                double brightness = (double) (Double.parseDouble(intent.getStringExtra("Brightness")));
                changeBrightness(20*brightness);
            }
        }
    };

    private void updateUI(Intent intent) {
        String brightness = intent.getStringExtra("brightness");
        changeBrightness(20*(double)(Double.parseDouble(brightness)));
    }



    /*
    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player);
    }*/
}
