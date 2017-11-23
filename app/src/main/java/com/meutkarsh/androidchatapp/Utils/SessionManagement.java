package com.meutkarsh.androidchatapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.meutkarsh.androidchatapp.Activities.MainActivity;
import com.meutkarsh.androidchatapp.Activities.Users;
import com.meutkarsh.androidchatapp.POJO.UserDetails;

import java.util.HashMap;

/**
 * Created by aditya on 28/09/17.
 */

public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "NoobtoPro";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "pass";
    private static final String KEY_CF = "codeforces";
    private static final String KEY_CC = "codechef";
    private static final String KEY_SP = "spoj";
    private static final String KEY_EMAIL = "emailId";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String pass, String cf, String cc, String sp, String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD, pass);
        editor.putString(KEY_CF, cf);
        editor.putString(KEY_CC, cc);
        editor.putString(KEY_SP, sp);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin() {
        if(this.isLoggedIn()){
            UserDetails.username = pref.getString(KEY_NAME, null);
            UserDetails.password = pref.getString(KEY_PASSWORD, null);
            UserDetails.codeforcesHandle = pref.getString(KEY_CF, null);
            UserDetails.codechefHandle = pref.getString(KEY_CC, null);
            UserDetails.spojHandle = pref.getString(KEY_SP, null);
            UserDetails.emailId = pref.getString(KEY_EMAIL, null);
            Intent i = new Intent(_context, Users.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_CF, pref.getString(KEY_CF, null));
        user.put(KEY_CC, pref.getString(KEY_CC, null));
        user.put(KEY_SP, pref.getString(KEY_SP, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }
}
