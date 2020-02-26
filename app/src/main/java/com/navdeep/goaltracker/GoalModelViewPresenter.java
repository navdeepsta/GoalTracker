package com.navdeep.goaltracker;

import android.content.Context;

import java.util.ArrayList;

interface GoalModelViewPresenter {
    interface GoalPresenter{
        boolean createGoal(String goalName, int duration, MilestonePresenter milestonePresenter, GoalTimer goalTimer);
        ArrayList<Goal> getGoalsList();

    }

    interface GoalView{
        void displayGoals(ArrayList<Goal> goals);
        Context getContext();
    }
    interface GoalInputView{

    }
    interface GoalModel{
        boolean insertGoal(Goal goal);
        ArrayList<Goal> fetchGoals();
    }
}
