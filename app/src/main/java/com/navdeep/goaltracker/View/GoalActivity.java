/* @author : Navdeep Singh
*  The class manages a list of goals.
* */
package com.navdeep.goaltracker.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.navdeep.goaltracker.adapters.GoalAdapter;
import com.navdeep.goaltracker.interfaces.GoalModelViewPresenter;
import com.navdeep.goaltracker.presenter.GoalPresenter;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.adapters.RecyclerAdapter;

public class GoalActivity extends AppCompatActivity implements GoalModelViewPresenter.GoalView {
    private static GoalPresenter goalPresenter;
    private RecyclerView goalRecycler;
    private FloatingActionButton floatingActionAddGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        goalRecycler = findViewById(R.id.goal_recycler);
        floatingActionAddGoal = findViewById(R.id.floatingActionAddGoal);
        goalPresenter = GoalPresenter.getGoalPresenter(this);

        floatingActionAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoalActivity.this, GoalInputActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void displayGoals() {
        updateGoalRecyclerViewAdapter();
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
        goalPresenter.initiateMilestones(); //update progress bar
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
        updateGoalRecyclerViewAdapter();
    }

    private void updateGoalRecyclerViewAdapter(){
        RecyclerAdapter goalAdapter = new GoalAdapter(goalPresenter.getGoals(), this);
        goalRecycler.setAdapter(goalAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        goalRecycler.setLayoutManager(layoutManager);
    }
}
