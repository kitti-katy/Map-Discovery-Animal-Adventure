package com.kittikaty.kate.travelopener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by kate on 3/8/17.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
  public static MapViewFragment tab1;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;


    }

    public MapViewFragment getTab1(){
        return tab1;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:{
                return new MapViewFragment();}
            case 1:
                AchievementsFragment tab2 = new AchievementsFragment();
                return tab2;
            case 2:
                BuyCircle tab4 = new BuyCircle();
                return tab4;
            case 3:
                Settings tab5 = new Settings();
                return tab5;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}