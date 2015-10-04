package com.smbc.droid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ComicPagerAdapter extends FragmentStatePagerAdapter {

    public ComicPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ComicFragment();
        Bundle args = new Bundle();
        args.putInt(ComicFragment.INDEX, 3880 - position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1000;
    }
}
