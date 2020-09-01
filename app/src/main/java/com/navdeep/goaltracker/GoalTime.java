package com.navdeep.goaltracker;

public class GoalTime {
    private String[] years = {"0","1","2","3","4","5"};
    private String[] months;
    private String[] days;

    public GoalTime() {
        months = new String[13];
        days = new String[30];
        initGoalTime();
    }

    private void initGoalTime(){
        initMonths();
        intiDays();
    }
    private void initMonths(){
        for (int i = 0; i <= 12; ++i) {
            months[i] = (i) + "";
        }
    }

    private void intiDays() {
        for (int i = 0; i < 30; ++i) {
            days[i] = (i + 1) + "";
        }
    }
    public String[] getYears(){
        return years;
    }

    public String[] getMonths(){
        return months;
    }

    public String[] getDays(){
        return days;
    }
}

