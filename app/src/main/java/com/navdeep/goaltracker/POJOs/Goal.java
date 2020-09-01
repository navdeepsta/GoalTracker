package com.navdeep.goaltracker.POJOs;
import com.navdeep.goaltracker.GoalUtil;
public class Goal{
    private int goalId;
    private String goalName;
    private int goalDuration;
    private int goalProgress;
    private String goalStartTime;

    public Goal(String goalName, String goalStartTime, int goalDuration, int goalProgress){
        this.goalName = goalName;
        this.goalDuration = goalDuration;
        this.goalStartTime = goalStartTime;
        this.goalProgress = goalProgress;
    }

    public Goal(int goalId, String goalName, String goalStartTime, int goalDuration, int goalProgress){
        this(goalName, goalStartTime, goalDuration, goalProgress);
        this.goalId = goalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public  int getDuration(){
        return goalDuration;
    }

    public int getGoalId(){
        return goalId;
    }

    public String getGoalStartTime(){
        return goalStartTime;
    }

    public int getGoalProgress(){
        return goalProgress;
    }

    public void setGoalProgress(int goalProgress){
        this.goalProgress = goalProgress;
    }

    public String getGoalStartTimeWithFormat(){
        return GoalUtil.getStartTime(GoalUtil.getCalendarObject(goalStartTime));
    }

    @Override
    public String toString(){
        return getGoalName();
    }
}
