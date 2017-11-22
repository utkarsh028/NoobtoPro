package com.meutkarsh.androidchatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.meutkarsh.androidchatapp.Utils.CustomPagerAdapter;
import com.meutkarsh.androidchatapp.Fragments.BlogFragment;
import com.meutkarsh.androidchatapp.Fragments.UserFragment;
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.Utils.SessionManagement;

/**
 * Created by utkarsh on 27/9/17.
 */

public class Users extends AppCompatActivity {

    TabLayout userTab;
    ViewPager userViewPager;

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

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

        UserFragment userFragment = new UserFragment();
        cpa.addFrag(userFragment,"Users Available");
        //ClassName cn = new ClassName();
        //cpa.addFrag(cn, "title")

        BlogFragment blogFragment = new BlogFragment();
        cpa.addFrag(blogFragment, "Blogs");

        vp.setAdapter(cpa);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bye...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);

    }
}
