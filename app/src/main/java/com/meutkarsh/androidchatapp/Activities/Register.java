package com.meutkarsh.androidchatapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.meutkarsh.androidchatapp.POJO.UserDetails;
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.Utils.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by utkarsh on 27/9/17.
 */

public class Register extends AppCompatActivity {

    EditText username, password, codeforces, codechef, spoj, email;
    Button registerButton;
    String user, pass, cf, cc, sp, emailId;
    TextView login;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        codeforces = (EditText) findViewById(R.id.codeforces);
        codechef = (EditText) findViewById(R.id.codechef);
        spoj = (EditText) findViewById(R.id.spoj);
        email = (EditText) findViewById(R.id.email_id);

        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);
        Firebase.setAndroidContext(this);

        session = new SessionManagement(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Register.this, MainActivity.class);
//                startActivity(i);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();
                cf = codeforces.getText().toString();
                cc = codechef.getText().toString();
                sp = spoj.getText().toString();
                emailId = email.getText().toString();
                if(user.equals("")){
                    username.setError("User Name cannot be blank");
                } else if (pass.equals("")) {
                    password.setError("Password cannot be blank");
                } else if(emailId.equals("")){
                    email.setError("Email id cannot be blank");
                } else if ( !user.matches("[A-Za-z0-9]+") ) {
                    username.setError("Only alphabet or number allowed");
                } else if (user.length() < 5) {
                    username.setError("at least 5 characters long");
                } else if (pass.length() < 5) {
                    password.setError("at least 5 characters long");
                } else {
                    Pattern emailRegex = Pattern.compile(
                            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = emailRegex .matcher(emailId);
                    if(!matcher.find()){
                        email.setError("Enter valid email address");
                        return;
                    }

                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    String url = "https://androidchatapp-7aaaa.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            Firebase reference = new Firebase("https://androidchatapp-7aaaa.firebaseio.com/users");
                            if(response.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                reference.child(user).child("codeforcesHandle").setValue(cf);
                                reference.child(user).child("codechefHandle").setValue(cc);
                                reference.child(user).child("spojHandle").setValue(sp);
                                reference.child(user).child("emailId").setValue(emailId);
                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                UserDetails.username = user;
                                UserDetails.password = pass;
                                UserDetails.codeforcesHandle = cf;
                                UserDetails.codechefHandle = cc;
                                UserDetails.spojHandle = sp;
                                UserDetails.emailId = emailId;

                                session.createLoginSession(user, pass, cf, cc, sp, emailId);
                                Intent i = new Intent(Register.this, Users.class);
                                startActivity(i);
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(response);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("codeforcesHandle").setValue(cf);
                                        reference.child(user).child("codechefHandle").setValue(cc);
                                        reference.child(user).child("spojHandle").setValue(sp);
                                        reference.child(user).child("emailId").setValue(emailId);
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        UserDetails.codeforcesHandle = cf;
                                        UserDetails.codechefHandle = cc;
                                        UserDetails.spojHandle = sp;
                                        UserDetails.emailId = emailId;
                                        session.createLoginSession(user, pass, cf, cc, sp, emailId);
                                        Intent i = new Intent(Register.this, Users.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQ = Volley.newRequestQueue(Register.this);
                    rQ.add(request);
                }
            }
        });
    }
}
