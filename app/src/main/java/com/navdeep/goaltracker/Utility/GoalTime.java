package com.navdeep.goaltracker.utility;

public class GoalTime {
    private String[] years;
    private String[] months;
    private String[] days;

    public GoalTime() {
        years = new String[11];
        months = new String[13];
        days = new String[30];
        initGoalTime();
    }

    private void initGoalTime(){
        initYears();
        initMonths();
        intiDays();
    }

    private void initYears(){
        for (int i = 0; i <= 10; ++i) {
            years[i] = (i) + "";
        }
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

