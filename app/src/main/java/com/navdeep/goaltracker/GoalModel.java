package com.navdeep.goaltracker;

import java.util.ArrayList;

public class GoalModel implements GoalModelViewPresenter.GoalModel {
    private ArrayList<Goal> goals;
    GoalModel(){
        goals = new ArrayList<>();
    }
    @Override
    public boolean insertGoal(Goal goal) {
        goals.add(goal);
        return true;
    }

    @Override
    public ArrayList<Goal> fetchGoals() {
        return goals;
    }
}
