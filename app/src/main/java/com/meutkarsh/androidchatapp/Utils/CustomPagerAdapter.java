package com.meutkarsh.androidchatapp.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by tanay on 11/11/17.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTilteArr = new ArrayList<>();
    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFrag(Fragment fragment,String fragmentTitle){

        fragmentArrayList.add(fragment);
        fragmentTilteArr.add(fragmentTitle);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTilteArr.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
}
