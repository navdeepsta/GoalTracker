package com.navdeep.goaltracker.interfaces;

import com.navdeep.goaltracker.pojo.Milestone;
import com.navdeep.goaltracker.pojo.MilestoneImage;
import java.util.ArrayList;

public interface MilestoneModelViewPresenter {

    interface MilestonePresenter{
        void updateMilestone(Milestone milestone);
        void createMilestones(String previousTime,int goalId, int duration, String timer);
        void createMilestone(int goalId, String description, String time, String title, String timer);
        void addImage(int milestoneId, MilestoneImage image);
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
        void insertMilestone(Milestone milestone, int goalId);
        ArrayList<Milestone> getMilestones(int goalId);
        void updateMilestone(Milestone milestone);
        void insertImage(int id, MilestoneImage image);
        ArrayList<MilestoneImage> getImages(int milestoneId);
        void deleteImages(ArrayList<MilestoneImage> images, int milestoneId);
    }
}
