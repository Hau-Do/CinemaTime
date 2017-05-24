package group6.com.cimenatime.MyVideoPlayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import group6.com.cimenatime.Model.MoviesURL;
import group6.com.cimenatime.R;

/**
 * Created by HauDT on 04/28/2017.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {
    private List<MoviesURL> mDataset;
    RecyclerView recyclerView;
    FrameLayout frameLayout;
    Context context;
    private RecycleViewVideoRelateAdapter.onClickItemFavorite onClickItemFavorite;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imgHinh;
        LinearLayout suggestionLayout;

        public ViewHolder(View v) {
            super(v);
            imgHinh = (ImageView) v.findViewById(R.id.imgHinh);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();
            Log.d("width", width + "");
            Log.d("height", height + "");
            imgHinh.getLayoutParams().height = height / 3;
            imgHinh.getLayoutParams().width = width / 3;
            suggestionLayout = (LinearLayout) v.findViewById(R.id.linearSuggestLayout);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public SuggestionAdapter(List<MoviesURL> myDataset, RecyclerView recyclerView) {
        this.mDataset = myDataset;
        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String poster = "http://image.tmdb.org/t/p/original/" + mDataset.get(position).getMovie_poster();
        Picasso.with(context).load(poster).placeholder(R.drawable.progress_animation).resize(180, 120).into(holder.imgHinh);
        holder.suggestionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemFavorite.selectFavorite(mDataset.get(position).getURL());
            }
        });


    }

    public interface onClickItemFavorite {
        void selectFavorite(String uri);
    }

    public void setOnClickItemFavorite(RecycleViewVideoRelateAdapter.onClickItemFavorite onClickItemFavorite) {
        this.onClickItemFavorite = onClickItemFavorite;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
