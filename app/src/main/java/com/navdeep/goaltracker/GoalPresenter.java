package com.navdeep.goaltracker;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;

public class GoalPresenter implements GoalModelViewPresenter.GoalPresenter {
    private GoalModelViewPresenter.GoalView mGoalView;
    private GoalModelViewPresenter.GoalModel mGoalModel;
    public static GoalPresenter goalPresenter;
    private GoalPresenter(GoalModelViewPresenter.GoalView view){
        mGoalView = view;
        mGoalModel = new GoalModel();
    }

    public static GoalPresenter getGoalPresenter(GoalModelViewPresenter.GoalView view){
        if(goalPresenter==null){
            goalPresenter = new GoalPresenter(view);
        }
        return goalPresenter;
    }
    public static GoalPresenter getGoalPresenter(GoalModelViewPresenter.GoalInputView view){
        return goalPresenter;
    }

    @Override
    public boolean createGoal(String goalName, int duration, MilestonePresenter milestonePresenter, GoalTimer goalTimer) {
        Goal goal = new Goal(goalName,duration, milestonePresenter, goalTimer);
        mGoalModel.insertGoal(goal);
        mGoalView.displayGoals(getGoalsList());
        return true;
    }

    @Override
    public ArrayList<Goal> getGoalsList() {
        return mGoalModel.fetchGoals();
    }

}
