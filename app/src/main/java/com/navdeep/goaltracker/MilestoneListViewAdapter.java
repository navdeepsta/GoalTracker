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
        milestoneCount = 0;

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
        setDayTitle(dayTitle, position);
        setMilestoneTitle(milestoneTitle, position);
        setTimer(timer, position);
        changeBackgroundBasedOnTimer(convertView, dayTitle, position);
        return convertView;
    }


    private void setDayTitle(TextView dayTitle, int position) {
        dayTitle.setText(milestones.get(position).toString()+" "+(++milestoneCount));
    }

    private void setMilestoneTitle(TextView milestoneTitle, int position) {
        if(milestones.get(position).getTitle().equals("")){
            milestoneTitle.setText(String.format("%s", "Title"));
        }else {
            milestoneTitle.setText(milestones.get(position).getTitle());
        }
    }

    private void setTimer(TextView timer, int position) {
        timer.setText(milestones.get(position).getTimer());
    }

    private void changeBackgroundBasedOnTimer(View convertView, TextView dayTitle ,int position) {
        if(milestones.get(position).getTimer().equals("00:00:00")) {
            convertView.setBackgroundColor(Color.RED);
            dayTitle.setBackgroundColor(Color.RED);
        }
    }
}


