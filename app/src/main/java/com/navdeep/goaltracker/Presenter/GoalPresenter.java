package com.navdeep.goaltracker.Presenter;

import android.database.SQLException;
import android.widget.Toast;

import com.navdeep.goaltracker.GoalTime;
import com.navdeep.goaltracker.GoalTrackerDatabaseConnection;
import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.Interfaces.GoalModelViewPresenter;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Repository.GoalModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class GoalPresenter implements GoalModelViewPresenter.GoalPresenter {
    private static GoalModelViewPresenter.GoalView mGoalView;
    private static GoalModelViewPresenter.GoalInputView mGoalInputView;
    private static GoalModelViewPresenter.GoalModel mGoalModel;
    private GoalTrackerDatabaseConnection connection;

    private static int goalYear, goalMonth, goalDay;

    private static GoalPresenter goalPresenter;

    private GoalPresenter(GoalModelViewPresenter.GoalView view ){
       //mGoalView = view;
        mGoalModel = new GoalModel(view.getContext());
    }

    public static GoalPresenter getGoalPresenter(GoalModelViewPresenter.GoalView view){
        if(goalPresenter==null){
            goalPresenter = new GoalPresenter(view);
        }
        mGoalView = view;
        return goalPresenter;
    }

    public static GoalPresenter getGoalPresenter(GoalModelViewPresenter.GoalInputView view){
        mGoalInputView = view;
        return goalPresenter;
    }

    public static GoalPresenter getGoalPresenter(){
        return goalPresenter;
    }

    @Override
    public void createGoal(String categoryName, String goalName,String goalStartTime, int duration, int goalProgress) {
        Goal goal = new Goal(categoryName,goalName,goalStartTime, duration, goalProgress);
        mGoalModel.insertGoal(goal);
        mGoalView.displayGoals();
    }

    @Override
    public void deleteGoals(ArrayList<Goal> goals) {
        mGoalModel.deleteGoals(goals);
    }


    @Override
    public ArrayList<Goal> getGoals() {
        return mGoalModel.getGoals();
    }

    @Override
    public void incrementGoalProgress(Goal goal) {
        mGoalModel.updateGoal(goal);
    }

    @Override
    public void initGoalDuration() {
        mGoalInputView.displayGoalDuration(new GoalTime());
    }

    @Override
    public int calculateGoalDuration() {
        return (goalYear * 365) + (goalMonth *30)+ goalDay;

    }

    @Override
    public void createGoalTrackerDatabase() {
        try {
            connection = GoalTrackerDatabaseConnection.getConnection(mGoalView.getContext());
        }catch (SQLException e){
            Toast.makeText(mGoalView.getContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void closeGoalTrackerDatabase() {
        connection.closeGoalTrackerDatabaseHelper();
    }

    /*after creating goal, user clicks it on the same date when it got created
    * after creating goal, user does not click it one the same day when it got created
    * A goal has already been created, user re-enters it
    * */
    @Override
    public void initiateMilestones() {
        //Todo : understand it again anf again
        MilestonePresenter milestonePresenter = MilestonePresenter.getMilestonePresenter();
        String currentTime = Calendar.getInstance().getTime().toString();
        ArrayList<Goal> goals = getGoals();
        for(Iterator iterator = goals.iterator(); iterator.hasNext();){
            Goal goal = (Goal)iterator.next();
            if(milestonePresenter.getMilestones(goal.getGoalId()).size()!=0){
                // User clicks one of the saved goals
                int recentMilestoneIndex = milestonePresenter.getMilestones(goal.getGoalId()).size()-1;
                Milestone milestone = milestonePresenter.getMilestones(goal.getGoalId()).get(recentMilestoneIndex);
                milestonePresenter.createMilestones(milestone.getTime(), currentTime, goal.getGoalId(), goal.getDuration(),"00:00:00");
            }else{
                // User clicks newly created goal on the same day or User clicks newly created goal not on the same day
                milestonePresenter.createMilestone(goal.getGoalId(), "", currentTime, "","00:00:00");
                milestonePresenter.createMilestones(goal.getGoalStartTime(),currentTime, goal.getGoalId(), goal.getDuration(),"00:00:00");
            }
        }

    }

    public void setYear(int year){
        goalYear = year;
    }

    public void setMonth(int month){
        goalMonth = month;
    }

    public void setDay(int day){
        goalDay = day;
    }




}
