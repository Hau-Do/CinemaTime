package group6.com.cimenatime.MyVideoPlayer;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import group6.com.cimenatime.Model.Movies;
import group6.com.cimenatime.Model.MoviesURL;
import group6.com.cimenatime.R;

/**
 * Created by HauDT on 05/04/2017.
 */
public class RecycleViewVideoRelateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MoviesURL> mList;
    private onClickItemFavorite onClickItemFavorite;
    Context context;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_DESCRIPTION = 0;
    private static boolean flag = true;
    Movies movies;

    public RecycleViewVideoRelateAdapter(List<MoviesURL> mList,Movies movies) {
        this.mList = mList;
        this.movies = movies;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == VIEW_TYPE_DESCRIPTION) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.item_recycle_view_type2, parent, false);

            descriptionViewHolder viewHolder = new descriptionViewHolder(contactView);
            return viewHolder;
        } else if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.item_recycle_view, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(contactView);
            return viewHolder;
        }
        return null;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case 0:
                descriptionViewHolder holder2 = (descriptionViewHolder) holder;
                holder2.txvTittle.setText(movies.getMovie_title());
                holder2.txvDescription.setText(movies.getMovie_overview());
                holder2.txvRating.setText(movies.getMovie_rating().toString());
                holder2.txvRelease.setText(movies.getMovie_releaseday());
                break;
            case 1:
                MyViewHolder holder1 = (MyViewHolder) holder;


                holder1.tvAddress.setText(mList.get(position).getMovie_title());
                String poster = "http://image.tmdb.org/t/p/original/" + mList.get(position).getMovie_poster();
                Picasso.with(context).load(poster).placeholder(R.drawable.progress_animation).resize(95, 80).into(holder1.imgAnh);
                holder1.contentItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickItemFavorite.selectFavorite(mList.get(position).getURL());
                    }
                });
                break;

        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        ImageView imgAnh;
        CardView contentItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAddress = (TextView) itemView.findViewById(R.id.PlayerActivity_txv_title);
            imgAnh = (ImageView) itemView.findViewById(R.id.PlayerActivity_img_images);
            contentItem = (CardView) itemView.findViewById(R.id.CardView);
        }
    }

    public class descriptionViewHolder extends RecyclerView.ViewHolder {
        TextView txvTittle,txvDescription,txvRelease,txvRating;
        LinearLayout desLayout,desTitle;

        public descriptionViewHolder(View itemView) {
            super(itemView);
            txvTittle = (TextView) itemView.findViewById(R.id.player_item_title);
            desLayout = (LinearLayout) itemView.findViewById(R.id.desLayout);
            desTitle = (LinearLayout) itemView.findViewById(R.id.des_title);
            txvDescription = (TextView) itemView.findViewById(R.id.description);
            txvRelease = (TextView) itemView.findViewById(R.id.releaseDay_item);
            txvRating = (TextView) itemView.findViewById(R.id.rating_item);
            desLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag){
                        desTitle.setVisibility(View.VISIBLE);
                        flag =false;
                    }
                    else{
                        desTitle.setVisibility(View.GONE);
                        flag =true;
                    }
                }
            });
        }
    }


    public interface onClickItemFavorite {
        void selectFavorite(String uri);
    }

    public void setOnClickItemFavorite(onClickItemFavorite onClickItemFavorite) {
        this.onClickItemFavorite = onClickItemFavorite;
    }

    @Override
    public int getItemViewType(int position) {
//        return mList.size() >= 1 ? VIEW_TYPE_DESCRIPTION : VIEW_TYPE_ITEM;
        int i=0;
        if(position ==0){
            i= VIEW_TYPE_DESCRIPTION;
        }
        else {
            i = VIEW_TYPE_ITEM;
        }
        return i;
    }
}
