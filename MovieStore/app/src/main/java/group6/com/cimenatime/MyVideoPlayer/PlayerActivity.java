/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group6.com.cimenatime.MyVideoPlayer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelections;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import group6.com.cimenatime.Model.Movies;
import group6.com.cimenatime.Model.MoviesURL;
import group6.com.cimenatime.R;

import static android.view.View.INVISIBLE;

/**
 * An activity that plays media using {@link SimpleExoPlayer}.
 */
public class PlayerActivity extends Activity implements OnClickListener, ExoPlayer.EventListener,
        TrackSelector.EventListener<MappedTrackInfo>, PlaybackControlView.VisibilityListener {

    public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";

    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
    public static final String EXTENSION_EXTRA = "extension";

    public static final String ACTION_VIEW_LIST =
            "com.google.android.exoplayer.demo.action.VIEW_LIST";
    public static final String URI_LIST_EXTRA = "uri_list";
    public static final String EXTENSION_LIST_EXTRA = "extension_list";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private Handler mainHandler;
    private Timeline.Window window;
    public SimpleExoPlayerView simpleExoPlayerView;
    private LinearLayout debugRootView;
    private Button retryButton;
    private ImageButton moreButton;
    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private MappingTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private DebugTextViewHelper debugViewHelper;
    private boolean playerNeedsSource;

    private boolean shouldAutoPlay;
    private boolean isTimelineStatic;
    private int playerWindow;
    private long playerPosition;
    ImageButton mFullscreenButton;
    public FrameLayout frameLayout;
    private FrameLayout frameVideo;
    private RecyclerView mRecyclerView;
    private RecycleViewVideoRelateAdapter recycleViewVideoRelateAdapter;
    ProgressBar mProgressBar;
    boolean key = true;
    RelativeLayout controlLayout;
    LinearLayout.LayoutParams params;
    LinearLayout.LayoutParams params2;
    public List<MoviesURL> listUri = new ArrayList<>();
    ImageButton previousButton, nextButton, playButton;
    ImageButton refreshButton;
    SuggestionFragment suggestionFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouldAutoPlay = true;
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();

        window = new Timeline.Window();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        setContentView(R.layout.player_activity);
        listUri.add(new MoviesURL("http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        View rootView = findViewById(R.id.root);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcl_list_video);
        rootView.setOnClickListener(this);
        debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        retryButton = (Button) findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.frame_content);
        frameVideo = (FrameLayout) findViewById(R.id.frame_video);

        controlLayout = (RelativeLayout) findViewById(R.id.controlLayout);
        suggestionFragment = new SuggestionFragment();
        moreButton = (ImageButton) findViewById(R.id.more_button);
        moreButton.setOnClickListener(this);

        previousButton = (ImageButton) findViewById(R.id.prev);
        previousButton.setOnClickListener(this);
        nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setOnClickListener(this);
        playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        refreshButton = (ImageButton) findViewById(R.id.refresh);
        refreshButton.setOnClickListener(this);

        mFullscreenButton = (ImageButton) findViewById(com.google.android.exoplayer2.R.id.fullscreen);
        mFullscreenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (key) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    key = false;
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    key = true;
                }

            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();
        Movies movies = new Movies();
        movies.setId(346672);
        movies.setMovie_title("Underworld: Blood Wars");
        movies.setMovie_favorite(0);
        movies.setMovie_adult(0);
        movies.setMovie_rating(4.2);
        movies.setMovie_overview("Underworld: Blood Wars follows Vampire death dealer, Selene, as she fends off brutal attacks from both the Lycan clan and the Vampire faction that betrayed her. With her only allies, David and his father Thomas, she must stop the eternal war between Lycans and Vampires, even if it means she has to make the ultimate sacrifice.");
        movies.setMovie_releaseday("2016-12-01");
        movies.setMovie_poster("http://image.tmdb.org/t/p/original//nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleViewVideoRelateAdapter = new RecycleViewVideoRelateAdapter(listUri, movies);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(recycleViewVideoRelateAdapter);
        recycleViewVideoRelateAdapter.setOnClickItemFavorite(new RecycleViewVideoRelateAdapter.onClickItemFavorite() {
            @Override
            public void selectFavorite(final String uri) {
                Toast.makeText(PlayerActivity.this, "" + uri, Toast.LENGTH_SHORT).show();
//                initializePlayer();
                initializePlayer2(Uri.parse(uri));
            }
        });

        params = (LinearLayout.LayoutParams) frameVideo.getLayoutParams();

        params2 = (LinearLayout.LayoutParams) frameVideo.getLayoutParams();


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
//        params.height = MainActivity.height / 4;
//        params2.height = MainActivity.width;

        switch (orientation) {

            case Configuration.ORIENTATION_LANDSCAPE:
                Toast.makeText(this, "LandScape", Toast.LENGTH_SHORT).show();
                frameLayout.setVisibility(INVISIBLE);
                mFullscreenButton.setImageResource(R.drawable.ic_fullscreen_exit);
                if(player!=null){
                    if(player.getPlaybackState()==ExoPlayer.STATE_ENDED){
                        getFragmentManager().beginTransaction().replace(R.id.suggestionContainer, suggestionFragment).commit();
                    }
                }

                key = false;
                break;


            case Configuration.ORIENTATION_PORTRAIT:
                Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();
                frameLayout.setVisibility(View.VISIBLE);
                mFullscreenButton.setImageResource(R.drawable.ic_fullscreen);


//                frameVideo.setLayoutParams(params);
                key = true;
                break;
        }


    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        isTimelineStatic = false;
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            frameLayout.setVisibility(INVISIBLE);
            mFullscreenButton.setImageResource(R.drawable.ic_fullscreen_exit);
            key = false;
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            frameLayout.setVisibility(View.VISIBLE);
            mFullscreenButton.setImageResource(R.drawable.ic_fullscreen);
            key = true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        } else {
            showToast(R.string.storage_permission_denied);
            finish();
        }
    }

    // OnClickListener methods

    @Override
    public void onClick(View view) {
        if (view == retryButton) {
            initializePlayer();
//        } else if (view.getParent() == debugRootView) {
//            trackSelectionHelper.showSelectionDialog(this, ((Button) view).getText(),
//                    trackSelector.getCurrentSelections().info, (int) view.getTag());
        } else if (view == moreButton) {
            PopupMenu popup = new PopupMenu(PlayerActivity.this, moreButton);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.menu_popup_player, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.player_share:
                            shareEvent();
                            break;

                    }
                    return true;
                }
            });

            popup.show();//showing popup menu
        } else if (view == nextButton) {
            next();
        } else if (view == previousButton) {
            previous();
        } else if (view == playButton) {
            player.setPlayWhenReady(!player.getPlayWhenReady());
        } else if (refreshButton == view) {
            player.seekTo(0);
        }
    }

    private void shareEvent() {
        player.setPlayWhenReady(false);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this link : " + listUri.get(0).getURL());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    // PlaybackControlView.VisibilityListener implementation

    @Override
    public void onVisibilityChange(int visibility) {
        if (player != null) {
            debugRootView.setVisibility(visibility);
            if (player.getPlaybackState() == ExoPlayer.STATE_ENDED) {
                controlLayout.setVisibility(INVISIBLE);
            } else {
                controlLayout.setVisibility(visibility);
            }
        }


    }

    // Internal methods

    public void initializePlayer() {

        Intent intent = getIntent();
        if (player == null) {
            boolean preferExtensionDecoders = true;
//                    intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
            UUID drmSchemeUuid = intent.hasExtra(DRM_SCHEME_UUID_EXTRA)
                    ? UUID.fromString(intent.getStringExtra(DRM_SCHEME_UUID_EXTRA)) : null;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (drmSchemeUuid != null) {
                String drmLicenseUrl = DRM_LICENSE_URL;
                String[] keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
                Map<String, String> keyRequestProperties;
                if (keyRequestPropertiesArray == null || keyRequestPropertiesArray.length < 2) {
                    keyRequestProperties = null;
                } else {
                    keyRequestProperties = new HashMap<>();
                    for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                        keyRequestProperties.put(keyRequestPropertiesArray[i],
                                keyRequestPropertiesArray[i + 1]);
                    }
                }
                try {
                    drmSessionManager = buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl,
                            keyRequestProperties);
                } catch (UnsupportedDrmException e) {
                    int errorStringId = Util.SDK_INT < 18 ? R.string.error_drm_not_supported
                            : (e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                            ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown);
                    showToast(errorStringId);
                    return;
                }
            }

            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            trackSelector.addListener(this);
            trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(),
                    drmSessionManager, preferExtensionDecoders);
            player.addListener(this);
            simpleExoPlayerView.setPlayer(player);
            if (isTimelineStatic) {
                if (playerPosition == C.TIME_UNSET) {
                    player.seekToDefaultPosition(playerWindow);
                } else {
                    player.seekTo(playerWindow, playerPosition);
                }
            }
            if (shouldAutoPlay == true) {
                player.setPlayWhenReady(shouldAutoPlay);
            } else {
                player.setPlayWhenReady(shouldAutoPlay);
            }


            debugViewHelper = new DebugTextViewHelper(player);
            debugViewHelper.start();
            playerNeedsSource = true;

        }
        if (playerNeedsSource) {
            Uri[] uris;
            String[] extensions;
//            if (ACTION_VIEW.equals(action)) {
//                uris = new Uri[]{intent.getData()};
//                extensions = new String[]{intent.getStringExtra(EXTENSION_EXTRA)};
//            } else if (ACTION_VIEW_LIST.equals(action)) {
            String[] uriStrings = new String[listUri.size()];
//            uriStrings = listUri.toArray(uriStrings);
            for (int j = 0; j < listUri.size(); j++) {
                uriStrings[j] = listUri.get(j).getURL();
            }
//                        intent.getStringArrayExtra(URI_LIST_EXTRA);
            uris = new Uri[uriStrings.length];
            for (int i = 0; i < uriStrings.length; i++) {
                uris[i] = Uri.parse(uriStrings[i]);
            }
            extensions = new String[uriStrings.length];
            extensions = uriStrings;

//            } else {
//                showToast(getString(R.string.unexpected_intent_action, action));
//                return;
//            }
            if (Util.maybeRequestReadExternalStoragePermission(this, uris)) {
                // The player will be reinitialized if the permission is granted.
                return;
            }
            MediaSource[] mediaSources = new MediaSource[uris.length];
            for (int i = 0; i < uris.length; i++) {
                mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
            }
            MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                    : new ConcatenatingMediaSource(mediaSources);
            player.prepare(mediaSource);
            playerNeedsSource = false;
//            updateButtonVisibilities();

        }
    }

    public void initializePlayer2(Uri uri) {
        // 1. Create a default TrackSelector
        if (player != null) {
            player.stop();
        }

        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();
        trackSelector.addListener(this);
        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        player.addListener(this);
        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);
        if (shouldAutoPlay == true) {
            player.setPlayWhenReady(shouldAutoPlay);
        } else {
            player.setPlayWhenReady(shouldAutoPlay);
        }
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "yourApplicationName"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
//        mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
//        MediaSource videoSource = new ExtractorMediaSource(uri,
//                dataSourceFactory, extractorsFactory, null, null);

        MediaSource videoSource = buildMediaSource(uri, uri.toString());
        // Prepare the player with the source.
        player.prepare(videoSource);
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {////////////////////////////////////////////////////////////////////////////
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
                : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, null);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid,
                                                                           String licenseUrl, Map<String, String> keyRequestProperties) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false), keyRequestProperties);
        return new StreamingDrmSessionManager<>(uuid,
                FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, null);
    }

    private void releasePlayer() {
        if (player != null) {
            debugViewHelper.stop();
            debugViewHelper = null;
            shouldAutoPlay = player.getPlayWhenReady();
            playerWindow = player.getCurrentWindowIndex();
            playerPosition = C.TIME_UNSET;
            Timeline timeline = player.getCurrentTimeline();
            if (timeline != null && timeline.getWindow(playerWindow, window).isSeekable) {
                playerPosition = player.getCurrentPosition();
            }
            player.release();
            player = null;
            trackSelector = null;
            trackSelectionHelper = null;
        }
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((DemoApplication) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    /**
     * Returns a new HttpDataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new HttpDataSource factory.
     */
    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((DemoApplication) getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    // ExoPlayer.EventListener implementation

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Do nothing.


    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updatePlayPauseButton();
        if (playbackState == ExoPlayer.STATE_ENDED) {

            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                getFragmentManager().beginTransaction().replace(R.id.suggestionContainer, suggestionFragment).commit();

            }
            showControls();
            refreshButton.setVisibility(View.VISIBLE);
        }
        if (playbackState == ExoPlayer.STATE_BUFFERING) {
            mProgressBar.setVisibility(View.VISIBLE);

        }
        if (playbackState == ExoPlayer.STATE_READY) {
            mProgressBar.setVisibility(View.GONE);
            refreshButton.setVisibility(View.INVISIBLE);
            getFragmentManager().beginTransaction().remove(suggestionFragment).commit();
        }
        if(playbackState == ExoPlayer.STATE_IDLE){

        }

//        updateButtonVisibilities();
    }

    @Override
    public void onPositionDiscontinuity() {
        // Do nothing.
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        isTimelineStatic = timeline != null && timeline.getWindowCount() > 0
                && !timeline.getWindow(timeline.getWindowCount() - 1, window).isDynamic;

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof DecoderInitializationException) {
                // Special case for decoder initialization failures.
                DecoderInitializationException decoderInitializationException =
                        (DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            showToast(errorString);
        }
        playerNeedsSource = true;
//        updateButtonVisibilities();
        showControls();
    }

    // MappingTrackSelector.EventListener implementation

    @Override
    public void onTrackSelectionsChanged(TrackSelections<? extends MappedTrackInfo> trackSelections) {
//        updateButtonVisibilities();
        MappedTrackInfo trackInfo = trackSelections.info;
        if (trackInfo.hasOnlyUnplayableTracks(C.TRACK_TYPE_VIDEO)) {
            showToast(R.string.error_unsupported_video);
        }
        if (trackInfo.hasOnlyUnplayableTracks(C.TRACK_TYPE_AUDIO)) {
            showToast(R.string.error_unsupported_audio);
        }
    }

    // User controls

    private void updateButtonVisibilities() {
        debugRootView.removeAllViews();


        retryButton.setVisibility(playerNeedsSource ? View.VISIBLE : View.GONE);
        debugRootView.addView(retryButton);
        debugRootView.addView(moreButton);

        if (player == null) {
            return;
        }

        TrackSelections<MappedTrackInfo> trackSelections = trackSelector.getCurrentSelections();
        if (trackSelections == null) {
            return;
        }

        int rendererCount = trackSelections.length;
        for (int i = 0; i < rendererCount; i++) {
            TrackGroupArray trackGroups = trackSelections.info.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(this);

                int label;
                switch (player.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.text;
                        break;
                    default:
                        continue;
                }
                button.setText(label);
                button.setTag(i);
                button.setOnClickListener(this);
                debugRootView.addView(button, debugRootView.getChildCount() - 1);
            }
        }
    }

    private void showControls() {
        debugRootView.setVisibility(View.VISIBLE);
        controlLayout.setVisibility(View.VISIBLE);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void next() {
        Timeline currentTimeline = player.getCurrentTimeline();
        if (currentTimeline == null) {
            return;
        }
        int currentWindowIndex = player.getCurrentWindowIndex();
        if (currentWindowIndex < currentTimeline.getWindowCount() - 1) {
            player.seekToDefaultPosition(currentWindowIndex + 1);
        } else if (currentTimeline.getWindow(currentWindowIndex, window, false).isDynamic) {
            player.seekToDefaultPosition();
        }
    }

    private void previous() {
        Timeline currentTimeline = player.getCurrentTimeline();
        if (currentTimeline == null) {
            return;
        }
        int currentWindowIndex = player.getCurrentWindowIndex();
        currentTimeline.getWindow(currentWindowIndex, window);
        if (currentWindowIndex > 0 && (player.getCurrentPosition() <= player.getDuration()
                || (window.isDynamic && !window.isSeekable))) {
            player.seekToDefaultPosition(currentWindowIndex - 1);
        } else {
            player.seekTo(0);
        }
    }

    private void updatePlayPauseButton() {
        boolean playing = player != null && player.getPlayWhenReady();
        String contentDescription = getResources().getString(
                playing ? com.google.android.exoplayer2.R.string.exo_controls_pause_description : com.google.android.exoplayer2.R.string.exo_controls_play_description);
        playButton.setContentDescription(contentDescription);
        playButton.setImageResource(
                playing ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
    }


}
