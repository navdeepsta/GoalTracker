package com.navdeep.goaltracker.presenter;

import android.util.Log;
import com.navdeep.goaltracker.utility.GoalUtil;
import com.navdeep.goaltracker.pojo.Goal;
import com.navdeep.goaltracker.pojo.Milestone;
import com.navdeep.goaltracker.interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.pojo.MilestoneImage;
import com.navdeep.goaltracker.repository.MilestoneModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MilestonePresenter implements MilestoneModelViewPresenter.MilestonePresenter, Serializable {
    private static MilestoneModelViewPresenter.MilestoneView mMilestoneView;
    private static MilestoneModelViewPresenter.MilestoneInputView mMilestoneInputView;
    private static MilestoneModelViewPresenter.MilestoneModel mMilestoneModel;
    private static MilestonePresenter milestonePresenter;

    private MilestonePresenter() {
        mMilestoneModel = MilestoneModel.getMilestoneModel();
    }

    public static MilestonePresenter getMilestonePresenter() {
        if(milestonePresenter == null) {
            milestonePresenter = new MilestonePresenter();
        }
        return milestonePresenter;
    }

    public static MilestonePresenter getMilestonePresenter(MilestoneModelViewPresenter.MilestoneView view){
       mMilestoneView = view;
       return milestonePresenter;
    }

    public static MilestonePresenter getMilestonePresenter(MilestoneModelViewPresenter.MilestoneInputView view){
        mMilestoneInputView = view;
        return milestonePresenter;
    }

    @Override
    public void updateMilestone(Milestone milestone) {
        mMilestoneInputView.showDescription();
        mMilestoneModel.updateMilestone(milestone);
    }

    @Override
    public void createMilestones(String previousTime, int goalId, int duration, String timer) {
        Calendar previousCalendar = GoalUtil.getCalendarObject(previousTime);
        Calendar currentCalendar = Calendar.getInstance();
        String currentTime = currentCalendar.getTime().toString();

        int diffInDays = getDateDifference(previousCalendar, currentCalendar);
        Goal goal = getGoal(goalId);

        Calendar goalStartTime = GoalUtil.getCalendarObject(goal.getGoalStartTime());
        Calendar endDateGoal = getGoalEndDate(goal, goalStartTime);
        // int diffmin = (int) (diff / (60 * 1000));
        addMilestones(goal, previousTime, currentTime, diffInDays, timer);
        setGoalProgress(goal, currentCalendar, endDateGoal, goalStartTime);

    }

    private int getDateDifference(Calendar previousCalendar, Calendar currentCalendar) {
        long diff = currentCalendar.getTime().getTime() - previousCalendar.getTime().getTime();
        return (int) (diff / (60 * 60 * 24 * 1000));
   }

    private Goal getGoal(int goalId) {
        ArrayList<Goal> goals = GoalPresenter.getGoalPresenter().getGoals();
        for(Goal goal : goals) {
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

    private void addMilestones(Goal goal, String previousTime, String currentTime, int diffInDays, String timer) {
        int milestoneListSize = getMilestones(goal.getGoalId()).size();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Calendar calendar = GoalUtil.getCalendarObject(previousTime);
        for (int i = 0; i < diffInDays; ++i) {
            if (milestoneListSize < goal.getDuration()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                setCalendarTimeToZero(calendar);
                createMilestone(goal.getGoalId(), "", calendar.getTime().toString(), "", timer);
                calculateAndUpdateGoalProgress(goal, milestoneListSize * hour, goal.getDuration());
                milestoneListSize = getMilestones(goal.getGoalId()).size();
            }
        }
    }

    private void setGoalProgress(Goal goal, Calendar currentCalendar, Calendar endDateGoal, Calendar goalStartTime) {
        setCalendarTimeToZero(endDateGoal);
        if(currentCalendar.before(endDateGoal)) {
            Log.i("Goal Not ended yet","Before");
            //start time
            long td = currentCalendar.getTime().getTime()-goalStartTime.getTime().getTime();
            int timeDifference =  (int)(td/(60*60*1000));
            int totalHours = goal.getDuration() * 24;
            float progress= ((float)timeDifference/(float)totalHours);
            int p =  (int)(progress*100);
            updateGoalProgress(goal, p);
        }else {
            Log.i("Goal ended","After");
            updateGoalProgress(goal, 100);
        }
    }

    private void setCalendarTimeToZero(Calendar currentCalendar) {
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
    }

    private void calculateAndUpdateGoalProgress(Goal goal, int totalHoursPassed, int duration){
       int goalProgress = (int) (100 * ((float) totalHoursPassed / (float) (duration *24)));
        updateGoalProgress(goal, goalProgress);
    }

    private void updateGoalProgress(Goal goal, int goalProgress){
                goal.setGoalProgress(goalProgress);
                GoalPresenter.getGoalPresenter().incrementGoalProgress(goal);
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
    public ArrayList<MilestoneImage> getImages(int milestoneId) {
       return mMilestoneModel.getImages(milestoneId);
    }

    @Override
    public void deleteMilestoneImages(ArrayList<MilestoneImage> images, int milestoneId) {
        mMilestoneModel.deleteImages(images, milestoneId);
    }

    @Override
    public ArrayList<Milestone> getMilestones(int goadId) {
        return mMilestoneModel.getMilestones(goadId);
    }
}
