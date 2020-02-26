package com.navdeep.goaltracker;

import android.os.Handler;

 class GoalTimer {
    private Handler handler;

    private int seconds, milestoneCount=1, duration;
    private MilestoneModelViewPresenter.MilestonePresenter milestonePresenterWithView;
    GoalTimer(int duration){
        this.duration = duration;
    }
    void runTimer(final MilestoneModelViewPresenter.MilestonePresenter milestonePresenter){
        Runnable runnable;
        handler = new android.os.Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                //int secs = seconds%60;

                if(seconds%60==0){
                    if(milestoneCount<=duration)
                    {
                        // update milestone list
                        milestonePresenter.createMilestone("Day "+milestoneCount, "default");
                        if(milestonePresenterWithView!=null){
                            ((MilestonePresenter)milestonePresenterWithView).getMileStoneView().displayMilestones(milestonePresenterWithView.getMilestoneList());
                        }
                         ++milestoneCount;
                    }
                    else
                        handler.removeCallbacks(this);
                }
                seconds++;
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    void milestonePresenterWithView(MilestonePresenter milestonePresenter){
        milestonePresenterWithView = milestonePresenter;
    }
}
