/* @author : Navdeep Singh
*  The class manages a list of goals.
*  Users can see a list of goals. They can delete any goal.
* */
package com.navdeep.goaltracker.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.navdeep.goaltracker.GoalListViewAdapter;
import com.navdeep.goaltracker.Interfaces.GoalModelViewPresenter;
import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.Presenter.GoalPresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

public class GoalActivity extends AppCompatActivity implements GoalModelViewPresenter.GoalView {
    private ListView mGoalListView;
    private FloatingActionButton floatingActionButton;
    private GoalListViewAdapter goalListViewAdapter;
    private static GoalPresenter goalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        mGoalListView = findViewById(R.id.goalListView);
       // floatingActionButton = findViewById(R.id.floatingActionButton);
        goalPresenter = GoalPresenter.getGoalPresenter(this);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Goal> goals = goalPresenter.getGoals();
                Intent intent = new Intent(GoalActivity.this, MilestoneActivity.class);
                intent.putExtra(MilestoneActivity.GOAL_ID, goals.get(position).getGoalId());
                startActivity(intent);
            }
        };

        mGoalListView.setOnItemClickListener(itemClickListener);

        /*This gives user an ability to select and delete multiple goals */
        mGoalListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mGoalListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            ArrayList<Goal> goals = new ArrayList<>();
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Goal goal = (Goal)goalListViewAdapter.getItem(position);
                if(goals.contains(goal)){
                    goals.remove(goal);
                }else {
                    goals.add(goal);
                }
                TextView modeTitle = new TextView(GoalActivity.this);
                modeTitle.setText(goals.size()+" items selected");
                modeTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                modeTitle.setTextSize(18);
                mode.setCustomView(modeTitle);

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
                    goalPresenter.deleteGoals(goals);
                    mode.finish();
                    goalListViewAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                goals.clear();
                updateGoalListViewAdapter();
            }
        });

        /*
          floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalActivity.this, GoalInputActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    @Override
    public void displayGoals() {
        updateGoalListViewAdapter();
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.goal_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_goal:
                Intent intent = new Intent(GoalActivity.this, GoalInputActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        goalPresenter.closeGoalTrackerDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        goalPresenter.initiateMilestones(); /// update progress bar
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        goalPresenter.createGoalTrackerDatabase();
        goalPresenter.initiateMilestones(); // update progress bar
        updateGoalListViewAdapter();
    }

    private void updateGoalListViewAdapter(){
        goalListViewAdapter=new GoalListViewAdapter(GoalActivity.this, goalPresenter.getGoals());
        mGoalListView.setAdapter(goalListViewAdapter);
    }
}
