package com.crux.qxm.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.crux.qxm.R;
import com.crux.qxm.utils.StaticValues;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.Objects;

import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubePlaybackFragment extends DialogFragment {


    String youtubeLink;

    YouTubePlayerSupportFragment youTubePlayerSupportFragment;

    public static YoutubePlaybackFragment newInstance(String youtubeLink) {

        Bundle args = new Bundle();
        args.putString(YOUTUBE_LINK_KEY,youtubeLink);
        YoutubePlaybackFragment fragment = new YoutubePlaybackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public YoutubePlaybackFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        // Get existing layout params for the window
//
//        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
//        // Assign window properties to fill the parent
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//        // Call super onResume after sizing
//
//        super.onResume();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // binding view with butterKnife
        View view = inflater.inflate(R.layout.fragment_youtube_playback, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        youTubePlayerSupportFragment = (YouTubePlayerSupportFragment) Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.youtube_fragment);
        if (getArguments() != null) {

            youtubeLink = getArguments().getString(YOUTUBE_LINK_KEY);
            if(youtubeLink!=null){
                if(getYouTubeVideoIDfromURL(youtubeLink)!=null){

                    String youTubeVideoId= getYouTubeVideoIDfromURL(youtubeLink);
                    youTubePlayerSupportFragment.initialize(StaticValues.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


                            youTubePlayer.loadVideo(youTubeVideoId);
                            youTubePlayer.play();
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), "YoutubeId is null", Toast.LENGTH_SHORT).show();
                }
            }else {

                Toast.makeText(getContext(), "YoutubeLink is null!", Toast.LENGTH_SHORT).show();
            }

        }

    }




    //region getYouTubeVideoIDfromURL
    private String getYouTubeVideoIDfromURL(String youTubeLink){

        if (!youTubeLink.isEmpty() && youTubeLink.length() > 11) {

            return youTubeLink.substring(youTubeLink.length()-11);
        }

        return null;
    }
    //endregion
}
