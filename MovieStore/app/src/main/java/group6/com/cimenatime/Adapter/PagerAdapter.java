package group6.com.cimenatime.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import group6.com.cimenatime.Fragment.AboutFragment;
import group6.com.cimenatime.Fragment.FavoriteFragment;
import group6.com.cimenatime.Fragment.SettingFragment;
import group6.com.cimenatime.Fragment.rootFragment;

/**
 * Created by HauDT on 04/2/2017.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    FragmentManager fm;
    int numofTabs;


    public PagerAdapter(FragmentManager fm, int numofTabs) {
        super(fm);
        this.fm = fm;
        this.numofTabs = numofTabs;
    }

    @Override
    /**
     * HauDT
     * getItem
     * 0:Run Tab1* MovieList
     * 1:RunTab2 * Favorite
     * 2:RunTab3 * Setting
     * 3:RunTab4 * About
     */
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                rootFragment tab1 = new rootFragment();
                return tab1;
            case 1:
                FavoriteFragment tab2 = new FavoriteFragment();
                return tab2;
            case 2:
                SettingFragment tab3 = new SettingFragment();
                return tab3;
            case 3:
                AboutFragment tab4 = new AboutFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numofTabs;
    }







    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}

