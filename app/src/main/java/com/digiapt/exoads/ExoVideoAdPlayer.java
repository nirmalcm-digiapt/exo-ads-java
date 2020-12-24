package com.digiapt.exoads;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoVideoAdPlayer implements Player.EventListener {

    private LifecycleOwner lifeCycleOwner;

    private String adTagUrl;
    private String contentUrl;

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ImaAdsLoader adsLoader;

    private MyObserver myObserver;

    public ExoVideoAdPlayer(ExoVideoAdView kritterVideoAdView){
        this.playerView = kritterVideoAdView.getPlayerView();
        this.playerView.setControllerHideDuringAds(false);
        this.playerView.showController();
        this.lifeCycleOwner = (LifecycleOwner) playerView.getContext();
    }

    public void playVideo(){

        this.adTagUrl = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";
        this.contentUrl = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";

        lifeCycleOwner.getLifecycle().addObserver(new MyObserver());

    }

    private void initializePlayer() {

        if (adsLoader!=null&&playerView!=null) {
            adsLoader.release();
            releasePlayer();
        }

        // Create an AdsLoader with the ad tag url.
        adsLoader = new ImaAdsLoader((Context)lifeCycleOwner, Uri.parse(adTagUrl));

        // Create a SimpleExoPlayer and set is as the player for content and ads.
        player = new SimpleExoPlayer.Builder((Context)lifeCycleOwner).build();
        playerView.setPlayer(player);
        adsLoader.setPlayer(player);

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory((Context)lifeCycleOwner, Util.getUserAgent((Context)lifeCycleOwner, ((Context)lifeCycleOwner).getString(R.string.app_name)));

        ProgressiveMediaSource.Factory mediaSourceFactory =
                new ProgressiveMediaSource.Factory(dataSourceFactory);

        // Create the MediaSource for the content you wish to play.
        MediaSource mediaSource =
                mediaSourceFactory.createMediaSource(Uri.parse(contentUrl));

        // Create the AdsMediaSource using the AdsLoader and the MediaSource.
        AdsMediaSource adsMediaSource =
                new AdsMediaSource(mediaSource, dataSourceFactory, adsLoader, playerView);

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player.prepare(adsMediaSource);

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player.setPlayWhenReady(true);

        player.addListener(this);

        playerView.setControllerHideDuringAds(false);
    }

    private void releasePlayer() {
        adsLoader.setPlayer(null);
        playerView.setPlayer(null);
        player.release();
        player = null;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playWhenReady) {
            switch (playbackState){
                case Player.STATE_IDLE:
                    // free
                    break;
                case Player.STATE_BUFFERING:
                    // Buffer
                    break;
                case Player.STATE_READY:
                    if (playerView != null && player != null) {
                    }
                    break;
                case Player.STATE_ENDED:
                    break;
                default:
                    break;
            }
        }
    }


    public void play(){
        player.setPlayWhenReady(true);
    }

    public void pause(){
        player.setPlayWhenReady(false);
    }

    public void mute(){
        player.setVolume(0f);
    }

    public void unmute(){
        player.setVolume(1f);
    }

    public class MyObserver implements LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onStart() {
            if (Util.SDK_INT > 23) {
                initializePlayer();
                if (playerView != null) {
                    playerView.onResume();
                }
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
            if (Util.SDK_INT <= 23 || player == null) {
                initializePlayer();
                if (playerView != null) {
                    playerView.onResume();
                }
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause() {
            if (Util.SDK_INT <= 23) {
                if (playerView != null) {
                    playerView.onPause();
                }
                releasePlayer();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onStop() {
            if (Util.SDK_INT > 23) {
                if (playerView != null) {
                    playerView.onPause();
                }
                releasePlayer();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        protected void onDestroy() {
            adsLoader.release();
        }
    }

    public void destroy(){
        adsLoader.release();
        releasePlayer();
    }
}
