package com.meutkarsh.androidchatapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.UserDetails;

import java.util.ArrayList;


public class BlogFragment extends Fragment {

    EditText newBlog;
    Button addBlog;
    Firebase blogReference, questionReference;
    ArrayList<String> questions = new ArrayList<>();
    LinearLayout layout;
    AutoCompleteTextView autoComplete;

    public BlogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog, container, false);

        newBlog = (EditText) rootView.findViewById(R.id.new_blog);
        addBlog = (Button) rootView.findViewById(R.id.add_blog);
        layout = (LinearLayout) rootView.findViewById(R.id.blog_list);
        autoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.new_blog);

        ArrayAdapter ad = new ArrayAdapter(BlogFragment.super.getContext(), R.layout.support_simple_spinner_dropdown_item, questions);
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
                if(blogEntry.length() < 5){
                    Toast.makeText(BlogFragment.super.getContext(), "Enter more details...", Toast.LENGTH_SHORT).show();
                    return;
                }

                String queUrl = "https://androidchatapp-7aaaa.firebaseio.com/questions/" +blogEntry;
                Firebase queReference = new Firebase(queUrl);

                queReference.push().setValue("currently unsolved!");

                blogReference.push().setValue(blogEntry);
                newBlog.setText("");
            }
        });

        String qurl = "https://androidchatapp-7aaaa.firebaseio.com/questions";
        questionReference = new Firebase(qurl);

        questionReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String question = dataSnapshot.getKey();
                questions.add(question);
                questionsList(question);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }

    void questionsList(String que){
            TextView tv = new TextView(BlogFragment.super.getContext());
            tv.setText(que);
            tv.setHeight(100);
            tv.setTextSize(20.0f);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.weight = 1.0f;
            tv.setLayoutParams(lp);
            layout.addView(tv);
    }

}
