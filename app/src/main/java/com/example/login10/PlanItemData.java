package com.example.login10;

import android.widget.ToggleButton;

public class PlanItemData {

    String planName;
    String memo;
    String date;
    String time;
    String alarm;
    String key;
    String repeatDay;
//    ToggleButton complete;

    public PlanItemData() {
    }

    public PlanItemData(String planName, String memo, String date, String time, String alarm, String key, String repeatDay) {
        this.planName = planName;
        this.memo = memo;
        this.date = date;
        this.time = time;
        this.alarm = alarm;
        this.key = key;
        this.repeatDay = repeatDay;
    }

    public String getRepeatDay() {
        return repeatDay;
    }

    public void setRepeatDay(String repeatDay) {
        this.repeatDay = repeatDay;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
