package com.navdeep.goaltracker.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.navdeep.goaltracker.GoalTrackerDatabaseConnection;
import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.Interfaces.GoalModelViewPresenter;

import java.util.ArrayList;
import java.util.Iterator;

public class GoalModel implements GoalModelViewPresenter.GoalModel {
    private ArrayList<Goal> goals;
    private Context context;
    private GoalTrackerDatabaseConnection connection;
    public GoalModel(Context context) {
        this.context = context;
        goals = new ArrayList<>();
        connection = GoalTrackerDatabaseConnection.getConnection(context);

    }


    @Override
    public void insertGoal(Goal goal) {
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        String goalName = goal.getGoalName();
        String goalStartTime = goal.getGoalStartTime();
        int goalDuration = goal.getDuration();

        ContentValues goalContentValues = new ContentValues();
        goalContentValues.put("GOAL_NAME", goalName);
        goalContentValues.put("GOAL_START_TIME", goalStartTime);
        goalContentValues.put("GOAL_DURATION", goalDuration);
        goalTrackerDatabase.insert("GOAL", null, goalContentValues);
        connection.closeDatabase();
    }

    @Override
    public ArrayList<Goal> getGoals() {
        goals = new ArrayList<>();
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        Cursor cursor = goalTrackerDatabase.query("GOAL",
                new String[]{"GOAL_ID", "GOAL_NAME", "GOAL_START_TIME", "GOAL_DURATION", "GOAL_PROGRESS"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int goalId = cursor.getInt(0);
            String goalName = cursor.getString(1);
            String goalStartTime = cursor.getString(2);
            int goalDuration = cursor.getInt(3);
            int goalProgress = cursor.getInt(4);
            Goal goal = new Goal(goalId, goalName,goalStartTime,goalDuration, goalProgress);
            goals.add(goal);
        }
        cursor.close();
        connection.closeDatabase();
        return goals;
    }

    @Override
    public void updateGoal(Goal goal) {
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        int goalId =  goal.getGoalId();
        int goalProgress = goal.getGoalProgress();
        ContentValues goalValues = new ContentValues();
        goalValues.put("GOAL_PROGRESS", goalProgress);
        goalTrackerDatabase.update("GOAL",
                goalValues,
                "GOAL_ID = ?",
                new String[]{ String.valueOf(goalId)});
        connection.closeDatabase();
    }

    @Override
    public void deleteGoals(ArrayList<Goal> selectedGoals) {
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        for(Iterator itr = selectedGoals.iterator(); itr.hasNext();){
            Goal goal = (Goal)itr.next();
            goalTrackerDatabase.delete("GOAL",
                    "GOAL_ID = ?",
                    new String[]{ String.valueOf(goal.getGoalId())});
        }
        goalTrackerDatabase.close();
    }
}
