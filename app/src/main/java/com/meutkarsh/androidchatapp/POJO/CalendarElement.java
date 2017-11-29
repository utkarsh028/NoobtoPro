package com.meutkarsh.androidchatapp.POJO;

/**
 * Created by aditya on 29/11/17.
 */

public class CalendarElement {
    String startEndTime, duration, event, link;

    public CalendarElement(String startEndTime, String duration, String event, String link) {
        this.startEndTime = startEndTime;
        this.duration = duration;
        this.event = event;
        this.link = link;
    }

    public String getStartEndTime() {
        return startEndTime;
    }

    public void setStartEndTime(String startEndTime) {
        this.startEndTime = startEndTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
