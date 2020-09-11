package com.navdeep.goaltracker;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalTrackerDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "goaltracker";
    private static int DATABASE_VERSION = 1;

    public GoalTrackerDatabaseHelper(Context context){
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE GOAL ("
                       + "GOAL_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                       + "GOAL_CATEGORY TEXT, "
                       + "GOAL_NAME TEXT, "
                       + "GOAL_START_TIME TEXT, "
                       + "GOAL_DURATION INTEGER, "
                       + "GOAL_PROGRESS INTEGER);");

            db.execSQL("CREATE TABLE MILESTONE("
                       + "MILESTONE_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                       + "GOAL_ID INTEGER NOT NULL, "
                       + "MILESTONE_TITLE TEXT, "
                       + "MILESTONE_DESCRIPTION TEXT, "
                       + "MILESTONE_TIME TEXT, "
                       + "MILESTONE_TIMER TEXT, "
                       + "FOREIGN KEY (GOAL_ID) "
                       + "REFERENCES GOAL(GOAL_ID) ON DELETE CASCADE);");

            db.execSQL("CREATE TABLE IMAGE("
                       + "IMAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                       + "MILESTONE_ID INTEGER NOT NULL,"
                       + "IMAGE_DATA BLOB, "
                       + "FOREIGN KEY (MILESTONE_ID) "
                       + "REFERENCES MILESTONE(MILESTONE_ID) ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
