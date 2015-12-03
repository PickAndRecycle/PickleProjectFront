package com.pickle.pickleproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by admin on 12/3/2015.
 */
public class PagerAdapterPicklejar extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterPicklejar(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabThrower tab1 = new TabThrower();
                return tab1;
            case 1:
                TabPicker tab2 = new TabPicker();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
