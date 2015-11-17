package com.pickle.pickleproject;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 * Created by admin on 11/18/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabUnused tab1 = new TabUnused();
                return tab1;
            case 1:
                TabGeneral tab2 = new TabGeneral();
                return tab2;
            case 2:
                TabRecycle tab3 = new TabRecycle();
                return tab3;
            case 3:
                TabGreen tab4 = new TabGreen();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
