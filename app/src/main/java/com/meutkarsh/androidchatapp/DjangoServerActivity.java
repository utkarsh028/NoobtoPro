package com.meutkarsh.androidchatapp;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import com.meutkarsh.androidchatapp.Fragments.UserStatsFragment;

/**
 * Created by tanay on 12/11/17.
 */


public class DjangoServerActivity extends AppCompatActivity {




    ViewPager viewPager;
    TabLayout tablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_django_server);
//        Toast.makeText(this, "django activity", Toast.LENGTH_SHORT).show();
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setViewPager(viewPager);
        tablayout.setupWithViewPager(viewPager);





    }


    void setViewPager(ViewPager viewPager){
        CustomPagerAdapter adapter = new CustomPagerAdapter(getSupportFragmentManager());

        UserStatsFragment userStatsFragment = new UserStatsFragment();

        adapter.addFrag(userStatsFragment,"User Stats");
//        adapter.addFrag(      ,"Recommendation");
        viewPager.setAdapter(adapter);

    }



}
