package group6.com.cimenatime.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Activity.MainActivity;
import group6.com.cimenatime.Adapter.MoviesAdapter;
import group6.com.cimenatime.Interface.OnItemClickListener;
import group6.com.cimenatime.Interface.setOnLoadMoreListener;
import group6.com.cimenatime.Model.Movies;
import group6.com.cimenatime.Other.DatabaseHandler;
import group6.com.cimenatime.Other.Events;
import group6.com.cimenatime.R;

/**
 * Create by HauDT
 */
public class MovieListFragment extends Fragment implements OnItemClickListener {
    RecyclerView recyclerView, recyclerGridView;
    List<Movies> movieList = new ArrayList<>();
    MoviesAdapter adapter;
    MoviesAdapter adapterGrid;
    SwipeRefreshLayout swipeRefreshLayout, swipeGridRefreshLayout;
    boolean itemClick = true;
    List<Movies> moviesList1;
    DatabaseHandler handler;
    public static final String JSON_URL1 = "http://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    private String preferenceName = "my_state";
    int alpha =0;
//public MovieListFragment()


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movielist, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Popular Movies");
        recyclerView = (RecyclerView) view.findViewById(R.id.Fragment_MovieList_RecyclerView);
        recyclerGridView = (RecyclerView) view.findViewById(R.id.Fragment_MovieList_RecyclerGridView);
        handler = new DatabaseHandler(getContext());
//        movieList = handler.getAllContacts();

        ManageRecyclerView();
        ManageRecyclerGridView();
        setHasOptionsMenu(true);
        SwipeRefresh(view);
        SwipeGridRefresh(view);
        adapter.SetOnItemClickListener(this);

        return view;
    }


    private void ManageRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MoviesAdapter(movieList, recyclerView, 0, R.layout.fragment_movielist_recycler_item);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        adapter.setOnLoadMoreListener(new setOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                movieList.add(null);
//                adapter.notifyItemInserted(movieList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        movieList.remove(movieList.size() - 1);
//                        adapter.notifyItemRemoved(movieList.size());
//                        int index = movieList.size();
//                        int end = index + 10;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                SharedPreferences preferences = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
//                                String URL = preferences.getString("URL", JSON_URL1);
//                                String last = URL.substring(URL.length()-1,URL.length());
//                                int a = Integer.parseInt(last);
//                                alpha =a;
//                                alpha++;
//                                URL = URL.substring(0,URL.length()-1) +alpha;
//                                new DownloadAPI(getContext()).readPreferencesState(URL);
                            }
                        });

//                        movieList.clear();
//                        movieList.addAll(moviesList1);
//                        adapter.notifyDataSetChanged();
//                        adapter.setLoaded();
                    }
                }, 2000);
            }
        });
    }

    private void RecyclerViewEvent(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("Title", movieList.get(position).getMovie_title());
        bundle.putString("OverView", movieList.get(position).getMovie_overview());
        bundle.putString("ReleaseDay", movieList.get(position).getMovie_releaseday());
        bundle.putString("Poster", movieList.get(position).getMovie_poster());
        bundle.putDouble("Rating", movieList.get(position).getMovie_rating());
        bundle.putInt("Adult", movieList.get(position).isMovie_adult());
        bundle.putInt("Favorite", movieList.get(position).isMovie_favorite());
        bundle.putInt("Id", movieList.get(position).getId());
        // transfer bundle to DetailMoviesFragment
        openDetailMoviesFragment(bundle);
    }


    public void openDetailMoviesFragment(Bundle bundle) {

        ((MainActivity) getActivity()).isBack = true;
        ((MainActivity) getActivity()).setToolbarIcon(true);

        MainActivity.isBundle = 1;
        DetailMoviesFragment detail = new DetailMoviesFragment();
        detail.setArguments(bundle);

        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_container, detail);
        trans.addToBackStack(null);
        trans.commit();
    }

    private void ManageRecyclerGridView() {

        RecyclerView.LayoutManager gLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerGridView.setLayoutManager(gLayoutManager);
        recyclerGridView.setItemAnimator(new DefaultItemAnimator());
        adapterGrid = new MoviesAdapter(movieList, recyclerGridView, 1, R.layout.fragment_movielist_gridrecycler_item);
        recyclerGridView.setAdapter(adapterGrid);
        recyclerGridView.setHasFixedSize(true);

        adapterGrid.SetOnItemClickListener(this);

//        });

        adapterGrid.setOnLoadMoreListener(new setOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int index = movieList.size();
                        int end = index + 10;

                        for (int i = 0; i < end; i++) {
//                            Movies movie = new Movies("Mad Max: Fury Road", R.drawable.header, "2015", 7.0, true, true, "Phim mới lấy bối cảnh thế giới ở thời kỳ văn minh nhân loại đã sụp đổ, con người trở nên mất nhân tính và cuồng điên trong những trận chiến để giành giật sự sống,");
//                            movieList.add(movie);
//                            movie = new Movies("SpiderMan 3", R.drawable.spiderman, "2015", 7.0, true, true, "Phim Noi Ve SpiderMan");
//                            movieList.add(movie);
//                            movie = new Movies("Dr.Strange", R.drawable.drstrange, "2016", 7.0, true, false, "Phim Noi Ve Phu Thuy Toi Thuong");
//                            movieList.add(movie);
                        }
                        adapterGrid.notifyDataSetChanged();
                        adapterGrid.setLoaded();
                    }
                }, 2000);
            }
        });
    }


    private void SwipeRefresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
//                movieList.clear();
//                movieList.addAll(handler.getAllContacts());
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void SwipeGridRefresh(View view) {
        swipeGridRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_Gridrefresh);
        swipeGridRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeGridRefreshLayout.setRefreshing(true);

                adapter.notifyDataSetChanged();
                swipeGridRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_item_ChangeLayout:
                if (itemClick) {
                    item.setIcon(R.drawable.ic_list);
                    recyclerGridView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    itemClick = false;
                } else {
                    item.setIcon(R.drawable.ic_grid_on);
                    recyclerGridView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    itemClick = true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onEvent(Events.MovieListFragmentMessage mov) {


    moviesList1 = mov.getMoviesList();

    movieList.clear();
    movieList.addAll(moviesList1);
    adapter.notifyDataSetChanged();


        }



    @Subscribe(sticky = true)
    public void onEvent(Events.MovieList2FragmentMassage mov2) {
        mov2.getMovies();
        for(int i=0;i<movieList.size();i++){
            if(movieList.get(i).getId() ==mov2.getMovies().getId()&&movieList.get(i).isMovie_favorite() !=mov2.getMovies().isMovie_favorite())
                movieList.set(i,mov2.getMovies());
        }


    }

    @Override
    public void onItemClick(View view, int position) {
        RecyclerViewEvent(position);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
