package com.navdeep.goaltracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.navdeep.goaltracker.pojo.Milestone;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.utility.GoalUtil;
import com.navdeep.goaltracker.view.MilestoneInputActivity;
import java.util.ArrayList;
import java.util.Calendar;

import static com.navdeep.goaltracker.view.MilestoneActivity.milestonePresenter;


public class MilestoneAdapter extends RecyclerAdapter {
    private ArrayList<Milestone> milestones;
    private int milestoneCount;
    private Context context;
    private int goalId;
    private TextView dayTitle, milestoneTitle, milestoneTimer, milestonetime, milestoneStatus;
    private static String DEFAULT_TIME = "00:00:00";

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
        inflateCardView(cardView, position);
        setOnClickListener(cardView, position);
    }

    private void inflateCardView(CardView cardView, int position) {
        findViewsOnCardView(cardView);
        setViewsOnCardView(cardView, position);
    }


    private void findViewsOnCardView(CardView cardView) {
        dayTitle = cardView.findViewById(R.id.dayTitle);
        milestoneTitle = cardView.findViewById(R.id.milestoneTitle);
        milestoneTimer = cardView.findViewById(R.id.milestoneTimer);
        milestonetime = cardView.findViewById(R.id.milestoneTime);
        milestoneStatus = cardView.findViewById(R.id.task_status);
    }

    private void setViewsOnCardView(CardView cardView, int position) {
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.baselineSurfaceColor));
        dayTitle.setText(String.format("Day %d", (position+1)));
        milestonetime.setText(String.format("%s",createTitle(position)));
        milestoneTitle.setText(milestones.get(position).getTitle());
        milestoneTimer.setText(milestones.get(position).getTimer());
        if(!milestoneTimer.getText().toString().trim().equalsIgnoreCase(DEFAULT_TIME)){
            setBackgroundToOkay();
        }else{
            setBackgroundToError();
        }
        milestoneStatus.setText(R.string.status_active);
        milestoneStatus.setTextColor(context.getResources().getColor(R.color.onBackgroundColor));
    }

    private String createTitle(int position) {
        Calendar calendar = GoalUtil.getCalendarObject(milestones.get(position).getTime());
        return  GoalUtil.dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)]+", "+calendar.get(Calendar.DATE)+" "
                +GoalUtil.months[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.YEAR);
    }

    private void setBackgroundToOkay() {
        milestoneStatus.setBackgroundColor(context.getResources().getColor(R.color.baselineOkayColor));
        milestoneStatus.setBackground(context.getResources().getDrawable(R.drawable.milestone_textview_active_border));
        dayTitle.setBackgroundColor(context.getResources().getColor(R.color.baselineOkayColor));
    }

    private void setBackgroundToError() {
        dayTitle.setBackgroundColor(context.getResources().getColor(R.color.baselineErrorColor));
        milestoneStatus.setBackground(context.getResources().getDrawable(R.drawable.milestone_textview_idle_border));
    }

    private void setOnClickListener(final CardView cardView, final int position) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMilestoneInputActivity(position);
            }
        });
    }

    private void startMilestoneInputActivity(int position) {
        Intent intent = new Intent(context, MilestoneInputActivity.class);
        intent.putExtra(MilestoneInputActivity.MILESTONE_ID, milestonePresenter.getMilestones(goalId).get(position).getMilestoneId());
        intent.putExtra(MilestoneInputActivity.GOAL_ID, goalId);
        if(position==milestonePresenter.getMilestones(goalId).size()-1){
            intent.putExtra(MilestoneInputActivity.INPUT_FLAG , false);
        }
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return milestones.size();
    }

}