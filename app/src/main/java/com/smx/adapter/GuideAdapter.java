package com.smx.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smx.fragment.Guide1Fragment;
import com.smx.fragment.Guide2Fragment;
import com.smx.fragment.Guide3Fragment;

/**
 * Created by vivo on 2017/10/2.
 */
public class GuideAdapter extends FragmentPagerAdapter {

    public GuideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return Guide1Fragment.newInstance();
            case 1:
                return Guide2Fragment.newInstance();
            case 2:
                return Guide3Fragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
