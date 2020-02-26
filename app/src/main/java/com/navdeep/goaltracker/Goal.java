package com.navdeep.goaltracker;
import java.util.Calendar;


public class Goal {
    private String goalName;
    private Calendar calendar;
    private int duration;
    private MilestonePresenter milestonePresenter;
    private GoalTimer goalTimer;
    private String[] months = {"January", "Febuary", "March", "April", "May",
                                 "June", "July", "August", "September", "October",
                                  "November", "December"};
    private String[] dayOfWeek = {"Invalid Day", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};

    Goal(String goalName, int duration, MilestonePresenter milestonePresenter, GoalTimer timer){
        this.goalName = goalName;
        this.duration = duration;
        this.milestonePresenter = milestonePresenter;
        this.goalTimer = timer;
        calendar = Calendar.getInstance();
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public MilestonePresenter getMilestonePresenter(){
        return  milestonePresenter;
    }
    public void setMilestonePresenter(MilestonePresenter milestonePresenter){
        this.milestonePresenter = milestonePresenter;
    }
    public GoalTimer getGoalTimer(){
        return goalTimer;
    }
    public  int getDuration(){
        return duration;
    }

    public String getCalendarTime(){
        return calendar.getTime()+"";
    }

    public String getStartTime(){
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        //int seconds = calendar.get(Calendar.SECOND);
        String beforeOrAfterNoon = calendar.get(Calendar.AM_PM) == Calendar.AM? "am" : "pm";
        if(calendar.get(Calendar.AM_PM) == Calendar.PM){
            hour+=12;
        }
         return dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)]+", "+calendar.get(Calendar.DATE)+" "
                +months[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.YEAR)
                +"\n"+hour+":"+minutes+" "+beforeOrAfterNoon;
    }

    @Override
    public String toString(){
        return getGoalName()+"\n"+getStartTime();
    }
}
