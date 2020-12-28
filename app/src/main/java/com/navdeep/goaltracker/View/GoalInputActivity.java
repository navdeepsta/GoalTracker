package com.navdeep.goaltracker.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.navdeep.goaltracker.utility.GoalTime;
import com.navdeep.goaltracker.interfaces.GoalModelViewPresenter;
import com.navdeep.goaltracker.presenter.GoalPresenter;
import com.navdeep.goaltracker.R;

import java.util.Calendar;

public class GoalInputActivity extends AppCompatActivity implements GoalModelViewPresenter.GoalInputView, AdapterView.OnItemSelectedListener {
    private static int GOAL_ID_DEFAULT = 0;
    private EditText mGoalName;
    private Spinner years, months, days;
    private RadioGroup category;
    private RadioButton radioButton;
    private Button mSaveGoal;
    private GoalPresenter goalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_input);
        findViewsFromLayout();
        setListeners();
        setToobar();

    }

    private void findViewsFromLayout() {
        mGoalName = findViewById(R.id.goalName);
        years = findViewById(R.id.years);
        months =  findViewById(R.id.months);
        days =  findViewById(R.id.days);
        category = findViewById(R.id.category);
        radioButton = findViewById(category.getCheckedRadioButtonId());
        mSaveGoal = findViewById(R.id.saveGoal);

    }

    private void setListeners() {
        setListenerOnSpinner();
        setListenerOnButton();
        setListenerOnTextField();

    }

    private void setListenerOnSpinner() {
        years.setOnItemSelectedListener(this);
        months.setOnItemSelectedListener(this);
        days.setOnItemSelectedListener(this);

    }

    private void setListenerOnButton() {
        mSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGoalName.getText().toString().length()<1){
                    Toast.makeText(GoalInputActivity.this,"Please enter goal name",Toast.LENGTH_LONG).show();
                }else {
                    int duration = goalPresenter.calculateGoalDuration();
                    goalPresenter.createGoal(GOAL_ID_DEFAULT, radioButton.getText().toString(), mGoalName.getText().toString(), Calendar.getInstance().getTime().toString(), duration, 0);
                    finish();
                }
            }
        });

    }

    private void setListenerOnTextField() {
        mGoalName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void setToobar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Goal Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    public void checkButton(View view) {
        int radioId = category.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        goalPresenter = GoalPresenter.getGoalPresenter(GoalInputActivity.this);
        goalPresenter.initGoalDuration();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
}
