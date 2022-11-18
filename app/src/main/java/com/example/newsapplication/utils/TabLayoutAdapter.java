package com.example.newsapplication.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newsapplication.activities.ui.gallery.GalleryFragment;
import com.example.newsapplication.activities.ui.home.HomeFragment;
import com.example.newsapplication.activities.ui.slideshow.SlideshowFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {
    /*String[] fragment_names = new String[]{"Kuliner", "Review", "Gallery"};*/

    Context mContext;
    int mTotalTabs;

    public TabLayoutAdapter(Context context, FragmentManager fragmentManager, int totalTabs) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SlideshowFragment();
            case 2:
                return new GalleryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}
