package com.meutkarsh.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by utkarsh on 27/9/17.
 */

public class Users extends AppCompatActivity {

    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        session = new SessionManagement(getApplicationContext());

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();
        String url = "https://androidchatapp-7aaaa.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                doOnSuccess(response);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQ = Volley.newRequestQueue(Users.this);
        rQ.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                Intent i = new Intent(Users.this, Chat.class);
                startActivity(i);
            }
        });
    }

    public void doOnSuccess(String response) {
        try{
            JSONObject obj = new JSONObject(response);
            Iterator it = obj.keys();
            String key = "";
            while(it.hasNext()){
                key = it.next().toString();
                if( !key.equals( UserDetails.username ) )   al.add(key);
                totalUsers++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(totalUsers <= 1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }
        pd.dismiss();
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
            case R.id.stats:
                Intent i = new Intent(Users.this,DjangoServerActivity.class);
                startActivity(i);
                break;
            default:
                Toast.makeText(this, "Wrong item selected", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
