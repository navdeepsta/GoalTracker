package com.navdeep.goaltracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.View.MilestoneActivity;
import com.navdeep.goaltracker.View.MilestoneInputActivity;

import java.util.ArrayList;

import static com.navdeep.goaltracker.View.MilestoneActivity.milestonePresenter;

public class MilestoneAdapter extends RecyclerView.Adapter<MilestoneAdapter.ViewHolder>{
    private ArrayList<Milestone> milestones;
    private int milestoneCount;
    private Context context;
    private int goalId;
    public MilestoneAdapter(ArrayList<Milestone> milestones, Context context, int goalId) {
        this.milestones = milestones;
        this.context = context;
        this.goalId = goalId;
        milestoneCount = 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }
    @NonNull
    @Override
    public MilestoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.milestone_cardview, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull MilestoneAdapter.ViewHolder holder, final int position) {
     CardView cardView = holder.cardView;
     TextView dayTitle = cardView.findViewById(R.id.dayTitle);
     TextView milestoneTitle = cardView.findViewById(R.id.milestoneTitle);
     TextView milestoneTimer = cardView.findViewById(R.id.milestoneTimer);
     dayTitle.setText(String.format("Day %d", (++milestoneCount)));
     milestoneTitle.setText(milestones.get(position).getTitle());
     milestoneTimer.setText(milestones.get(position).getTimer());
     cardView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(context, MilestoneInputActivity.class);
             intent.putExtra(MilestoneInputActivity.MILESTONE_ID, milestonePresenter.getMilestones(goalId).get(position).getMilestoneId());
             intent.putExtra(MilestoneInputActivity.GOAL_ID, goalId);
             if(position==milestonePresenter.getMilestones(goalId).size()-1){
                 intent.putExtra(MilestoneInputActivity.INPUT_FLAG , false);
             }
             context.startActivity(intent);
         }
     });

    }

    @Override
    public int getItemCount() {
        return milestones.size();
    }
}