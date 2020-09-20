package com.navdeep.goaltracker.Utility;


import android.os.Handler;
import android.widget.TextView;

public class MilestoneTimer {
    private Handler handler;
    private Runnable runnable;
    private int seconds;
    private String time="00:00:00";
    private int runTimerCount = 0;
    private boolean running = false;
    public void setRunning(boolean running){this.running = running;}
    public boolean isRunning(){return running;}
    public Handler getHandler(){
        return handler;
    }
    public Runnable getRunnable(){
        return runnable;
    }
    public void setSeconds(int seconds){
        this.seconds = seconds;
    }
    public int getSeconds(){
        return seconds;
    }
    public void setMilestoneTimer(String time){this.time = time;}
    public String getMilestoneTimer(){
        return time;
    }
    public void setRunTimerCounterToZero(){
        runTimerCount = 0;
    }
    public void runTimer(final TextView textView) {
        ++runTimerCount;
        if (runTimerCount < 2) {
            String[] timeUnits = textView.getText().toString().split(":");
            int hour = Integer.valueOf(timeUnits[0].trim());
            int min = Integer.valueOf(timeUnits[1]);
            int sec = Integer.valueOf(timeUnits[2]);
            seconds = (hour * 3600) + (min * 60) + sec;
            handler = new android.os.Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    //int secs = seconds%60;
                    seconds++;
                    int p1 = seconds % 60;
                    int p2 = seconds / 60;
                    int p3 = p2 % 60;
                    p2 = p2 / 60;
                    String s1 = "";
                    String s2 = "";
                    String s3 = "";
                    if (p1 < 10) {
                        s1 = "0";
                    }
                    if (p2 < 10) {
                        s2 = "0";
                    }
                    if (p3 < 10) {
                        s3 = "0";
                    }
                    time = s2 + p2 + ":" + s3 + p3 + ":" + s1 + p1;
                    textView.setText(time);
                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(runnable);
        }
    }
}
