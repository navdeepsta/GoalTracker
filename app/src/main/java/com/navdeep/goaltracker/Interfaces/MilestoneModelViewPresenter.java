package com.navdeep.goaltracker.Interfaces;

import android.graphics.Bitmap;

import com.navdeep.goaltracker.POJOs.Milestone;

import java.util.ArrayList;

public interface MilestoneModelViewPresenter {
    interface MilestonePresenter{
        void updateMilestone(Milestone milestone);
        void createMilestones(String previousTime, String currentTime, int goalId, int duration, String timer);
        void createMilestone(int goalId, String description, String time, String title, String timer);
        void addImage(int milestoneId, Bitmap bitmap);
        void setMilestoneTime(String time);
        String getMilestoneTime();
        ArrayList<Bitmap> getImages(int milestoneId);
        ArrayList<Milestone> getMilestones(int goalId);
    }

    interface MilestoneView{
        void displayMilestones(ArrayList<Milestone> milestones);

    }
    interface MilestoneInputView{
        void showDescription();
    }
    interface MilestoneModel{
        boolean insertMilestone(Milestone milestone, int goalId);
        ArrayList<Milestone> fetchMilestones(int goalId);
        void updateMilestoneToDatabase(Milestone milestone);
        void insertImage(int id, Bitmap bitmap);
        ArrayList<Bitmap> fetchImages(int milestoneId);
        void loadMilestoneFromDatabase(int milestoneId);
    }
}
