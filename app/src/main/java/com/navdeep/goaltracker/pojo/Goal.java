package com.navdeep.goaltracker.pojo;
import com.navdeep.goaltracker.utility.GoalUtil;
public class Goal{
    private int goalId;
    private String categoryName;
    private String goalName;
    private int goalDuration;
    private int goalProgress;
    private String goalStartTime;

    public Goal(String categoryName,String goalName, String goalStartTime, int goalDuration, int goalProgress){
        this.categoryName = categoryName;
        this.goalName = goalName;
        this.goalDuration = goalDuration;
        this.goalStartTime = goalStartTime;
        this.goalProgress = goalProgress;
    }

    public Goal(int goalId, String categoryName, String goalName, String goalStartTime, int goalDuration, int goalProgress){
        this(categoryName, goalName, goalStartTime, goalDuration, goalProgress);
        this.goalId = goalId;
    }

    public String getCategoryName() {
        return categoryName;
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
