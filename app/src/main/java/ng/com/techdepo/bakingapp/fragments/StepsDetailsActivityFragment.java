package ng.com.techdepo.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ng.com.techdepo.bakingapp.R;

import static ng.com.techdepo.bakingapp.activities.MainActivity.isTablet;
import static ng.com.techdepo.bakingapp.fragments.StepsActivityFragment.steps;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepsDetailsActivityFragment extends Fragment implements ExoPlayer.EventListener{

    private static final String TAG = StepsDetailsActivityFragment.class.getSimpleName();

    private TextView longDescription;
    private Button prev;
    private Button next;
    protected static int index = 0;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView playerView;
    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private static long position = 0;

    public StepsDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_details, container, false);

        longDescription = (TextView) view.findViewById(R.id.description);
        prev = (Button) view.findViewById(R.id.prev);
        next = (Button) view.findViewById(R.id.next);
        playerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        initializeMediaSession();
        initializePlayer(Uri.parse(steps.get(index).getVideoURL()));

        if (!isTablet) {
            index = getActivity().getIntent().getExtras().getInt("item");
        }

        getActivity().setTitle(steps.get(index).getShortDescription());
        longDescription.setText(steps.get(index).getDescription());

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) {
                    index--;
                    longDescription.setText(steps.get(index).getDescription());
                    restExoPlayer(0, false);
                    initializePlayer(Uri.parse(steps.get(index).getVideoURL()));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < steps.size() - 1) {
                    index++;
                    longDescription.setText(steps.get(index).getDescription());
                    restExoPlayer(0, false);
                    initializePlayer(Uri.parse(steps.get(index).getVideoURL()));
                }
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
            hideSystemUI();
            playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            //restExoPlayer(position, true);
            longDescription.setVisibility(View.GONE);
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }
        return view;
    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MySessionCallback());
        mediaSession.setActive(true);
    }


    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "StepsDetailsActivityFragment");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            restExoPlayer(position, false);
        }
    }


    private void restExoPlayer(long position, boolean playWhenReady) {
        this.position = position;
        exoPlayer.seekTo(position);
        exoPlayer.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        exoPlayer.setPlayWhenReady(false);
        mediaSession.setActive(false);

    }

    @Override
    public void onStop() {
        super.onStop();

        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaSession.setActive(true);
    }



    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            position = exoPlayer.getCurrentPosition();
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            restExoPlayer(0, false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }
}
