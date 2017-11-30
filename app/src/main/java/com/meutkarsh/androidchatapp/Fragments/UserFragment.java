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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meutkarsh.androidchatapp.Activities.Chat;
import com.meutkarsh.androidchatapp.POJO.UserDetails;
import com.meutkarsh.androidchatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UserFragment extends Fragment {

    //Context context = UserFragment.super.getContext();

    ListView usersList;
    TextView noUsersText;
    ArrayList<String> users, tags;
    int totalUsers = 0;
    ProgressDialog pd;
    Spinner uSpinner;
    ArrayAdapter<String> adapter;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        users = new ArrayList<>();
        tags = new ArrayList<>();

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        usersList = (ListView)rootView.findViewById(R.id.users_list);
        noUsersText = (TextView)rootView.findViewById(R.id.no_users_text);
        uSpinner = (Spinner) rootView.findViewById(R.id.tag_spinner);
        filter();

        adapter = new ArrayAdapter<String>(UserFragment.super.getContext(),
                android.R.layout.simple_list_item_1, users);
        usersList.setAdapter(adapter);

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
                Log.d("Utkarsh" , ""+volleyError);
            }
        });

        Log.d("Utkarsh", "After Volley");

        RequestQueue rQ = Volley.newRequestQueue(UserFragment.super.getContext());
        rQ.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = users.get(position);
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
            users.clear();
            while(it.hasNext()){
                key = it.next().toString();
                if( !key.equals( UserDetails.username ) )   users.add(key);
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
            adapter.notifyDataSetChanged();
        }
        pd.dismiss();
    }

    void filter(){

        tags.add("Select tag to filter");
        tags.add("implementation"); tags.add("binarySearch");   tags.add("dp");
        tags.add("gameTheory");    tags.add("graphs");         tags.add("greedy");
        tags.add("hashing");        tags.add("math");           tags.add("string");
        tags.add("dataStructures");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserFragment.super.getContext(),
                android.R.layout.simple_spinner_item, tags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uSpinner.setAdapter(adapter);

        uSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)  return;
                String tag = tags.get(i);
                String url = getString(R.string.IP) + "/cp/sortUsers/?tag=" + tag;

                JsonArrayRequest jsonArray = new JsonArrayRequest(
                        Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                sort(response);
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Log.d("TAG","Volley Error");
                            }
                        }
                );
                RequestQueue rQ = Volley.newRequestQueue(UserFragment.super.getContext());
                rQ.add(jsonArray);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void sort(JSONArray response){
        try{
            users.clear();
            for(int i = 0; i < response.length(); i++){
                JSONObject obj = response.getJSONObject(i);
                String name = obj.getString("uName");
                if(!name.equals(UserDetails.username))  users.add(name);
            }
            adapter.notifyDataSetChanged();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
