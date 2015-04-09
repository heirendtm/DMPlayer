package com.example.bguise.dmplayer;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DeveloperKey devKey = new DeveloperKey();
        //ListView listView = (ListView)findViewById(R.id.video_list);

        Button luckyButton = (Button)findViewById(R.id.lucky_button);
        // Register onClick listener

        /*
        DemoArrayAdapter adapter = new DemoArrayAdapter(this, R.layout.list_item, activities);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        TextView disabledText = (TextView) findViewById(R.id.some_demos_disabled_text);
        disabledText.setText(getString(R.string.some_demos_disabled, android.os.Build.VERSION.SDK_INT));

        if (adapter.anyDisabled()) {
            disabledText.setVisibility(View.VISIBLE);
        } else {
            disabledText.setVisibility(View.GONE);
        }
        */
    }

    /** Called when user clicks the Calculator button */
    public void getLucky(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }



    /*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Demo clickedDemo = (Demo) activities.get(position);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getPackageName(), clickedDemo.className));
        startActivity(intent);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
