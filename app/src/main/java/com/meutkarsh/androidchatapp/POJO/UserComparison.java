package com.meutkarsh.androidchatapp.POJO;

import java.util.ArrayList;

/**
 * Created by utkarsh on 29/11/17.
 */

public class UserComparison {
    ArrayList<ComparisonData> uName1,uName2;

    public UserComparison(ArrayList<ComparisonData> uName1, ArrayList<ComparisonData> uName2) {
        this.uName1 = uName1;
        this.uName2 = uName2;
    }

    public ArrayList<ComparisonData> getuName1() {
        return uName1;
    }

    public void setuName1(ArrayList<ComparisonData> uName1) {
        this.uName1 = uName1;
    }

    public ArrayList<ComparisonData> getuName2() {
        return uName2;
    }

    public void setuName2(ArrayList<ComparisonData> uName2) {
        this.uName2 = uName2;
    }
}
