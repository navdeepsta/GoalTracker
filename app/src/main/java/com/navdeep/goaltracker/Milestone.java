package com.navdeep.goaltracker;

import android.provider.MediaStore;

public class Milestone {
    private String description;
    private String time;
    private MediaStore.Images images;
    public Milestone(String description, String time){
        this.description = description;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return  description;
    }
}

