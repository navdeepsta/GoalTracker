package com.navdeep.goaltracker.POJOs;

import android.graphics.Bitmap;

import java.util.Calendar;

public class MilestoneImage {
     private int imageId;
     private String imageUri;
     private Calendar calendar;

     public  MilestoneImage(String imageUri, Calendar calendar) {
         this.imageUri = imageUri;
         this.calendar = calendar;
     }
     public MilestoneImage(int imageId, String imageUri, Calendar calendar) {
         this(imageUri, calendar);
         this.imageId = imageId;
     }

     public int getImageId(){
         return imageId;
     }

     public String getImageUri(){
         return imageUri;
     }

    public Calendar getCalendar() {
        return calendar;
    }
}
