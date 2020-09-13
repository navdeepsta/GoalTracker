package com.navdeep.goaltracker.POJOs;
import android.graphics.Bitmap;
import java.util.ArrayList;
public class Milestone {
    private int goalId;
    private int milestoneId;
    private String description;
    private String title;
    private String time;
    private String timer;
    private ArrayList<MilestoneImage> bitmaps;

    public Milestone(int goalId, String description, String time, String title, String timer){
        this.goalId = goalId;
        this.description = description;
        this.time = time;
        this.title = title;
        this.timer = timer;
        bitmaps = new ArrayList<>();
    }

    public Milestone(int milestoneId, int goalId, String description, String time, String title, String timer){
        this(goalId, description, time, title, timer);
        this.milestoneId = milestoneId;
    }


    public String getDescription() {
        return description;
    }

    public int getMilestoneId(){
        return milestoneId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTimer(){
        return  timer;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public ArrayList<MilestoneImage> getBitmapList(){
        return bitmaps;
    }

    public int getGoalId() {
        return goalId;
    }
    @Override
    public String toString() {
        return  "Day ";
    }
}

