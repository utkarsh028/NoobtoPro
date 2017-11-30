package com.meutkarsh.androidchatapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.meutkarsh.androidchatapp.POJO.UserComparison;
import com.meutkarsh.androidchatapp.POJO.UserDetails;
import com.meutkarsh.androidchatapp.R;

import org.json.JSONObject;

import java.util.ArrayList;


public class ComparisonFragment extends Fragment {

    class pair{
        int First, Third;
        String Second;

        public pair(int first, String second, int third) {
            First = first;
            Third = third;
            Second = second;
        }
    }

    ArrayList<pair> data = new ArrayList<>();
    CompareDataRVAdapter adapter;
    RecyclerView compareRV ;
    TextView userFirst, userSecond;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_comparison, container, false);

        userFirst = (TextView) rootView.findViewById(R.id.user_first);
        userSecond = (TextView) rootView.findViewById(R.id.user_second);
        userFirst.setText(UserDetails.username);
        userSecond.setText(UserDetails.chatWith);

        compareRV = (RecyclerView) rootView.findViewById(R.id.compareRV);
        final Gson gson = new Gson();
        final RequestQueue requestQueue = Volley.newRequestQueue(ComparisonFragment.super.getContext());

        String url = getString(R.string.IP) + "/cp/compare/?uName1=" + UserDetails.username + "&uName2=" + UserDetails.chatWith;
        JsonObjectRequest jsonObject = new JsonObjectRequest(
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserComparison usersData = gson.fromJson(response.toString(), UserComparison.class);
                        for(int i = 0; i < usersData.getuName1().size(); i++){
                            data.add(new pair(usersData.getuName1().get(i).getCount(),
                                    usersData.getuName1().get(i).getType(),
                                    usersData.getuName2().get(i).getCount()));

                        }
                        adapter = new CompareDataRVAdapter();
                        compareRV.setLayoutManager(new LinearLayoutManager(getContext()));
                        compareRV.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("TAG","onError");
                    }
                }
        );
        requestQueue.add(jsonObject);
        Log.d("TAG","onCreateView");


        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class CompareDataHolder extends RecyclerView.ViewHolder{
        TextView user1Solved,user2Solved,tag;
        public CompareDataHolder(View itemView) {
            super(itemView);
            user1Solved = (TextView) itemView.findViewById(R.id.user1_solved);
            user2Solved = (TextView) itemView.findViewById(R.id.user2_solved);
            tag = (TextView) itemView.findViewById(R.id.tag);
        }
    }

    class CompareDataRVAdapter extends RecyclerView.Adapter<CompareDataHolder>{
        @Override
        public CompareDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) ComparisonFragment.super.getContext().getSystemService(ComparisonFragment.super.getContext().LAYOUT_INFLATER_SERVICE);
            View itemView = li.inflate(R.layout.comparison_element, parent,false);
            return new CompareDataHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CompareDataHolder holder, int position) {


            holder.tag.setText(data.get(position).Second);
            holder.user1Solved.setText(String.valueOf( data.get(position).First ));
            holder.user2Solved.setText(String.valueOf( data.get(position).Third ));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
