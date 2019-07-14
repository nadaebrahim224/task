package com.example.events;

import java.util.Date;

public class event_data
{
    private String event_name;
    private Date date;
    private String weather;

    public event_data(String event_name, Date date, String weather) {
        this.event_name = event_name;
        this.date = date;
        this.weather = weather;
    }

    public event_data() {
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
