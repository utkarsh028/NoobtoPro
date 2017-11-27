package com.meutkarsh.androidchatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.meutkarsh.androidchatapp.Fragments.ChatFragment;
import com.meutkarsh.androidchatapp.Fragments.ComparisonFragment;
import com.meutkarsh.androidchatapp.POJO.UserDetails;
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.Utils.CustomPagerAdapter;
import com.meutkarsh.androidchatapp.Utils.SessionManagement;

/**
 * Created by utkarsh on 27/9/17.
 */

public class Chat extends AppCompatActivity { 

    TabLayout userTab;
    ViewPager userViewPager;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userTab = (TabLayout) findViewById(R.id.user_tab);
        userViewPager = (ViewPager) findViewById(R.id.user_view_pager);

        session = new SessionManagement(getApplicationContext());

        userTab.setupWithViewPager(userViewPager);
        setViewPager(userViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.update:
                Intent i = new Intent(Chat.this, UpdateDetails.class);
                startActivity(i);
                break;
            case R.id.logout:
                session.logoutUser();
                Toast.makeText(this, "Login to enter.", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Wrong item selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void setViewPager(ViewPager vp){
        CustomPagerAdapter cpa = new CustomPagerAdapter(getSupportFragmentManager());

        ChatFragment chatFragment = new ChatFragment();
        cpa.addFrag(chatFragment, ""+ UserDetails.chatWith);

        ComparisonFragment comparisonFragment = new ComparisonFragment();
        cpa.addFrag(comparisonFragment, "Comparison");

        vp.setAdapter(cpa);
    }

}
