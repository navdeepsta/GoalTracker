package com.navdeep.goaltracker.Presenter;

import android.graphics.Bitmap;

import com.navdeep.goaltracker.GoalUtil;
import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.Repository.MilestoneModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MilestonePresenter implements MilestoneModelViewPresenter.MilestonePresenter, Serializable {
    private MilestoneModelViewPresenter.MilestoneView mMilestoneView;
    private MilestoneModelViewPresenter.MilestoneInputView mMilestoneInputView;
    private static MilestoneModelViewPresenter.MilestoneModel mMilestoneModel;

   //public final static String POSITION = "position";

    public MilestonePresenter(){
        mMilestoneModel = MilestoneModel.getMilestoneModel();

    }

   public MilestonePresenter(MilestoneModelViewPresenter.MilestoneView view, int goalId){
       mMilestoneView = view;
        mMilestoneView.displayMilestones(getMilestones(goalId));
       // goal.getGoalTimer().milestonePresenterWithView(this);

    }

    public MilestonePresenter(MilestoneModelViewPresenter.MilestoneInputView view){
        mMilestoneInputView = view;
    }

    @Override
    public void updateMilestone(Milestone milestone) {
        mMilestoneInputView.showDescription();
        MilestoneModel.getMilestoneModel().updateMilestoneToDatabase(milestone);
    }

    @Override
    public void createMilestones(String previousTime, String currentTime, int goalId, int duration) {
        Calendar previousCalendar = GoalUtil.getCalendarObject(previousTime);
        Calendar currentCalendar = GoalUtil.getCalendarObject(currentTime);
        //  setGoalPreviousAndCurrentTimeToZero(previousCalendar,currentCalendar);
        long diff = currentCalendar.getTime().getTime() - previousCalendar.getTime().getTime();
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        int diffmin = (int) (diff / (60 * 1000));
        int milestoneListSize = getMilestones(goalId).size();
        for (int i = 0; i < diffmin; ++i) {
            if (milestoneListSize < duration) {
                createMilestone(goalId, "Default", currentTime, "Title");
                calculateAndUpdateGoalProgress(goalId, milestoneListSize, duration);
                milestoneListSize = getMilestones(goalId).size();
            }
        }

        if (milestoneListSize == duration) {
            calculateAndUpdateGoalProgress(goalId, milestoneListSize, duration);
        }
    }

    private void calculateAndUpdateGoalProgress(int goalId, int milestoneListSize, int duration){
        int goalProgress =  (int)(100 * ((float)milestoneListSize/(float)duration) );
        updateGoalProgress(goalId, goalProgress);
    }

    private void updateGoalProgress(int goalId, int goalProgress){
        ArrayList<Goal> goals = GoalPresenter.goalPresenter.getGoals();
        for(Iterator itr = goals.iterator(); itr.hasNext();){
            Goal goal = (Goal)itr.next();
            if(goal.getGoalId() == goalId){
                goal.setGoalProgress(goalProgress);
                GoalPresenter.goalPresenter.incrementGoalProgress(goal);
            }
        }

    }

    @Override
    public void createMilestone(int goalId, String description, String time, String title) {
        Milestone milestone = new Milestone(goalId, description, time, title);
        MilestoneModel.getMilestoneModel().insertMilestone(milestone, goalId);
    }

    @Override
    public void addImage(int id, Bitmap bitmap) {
        MilestoneModel.getMilestoneModel().insertImage(id, bitmap);
    }

    @Override
    public void setMilestoneTime(String time) {
        //Todo : Insert MilestoneTime into database
    }

    @Override
    public String getMilestoneTime() {
        // Todo : Fetch MilestoneTime From database
        return null;
    }

    @Override
    public ArrayList<Bitmap> getImages(int milestoneId) {
       return MilestoneModel.getMilestoneModel().fetchImages(milestoneId);
    }

    private void setGoalPreviousAndCurrentTimeToZero(Calendar previousCalendar, Calendar currentCalendar){
        previousCalendar.set(Calendar.HOUR_OF_DAY, 0);
        previousCalendar.set(Calendar.MINUTE, 0);
        previousCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.HOUR_OF_DAY,0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
    }



    @Override
    public ArrayList<Milestone> getMilestones(int goadId) {
        return MilestoneModel.getMilestoneModel().fetchMilestones(goadId);
    }

    public MilestoneModelViewPresenter.MilestoneView getMileStoneView(){
        return mMilestoneView;
    }


}
