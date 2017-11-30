package com.meutkarsh.androidchatapp.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.meutkarsh.androidchatapp.POJO.ComparisonData;
import com.meutkarsh.androidchatapp.POJO.ProfileData;
import com.meutkarsh.androidchatapp.POJO.Question;
import com.meutkarsh.androidchatapp.POJO.UserDetails;
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


    RequestQueue requestQueue;
    PieChart pieChart,tagpieChart;
    Button refreshStats ;
    ProfileData profileData;
    TextView profileUname,profileCfRating,profileSpjRating;
    public UserStatsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_stats, container, false);
        pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        tagpieChart = (PieChart) rootView.findViewById(R.id.tagpiechart);
        refreshStats = (Button) rootView.findViewById(R.id.refreshChart);
        profileUname = (TextView) rootView.findViewById(R.id.profileUname);
        profileCfRating = (TextView) rootView.findViewById(R.id.profileCfRating);
        profileSpjRating = (TextView) rootView.findViewById(R.id.profileSpjRating);
       // Log.d("TAG","iiiii"+UserDetails.codeforcesRating+" "+UserDetails.spojRank);
        profileUname.setText("Username : " + UserDetails.username);
        profileCfRating.setText("Codeforces Rating : " + UserDetails.codeforcesRating);
        profileSpjRating.setText("SPOJ Ranking : " + UserDetails.spojRank);



        refreshStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Gson gson = new Gson();

                requestQueue = Volley.newRequestQueue(getContext());


                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                String url = getString(R.string.IP) + "/cp/getUserProfileData/?uName=" + UserDetails.username;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                profileData = gson.fromJson(response.toString(),ProfileData.class);
                                Log.d("TAG", response.toString());

                                setChart(profileData.getqSolved());
                                setTagChart(profileData.getUsrstats());
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Log.d("TAG","onError");
                            }
                        }
                );

                requestQueue.add(jsonObjectRequest);

            }
        });
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","onCreate");

        Log.d("TAG","onCreateView");


    }

    void setChart(ArrayList<Question> questionArrayList){


        ArrayList<Entry> entries = new ArrayList<>();
        pieChart.setUsePercentValues(true);

        int solved=0;
        for(int i=0;i<questionArrayList.size();i++){
            if(questionArrayList.get(i).getSuccess()) solved++;
        }
        Log.d("TAG", ""+solved);
        entries.add( new Entry(solved,0) );
        entries.add( new Entry(questionArrayList.size()-solved,1));
        pieChart.setDrawHoleEnabled(false);
        PieDataSet set = new PieDataSet(entries, "Solved vs Unsolved");
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Solved");
        xVals.add("Unsolved");
        set.setColors(ColorTemplate.PASTEL_COLORS); // set the color
        PieData data = new PieData(xVals,set);
        pieChart.setData(data);


    }



    void setTagChart(ArrayList<ComparisonData> comparisonDataArrayList){

        tagpieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();

        for(int i = 0;i<comparisonDataArrayList.size();i++){

            yvalues.add(new Entry(comparisonDataArrayList.get(i).getCount(),i));
        }

        PieDataSet dataSet = new PieDataSet(yvalues,"Questions by Tags");

        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = 0;i<comparisonDataArrayList.size();i++){

            xVals.add(comparisonDataArrayList.get(i).getType());
        }

        tagpieChart.setDrawHoleEnabled(false);

        PieData data = new PieData(xVals,dataSet);

        data.setValueFormatter(new PercentFormatter());

        tagpieChart.setData(data);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS); // set the color
       // PieData tagData = new PieData(tagSet);
       // tagpieChart.setData(tagData);

    }
}
