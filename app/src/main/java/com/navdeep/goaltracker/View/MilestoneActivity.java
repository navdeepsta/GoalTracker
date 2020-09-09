package com.navdeep.goaltracker.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.MilestoneListViewAdapter;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

/* After creating a goal, first milestone object will be created. The milestone activity
 * list will get updated automatically with time.
 */
public class MilestoneActivity extends AppCompatActivity implements MilestoneModelViewPresenter.MilestoneView {
    private ListView mMilestoneListView;
    public static final String GOAL_ID = "goal_id";
    private int goalId;

    public static MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone);
        goalId = getIntent().getIntExtra(MilestoneActivity.GOAL_ID, 0);

        mMilestoneListView = findViewById(R.id.goalMilestoneView);
        milestonePresenter = MilestonePresenter.getMilestonePresenter(this, goalId);
        // mMilestoneListView = findViewById(R.id.goalMilestoneView);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MilestoneActivity.this, MilestoneInputActivity.class);
                intent.putExtra(MilestoneInputActivity.MILESTONE_ID, milestonePresenter.getMilestones(goalId).get(position).getMilestoneId());
                intent.putExtra(MilestoneInputActivity.GOAL_ID, goalId);
                if(position==milestonePresenter.getMilestones(goalId).size()-1){
                    intent.putExtra(MilestoneInputActivity.INPUT_FLAG , false);
                }
                startActivity(intent);
            }
        };
        mMilestoneListView.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MilestoneListViewAdapter milestoneListViewAdapter = new MilestoneListViewAdapter(MilestoneActivity.this, milestonePresenter.getMilestones(goalId));
        mMilestoneListView.setAdapter(milestoneListViewAdapter);
    }

    @Override
    public void displayMilestones(ArrayList<Milestone> milestones) {
       // MilestoneListViewAdapter milestoneListViewAdapter=new MilestoneListViewAdapter(MilestoneActivity.this, milestones);
       // mMilestoneListView.setAdapter(milestoneListViewAdapter);
    }


}
