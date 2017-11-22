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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.meutkarsh.androidchatapp.Activities.Answers;
import com.meutkarsh.androidchatapp.POJO.UserDetails;
import com.meutkarsh.androidchatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class BlogFragment extends Fragment {

    EditText newBlog;
    Button addBlog;
    Firebase blogReference;
    ArrayList<String> questions;
    ListView queList;
    AutoCompleteTextView autoComplete;
    ProgressDialog pd;

    public BlogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog, container, false);

        questions = new ArrayList<>();
        newBlog = (EditText) rootView.findViewById(R.id.new_blog);
        addBlog = (Button) rootView.findViewById(R.id.add_blog);
        queList = (ListView) rootView.findViewById(R.id.que_list);
        autoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.new_blog);

        pd = new ProgressDialog(BlogFragment.super.getContext());
        pd.setMessage("Loading...");
        pd.show();

        ArrayAdapter ad = new ArrayAdapter(BlogFragment.super.getContext(),
                R.layout.support_simple_spinner_dropdown_item, questions);
        autoComplete.setThreshold(5);
        autoComplete.setAdapter(ad);

        Firebase.setAndroidContext(BlogFragment.super.getContext());
        String url = "https://androidchatapp-7aaaa.firebaseio.com/blogs/" + UserDetails.username;
        blogReference = new Firebase(url);

        addBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Utkarsh", "add new blog");
                String blogEntry = newBlog.getText().toString();

                int z = blogEntry.length();
                if(blogEntry.charAt(z-1) == '?')    blogEntry = blogEntry.substring(0, z-1);

                if(blogEntry.length() < 5){
                    Toast.makeText(BlogFragment.super.getContext(), "Enter more details...", Toast.LENGTH_SHORT).show();
                    return;
                }

                String queUrl = "https://androidchatapp-7aaaa.firebaseio.com/questions/" +blogEntry;
                Firebase queReference = new Firebase(queUrl);
                queReference.push().setValue("currently unanswered!");

                blogReference.push().setValue(blogEntry);
                newBlog.setText("");

                questions.add(blogEntry + '?');
            }
        });

        String queUrl = "https://androidchatapp-7aaaa.firebaseio.com/questions.json";
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

        RequestQueue rQ = Volley.newRequestQueue(BlogFragment.super.getContext());
        rQ.add(request);

        queList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BlogFragment.super.getContext(), Answers.class);
                int sz = questions.get(i).length();
                intent.putExtra("Question", questions.get(i).substring(0, sz-1));
                startActivity(intent);
            }
        });

        return rootView;
    }

    void doOnSuccess(String response){
        try{
            JSONObject obj = new JSONObject(response);
            Iterator it = obj.keys();
            while(it.hasNext()){
                String key = it.next().toString();
                questions.add(key + '?');
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queList.setAdapter(new ArrayAdapter<String>(
                BlogFragment.super.getContext(), android.R.layout.simple_list_item_1, questions));
        pd.dismiss();
    }

}
