package com.meutkarsh.androidchatapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.meutkarsh.androidchatapp.POJO.QuestionList;
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.Utils.QuestionAdapter;
import org.json.JSONObject;

/**
 * Created by tanay on 12/11/17.
 */


public class UserStatsFragment extends Fragment {

    RecyclerView questionRecyclerView;
    QuestionList questions;
    RequestQueue requestQueue;
    QuestionAdapter questionAdapter;
    public UserStatsFragment() {// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_stats, container, false);
        questionRecyclerView = (RecyclerView) rootView.findViewById(R.id.userProblemRV);


        final Gson gson = new Gson();
        requestQueue = Volley.newRequestQueue(getContext());

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                "192.168.43.190:8000/cp/spoj/handle=ty_samurai97&uname=tanay",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        questions =gson.fromJson(response.toString(),QuestionList.class);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG","onError");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
        if(!questions.getQuestions().isEmpty()){

            questionAdapter = new QuestionAdapter(questions.getQuestions(),getContext());
            questionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            questionRecyclerView.setAdapter(questionAdapter);
        }

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
