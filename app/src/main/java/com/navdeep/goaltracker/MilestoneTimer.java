package com.navdeep.goaltracker;


import android.os.Handler;
import android.widget.TextView;

public class MilestoneTimer {
    private Handler handler;
    private Runnable runnable;
    private int seconds;
    private String time="00:00:00";
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
    public String getMilestoneTimer(){
        return time;
    }
    public void runTimer(final TextView textView){

        handler = new android.os.Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                //int secs = seconds%60;
                seconds++;
                int p1 = seconds % 60;
                int p2 = seconds / 60;
                int p3 = p2 % 60;
                p2 = p2 / 60;
                time =  p2 + ":" + p3 + ":" + p1;
                textView.setText(time);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

}
