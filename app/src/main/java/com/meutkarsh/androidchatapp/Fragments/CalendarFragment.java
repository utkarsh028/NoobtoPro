package com.meutkarsh.androidchatapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.meutkarsh.androidchatapp.POJO.CalendarElement;
import com.meutkarsh.androidchatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aditya on 29/11/17.
 */

public class CalendarFragment extends Fragment {
    ArrayList<CalendarElement> calendarElementArrayList = new ArrayList<>();
    Context context = CalendarFragment.super.getContext();
    CalendarRVAdapter adapter;
    RecyclerView calendarRV;
    Button refreshButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        refreshButton = (Button) rootView.findViewById(R.id.refreshCalendar);
        calendarRV = (RecyclerView) rootView.findViewById(R.id.calendarRV);
        final Gson gson = new Gson();
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                String url = getString(R.string.IP) + "/cp/getCal";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    Log.d("TAG",response.toString());
                                    // Loop through the array elements
                                    calendarElementArrayList.clear();
                                    for(int i = 0;i < response.length(); i++){
                                        JSONObject obj = response.getJSONObject(i);
                                        calendarElementArrayList.add( new CalendarElement(
                                                obj.getString("startEndTime"),
                                                obj.getString("duration"),
                                                obj.getString("event"),
                                                obj.getString("link")));

                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                                if(calendarElementArrayList != null && !calendarElementArrayList.isEmpty()){
                                    adapter = new CalendarRVAdapter();
                                    calendarRV.setLayoutManager(new LinearLayoutManager(context));
                                    calendarRV.setAdapter(adapter);
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

            }
        });
        return rootView;
    }

    class CalendarHolder extends RecyclerView.ViewHolder{
        TextView seTime,duration,contestName;
        LinearLayout calElement;
        public CalendarHolder(View itemView) {
            super(itemView);
            seTime = (TextView) itemView.findViewById(R.id.setime);
            duration = (TextView) itemView.findViewById(R.id.duration);
            contestName = (TextView) itemView.findViewById(R.id.contest_name);
            calElement = (LinearLayout) itemView.findViewById(R.id.calendar_element);
        }
    }

    class CalendarRVAdapter extends RecyclerView.Adapter<CalendarHolder>{

        @Override
        public CalendarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) CalendarFragment.super.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View itemView = li.inflate(R.layout.calendar_element, parent,false);
            return new CalendarHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CalendarHolder holder, int position) {
            final CalendarElement ce = calendarElementArrayList.get(position);
            holder.contestName.setText(ce.getEvent());
            holder.duration.setText(ce.getDuration());
            holder.seTime.setText(ce.getStartEndTime());

            holder.calElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link = ce.getLink();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return calendarElementArrayList.size();
        }
    }
}
