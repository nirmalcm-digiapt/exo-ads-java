package com.digiapt.exoads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.exoplayer2.ui.PlayerView;

public class ExoVideoAdView extends RelativeLayout {

    private String TAG = "Kritter_Video_View";

    private PlayerView playerView;

    private Context context;
    private LayoutInflater layoutInflater;

    public ExoVideoAdView(Context context) {
        super(context);
    }

    public ExoVideoAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView getPlayerView(){

        context = this.getContext();

        layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.layout_kritter_video_ad, this, true);

        playerView = v.findViewById(R.id.player_view);

        return playerView;
    }
}
