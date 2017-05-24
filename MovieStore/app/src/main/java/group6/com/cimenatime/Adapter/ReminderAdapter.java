package group6.com.cimenatime.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import group6.com.cimenatime.Model.Reminder;
import group6.com.cimenatime.R;

/**
 * Created by HauDT on 04/7/2017.
 */
  public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.PosterViewHolder> {

    private Context mContext;
    List<Reminder> mReminders;
    private OnClickItemViewListener onClickItemViewListener;

    public ReminderAdapter(Context mContext, List<Reminder> reminders) {
        this.mContext = mContext;
        this.mReminders = reminders;
    }

    @Override
    public ReminderAdapter.PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from( parent.getContext())
                .inflate(R.layout.poster_item, parent, false );

        PosterViewHolder posterViewHolder = new PosterViewHolder(v);

        return posterViewHolder;

    }

    @Override
    public void onBindViewHolder(ReminderAdapter.PosterViewHolder posterViewHolder, int position) {

        final Reminder reminder = mReminders.get(position);

        String posterId = reminder.getPosterId();
        String movieTitle = reminder.getMovieTitle();
        String releaseDate = reminder.getReleaseDate();
        Double rating = reminder.getRating();
        String reminderedDate = reminder.getReminderedDate();

        Picasso.with(mContext)
                .load(posterId)
                .resize(120, 91)
                .into(((PosterViewHolder) posterViewHolder).imvPosterReminder);
        posterViewHolder.txtMovieTitleReminder.setText(movieTitle);
        posterViewHolder.txtYearReminder.setText(releaseDate);
        posterViewHolder.txtRateReminder.setText(rating+"");
        posterViewHolder.txtDateReminder.setText(reminderedDate);

        /**
         * register click event on each row
         */
        posterViewHolder.row_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemViewListener.onClickItemView( reminder.getId() );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class PosterViewHolder extends RecyclerView.ViewHolder{

        CardView row_cardview;

        ImageView imvPosterReminder;
        TextView txtMovieTitleReminder;
        TextView txtYearReminder;
        TextView txtRateReminder;
        TextView txtDateReminder;


        public PosterViewHolder(View itemView) {
            super(itemView);

            row_cardview = (CardView) itemView.findViewById(R.id.row_cardview);

            imvPosterReminder = (ImageView) itemView.findViewById(R.id.imvPosterReminder);
            txtMovieTitleReminder = (TextView) itemView.findViewById(R.id.txtMovieTitleReminder);
            txtYearReminder = (TextView) itemView.findViewById(R.id.txtYearReminder);
            txtRateReminder = (TextView) itemView.findViewById(R.id.txtRateReminder);
            txtDateReminder = (TextView) itemView.findViewById(R.id.txtDatetimeReminder);

            itemView.setTag(itemView);
        }
    }

    /**
     * A Interface to set click event for each row on recycler view, then call it from Setting Fragment
     */
    public interface OnClickItemViewListener{
        void onClickItemView(int idMovie);
    }

    public void setOnClickItemViewListener( OnClickItemViewListener onClickItemViewListener ){
        this.onClickItemViewListener = onClickItemViewListener;
    }
}
