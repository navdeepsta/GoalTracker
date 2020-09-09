package com.navdeep.goaltracker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Presenter.GoalPresenter;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.View.MilestoneActivity;

import java.util.ArrayList;

public class MilestoneListViewAdapter extends BaseAdapter {
    private static  ArrayList<Milestone> milestones;
    private int milestoneCount = 0;
    Context context;

    public MilestoneListViewAdapter(Context context, ArrayList<Milestone> milestone){
        this.context = context;
        milestones = milestone;

    }
    public MilestoneListViewAdapter(Context context){
        this.context = context;

    }
    @Override
    public int getCount() {
        return milestones.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.milestone_list_hold,null);
        TextView dayTitle = convertView.findViewById(R.id.dayTitle);
        TextView milestoneTitle = convertView.findViewById(R.id.milestoneTitle);
        TextView timer = convertView.findViewById(R.id.milestoneTimer);
        Log.i("Goal Name",milestones.get(position).getDescription() +"\n");
        Log.i("Goal Name",milestones.get(position).toString());

        //poistion >= 0
        dayTitle.setText(milestones.get(position).toString()+" "+(++milestoneCount));
        milestoneTitle.setText(milestones.get(position).getTitle());
        timer.setText(milestones.get(position).getTimer());

        if(milestones.get(position).getTimer().equals("00:00:00")) {
            convertView.setBackgroundColor(Color.RED);
            dayTitle.setBackgroundColor(Color.RED);
        }
        //int id = milestones.get(position).getGoalId();

       // fillLastMilestoneWithColorToShowEndOfGoal(id,convertView);

        return convertView;
    }

    private void fillLastMilestoneWithColorToShowEndOfGoal(int goalId, View convertView){
        TextView dayTitle = convertView.findViewById(R.id.dayTitle);
        ArrayList<Goal> goals = GoalPresenter.getGoalPresenter().getGoals();
        Goal goal = null;
        for(Goal g : goals){
            if(g.getGoalId() == goalId){
                goal = g;
            }
        }
        if(goal!=null && goal.getDuration() == milestoneCount) {
            convertView.setBackgroundColor(Color.BLUE);
            dayTitle.setBackgroundColor(Color.BLUE);
        }
    }
}


