/* Applying knowledge gained from Code Clean
*  Converting names to meaningful, informative and unambiguous names
*/
package com.navdeep.goaltracker.interfaces;
import android.content.Context;
import com.navdeep.goaltracker.utility.GoalTime;
import com.navdeep.goaltracker.pojo.Goal;
import java.util.ArrayList;

public interface GoalModelViewPresenter {
    String TIMER = "00:00:00";

    interface GoalPresenter {
        void createGoal(int goalId, String categoryName, String goalName, String goalStartTime, int duration, int goalProgress);
        void deleteGoals(ArrayList<Goal> goals);
        ArrayList<Goal> getGoals();
        void incrementGoalProgress(Goal goal);
        void initGoalDuration();
        int calculateGoalDuration();
        void createGoalTrackerDatabase();
        void closeGoalTrackerDatabase();
        void initiateMilestones();
    }

    interface GoalView {
        void displayGoals();
        Context getContext();
    }

    interface GoalInputView {
       void displayGoalDuration(GoalTime goalTime);
    }

    interface GoalModel {
        void insertGoal(Goal goal);
        ArrayList<Goal> getGoals();
        void updateGoal(Goal goal);
        void deleteGoals(ArrayList<Goal> goals);
    }
}
