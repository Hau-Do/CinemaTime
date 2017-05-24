package group6.com.cimenatime.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Activity.MainActivity;
import group6.com.cimenatime.Adapter.ReminderAdapter;
import group6.com.cimenatime.Model.Reminder;
import group6.com.cimenatime.R;

/**
 * Created by HauDT on 05/03/2017.
 */
public class ReminderFragment extends Fragment implements
        ReminderAdapter.OnClickItemViewListener {

    private RecyclerView mRecyclerViewReminder;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Reminder> mReminderList = new ArrayList<>();
    private ReminderAdapter mReminderAdapter;
    //private OnReminderFragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mListener = (OnReminderFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

//    public interface OnReminderFragmentListener{
//        void onOpenDetail(int idMovie);
//    }
//
//    public void setOnReminderFragmentListener( OnReminderFragmentListener onReminderFragmentListener ){
//        this.mListener = onReminderFragmentListener;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reminder_fragment, container, false);

        mRecyclerViewReminder = (RecyclerView) view.findViewById(R.id.recyclerViewReminder);

        mLinearLayoutManager = new LinearLayoutManager( getActivity() );
        mRecyclerViewReminder.setLayoutManager(mLinearLayoutManager);
        mRecyclerViewReminder.setHasFixedSize(true);

        mReminderAdapter = new ReminderAdapter(getActivity(), mReminderList);
        mRecyclerViewReminder.setAdapter(mReminderAdapter);

        // register click event for each row of adapter in RecyclerView
        mReminderAdapter.setOnClickItemViewListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * this method to receive a reminder object
     * @param reminder
     */
    @Subscribe(sticky = true)
    public void onEvent(Reminder reminder){

        if(reminder != null){
            mReminderList.add(reminder);
            mReminderAdapter.notifyDataSetChanged();
        }
    }

    /**
     * A callback to set click event on each row in recyclerview
     * @param idMovie
     */
    @Override
    public void onClickItemView(int idMovie) {
//        if(null != mListener){
//            mListener.onOpenDetail(idMovie);
//        }

        Bundle bundle = new Bundle();
        bundle.putInt("Id_Movie", idMovie);

        // transfer bundle to DetailMoviesFragment
        ((MainActivity) getActivity()).isBack = true;
        ((MainActivity) getActivity()).setToolbarIcon(true);


        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        viewPager.setCurrentItem(0);

        MainActivity.isBundle = 2; // set flag = 2 for ReminderFragment
        DetailMoviesFragment detail = new DetailMoviesFragment();
        detail.setArguments(bundle);
        // transfer bundle to DetailMoviesFragment
        FragmentTransaction transaction = getParentFragment().getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_container, detail);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
