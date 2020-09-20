package com.navdeep.goaltracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.navdeep.goaltracker.Adapters.RecyclerAdapter;
import com.navdeep.goaltracker.Adapters.ViewHolder;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.View.MilestoneInputActivity;
import java.util.ArrayList;
import static com.navdeep.goaltracker.View.MilestoneActivity.milestonePresenter;


public class MilestoneAdapter extends RecyclerAdapter {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.milestone_cardview, parent, false);
        return ViewHolder.getViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.getCardView();
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        TextView dayTitle = cardView.findViewById(R.id.dayTitle);
        TextView milestoneTitle = cardView.findViewById(R.id.milestoneTitle);
        TextView milestoneTimer = cardView.findViewById(R.id.milestoneTimer);
        TextView milestoneStatus = cardView.findViewById(R.id.task_status);
        dayTitle.setText(String.format("Day %d", (++milestoneCount)));

        milestoneTitle.setText(milestones.get(position).getTitle());
        milestoneTimer.setText(milestones.get(position).getTimer());
        Log.i("MilestoneTimer", milestoneTimer.getText().toString());
        if(!milestoneTimer.getText().toString().trim().equalsIgnoreCase("00:00:00")){

         milestoneStatus.setBackgroundColor(context.getResources().getColor(R.color.colorSecondaryDark));

         milestoneStatus.setBackground(context.getResources().getDrawable(R.drawable.milestone_textview_active_border));
         dayTitle.setBackgroundColor(context.getResources().getColor(R.color.colorSecondaryDark));

        }else{
            dayTitle.setBackgroundColor(context.getResources().getColor(R.color.baselineErrorColor));
            milestoneStatus.setBackground(context.getResources().getDrawable(R.drawable.milestone_textview_idle_border));
        }
        milestoneStatus.setText(R.string.status_active);
        milestoneStatus.setTextColor(context.getResources().getColor(R.color.onBackgroundColor));
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