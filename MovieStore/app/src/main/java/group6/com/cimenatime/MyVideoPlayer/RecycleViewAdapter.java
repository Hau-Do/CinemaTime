package group6.com.cimenatime.MyVideoPlayer;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import group6.com.cimenatime.R;

/**
 * Created by HauDT on 04/21/2017.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private List<String> mList;
    private onClickItemFavorite onClickItemFavorite;

    public RecycleViewAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvAddress.setText("Uri: " + mList.get(position));
        holder.contentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemFavorite.selectFavorite(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAddress;
        public CardView contentItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAddress = (TextView) itemView.findViewById(R.id.PlayerActivity_txv_title);
            contentItem = (CardView) itemView.findViewById(R.id.CardView);
        }
    }

    public interface onClickItemFavorite {
        void selectFavorite(String uri);
    }

    public void setOnClickItemFavorite(onClickItemFavorite onClickItemFavorite) {
        this.onClickItemFavorite = onClickItemFavorite;
    }
}
