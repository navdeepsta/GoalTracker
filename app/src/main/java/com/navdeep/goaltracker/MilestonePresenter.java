package com.navdeep.goaltracker;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MilestonePresenter implements MilestoneModelViewPresenter.MilestonePresenter, Serializable {
    private MilestoneModelViewPresenter.MilestoneView mMilestoneView;
    private MilestoneModelViewPresenter.MilestoneInputView mMilestoneInputView;
    private MilestoneModelViewPresenter.MilestoneModel mMilestoneModel;

   //public final static String POSITION = "position";

    MilestonePresenter(){
        mMilestoneModel = new MilestoneModel();

    }

    MilestonePresenter(MilestoneModelViewPresenter.MilestoneView view, int position){
        mMilestoneView = view;
        Goal goal = GoalPresenter.goalPresenter.getGoalsList().get(position);
        mMilestoneModel = goal.getMilestonePresenter().getMilestoneModel();
        mMilestoneView.displayMilestones(getMilestoneList());
        goal.getGoalTimer().milestonePresenterWithView(this);
    }

    MilestonePresenter(MilestoneModelViewPresenter.MilestoneInputView view){
        mMilestoneInputView = view;
    }

    public MilestoneModelViewPresenter.MilestoneModel getMilestoneModel(){
        return mMilestoneModel;
    }
    @Override
    public void updateMilestone() {
        mMilestoneInputView.showDescription();
    }

    @Override
    public boolean createMilestone(String description, String time) {
        Milestone milestone = new Milestone(description, time);
        mMilestoneModel.insertMilestone(milestone);
        return true;
    }

    @Override
    public ArrayList<Milestone> getMilestoneList() {
        return mMilestoneModel.fetchMilestones();
    }

    public MilestoneModelViewPresenter.MilestoneView getMileStoneView(){
        return mMilestoneView;
    }


}
