package com.navdeep.goaltracker.Interfaces;

import android.graphics.Bitmap;

import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.POJOs.MilestoneImage;

import java.util.ArrayList;

public interface MilestoneModelViewPresenter {
    interface MilestonePresenter{
        void updateMilestone(Milestone milestone);
        void createMilestones(String previousTime, String currentTime, int goalId, int duration, String timer);
        void createMilestone(int goalId, String description, String time, String title, String timer);
        void addImage(int milestoneId, MilestoneImage image);
        void setMilestoneTime(String time);
        String getMilestoneTime();
        ArrayList<MilestoneImage> getImages(int milestoneId);
        void deleteMilestoneImages(ArrayList<MilestoneImage> images, int milestoneId);
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
        void insertImage(int id, MilestoneImage image);
        ArrayList<MilestoneImage> fetchImages(int milestoneId);
        void deleteImages(ArrayList<MilestoneImage> images, int milestoneId);
        void loadMilestoneFromDatabase(int milestoneId);
    }
}
