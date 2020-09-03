package com.navdeep.goaltracker.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.navdeep.goaltracker.GoalTrackerDatabaseConnection;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MilestoneModel implements MilestoneModelViewPresenter.MilestoneModel {
     private ArrayList<Milestone> milestones;
     private static MilestoneModel milestoneModel = null;
     private final GoalTrackerDatabaseConnection connection;
     private MilestoneModel(Context context){
        milestones = new ArrayList<>();
        connection = GoalTrackerDatabaseConnection.getConnection(context);
    }

    public static MilestoneModel getMilestoneModel() {
        if(milestoneModel==null){
            milestoneModel = new MilestoneModel(null);
        }
        return milestoneModel;
    }

    @Override
    public boolean insertMilestone(Milestone milestone, int goalId) {
      //  milestones.add(milestone);
       //Log.i("Milestone", "Milestone Inserted");
       // Log.i("Milestone goal", goalId+"");
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        String title = milestone.getTitle();
        String description = milestone.getDescription();
        String time = milestone.getTime();

        ContentValues milestoneContentValues = new ContentValues();
        milestoneContentValues.put("GOAL_ID", goalId);
        milestoneContentValues.put("MILESTONE_TITLE", title);
        milestoneContentValues.put("MILESTONE_DESCRIPTION", description);
        milestoneContentValues.put("MILESTONE_TIME", time);

        goalTrackerDatabase.insert("MILESTONE", null, milestoneContentValues);
        //Toast.makeText(context, "Goal Inserted",Toast.LENGTH_SHORT).show();
        connection.closeDatabase();
        return true;
    }

    @Override
    public ArrayList<Milestone> fetchMilestones(int goalId)
    {
       /* ArrayList<Milestone> m = new ArrayList<>();
        for(Iterator itr = milestones.iterator(); itr.hasNext(); ){
            Milestone milestone = (Milestone) itr.next();
            if(milestone.getGoalId() == goalId){
                m.add(milestone);
            }
        }
        */
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        Log.i("Goal id", goalId+"");
        milestones = new ArrayList<>();
        Cursor cursor = goalTrackerDatabase.query("MILESTONE",
                new String[]{"MILESTONE_ID","GOAL_ID","MILESTONE_TITLE", "MILESTONE_DESCRIPTION", "MILESTONE_TIME"},
               "GOAL_ID = ?" ,new String[]{String.valueOf(goalId)},null,null,null);

        while (cursor.moveToNext()){
            int milestoneId = cursor.getInt(0);
            int goal = cursor.getInt(1);
            String title = cursor.getString(2);
            String description = cursor.getString(3);
            String time = cursor.getString(4);
            Log.i("Milestone", milestoneId+" "+goal+""+title);
            /* TODO
            *   Alter the table to incluse milestone count */
            milestones.add(new Milestone(milestoneId, goalId, description, time, title));
            Log.i("Milestone From Database", goal+"");
        }

        cursor.close();
        connection.closeDatabase();
     return milestones;

    }

    @Override
    public void updateMilestoneToDatabase(Milestone milestone) {
    /*TODO*/
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        int milestoneId =  milestone.getMilestoneId();
        ContentValues milestoneValues = new ContentValues();
        milestoneValues.put("MILESTONE_TITLE", milestone.getTitle());
        milestoneValues.put("MILESTONE_DESCRIPTION", milestone.getDescription());
        Log.d("updatedTitle", milestone.getTitle());
        goalTrackerDatabase.update("MILESTONE",
                milestoneValues,
                "MILESTONE_ID = ?",
                new String[]{ String.valueOf(milestoneId)});
        connection.closeDatabase();
    }

    @Override
    public void insertImage(int id, Bitmap bitmap) {
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        ContentValues imageContentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,stream);
        byte[] imageByteArray = stream.toByteArray();
        imageContentValues.put("MILESTONE_ID" , id);
        imageContentValues.put("IMAGE_DATA", imageByteArray);
        goalTrackerDatabase.insert("IMAGE", null, imageContentValues);
        connection.closeDatabase();
    }

    @Override
    public ArrayList<Bitmap> fetchImages(int milestoneId) {
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        Cursor cursor = goalTrackerDatabase.query("IMAGE",
                new String[]{"IMAGE_ID","MILESTONE_ID","IMAGE_DATA"},
                "MILESTONE_ID = ?" ,new String[]{String.valueOf(milestoneId)},null,null,null);

        while (cursor.moveToNext()){
            int imageId = cursor.getInt(0);
            int milestone_Id = cursor.getInt(1);
            byte[] imageByteArray = cursor.getBlob(2);
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
            bitmaps.add(compressedBitmap);
        }
        cursor.close();
        connection.closeDatabase();
        return bitmaps;
    }

    @Override
    public void loadMilestoneFromDatabase(int milestoneId) {

    }
}