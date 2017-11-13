package com.meutkarsh.androidchatapp.POJO;

import java.util.ArrayList;

/**
 * Created by tanay on 12/11/17.
 */

public class QuestionList {
    ArrayList<Question> questions;

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Question> getQuestions() {

        return questions;
    }

    public QuestionList(ArrayList<Question> questions) {

        this.questions = questions;
    }
}
