package com.navdeep.goaltracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GoalInputActivity extends AppCompatActivity implements GoalModelViewPresenter.GoalInputView{
    private EditText mGoalName;
    private EditText mGoalDuration;
    private Button mSaveGoal;
    private GoalModelViewPresenter.GoalInputView goalInputView;
    private MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_input);
        mGoalName = findViewById(R.id.goalName);
        mGoalDuration = findViewById(R.id.goalDuration);
        mSaveGoal = findViewById(R.id.saveGoal);
        goalInputView = this;

        mSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = Integer.valueOf(mGoalDuration.getText().toString());
                milestonePresenter = new MilestonePresenter();
                GoalTimer goalTimer = new GoalTimer(duration);
                GoalPresenter.getGoalPresenter(goalInputView).createGoal(mGoalName.getText().toString(), duration,(MilestonePresenter)milestonePresenter, goalTimer);
                goalTimer.runTimer(milestonePresenter);
                finish();
            }
        });


    }
}
