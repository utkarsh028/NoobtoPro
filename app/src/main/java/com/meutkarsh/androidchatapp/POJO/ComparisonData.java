package com.meutkarsh.androidchatapp.POJO;

/**
 * Created by utkarsh on 27/11/17.
 */

public class ComparisonData {
    String type;
    Integer count;

    public ComparisonData(String type, Integer count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}