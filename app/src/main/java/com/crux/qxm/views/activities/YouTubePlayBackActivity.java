package com.crux.qxm.views.activities;

import android.os.Bundle;
import android.util.Log;

import com.crux.qxm.R;
import com.crux.qxm.utils.userSessionTrackerHelper.UserActivitySessionTrackerHelper;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Calendar;

import static com.crux.qxm.utils.StaticValues.YOUTUBE_API_KEY;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

public class YouTubePlayBackActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private static final String TAG = "YouTubePlayBackActivity";
    //declaring youtube player
    YouTubePlayerView youTubePlayerView;
    String videoId;
    String youTubeLink;
    private YouTubePlayer mYouTubePlayer;
    private long startTime = 0;
    private UserActivitySessionTrackerHelper activitySessionTrackerHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_play_back);

        activitySessionTrackerHelper = new UserActivitySessionTrackerHelper();
        youTubePlayerView = findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(YOUTUBE_API_KEY, this);
        youTubeLink = getIntent().getStringExtra(YOUTUBE_LINK_KEY);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (!b) {

            if (youTubeLink != null) {
                mYouTubePlayer = youTubePlayer;
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                youTubePlayer.setPlaybackEventListener(playbackEventListener);

                videoId = getYouTubeVideoIDfromURL(youTubeLink);
                youTubePlayer.cueVideo(videoId);
            }

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
            Log.d(TAG, "onLoaded: ");
            mYouTubePlayer.play();

        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };


    //region getYouTubeVideoIDfromURL
    private String getYouTubeVideoIDfromURL(String youTubeLink) {

        if (!youTubeLink.isEmpty() && youTubeLink.length() > 11) {
            return youTubeLink.substring(youTubeLink.length() - 11);
        }
        return null;
    }
    //endregion


    @Override
    protected void onStart() {
        super.onStart();
        startTime = Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "onStart: startTime: " + startTime);
    }

    @Override
    protected void onStop() {
        super.onStop();
        long endTime = Calendar.getInstance().getTimeInMillis();
        activitySessionTrackerHelper.saveYouTubePlayBackActivitySession(startTime, endTime);
        Log.d(TAG, "onStop: activitySession: " + activitySessionTrackerHelper.getActivitySessionTracker().toString());
    }
}
