package com.meutkarsh.androidchatapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.meutkarsh.androidchatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Answers extends AppCompatActivity {

    TextView que;
    Button addNewAnswer;
    EditText newAnswer;
    ArrayList<String> answers;
    ProgressDialog pd;
    ListView ansList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        answers = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        ansList = (ListView) findViewById(R.id.all_answers);

        que = (TextView) findViewById(R.id.question);
        String question = getIntent().getStringExtra("Question");
        final String url = "https://androidchatapp-7aaaa.firebaseio.com/questions/" + question;
        que.setText(question + '?');

        newAnswer = (EditText) findViewById(R.id.new_answer);
        addNewAnswer = (Button) findViewById(R.id.add_new_answer);

        addNewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans = newAnswer.getText().toString();
                Firebase queRefrence = new Firebase(url);
                queRefrence.push().setValue(ans);

                newAnswer.setText("");
                if(answers.get(0).equals("currently unanswered!")){
                    answers.remove(0);
                    adapter.remove(0);
                    ansList.setAdapter(adapter);
                }
                answers.add(ans);
            }
        });

        String queUrl = "https://androidchatapp-7aaaa.firebaseio.com/questions/" + question + ".json";
        StringRequest request = new StringRequest(Request.Method.GET, queUrl, new Response.Listener<String>(){
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

        RequestQueue rQ = Volley.newRequestQueue(this);
        rQ.add(request);
    }

    void doOnSuccess(String response){
        try{
            JSONObject obj = new JSONObject(response);
            Iterator it = obj.keys();
            while(it.hasNext()){
                String key = it.next().toString();
                answers.add((String) obj.get(key));
            }
            if(answers.size() > 1){
                answers.remove(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, answers);
        ansList.setAdapter(adapter);
        pd.dismiss();
    }
}
