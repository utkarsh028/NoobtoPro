package com.meutkarsh.androidchatapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.meutkarsh.androidchatapp.POJO.ComparisonData;
import com.meutkarsh.androidchatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ComparisonFragment extends Fragment {

    ArrayList<ComparisonData> comparisonDataArrayList;
    Context context = getActivity();
    CompareDataRVAdapter adapter;
    RecyclerView compareRV ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_comparison, container, false);
        compareRV = (RecyclerView) rootView.findViewById(R.id.compareRV);
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comparisonDataArrayList = new ArrayList<>();
        final Gson gson = new Gson();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String url = "http://192.168.43.190:8000/cp/compare/?uname=tanay";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            // Loop through the array elements
                            for(int i = 0;i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                comparisonDataArrayList.add( new ComparisonData(
                                        obj.getString("type"),obj.getInt("count")));

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if(comparisonDataArrayList != null && !comparisonDataArrayList.isEmpty()){
                            adapter = new CompareDataRVAdapter();
                            compareRV.setLayoutManager(new LinearLayoutManager(getContext()));
                            compareRV.setAdapter(adapter);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("TAG","onError");
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
        Log.d("TAG","onCreateView");

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
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = li.inflate(R.layout.comparison_element, parent,false);
            return new CompareDataHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CompareDataHolder holder, int position) {
            ComparisonData data = comparisonDataArrayList.get(position);

            holder.tag.setText(data.getType());
            holder.user1Solved.setText(String.valueOf( data.getCount() ));
        }

        @Override
        public int getItemCount() {
            return comparisonDataArrayList.size();
        }
    }
}
