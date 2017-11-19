package com.meutkarsh.androidchatapp.POJO;

/**
 * Created by tanay on 12/11/17.
 */

public class Question {
    String title,link;
    Boolean success;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTitle() {

        return title;
    }

    public String getLink() {
        return link;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Question(String title, String link, Boolean success) {

        this.title = title;
        this.link = link;
        this.success = success;
    }
}
