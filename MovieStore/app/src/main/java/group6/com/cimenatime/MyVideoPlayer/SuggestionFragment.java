package group6.com.cimenatime.MyVideoPlayer;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Model.MoviesURL;
import group6.com.cimenatime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionFragment extends Fragment {


    public SuggestionFragment() {
        // Required empty public constructor
    }

    RecyclerView mRecycler;
    private SuggestionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Integer> myDataset;
    FrameLayout frameLayout;
    List<MoviesURL> listUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);

        handleRecycler(view);
        return view;
    }

    private void handleRecycler(View view) {
        mRecycler = (RecyclerView) view.findViewById(R.id.RecyclerView1);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecycler.setHasFixedSize(true);
        frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
        // use a linear layout manager

        mLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false);
        mRecycler.setLayoutManager(mLayoutManager);
//        myDataset = new ArrayList<>();
        listUri = new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new SuggestionAdapter(listUri, mRecycler);
        mRecycler.setAdapter(mAdapter);
        prepareData2();
        mAdapter.setOnClickItemFavorite(new RecycleViewVideoRelateAdapter.onClickItemFavorite() {
            @Override
            public void selectFavorite(String uri) {
                ((PlayerActivity)getActivity()).initializePlayer2(Uri.parse(uri));
//                ((PlayerActivity)getActivity()).initializePlayer();
            }
        });
    }



    private void prepareData2() {
        listUri.add(new MoviesURL("http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/BigBuckBunny.m3u8", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        listUri.add(new MoviesURL("http://demos.webmproject.org/exoplayer/glass.mp4", "Underworld: Blood Wars", "/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg"));
        mAdapter.notifyDataSetChanged();
    }

}
