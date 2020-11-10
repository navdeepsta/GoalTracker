package com.navdeep.goaltracker.utility;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.navdeep.goaltracker.pojo.Goal;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

public class GoalListViewAdapter extends BaseAdapter {
    private ArrayList<Goal> goals;
    private Context context;
    private static int[] categoryIcons = {R.drawable.category_education,
                                          R.drawable.category_exercise,
                                          R.drawable.category_business,
                                          R.drawable.category_goal};
    private static String[] categoryNames = {"education", "fitness", "business", "other"};
    public GoalListViewAdapter(Context context, ArrayList<Goal> goals){
       this.context = context;
       this.goals = goals;
   }
    @Override
    public int getCount() {
        return goals.size();
    }

    @Override
    public Object getItem(int position) {
        return goals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.goal_list_hold,null);
        setConvertViewWidgets(position, convertView);
        return convertView;
    }

    private void setConvertViewWidgets(int position, View convertView) {
        TextView goalName = convertView.findViewById(R.id.goalName);
        TextView time = convertView.findViewById(R.id.time);
        ImageView categoryIcon = convertView.findViewById(R.id.categoryIcon);
        CircularProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        TextView progressBarText = convertView.findViewById(R.id.progressBarText);
        Goal goal = goals.get(position);
        String progressText = goal.getGoalProgress()+"%";
        progressBar.setProgress(goal.getGoalProgress());
        progressBarText.setText(progressText);
        categoryIcon.setImageResource(categoryIcons[getCategoryIconIndex(goal)]);
        goalName.setText(getFormattedGoalName(goal));
        time.setText(goal.getGoalStartTimeWithFormat());
    }



    private int getCategoryIconIndex(Goal goal) {
        int index = categoryNames.length-1;
        for(int i=0; i<categoryNames.length; ++i) {
            if(categoryNames[i].equalsIgnoreCase(goal.getCategoryName())) {
                Log.i("Category", goal.getCategoryName());
                index = i;
            }
        }
        return  index;
    }

    private String getFormattedGoalName(Goal goal) {
        String firstLetter = goal.getGoalName().substring(0,1).toUpperCase();
        String goalName = goal.getGoalName().substring(1).toLowerCase();
        return firstLetter + goalName;
    }
}
