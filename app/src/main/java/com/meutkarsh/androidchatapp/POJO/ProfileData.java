package com.meutkarsh.androidchatapp.POJO;

import java.util.ArrayList;

/**
 * Created by aditya on 30/11/17.
 */

public class ProfileData {
    ArrayList<ComparisonData> usrstats;
    ArrayList<Question> qSolved;

    public ProfileData(ArrayList<ComparisonData> usrstats, ArrayList<Question> qSolved) {
        this.usrstats = usrstats;
        this.qSolved = qSolved;
    }

    public ArrayList<ComparisonData> getUsrstats() {
        return usrstats;
    }

    public void setUsrstats(ArrayList<ComparisonData> usrstats) {
        this.usrstats = usrstats;
    }

    public ArrayList<Question> getqSolved() {
        return qSolved;
    }

    public void setqSolved(ArrayList<Question> qSolved) {
        this.qSolved = qSolved;
    }
}
