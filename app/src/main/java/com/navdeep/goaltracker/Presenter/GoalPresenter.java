package com.navdeep.goaltracker.presenter;

import android.database.SQLException;
import android.widget.Toast;

import com.navdeep.goaltracker.utility.GoalTime;
import com.navdeep.goaltracker.utility.GoalTrackerDatabaseConnection;
import com.navdeep.goaltracker.pojo.Goal;
import com.navdeep.goaltracker.interfaces.GoalModelViewPresenter;
import com.navdeep.goaltracker.pojo.Milestone;
import com.navdeep.goaltracker.repository.GoalModel;

import java.util.ArrayList;
import java.util.Calendar;

public class GoalPresenter implements GoalModelViewPresenter.GoalPresenter {
    private static GoalModelViewPresenter.GoalView mGoalView;
    private static GoalModelViewPresenter.GoalInputView mGoalInputView;
    private static GoalModelViewPresenter.GoalModel mGoalModel;
    private static GoalPresenter goalPresenter;
    private GoalTrackerDatabaseConnection connection;
    private static int goalYear, goalMonth, goalDay;

    private GoalPresenter(GoalModelViewPresenter.GoalView view ) {
        mGoalModel = new GoalModel(view.getContext());
    }

    public static GoalPresenter getGoalPresenter(GoalModelViewPresenter.GoalView view) {
        if(goalPresenter == null){
            goalPresenter = new GoalPresenter(view);
        }
        mGoalView = view;
        return goalPresenter;
    }

    public static GoalPresenter getGoalPresenter(GoalModelViewPresenter.GoalInputView view) {
        mGoalInputView = view;
        return goalPresenter;
    }

    public static GoalPresenter getGoalPresenter(){
        return goalPresenter;
    }

    @Override
    public void createGoal(int goalId, String categoryName, String goalName, String goalStartTime, int duration, int goalProgress) {
        Goal goal = new Goal(goalId, categoryName,goalName,goalStartTime, duration, goalProgress);
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
        return (goalYear * 365) + (goalMonth *30) + goalDay;
    }

    @Override
    public void createGoalTrackerDatabase() {
        try {
            connection = GoalTrackerDatabaseConnection.getConnection(mGoalView.getContext());
        }catch (SQLException e){
            Toast.makeText(mGoalView.getContext(), "Database Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void closeGoalTrackerDatabase() {
        connection.closeGoalTrackerDatabaseHelper();
    }

    /*
    *  This function handles various scenarios for creating milestones
    *  1. User creates a new goal and visits it on the same day
    *  2. User creates a new goal but does not visit it on the same day
    *  3. User visits one of the saved goals
    * */
    @Override
    public void initiateMilestones() {
        MilestonePresenter milestonePresenter = MilestonePresenter.getMilestonePresenter();
        ArrayList<Goal> goals = getGoals();
        for(Goal goal : goals){
            if(milestonePresenter.getMilestones(goal.getGoalId()).size() != 0){
                // User clicks one of the saved goals
                createMilestonesForSavedGoal(goal);
            }else {
                // User clicks newly created goal on any day
                createMilestonesForNewGoal(goal);
            }
        }
    }

    private void createMilestonesForSavedGoal(Goal goal) {
        MilestonePresenter milestonePresenter = MilestonePresenter.getMilestonePresenter();
        int recentMilestoneIndex = milestonePresenter.getMilestones(goal.getGoalId()).size()-1;
        Milestone milestone = milestonePresenter.getMilestones(goal.getGoalId()).get(recentMilestoneIndex);
        milestonePresenter.createMilestones(milestone.getTime(), goal.getGoalId(), goal.getDuration(), GoalModelViewPresenter.TIMER);
    }

    private void createMilestonesForNewGoal(Goal goal) {
        MilestonePresenter milestonePresenter = MilestonePresenter.getMilestonePresenter();
        String currentTime = Calendar.getInstance().getTime().toString();
        milestonePresenter.createMilestone(goal.getGoalId(), "", currentTime, "", GoalModelViewPresenter.TIMER);
        milestonePresenter.createMilestones(goal.getGoalStartTime(), goal.getGoalId(), goal.getDuration(),GoalModelViewPresenter.TIMER);
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
