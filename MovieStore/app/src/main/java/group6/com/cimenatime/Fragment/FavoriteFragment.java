package group6.com.cimenatime.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Activity.MainActivity;
import group6.com.cimenatime.Adapter.MoviesAdapter;
import group6.com.cimenatime.Interface.OnItemClickListener;
import group6.com.cimenatime.Interface.OnItemLongClickListener;
import group6.com.cimenatime.Interface.setOnLoadMoreListener;
import group6.com.cimenatime.Model.Movies;
import group6.com.cimenatime.Other.DatabaseHandler;
import group6.com.cimenatime.Other.Events;
import group6.com.cimenatime.R;

/**
 * Create by Dinh-Chuong
 */
public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    List<Movies> movieList = new ArrayList<>();
    MoviesAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean itemClick = true;
    List<Movies> movieListFavorite = new ArrayList<>();
    DatabaseHandler handler;
    EditText edt_Fragment;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Favorite");
        setHasOptionsMenu(true);
        Init(view);
        ManageRecyclerView();
        SwipeRefresh(view);
//        registerForContextMenu(recyclerView);
        return view;
    }

    private void Init(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.Fragment_Favorite_RecyclerView);
        handler = new DatabaseHandler(getContext());
        movieList = handler.getAllFavContacts();
        recyclerView.setHasFixedSize(true);
        edt_Fragment = (EditText) view.findViewById(R.id.Fragment_Favorite_RecyclerView_edt);

        edt_Fragment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchEngine();
                    return true;
                }
                return false;
            }
        });
        edt_Fragment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    recyclerView.setAdapter(adapter);
                } else {
                    searchEngine();
                }
                return false;
            }
        });
        edt_Fragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchEngine();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void searchEngine() {
        movieListFavorite.clear();
        movieList.clear();

        movieList.addAll(handler.getQueryFavContacts(edt_Fragment.getText().toString().toLowerCase().trim()));
        adapter.notifyDataSetChanged();

    }

    private void ManageRecyclerView() {


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MoviesAdapter(movieList, recyclerView, 2, R.layout.fragment_movielist_recycler_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onLongItemClick(View view, int position) {
                PopUpMenu(view, position);

            }
        });
        adapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
                viewPager.setCurrentItem(0);
                RecyclerViewEvent(position);
            }
        });
        adapter.setOnLoadMoreListener(new setOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        movieList.remove(movieList.size() - 1);
//                        adapter.notifyItemRemoved(movieList.size());
                        int index = movieList.size();
                        int end = index + 3;
                        for (int i = 0; i < end; i++) {
//                            Movies movie = new Movies("Mad Max: Fury Road", R.drawable.header, "2015", 7.0, true, true, "Phim mới lấy bối cảnh thế giới ở thời kỳ văn minh nhân loại đã sụp đổ, con người trở nên mất nhân tính và cuồng điên trong những trận chiến để giành giật sự sống, các nhu yếu phẩm như nước và xăng. Thế giới giờ là sa mạc mênh mông, con người chia làm nhiều phe chuyên giết nhau để tồn tại. Đứng giữa cuộc chạy đua vũ trang và phản kháng khốc liệt. Người hùng cô độc Max Rockatansky (Tom Hardy) trên đường lang thang để trốn chạy những ám ảnh từ quá khứ đã chạm trán đám quân lính (War Boys) của bạo chúa Immortan Joe (Hugh Keays-Byrne), không may, Max bị chúng bắt giữ làm tù binh. Anh bị biến thành “túi máu” cho một tên War Boys lái xe ốm yếu mang tên Nux (Nicholas Hoult).");
//                            movieList.add(movie);
//                            movie = new Movies("SpiderMan 3", R.drawable.spiderman, "2015", 7.0, true, true, "Phim Noi Ve SpiderMan");
//                            movieList.add(movie);
//                            movie = new Movies("Dr.Strange", R.drawable.drstrange, "2016", 7.0, true, true, "Phim Noi Ve Phu Thuy Toi Thuong");
//                            movieList.add(movie);
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();
                    }
                }, 2000);
            }
        });
    }

    private void RecyclerViewEvent(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("OverView", movieList.get(position).getMovie_overview());
        bundle.putString("ReleaseDay", movieList.get(position).getMovie_releaseday());
        bundle.putString("Poster", movieList.get(position).getMovie_poster());
        bundle.putDouble("Rating", movieList.get(position).getMovie_rating());
        bundle.putInt("Adult", movieList.get(position).isMovie_adult());
        bundle.putString("Title", movieList.get(position).getMovie_title());
        bundle.putInt("Favorite", movieList.get(position).isMovie_favorite());
        bundle.putInt("Id", movieList.get(position).getId());

        ((MainActivity) getActivity()).isBack = true;
        ((MainActivity) getActivity()).setToolbarIcon(true);

        DetailMoviesFragment detail = new DetailMoviesFragment();
        detail.setArguments(bundle);
        // transfer bundle to DetailMoviesFragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_container, detail);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Subscribe
    public void onEvent(Events.FavoriteFragmentMessage fav) {
        this.movieList.clear();
        this.movieList.addAll(fav.getMoviesList());
        adapter.notifyDataSetChanged();
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setNotification(movieList.size(), 1);
//        bottomNavigation.setOn
    }


    private void SwipeRefresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_refresh_1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
//                movieList = handler.getAllFavContacts();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void PopUpMenu(View view, final int pos) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_favorite_context);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_context_Delete:
                        adapter.setFavorite(true, pos);
                        break;
                    case R.id.menu_context_Cancel:
                        adapter.setFavorite(false, pos);
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menu_main_item_MovieList).setVisible(true);
        menu.findItem(R.id.menu_main_item_Favorite).setVisible(false);
        menu.findItem(R.id.menu_main_item_About).setVisible(true);
        menu.findItem(R.id.menu_main_item_Setting).setVisible(true);
        menu.findItem(R.id.menu_main_item_ChangeLayout).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setNotification(movieList.size(), 1);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
