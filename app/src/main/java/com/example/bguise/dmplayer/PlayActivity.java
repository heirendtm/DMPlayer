package com.example.bguise.dmplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class PlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private Intent intent;
    private final Handler handler = new Handler();
    private YouTubePlayerView youTubeView;
    private YouTubePlayer ytplayer;
    protected int frequency = 1;
    private int initialized = 0;

    BrightnessService mService;
    boolean mBound;

    ServiceConnection mConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBound = true;
            BrightnessService.LocalBinder binder = (BrightnessService.LocalBinder)service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            //mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Check for
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        youTubeView = (YouTubePlayerView)findViewById(R.id.youtube_player);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        Intent i = new Intent(this, BrightnessService.class);
        bindService(i, mConnection, BIND_AUTO_CREATE);

/*
        registerReceiver(broadcastReceiver, new IntentFilter(BrightnessService.ACTION));*/
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mBound){
            mService.unbindService(mConnection);
            mBound = false;
        }
    }

    private Runnable tick = new Runnable() {
        public void run() {
            handleFrame();
            handler.postDelayed(this, 1000/frequency);
        }
    };

    private void handleFrame(){


        if(ytplayer!=null && ytplayer.isPlaying()!=true){
            return;
        }
        else if(initialized == 0){
            int length = (int) (ytplayer.getDurationMillis()/1000);
            intent = new Intent(this, BrightnessService.class);
            intent.putExtra("dmtype", "initialize");
            intent.putExtra("length", length);
            intent.putExtra("frequency", frequency);
            intent.putExtra("id", "id");
            intent.putExtra("name", "name");
            startService(intent);//starts  service
            initialized = 1;
        }
        else {
            int duration = ytplayer.getDurationMillis();
            int time = ytplayer.getCurrentTimeMillis();
            if(time==0){
                return;
            }
            else {
                time = (int) (time/1000);

                View v = findViewById(R.id.view);
                View view = v.getRootView();
                v.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());

                v.setDrawingCacheEnabled(false);

               /* ImageView iv = (ImageView)findViewById(R.id.imageView);
                iv.setImageBitmap(bitmap);*/

                intent = new Intent(this, BrightnessService.class);
                intent.putExtra("dmtype", "frame");
                intent.putExtra("time", time);
                intent.putExtra("duration", duration);
                intent.putExtra("bitmap", bitmap);
                intent.putExtra("name", "name");

                int bright = mService.handleIntent(intent);
                changeBrightness(bright);
            }
        }
    }

    private void changeBrightness(int brightness) {
        TextView text = (TextView) findViewById(R.id.textView);
        text.setText("Brightness: "+(brightness));
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
        ytplayer = player;

        handler.removeCallbacks(tick);
        handler.postDelayed(tick, 1000/frequency);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }

    /*
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

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player);
    }*/
}
