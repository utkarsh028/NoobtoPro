package com.meutkarsh.androidchatapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.meutkarsh.androidchatapp.POJO.Question;
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.Utils.QuestionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tanay on 12/11/17.
 */


public class UserStatsFragment extends Fragment {

    RecyclerView questionRecyclerView;
    ArrayList<Question> questions;
    RequestQueue requestQueue;
    QuestionAdapter questionAdapter;
    PieChart pieChart;
    public UserStatsFragment() {// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_stats, container, false);
        questionRecyclerView = (RecyclerView) rootView.findViewById(R.id.userProblemRV);
        pieChart = (PieChart) rootView.findViewById(R.id.piechart);

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","onCreate");
        final Gson gson = new Gson();
        requestQueue = Volley.newRequestQueue(getContext());

        questions = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://192.168.43.190:8000/cp/spoj/handle=ty_samurai97&uname=tanay",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                JSONObject obj = response.getJSONObject(i);

                                questions.add( new Question(obj.getString("title"),obj.getString("link"),obj.getBoolean("success")) );

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if( questions!=null && !questions.isEmpty()){
                            Log.d("TAG","onCreateView");
                            questionAdapter = new QuestionAdapter(questions,getContext());
                            questionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            questionRecyclerView.setAdapter(questionAdapter);

                            setChart();
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

    void setChart(){
        int solved=0,unsolved=0;
        for (int i = 0; i < questions.size(); i++ ){
            if(questions.get(i).getSuccess()) solved++;
        }
        unsolved = questions.size() - solved;

        ArrayList<PieEntry> entries= new ArrayList<>();

        entries.add( new PieEntry(solved ) );
        entries.add( new PieEntry(unsolved  ));
        PieDataSet set = new PieDataSet(entries, "Problem Status");
//        set.setColor(R.color.red);
        PieData data = new PieData(set);
        pieChart.setData(data);

    }
}
