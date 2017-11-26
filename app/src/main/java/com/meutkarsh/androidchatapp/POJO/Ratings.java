package com.meutkarsh.androidchatapp.POJO;

/**
 * Created by utkarsh on 25/11/17.
 */

public class Ratings {

    String cfRating, spjRating, ccRating;

    public Ratings(String cfRating, String spjRating, String ccRating) {
        this.cfRating = cfRating;
        this.spjRating = spjRating;
        this.ccRating = ccRating;
    }

    public String getCfRating() {
        return cfRating;
    }

    public String getSpjRating() {
        return spjRating;
    }

    public String getCcRating() {
        return ccRating;
    }

    public void setCfRating(String cfRating) {
        this.cfRating = cfRating;
    }

    public void setSpjRating(String spjRating) {
        this.spjRating = spjRating;
    }

    public void setCcRating(String ccRating) {
        this.ccRating = ccRating;
    }

}
