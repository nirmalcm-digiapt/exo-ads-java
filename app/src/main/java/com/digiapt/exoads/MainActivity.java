package com.digiapt.exoads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPlayer();

        Button loadVideo = findViewById(R.id.loadVideo);
        loadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
    }

    ExoVideoAdPlayer exoVideoAdPlayer;
    ExoVideoAdView exoVideoAdView;
    private void initPlayer() {
        exoVideoAdView = findViewById(R.id.exo_video_ad_view);
        exoVideoAdPlayer = new ExoVideoAdPlayer(exoVideoAdView);
    }

    private void playVideo() {
        exoVideoAdPlayer.playVideo();
    }
}
