package com.meutkarsh.androidchatapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateDetails extends AppCompatActivity {

    EditText oldPassword, newPassword, codeforces, codechef, spoj, email;
    String user, oldPass, newPass, cf, cc, sp, emailId;
    Button update;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        codeforces = (EditText) findViewById(R.id.codeforces);
        codechef = (EditText) findViewById(R.id.codechef);
        spoj = (EditText) findViewById(R.id.spoj);
        email = (EditText) findViewById(R.id.email_id);
        update = (Button) findViewById(R.id.update);

        Firebase.setAndroidContext(this);
        session = new SessionManagement(getApplicationContext());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPass = oldPassword.getText().toString();
                if(oldPass.equals("")){
                    oldPassword.setError("This field cannot be blank.");
                }else if(!oldPass.equals(UserDetails.password)){
                    oldPassword.setError("Invalid old password.");
                }else{
                    user = UserDetails.username;
                    newPass = newPassword.getText().toString();
                    cf = codeforces.getText().toString();
                    cc = codechef.getText().toString();
                    sp = spoj.getText().toString();
                    emailId = email.getText().toString();
                    if(!newPass.equals("")  && newPass.length() < 5){
                        newPassword.setError("at least 5 characters long");
                        return;
                    }
                    Pattern emailRegex = Pattern.compile(
                            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = emailRegex .matcher(emailId);
                    if(!emailId.equals("") && !matcher.find()){
                        email.setError("Enter valid email address");
                        return;
                    }

                    if(newPass.equals(""))  newPass = UserDetails.password;
                    if(cf.equals(""))   cf = UserDetails.codeforcesHandle;
                    if(cc.equals(""))   cc = UserDetails.codechefHandle;
                    if(sp.equals(""))   sp = UserDetails.spojHandle;
                    if(emailId.equals(""))  emailId = UserDetails.emailId;

                    final ProgressDialog pd = new ProgressDialog(UpdateDetails.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://androidchatapp-7aaaa.firebaseio.com/users.json";
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            Firebase reference = new Firebase("https://androidchatapp-7aaaa.firebaseio.com/users");

                            reference.child(user).child("password").setValue(newPass);
                            reference.child(user).child("codeforcesHandle").setValue(cf);
                            reference.child(user).child("codechefHandle").setValue(cc);
                            reference.child(user).child("spojHandle").setValue(sp);
                            reference.child(user).child("emailId").setValue(emailId);
                            Toast.makeText(UpdateDetails.this, "Updated successful", Toast.LENGTH_LONG).show();

                            UserDetails.password = newPass;
                            UserDetails.codeforcesHandle = cf;
                            UserDetails.codechefHandle = cc;
                            UserDetails.spojHandle = sp;
                            UserDetails.emailId = emailId;

                            session.createLoginSession(user, newPass, cf, cc, sp, emailId,
                                    UserDetails.codeforcesRating, UserDetails.codechefRating, UserDetails.spojRank);
                            Intent i = new Intent(UpdateDetails.this, Users.class);
                            startActivity(i);

                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                        }
                    });

                    RequestQueue rQ = Volley.newRequestQueue(UpdateDetails.this);
                    rQ.add(request);

                    pd.dismiss();
                }
            }
        });
    }
}
