package com.navdeep.goaltracker.POJOs;

import android.graphics.Bitmap;

import java.util.Calendar;

public class MilestoneImage {
     private int imageId;
     private Bitmap bitmap;
     private Calendar calendar;

     public  MilestoneImage(Bitmap bitmap, Calendar calendar) {
         this.bitmap = bitmap;
         this.calendar = calendar;
     }
     public MilestoneImage(int imageId, Bitmap bitmap, Calendar calendar) {
         this(bitmap, calendar);
         this.imageId = imageId;
     }

     public int getImageId(){
         return imageId;
     }

     public Bitmap getBitmap(){
         return bitmap;
     }

    public Calendar getCalendar() {
        return calendar;
    }
}
