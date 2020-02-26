package com.navdeep.goaltracker;

import java.util.ArrayList;

public class MilestoneModel implements MilestoneModelViewPresenter.MilestoneModel{
    private ArrayList<Milestone> milestones;
    MilestoneModel(){
        milestones = new ArrayList<>();
    }
    @Override
    public boolean insertMilestone(Milestone milestone) {
        milestones.add(milestone);
        return true;
    }

    @Override
    public ArrayList<Milestone> fetchMilestones() {
        return milestones;
    }
}
