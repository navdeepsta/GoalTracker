package com.navdeep.goaltracker.Presenter;

import android.graphics.Bitmap;

import com.navdeep.goaltracker.GoalUtil;
import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.POJOs.MilestoneImage;
import com.navdeep.goaltracker.Repository.MilestoneModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MilestonePresenter implements MilestoneModelViewPresenter.MilestonePresenter, Serializable {
    private static MilestoneModelViewPresenter.MilestoneView mMilestoneView;
    private static MilestoneModelViewPresenter.MilestoneInputView mMilestoneInputView;
    private static MilestoneModelViewPresenter.MilestoneModel mMilestoneModel;

   //public final static String POSITION = "position";
   private static MilestonePresenter milestonePresenter;
   private MilestonePresenter(){
        mMilestoneModel = MilestoneModel.getMilestoneModel();

    }
    public static MilestonePresenter getMilestonePresenter() {
        if(milestonePresenter==null){
            milestonePresenter = new MilestonePresenter();
        }
        return milestonePresenter;
    }


    public static MilestonePresenter getMilestonePresenter(MilestoneModelViewPresenter.MilestoneView view, int goalId){

        mMilestoneView = view;
        //mMilestoneView.displayMilestones(milestonePresenter.getMilestones(goalId));
       // goal.getGoalTimer().milestonePresenterWithView(this);
       return milestonePresenter;
    }

    public static MilestonePresenter getMilestonePresenter(MilestoneModelViewPresenter.MilestoneInputView view){
        mMilestoneInputView = view;
        return milestonePresenter;
    }


    @Override
    public void updateMilestone(Milestone milestone) {
        mMilestoneInputView.showDescription();
        mMilestoneModel.updateMilestoneToDatabase(milestone);
    }

    @Override
    public void createMilestones(String previousTime, String currentTime, int goalId, int duration, String timer) {
        Calendar previousCalendar = GoalUtil.getCalendarObject(previousTime);
        Calendar currentCalendar = GoalUtil.getCalendarObject(currentTime);
        setGoalPreviousAndCurrentTimeToZero(previousCalendar,currentCalendar);
        long diff = currentCalendar.getTime().getTime() - previousCalendar.getTime().getTime();
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
       // int diffmin = (int) (diff / (60 * 1000));
        int milestoneListSize = getMilestones(goalId).size();
        for (int i = 0; i < diffDays; ++i) {
            if (milestoneListSize < duration) {
                createMilestone(goalId, "", currentTime, "", timer);
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
        ArrayList<Goal> goals = GoalPresenter.getGoalPresenter().getGoals();
        for(Iterator itr = goals.iterator(); itr.hasNext();){
            Goal goal = (Goal)itr.next();
            if(goal.getGoalId() == goalId){
                goal.setGoalProgress(goalProgress);
                GoalPresenter.getGoalPresenter().incrementGoalProgress(goal);
            }
        }

    }

    @Override
    public void createMilestone(int goalId, String description, String time, String title, String timer) {
        Milestone milestone = new Milestone(goalId, description, time, title, timer);
        mMilestoneModel.insertMilestone(milestone, goalId);
    }

    @Override
    public void addImage(int id, MilestoneImage image) {
       mMilestoneModel.insertImage(id, image);
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
    public ArrayList<MilestoneImage> getImages(int milestoneId) {
       return mMilestoneModel.fetchImages(milestoneId);
    }

    @Override
    public void deleteMilestoneImages(ArrayList<MilestoneImage> images, int milestoneId) {
        mMilestoneModel.deleteImages(images, milestoneId);
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
        return mMilestoneModel.fetchMilestones(goadId);
    }

    public MilestoneModelViewPresenter.MilestoneView getMileStoneView(){
        return mMilestoneView;
    }


}
