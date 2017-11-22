package com.meutkarsh.androidchatapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meutkarsh.androidchatapp.Activities.Chat;
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.POJO.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UserFragment extends Fragment {

    //Context context = UserFragment.super.getContext();

    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        usersList = (ListView)rootView.findViewById(R.id.users_list);
        noUsersText = (TextView)rootView.findViewById(R.id.no_users_text);

        pd = new ProgressDialog(UserFragment.super.getContext());
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

        Log.d("Utkarsh", "After Volley");

        RequestQueue rQ = Volley.newRequestQueue(UserFragment.super.getContext());
        rQ.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                Intent i = new Intent(UserFragment.super.getContext(), Chat.class);
                startActivity(i);
            }
        });

        return rootView;
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
            usersList.setAdapter(new ArrayAdapter<String>(UserFragment.super.getContext(), android.R.layout.simple_list_item_1, al));
        }
        pd.dismiss();
    }

}
