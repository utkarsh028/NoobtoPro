package com.meutkarsh.androidchatapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.meutkarsh.androidchatapp.R;
import com.meutkarsh.androidchatapp.Utils.SessionManagement;
import com.meutkarsh.androidchatapp.POJO.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser = (TextView)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("User Name cannot be blank.");
                }else if(pass.equals("")){
                    Toast.makeText(MainActivity.this, "Password cannot be blank.", Toast.LENGTH_SHORT).show();
                    //password.setText("Password cannot be blank.");
                }else{
                    String url = "https://androidchatapp-7aaaa.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("null")){
                                Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.has(user)){
                                        Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String userpass = obj.getJSONObject(user).getString("password");
                                        String userCF = obj.getJSONObject(user).getString("codeforcesHandle");
                                        String userCC = obj.getJSONObject(user).getString("codechefHandle");
                                        String userSP = obj.getJSONObject(user).getString("spojHandle");
                                        String userEmailId = obj.getJSONObject(user).getString("emailId");
                                        if(userpass.equals(pass)){
                                            UserDetails.username = user;
                                            UserDetails.password = pass;
                                            session.createLoginSession(user, pass, userCF, userCC, userSP, userEmailId);
                                            Intent i = new Intent(MainActivity.this, Users.class);
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            pd.dismiss();
                        }
                    }
                    );
                    RequestQueue rQ = Volley.newRequestQueue(MainActivity.this);
                    rQ.add(request);
                }
            }
        });

    }
}
