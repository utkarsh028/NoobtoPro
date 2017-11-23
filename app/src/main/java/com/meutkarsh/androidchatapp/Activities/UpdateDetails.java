package com.meutkarsh.androidchatapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.meutkarsh.androidchatapp.POJO.UserDetails;
import com.meutkarsh.androidchatapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateDetails extends AppCompatActivity {

    EditText oldPassword, newPassword, codeforces, codechef, spoj, email;
    String oldPass, newPass, cf, cc, sp, emailId;
    Button update;

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPass = oldPassword.getText().toString();
                if(oldPass.equals("")){
                    oldPassword.setError("This field cannot be blank.");
                }else if(!oldPass.equals(UserDetails.password)){
                    oldPassword.setError("Invalid old password.");
                }else{
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
                    if(!matcher.find()){
                        email.setError("Enter valid email address");
                        return;
                    }
                    final ProgressDialog pd = new ProgressDialog(UpdateDetails.this);
                    pd.setMessage("Loading...");
                    pd.show();

                }
            }
        });
    }
}
