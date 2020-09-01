package com.navdeep.goaltracker.POJOs;
import android.graphics.Bitmap;
import java.util.ArrayList;
public class Milestone {
    private int goalId;
    private int milestoneId;
    private String description;
    private String title;
    private String time;
    private ArrayList<Bitmap> bitmaps;

    public Milestone(int goalId, String description, String time, String title){
        this.goalId = goalId;
        this.description = description;
        this.time = time;
        this.title = title;
        bitmaps = new ArrayList<>();
    }

    public Milestone(int milestoneId, int goalId, String description, String time, String title){
        this(goalId, description, time, title);
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

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public ArrayList<Bitmap> getBitmapList(){
        return bitmaps;
    }

    @Override
    public String toString() {
        return  "Day ";
    }
}

