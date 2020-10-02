package com.navdeep.goaltracker.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.Adapters.MilestoneAdapter;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.Adapters.RecyclerAdapter;

import java.util.ArrayList;

/* After creating a goal, first milestone object will be created. The milestone activity
 * list will get updated automatically with time.
 */
public class MilestoneActivity extends AppCompatActivity implements MilestoneModelViewPresenter.MilestoneView {
    private ListView mMilestoneListView;
    private RecyclerView milestoneRecycler;
    public static final String GOAL_ID = "goal_id";
    private int goalId;

    public static MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tasks");
        actionBar.setDisplayHomeAsUpEnabled(true);
        milestoneRecycler = findViewById(R.id.milestone_recycler);

        goalId = getIntent().getIntExtra(MilestoneActivity.GOAL_ID, 0);
        milestonePresenter = MilestonePresenter.getMilestonePresenter(this, goalId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerAdapter milestoneAdapter = new MilestoneAdapter(milestonePresenter.getMilestones(goalId), this, goalId);
        milestoneRecycler.setAdapter(milestoneAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        milestoneRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public void displayMilestones(ArrayList<Milestone> milestones) {
       // MilestoneListViewAdapter milestoneListViewAdapter=new MilestoneListViewAdapter(MilestoneActivity.this, milestones);
       // mMilestoneListView.setAdapter(milestoneListViewAdapter);
    }


}
