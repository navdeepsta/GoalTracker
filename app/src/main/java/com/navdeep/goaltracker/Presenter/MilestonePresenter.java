package com.navdeep.goaltracker.Presenter;

import android.util.Log;

import com.navdeep.goaltracker.Utility.GoalUtil;
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
    // todo reduce function size
    @Override
    public void createMilestones(String previousTime, String currentTime, int goalId, int duration, String timer) {
        Calendar previousCalendar = GoalUtil.getCalendarObject(previousTime);
        Calendar currentCalendar = GoalUtil.getCalendarObject(currentTime);
        int diffInDays = getDateDifference(previousCalendar, currentCalendar);
        Goal goal = getGoal(goalId);

        Calendar goalStartTime = GoalUtil.getCalendarObject(goal.getGoalStartTime());
        Calendar endDateGoal = getGoalEndDate(goal, goalStartTime);
        addMilestones(goal, currentTime, diffInDays, timer);
        // int diffmin = (int) (diff / (60 * 1000));
        setGoalProgress(goal, currentCalendar, endDateGoal, goalStartTime);

    }

    private int getDateDifference(Calendar previousCalendar, Calendar currentCalendar) {
        long diff = currentCalendar.getTime().getTime() - previousCalendar.getTime().getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
   }


    private Goal getGoal(int goalId) {
        ArrayList<Goal> goals = GoalPresenter.getGoalPresenter().getGoals();
        for(Iterator itr = goals.iterator(); itr.hasNext();) {
            Goal goal = (Goal) itr.next();
            if (goal.getGoalId() == goalId) {
              return goal;
            }
        }
        return null;
    }
    private Calendar getGoalEndDate(Goal goal, Calendar goalStartTime) {
        Calendar cloneOfGoalStartTime = (Calendar) goalStartTime.clone();
        Calendar endDateGoal = Calendar.getInstance();
        endDateGoal.setTime(cloneOfGoalStartTime.getTime());
        endDateGoal.add(Calendar.DAY_OF_MONTH, goal.getDuration());
        return endDateGoal;
    }


    private void addMilestones(Goal goal, String currentTime, int diffInDays, String timer) {
        int milestoneListSize = getMilestones(goal.getGoalId()).size();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        for (int i = 0; i < diffInDays; ++i) {
            if (milestoneListSize < goal.getDuration()) {
                createMilestone(goal.getGoalId(), "", currentTime, "", timer);
           //     calculateAndUpdateGoalProgress(goal, milestoneListSize * hour, goal.getDuration());
                milestoneListSize = getMilestones(goal.getGoalId()).size();
            }
        }
    }
    private void setGoalProgress(Goal goal, Calendar currentCalendar, Calendar endDateGoal, Calendar goalStartTime) {
        setEndDateTimeToZero(endDateGoal);
        if(currentCalendar.before(endDateGoal)){
            Log.i("Goal Not ended yet","Before");
            //start time
            long td = currentCalendar.getTime().getTime()-goalStartTime.getTime().getTime();
            int timeDifference =  (int)(td/(60*60*1000));
            int totalHours = goal.getDuration() * 24;
            float progress= ((float)timeDifference/(float)totalHours);
            int p =  (int)(progress*100);
            updateGoalProgress(goal, p);
        }else{
            Log.i("Goal ended","After");
            updateGoalProgress(goal, 100);
        }
    }
    private void calculateAndUpdateGoalProgress(Goal goal, int totalHoursPassed, int duration){
       int goalProgress = (int) (100 * ((float) totalHoursPassed / (float) (duration *24)));
        updateGoalProgress(goal, goalProgress);
    }

    private void updateGoalProgress(Goal goal, int goalProgress){
                goal.setGoalProgress(goalProgress);
                GoalPresenter.getGoalPresenter().incrementGoalProgress(goal);
    }
    private void setEndDateTimeToZero(Calendar currentCalendar){
        currentCalendar.set(Calendar.HOUR_OF_DAY,0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
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




    @Override
    public ArrayList<Milestone> getMilestones(int goadId) {
        return mMilestoneModel.fetchMilestones(goadId);
    }

    public MilestoneModelViewPresenter.MilestoneView getMileStoneView(){
        return mMilestoneView;
    }


}
