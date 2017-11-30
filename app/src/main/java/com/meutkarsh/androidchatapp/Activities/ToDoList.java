package com.meutkarsh.androidchatapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.meutkarsh.androidchatapp.POJO.Question;
import com.meutkarsh.androidchatapp.POJO.UserDetails;
import com.meutkarsh.androidchatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ToDoList extends AppCompatActivity {

    RecyclerView questionRV;
    ArrayList<Question> questionsList;
    QuestionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        questionRV = (RecyclerView) findViewById(R.id.questionRV);
        questionsList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getString(R.string.IP) + "/cp/getUnsolvedList/?uName=" + UserDetails.username;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("TAG",response.toString());
                            // Loop through the array elements
                            for(int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                questionsList.add( new Question( obj.getString("title"),
                                        obj.getString("link"),
                                        obj.getBoolean("success")));

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if(questionsList != null && !questionsList.isEmpty()){
                            adapter = new QuestionAdapter();
                            questionRV.setLayoutManager(new LinearLayoutManager(getApplication()));
                            questionRV.setAdapter(adapter);
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

    public class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {


        @Override
        public int getItemViewType(int position) {
            if(questionsList.get(position).getSuccess()) return 1;
            return 0;
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View itemView = li.inflate( ( viewType==1) ? R.layout.solved_problem : R.layout.unsolved_problem
                    , parent,false);
            return new QuestionHolder(itemView);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            final Question question = questionsList.get(position);
            holder.question.setText(question.getTitle());
            holder.questionElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link = question.getLink();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return questionsList.size();
        }

    }
    public class QuestionHolder extends RecyclerView.ViewHolder{
        public TextView question;
        public LinearLayout questionElement;
        public QuestionHolder(View itemView){
            super(itemView);
            this.question = (TextView) itemView.findViewById(R.id.questionid);
            this.questionElement = (LinearLayout) itemView.findViewById(R.id.question_element);
        }
    }

}
