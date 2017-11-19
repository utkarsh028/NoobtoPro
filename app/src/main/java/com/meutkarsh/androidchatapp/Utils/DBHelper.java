package com.meutkarsh.androidchatapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.meutkarsh.androidchatapp.POJO.Question;

import java.util.ArrayList;

/**
 * Created by tanay on 15/11/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "noobtopro.db";
    public static final String USER_QUES_TABLE = "userQuestions";
    public static final String COL_1 = "title";
    public static final String COL_2 = "link";
    public static final String COL_3 = "success";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_QUES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLE VARCHAR(20)," + "LINK VARCHAR(50)," + "SUCCESS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_QUES_TABLE);
        onCreate(db);
    }

    public boolean insertUserQues(String tilte, String link, int success){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,tilte);
        contentValues.put(COL_2,link);
        contentValues.put(COL_3,success);
        if(db.insert(USER_QUES_TABLE,null,contentValues)==-1){
            return false;
        }
        return true;
    }

    public ArrayList<Question> getUserQues(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+USER_QUES_TABLE,null);
        ArrayList<Question> questions = new ArrayList<>();
        if(res.getCount()!=0){
            while(res.moveToNext()){
                questions.add(new Question(res.getString(1),res.getString(2),(res.getInt(3)==0)?false:true));
            }
        }
        return questions;
    }

    public void deleteUserQues(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+USER_QUES_TABLE);
    }

}
