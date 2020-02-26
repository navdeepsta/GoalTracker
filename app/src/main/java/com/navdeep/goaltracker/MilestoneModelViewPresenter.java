package com.navdeep.goaltracker;

import android.content.Context;
import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;

interface MilestoneModelViewPresenter {
    interface MilestonePresenter{
        void updateMilestone();
        boolean createMilestone(String description, String time);
        ArrayList<Milestone> getMilestoneList();
    }

    interface MilestoneView{
        void displayMilestones(ArrayList<Milestone> milestones);
    }
    interface MilestoneInputView{
       void showDescription();
    }
    interface MilestoneModel{
        boolean insertMilestone(Milestone milestone);
        ArrayList<Milestone> fetchMilestones();
    }
}
