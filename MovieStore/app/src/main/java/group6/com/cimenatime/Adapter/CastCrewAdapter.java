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

import group6.com.cimenatime.Model.CastCrew;
import group6.com.cimenatime.R;


/**
 * Created by Phuong Doan on 04/11/2017.
 */

public class CastCrewAdapter extends RecyclerView.Adapter<CastCrewAdapter.PersonViewHolder> {
    private static final String TAG = CastCrewAdapter.class.getSimpleName();
    List<CastCrew> castCrews;
    private Context mContext;

    public CastCrewAdapter(List<CastCrew> castCrews, Context context){
        mContext = context;
        this.castCrews = castCrews;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.castcrew_item, parent, false);

        PersonViewHolder pvh = new PersonViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder,final int position) {

        CastCrew castCrew = castCrews.get(position);

//        Picasso.with(mContext)
//                .load(castCrew.getPhotoId())
//                .resize(105, 80)
//                .into(((PersonViewHolder) personViewHolder).personPhoto);
        Picasso.with(mContext)
                .load(castCrew.getPhotoPath())
                .resize(105, 80)
                .into(((PersonViewHolder) personViewHolder).personPhoto);
        personViewHolder.personName.setText( castCrew.getName() );
    }


    @Override
    public int getItemCount() {
        return castCrews.size();
    }


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        ImageView personPhoto;

        public PersonViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
        }
    }

}
