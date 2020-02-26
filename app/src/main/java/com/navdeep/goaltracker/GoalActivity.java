package com.navdeep.goaltracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class GoalActivity extends AppCompatActivity implements GoalModelViewPresenter.GoalView {
    private ListView mGoalListView;
    static GoalPresenter goalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        mGoalListView = findViewById(R.id.goalListView);
        goalPresenter = GoalPresenter.getGoalPresenter(this);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Goal> goals = goalPresenter.getGoalsList();
                Intent intent = new Intent(GoalActivity.this, MilestoneActivity.class);

              //  intent.putExtra(MilestoneActivity.EXTRA_TIME, goals.get(position).getCalendarTime());
                //intent.putExtra(MilestoneActivity.DURATION, goals.get(position).getDuration());
                intent.putExtra(MilestoneActivity.POSITION, position);
                startActivity(intent);
            }
        };
        mGoalListView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void displayGoals(ArrayList<Goal> goals) {
        ArrayAdapter<Goal> adapter = new ArrayAdapter<>(this, R.layout.goal_list_hold, goals);
        mGoalListView.setAdapter(adapter);
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
}
