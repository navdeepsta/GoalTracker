package com.navdeep.goaltracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.navdeep.goaltracker.utility.GoalTrackerDatabaseConnection;
import com.navdeep.goaltracker.pojo.Goal;
import com.navdeep.goaltracker.interfaces.GoalModelViewPresenter;

import java.util.ArrayList;

public class GoalModel implements GoalModelViewPresenter.GoalModel {
    private ArrayList<Goal> goals;
    private GoalTrackerDatabaseConnection connection;

    public GoalModel(Context context) {
        goals = new ArrayList<>();
        connection = GoalTrackerDatabaseConnection.getConnection(context);
    }

    @Override
    public void insertGoal(Goal goal) {
        SQLiteDatabase goalTrackerDatabase = connection.openDatabase();
        String goalCategory = goal.getCategoryName();
        String goalName = goal.getGoalName();
        String goalStartTime = goal.getGoalStartTime();
        int goalDuration = goal.getDuration();

        ContentValues goalContentValues = new ContentValues();
        goalContentValues.put("GOAL_CATEGORY", goalCategory);
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
                new String[]{"GOAL_ID", "GOAL_CATEGORY", "GOAL_NAME", "GOAL_START_TIME", "GOAL_DURATION", "GOAL_PROGRESS"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            int goalId = cursor.getInt(0);
            String goalCategory = cursor.getString(1);
            String goalName = cursor.getString(2);
            String goalStartTime = cursor.getString(3);
            int goalDuration = cursor.getInt(4);
            int goalProgress = cursor.getInt(5);
            Goal goal = new Goal(goalId, goalCategory, goalName,goalStartTime,goalDuration, goalProgress);
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
        for(Goal goal : selectedGoals){
            goalTrackerDatabase.delete("GOAL",
                    "GOAL_ID = ?",
                    new String[]{ String.valueOf(goal.getGoalId())});
        }
        goalTrackerDatabase.close();
    }
}
