package com.navdeep.goaltracker;


import android.os.Handler;
import android.widget.TextView;

public class MilestoneTimer {
    private Handler handler;
    private Runnable runnable;
    private int seconds;

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

    public void runTimer(final TextView textView){

        handler = new android.os.Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                //int secs = seconds%60;
                seconds++;
                textView.setText(String.valueOf(seconds));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }
}
