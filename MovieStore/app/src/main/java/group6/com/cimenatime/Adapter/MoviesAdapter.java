package group6.com.cimenatime.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Interface.OnItemClickListener;
import group6.com.cimenatime.Interface.OnItemLongClickListener;
import group6.com.cimenatime.Interface.setOnLoadMoreListener;
import group6.com.cimenatime.Model.Movies;
import group6.com.cimenatime.Other.DatabaseHandler;
import group6.com.cimenatime.Other.Events;
import group6.com.cimenatime.R;

//import org.greenrobot.eventbus.EventBus;

/**
 * Created by Dinh-Chuong on 04/2/2017.
 */
public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movies> moviesList;
    static List<Movies> moviesList1 ;
    private RecyclerView recyclerView;
    /**
     * typeList = 0 : movie list Type List
     * typeList = 1 :  movie list Type Grid
     * typeList = 2 : Favorite List Type List
     */
    private int typeList;
    Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private setOnLoadMoreListener mOnLoadMoreListener;
    private int resources;
    public  boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    OnItemClickListener mItemClickListener;
    OnItemLongClickListener mItemLongClickListener;

    public MoviesAdapter(List<Movies> moviesList, RecyclerView recyclerView, int typeList, int resources) {
        this.moviesList = moviesList;

        this.recyclerView = recyclerView;
        this.typeList = typeList;
        this.resources = resources;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;

                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return moviesList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMoreListener(setOnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == VIEW_TYPE_ITEM) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(resources, parent, false);

            MyViewHolder viewHolder = new MyViewHolder(contactView);
            return viewHolder;
        } else if (viewType == VIEW_TYPE_LOADING) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.layout_loading_item, parent, false);

            LoadingViewHolder loadingviewHolder = new LoadingViewHolder(contactView);
            return loadingviewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            final Movies movies = moviesList.get(position);

            final int pos = holder.getPosition();
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            if (typeList == 0) {
                moviesList1 = new ArrayList<>();
                moviesList1.addAll(moviesList);
                myViewHolder.txvTitle.setText(movies.getMovie_title());
                myViewHolder.txvRating.setText(String.valueOf(movies.getMovie_rating()));
                myViewHolder.txvDes.setText(movies.getMovie_overview());
                myViewHolder.txtReleaseday.setText(movies.getMovie_releaseday());
                if (moviesList.get(position).isMovie_adult() == 1) {
                    myViewHolder.imgAdult.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.imgAdult.setVisibility(View.INVISIBLE);
                }
                if (moviesList.get(position).isMovie_favorite() == 1) {
                    myViewHolder.chkLike.setChecked(true);
                } else {
                    myViewHolder.chkLike.setChecked(false);
                }

                myViewHolder.chkLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myViewHolder.chkLike.isChecked()) {
                            myViewHolder.chkLike.setChecked(true);
                            moviesList.get(pos).setMovie_favorite(1);
//                            new DatabaseHandler(context).updateFavorite(1, moviesList.get(pos).getId());
                            new DatabaseHandler(context).addMovie(moviesList.get(pos));


                        } else {

                            myViewHolder.chkLike.setChecked(false);
                            moviesList.get(pos).setMovie_favorite(0);
//                            new DatabaseHandler(context).updateFavorite(0, moviesList.get(pos).getId());
                            new DatabaseHandler(context).deleteContact(moviesList.get(pos));
                        }
                        Events.FavoriteFragmentMessage fav = new Events.FavoriteFragmentMessage();
                        fav.setMoviesList(new DatabaseHandler(context).getAllFavContacts());
                        EventBus.getDefault().post(fav);
                        notifyDataSetChanged();
                    }
                });


                Picasso.with(context).load(movies.getMovie_poster()).placeholder(R.drawable.progress_animation).resize(100, 110).into(((MyViewHolder) holder).imgFilm);
            }
            if (typeList == 1) {
                myViewHolder.txvTitle.setText(movies.getMovie_title());
                Picasso.with(context).load(movies.getMovie_poster()).placeholder(R.drawable.progress_animation).resize(150, 180).into(((MyViewHolder) holder).imgFilm);


            }
            if (typeList == 2) {


                myViewHolder.txvTitle.setText(movies.getMovie_title());
                myViewHolder.txvRating.setText(String.valueOf(movies.getMovie_rating()));
                myViewHolder.txvDes.setText(movies.getMovie_overview());
                myViewHolder.txtReleaseday.setText(movies.getMovie_releaseday());
                if (moviesList.get(position).isMovie_adult() == 1) {
                    myViewHolder.imgAdult.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.imgAdult.setVisibility(View.INVISIBLE);
                }

                if (moviesList.get(position).isMovie_favorite() == 1) {
                    myViewHolder.chkLike.setChecked(true);

                } else {
                    myViewHolder.chkLike.setChecked(false);
                    moviesList.get(pos).setMovie_favorite(0);
                }
                myViewHolder.chkLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog(myViewHolder, pos, movies);


                    }
                });
                Picasso.with(context).load(movies.getMovie_poster()).placeholder(R.drawable.progress_animation).resize(100, 110).into(((MyViewHolder) holder).imgFilm);


            }

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadViewHolder = (LoadingViewHolder) holder;
            loadViewHolder.loading_progressBar.setIndeterminate(true);

        }
    }

    @Override
    public int getItemCount() {
        if (moviesList == null) {
            return 0;
        } else {
            return moviesList.size();
        }
//                moviesList == null ? 0 : moviesList.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar loading_progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            loading_progressBar = (ProgressBar) itemView.findViewById(R.id.Loading_Item_Progress);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txvTitle, txtReleaseday, txvRating, txvDes;
        ImageView imgFilm, imgAdult;
        CheckBox chkLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (typeList == 0) {
                txvTitle = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Title);
                txtReleaseday = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Releaseday);
                txvRating = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Rating);
                txvDes = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Description);
                imgFilm = (ImageView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_FilmImage);
                chkLike = (CheckBox) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_ChkLike);
                imgAdult = (ImageView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_txvAdult);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }
            if (typeList == 1) {
                txvTitle = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Title);
                imgFilm = (ImageView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_FilmImage);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }
            if (typeList == 2) {

                txvTitle = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Title);
                txtReleaseday = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Releaseday);
                txvRating = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Rating);
                txvDes = (TextView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_Description);
                imgFilm = (ImageView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_FilmImage);
                chkLike = (CheckBox) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_ChkLike);
                imgAdult = (ImageView) itemView.findViewById(R.id.Fragment_MovieList_Recycler_Item_txvAdult);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }

        }


        @Override
        public boolean onLongClick(View v) {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onLongItemClick(v, getPosition());
            }
            return false;
        }
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public void setLoaded() {
        isLoading = false;
    }

    public List<Movies> favor() {
        return moviesList;
    }

    public void AlertDialog(final MyViewHolder myViewHolder, final int pos, final Movies movies) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");
        alertDialog.setCancelable(false);

        // Setting Icon to Dialog

        // Setting Yes Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myViewHolder.chkLike.setChecked(false);
//                moviesList = new DatabaseHandler(context).getAllFavContacts();
                moviesList.get(pos).setMovie_favorite(0);
                for(int i=0;i<moviesList1.size();i++){
                    if(moviesList1.get(i).getId() ==moviesList.get(pos).getId()&&moviesList1.get(i).isMovie_favorite() !=moviesList.get(pos).isMovie_favorite())
                        moviesList1.set(i,moviesList.get(pos));
                    }

                new DatabaseHandler(context).deleteContact(moviesList.get(pos));
                Events.MovieListFragmentMessage mov = new Events.MovieListFragmentMessage();
                mov.setMoviesList(moviesList1);
                EventBus.getDefault().post(mov);


//                moviesList1.set(a1,moviesList.get(pos));
                Events.DetailFragmentMessage Det = new Events.DetailFragmentMessage();
                Det.setMovies(moviesList.get(pos));
                EventBus.getDefault().post(Det);

                Events.FavoriteFragmentMessage fav = new Events.FavoriteFragmentMessage();
                fav.setMoviesList(new DatabaseHandler(context).getAllFavContacts());
                EventBus.getDefault().post(fav);


                notifyDataSetChanged();


            }
        });
        //Setting No Button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myViewHolder.chkLike.setChecked(true);



            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void setFavorite(boolean flag, int pos) {
//                          MyViewHolder myViewHolder, Movies movies) {
        if (flag) {
            Movies movies;
            movies = moviesList.get(pos);

            moviesList.get(pos).setMovie_favorite(0);
            for(int i=0;i<moviesList1.size();i++){
                if(moviesList1.get(i).getId() ==moviesList.get(pos).getId()&&moviesList1.get(i).isMovie_favorite() !=moviesList.get(pos).isMovie_favorite())
                    moviesList1.set(i,moviesList.get(pos));
            }
            new DatabaseHandler(context).updateFavorite(0, moviesList.get(pos).getId());
//
            new DatabaseHandler(context).deleteContact(moviesList.get(pos));
            Events.MovieListFragmentMessage mov = new Events.MovieListFragmentMessage();
            mov.setMoviesList(moviesList1);
            EventBus.getDefault().post(mov);


//                moviesList1.set(a1,moviesList.get(pos));
            Events.DetailFragmentMessage Det = new Events.DetailFragmentMessage();
            Det.setMovies(moviesList.get(pos));
            EventBus.getDefault().post(Det);

            Events.FavoriteFragmentMessage fav = new Events.FavoriteFragmentMessage();
            fav.setMoviesList(new DatabaseHandler(context).getAllFavContacts());
            EventBus.getDefault().post(fav);
//


            notifyDataSetChanged();

        } else {

            notifyDataSetChanged();
        }
    }


}
