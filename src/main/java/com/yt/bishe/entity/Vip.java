package com.yt.bishe.entity;

import java.time.Period;
import java.util.Date;

public class Vip {
    private int id;
    private String userName;
    private int vipLevel;
    private Date startTime;
    private Date duringTime;
    private Date endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDuringTime() {
        return duringTime;
    }

    public void setDuringTime(Date duringTime) {
        this.duringTime = duringTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
