package com.navdeep.goaltracker.utility;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalTrackerDatabaseConnection {
    private SQLiteOpenHelper goalTrackerDatabaseHelper;
    private SQLiteDatabase goalTrackerDatabase;
    private static GoalTrackerDatabaseConnection connection;

    private GoalTrackerDatabaseConnection(Context context){
        goalTrackerDatabaseHelper = new GoalTrackerDatabaseHelper(context);
    }

    public static GoalTrackerDatabaseConnection getConnection(Context context){
        if(connection == null){
            connection = new GoalTrackerDatabaseConnection(context);
        }
        return connection;
    }

    public SQLiteDatabase openDatabase(){
        goalTrackerDatabase = goalTrackerDatabaseHelper.getReadableDatabase();
        return goalTrackerDatabase;
    }

    public void closeDatabase(){
        goalTrackerDatabase.close();
    }

    public void closeGoalTrackerDatabaseHelper(){
        goalTrackerDatabaseHelper.close();
    }

}
