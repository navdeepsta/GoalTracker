package com.navdeep.goaltracker.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.navdeep.goaltracker.Adapters.RecyclerAdapter;
import com.navdeep.goaltracker.Adapters.ViewHolder;
import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.Presenter.GoalPresenter;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.View.GoalActivity;
import com.navdeep.goaltracker.View.MilestoneActivity;

import java.util.ArrayList;

public class GoalAdapter extends RecyclerAdapter {
    private ArrayList<Goal> goals;
    private Context context;
    private boolean multiSelect = false;
    private ArrayList<Goal> selectedGoals;

    ActionMode actionMode;
    CardView cardView;
    private static int[] categoryIcons = {R.drawable.category_education,
            R.drawable.category_exercise,
            R.drawable.category_business,
            R.drawable.category_goal};
    private static String[] categoryNames = {"education", "fitness", "business", "other"};

    public GoalAdapter(ArrayList<Goal> goals, Context context) {
        this.goals = goals;
        this.context = context;
        selectedGoals = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_cardview, parent, false);
        return ViewHolder.getViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final CardView cardView = holder.getCardView();
        TextView goalName = cardView.findViewById(R.id.goalName);
        TextView time = cardView.findViewById(R.id.time);
        ImageView categoryIcon = cardView.findViewById(R.id.categoryIcon);
        CircularProgressBar progressBar = cardView.findViewById(R.id.progressBar);
        TextView progressBarText = cardView.findViewById(R.id.progressBarText);
        final Goal goal = goals.get(position);
        String progressText = goal.getGoalProgress()+"%";
        progressBar.setProgress(goal.getGoalProgress());
        progressBarText.setText(progressText);
        categoryIcon.setImageResource(categoryIcons[getCategoryIconIndex(goal)]);
        goalName.setText(getFormattedGoalName(goal));
        time.setText(goal.getGoalStartTimeWithFormat());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(multiSelect){
                    selectItem(goal);
                    if (selectedGoals.contains(goal)) {
                        cardView.setBackgroundColor(Color.LTGRAY);
                    } else {
                        cardView.setBackgroundColor(Color.WHITE);
                    }
                    actionMode.setTitle(selectedGoals.size()+" selected");
                }else {
                    ArrayList<Goal> goals = GoalPresenter.getGoalPresenter().getGoals();
                    Intent intent = new Intent(context, MilestoneActivity.class);
                    intent.putExtra(MilestoneActivity.GOAL_ID, goals.get(position).getGoalId());
                    context.startActivity(intent);
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!multiSelect) {
                    actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(goal);
                    view.setBackgroundColor(Color.LTGRAY);
                    actionMode.setTitle(selectedGoals.size() + " selected");
                    return true;
                }
                return false;
            }

        });
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.goal_context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId() == R.id.delete_Goal) {
                GoalPresenter.getGoalPresenter().deleteGoals(selectedGoals);
                mode.finish();
               return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedGoals.clear();
            GoalActivity activity = (GoalActivity) context;
            activity.displayGoals();

        }
    };
    void selectItem(Goal goal) {
        if (multiSelect) {
            if (selectedGoals.contains(goal)) {
                selectedGoals.remove(goal);
            //    cardView.setBackgroundColor(Color.WHITE);
            } else {
                selectedGoals.add(goal);
              //  cardView.setBackgroundColor(Color.LTGRAY);
            }
        }
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
    @Override
    public int getItemCount() {
        return goals.size();
    }

}

