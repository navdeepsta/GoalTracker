package com.navdeep.goaltracker.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.navdeep.goaltracker.GoalTime;
import com.navdeep.goaltracker.Interfaces.GoalModelViewPresenter;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.Presenter.GoalPresenter;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class GoalInputActivity extends AppCompatActivity implements GoalModelViewPresenter.GoalInputView, AdapterView.OnItemSelectedListener {
    private EditText mGoalName;
    private EditText mGoalDuration;
    private Button mSaveGoal;
    private Spinner years, months, days;
    private GoalModelViewPresenter.GoalInputView goalInputView;
    //private MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;
    private GoalPresenter goalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_input);
        years = findViewById(R.id.years);
        months =  findViewById(R.id.months);
        days =  findViewById(R.id.days);

        years.setOnItemSelectedListener(this);
        months.setOnItemSelectedListener(this);
        days.setOnItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        goalPresenter = GoalPresenter.getGoalPresenter(GoalInputActivity.this);
        goalPresenter.initGoalDuration();

        mGoalName = findViewById(R.id.goalName);
        mSaveGoal = findViewById(R.id.saveGoal);
        goalInputView = this;

        mSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = goalPresenter.calculateGoalDuration();
                goalPresenter.createGoal(mGoalName.getText().toString(), Calendar.getInstance().getTime().toString(), duration, 0);
                finish();
            }
        });


    }


    /* Removed duplication to reduce the function size, resulted in better readability */
    @Override
    public void displayGoalDuration(GoalTime goalTime) {
        years.setAdapter(getGoalSubDurationAdapter(goalTime.getYears()));
        months.setAdapter(getGoalSubDurationAdapter(goalTime.getMonths()));
        days.setAdapter(getGoalSubDurationAdapter(goalTime.getDays()));
    }

    private ArrayAdapter<String> getGoalSubDurationAdapter(String[] goalTimeUnit){
        ArrayAdapter<String> goalSubDuration = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, goalTimeUnit);
        goalSubDuration.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        return goalSubDuration;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String msg = "";
        switch (parent.getId()){
            case R.id.years:
                String y = (String)parent.getItemAtPosition(position);
                int year = Integer.valueOf(y);
                goalPresenter.setYear(year);
                break;
            case R.id.months:
                String m = (String)parent.getItemAtPosition(position);
                int month = Integer.valueOf(m);
                goalPresenter.setMonth(month);
                break;
            case R.id.days:
                String d = (String)parent.getItemAtPosition(position);
                int day = Integer.valueOf(d);
                goalPresenter.setDay(day);
                break;
                default:
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}