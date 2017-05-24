package group6.com.cimenatime.MyVideoPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import group6.com.cimenatime.R;

public class MainActivity extends Activity {

    public static List<String> listUri = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mRecycleViewAdapter;
    public static int width;
    public static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);

        width = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        height = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        listUri.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8");
        listUri.add("http://demos.webmproject.org/exoplayer/glass.mp4");
        listUri.add("http://html5demos.com/assets/dizzy.mp4");
        listUri.add("http://www.kaltura.com/p/309/sp/0/playManifest/entryId/1_rcit0qgs/format/applehttp/protocol/http/flavorParamId/301971/video.mp4");
        listUri.add("http://www.kaltura.com/p/309/sp/0/playManifest/entryId/1_rcit0qgs/format/url/flavorParamId/301971/video.mp4");
        listUri.add("http://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov/playlist.m3u8");
        listUri.add("https://www.hdwplayer.com/videos/300.mp4");
        listUri.add("http://demo.digi-corp.com/S2LWebservice/Resources/SampleVideo.mp4");
        listUri.add("http://bitcast-a.bitgravity.com/media/marketing/bg_live2_v2_1125_700_640x360.mp4");
        listUri.add("http://videocloud/video.mp4");
        listUri.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        listUri.add("http://www.androidbegin.com/tutorial/AndroidCommercial.3gp");
        listUri.add("https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4");

        mRecyclerView = (RecyclerView) findViewById(R.id.rcl_list_favorite);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleViewAdapter = new RecycleViewAdapter(listUri);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecycleViewAdapter);

        mRecycleViewAdapter.setOnClickItemFavorite(new RecycleViewAdapter.onClickItemFavorite() {
            @Override
            public void selectFavorite(final String uri) {
                Sample sample = new Sample(null, null, null, null, false) {
                    @Override
                    public Intent buildIntent(Context context) {
                        return super.buildIntent(context).setData(Uri.parse(uri))
                                .setAction(PlayerActivity.ACTION_VIEW);
                    }
                };
                startActivity(sample.buildIntent(MainActivity.this));
            }
        });
    }

    private abstract static class Sample {

        public final String name;
        public final boolean preferExtensionDecoders;
        public final UUID drmSchemeUuid;
        public final String drmLicenseUrl;
        public final String[] drmKeyRequestProperties;

        public Sample(String name, UUID drmSchemeUuid, String drmLicenseUrl,
                      String[] drmKeyRequestProperties, boolean preferExtensionDecoders) {
            this.name = name;
            this.drmSchemeUuid = drmSchemeUuid;
            this.drmLicenseUrl = drmLicenseUrl;
            this.drmKeyRequestProperties = drmKeyRequestProperties;
            this.preferExtensionDecoders = preferExtensionDecoders;
        }

        public Intent buildIntent(Context context) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra(PlayerActivity.PREFER_EXTENSION_DECODERS, preferExtensionDecoders);
            if (drmSchemeUuid != null) {
                intent.putExtra(PlayerActivity.DRM_SCHEME_UUID_EXTRA, drmSchemeUuid.toString());
                intent.putExtra(PlayerActivity.DRM_LICENSE_URL, drmLicenseUrl);
                intent.putExtra(PlayerActivity.DRM_KEY_REQUEST_PROPERTIES, drmKeyRequestProperties);
            }
            return intent;
        }

    }

}
